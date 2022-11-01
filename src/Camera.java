import java.awt.*;

public class Camera {
    private double fov = 90;
    private double aspectRatio;
    private ThreeDVector facing = new ThreeDVector(new ThreeDPoint(-20, -20, 25), 1, 1, 0).toUnit();
    private ThreeDVector velocity = new ThreeDVector(0, 0, 0);
    private ThreeDVector forward = new ThreeDVector(facing.i, facing.j, 0).toUnit();
    private ThreeDVector left = new ThreeDVector(-1 * facing.j, facing.i, 0).toUnit();
    private ThreeDVector up = facing.cross(left);
    private moverThread moverThread;
    private double sensitivity;
    private ThreeMatrix matrix = new ThreeMatrix(facing, left, facing.cross(left));
    private ThreeDPoint originNew = facing.getOrigin().multByMatrix(matrix);
    private double distScale = 0.5 / Math.tan(fov / 2);
    int width;
    int height;
    public int toTurnX = 0;
    public int toTurnY = 0;
    public boolean w = false;
    public boolean a = false;
    public boolean s = false;
    public boolean d = false;
    public boolean space = false;
    public boolean ctrl = false;
    public boolean shift = false;
    public DisplayRender.CustomPanel customPanel;
    private Pillar[][] pillars;

    public ThreeDVector getForwards() {
        return forward;
    }

    public ThreeMatrix getMatrix() {
        return matrix;
    }

    public ThreeDVector getLeft() {
        return left;
    }

    public ThreeDPoint getOriginNew() {
        return originNew;
    }

    public double getDistScale() {
        return distScale;
    }


    public Camera(double aspectRatio, double sensitivity) {
        this.aspectRatio = aspectRatio;     // Aspect ratio should be the width divided by the height of the window
        moverThread = new moverThread(this);
        moverThread.start();
        this.sensitivity = sensitivity;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.width = (int) dim.getWidth() / 2;
        this.height = (int) dim.getHeight() / 2;
        //System.out.println("W and H -> " + this.width + ", " + this.height);
    }

    public void addPanel(DisplayRender.CustomPanel customPanel) {
        this.customPanel = customPanel;
    }

    public void addPillars(Pillar[][] pillars) {
        this.pillars = pillars;
    }

    protected void finalize() {
        moverThread.end();
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public double getAspectRatio() {
        return aspectRatio;
    }

    public void mouseMovement(int xy, int z) {
        ThreeDVector vec = new ThreeDVector(facing);               // xy is distance the mouse moved from last time this was called, left to right
        double downScaledXy = Math.sqrt(Math.pow(facing.i, 2) + Math.pow(facing.j, 2)) * xy;     // z is the same thing, for forward and backward
        if (Math.abs(downScaledXy) < 0.1 * Math.abs(xy)) downScaledXy = 0.1 * xy * Math.signum(xy);
        if (xy == 0) downScaledXy = 0;
        ThreeMatrix zAxisRotate = new ThreeMatrix(new double[][] {{Math.cos(xy * sensitivity), -1 * Math.sin(xy * sensitivity), 0}, {Math.sin(xy * sensitivity), Math.cos(xy * sensitivity), 0}, {0, 0, 1}});
        vec = vec.multiply(Math.cos(z * sensitivity));
        vec.add(left.cross(facing).multiply(Math.sin(z * sensitivity)));
        vec.add(left.multiply(left.dot(facing)).multiply(1 - Math.cos(z * sensitivity)));
        vec.multByMatrix(zAxisRotate);
        if (vec.k < -0.99 || vec.k > 0.99) {
            ThreeDVector temp = new ThreeDVector(vec.i, vec.j, 0).toUnit().multiply(0.141067);
            temp.k = 0.99 * Math.signum(vec.k);
            vec = temp;
        }
        toTurnX = 0;
        toTurnY = 0;
        this.rotateTo(vec);
    }

    public void rotateTo(ThreeDVector vec) {
        facing.changeDirection(vec.getUnit());    // Called by mouseMovement, sets new facing vector, then sets left and forwards accordingly
        left = new ThreeDVector(-1 * facing.j, facing.i, 0).toUnit();
        up = facing.cross(left);
        forward = new ThreeDVector(facing.i, facing.j, 0).toUnit();
        matrix = new ThreeMatrix(facing, left, facing.cross(left));
        originNew = facing.getOrigin().multByMatrix(matrix);
    }

    public void setFov(double fov) {
        this.fov = fov;
        distScale = 0.5 / Math.tan(fov / 2);
    }

    public double getFov() {
        return fov;
    }

    public void accelerate() {
        int ws = (w) ? 1 : 0;
        ws -= (s) ? 1 : 0;
        if (ws > 0 && shift) ws = 2;
        int ad = (d) ? 1 : 0;
        ad -= (a) ? 1 : 0;
        int z = (space) ? 1 : 0;
        z -= (ctrl) ? 1 : 0;
        //System.out.println("w: " + w + "   a: " + a + "   s: " + s + "   d: " + d + "   sp: " + space + "   ctrl: " + ctrl + "   |   ws: " + ws + "   ad: " + ad + "   z: " + z);
        velocity.add(forward.multiply(ws * 0.05));   // parameters are -1 for opposite direction being pressed,
        velocity.add(left.multiply(ad * 0.05));       // 0 for not being pressed, and 1 for main direction pressed
        velocity.k += z * 0.05;

        if (velocity.length() >= 0.5) velocity = velocity.multiply(0.5 / velocity.length());   // Implement a cap to how fast you can go, by scaling the vector back but keeping the same direction
    }

    public void move(ThreeDVector velocity) {
        facing.move(velocity);
    }

    public void moveTo(ThreeDPoint p) {
        facing.moveTo(p);           // Change position of camera to new point
    }

    public ThreeDPoint getPos() {
        return facing.getOrigin();  // Return pos of the camera
    }

    public ThreeDVector getFacing() {
        return facing;              // Return vector of where camera is facing
    }

    public String print() {
        return velocity.printNoPt() + " | " + facing.getOrigin().print();
    }

    public ThreeDVector getVelocity() {
        return velocity;
    }

    public void setOriginNew(ThreeDPoint pt) {
        originNew = pt;
    }
}

class moverThread extends Thread {
    private Camera cam;
    private boolean running = true;
    private DisplayRender.CustomPanel customPanel;

    public moverThread(Camera cam) {
        this.cam = cam;
    }

    public void end() {
        running = false;
    }

    @Override
    public void run() {
        System.out.println("Starting Mover Thread");
        while (running) {
            cam.mouseMovement(cam.toTurnX, cam.toTurnY);
            cam.move(cam.getVelocity());
            cam.setOriginNew(cam.getFacing().getOrigin().multByMatrix(cam.getMatrix()));
            ThreeDVector dir = cam.getVelocity().getUnit().multiply(0.02);
            cam.accelerate();
            cam.getVelocity().add(new ThreeDVector(-1 * (Math.min(Math.abs(cam.getVelocity().i), Math.abs(dir.i))) * Math.signum(cam.getVelocity().i), -1 * (Math.min(Math.abs(cam.getVelocity().j), Math.abs(dir.j))) * Math.signum(cam.getVelocity().j), -1 * (Math.min(Math.abs(cam.getVelocity().k), Math.abs(dir.k))) * Math.signum(cam.getVelocity().k)));
            if (cam.customPanel != null) cam.customPanel.repaint();
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Mover Thread Closing");
    }
}
