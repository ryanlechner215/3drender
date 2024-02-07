import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.Random;


public class DisplayRender extends JFrame {
    int width;
    int height;
    Camera cam;
    CustomPanel customPanel;
    int n = 40;
    int m = 40;



    public TwoDPoint[] ThreeDToTwoDShape(ThreeDPoint[] points, int numPoints, Camera cam) {
        TwoDPoint[] ret = new TwoDPoint[numPoints];
        for (int i = 0; i < numPoints; i++) {
            ret[i] = points[i].ThreeDToTwoDPoint(cam);
        }
        return ret;
    }

    public DisplayRender() {
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        cam = new Camera((double) dim.width / (double) dim.height, 0.002);

        Random rand = new Random();
        Pillar[][] pillars = new Pillar[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                //pillars[i][j] = new Pillar(i * 5, j * 5, 0, 4, 1 + i * i + j * j);
                pillars[i][j] = new Pillar(i * 5, j * 5, 0, 4, rand.nextInt(100) + 5);
            }
        }

        CustomPanel customPanel = new CustomPanel(cam, pillars);
        cam.addPanel(customPanel);
        this.customPanel = customPanel;
        this.cam = cam;
        add(customPanel);
        addKeyListener(new MyKeyListener(cam, pillars));

        this.width = (int) dim.getWidth();
        this.height = (int) dim.getHeight();

        setSize(width / 2, height / 2);
        setTitle("3D Render");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setCursor( getToolkit().createCustomCursor(new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ), new Point(), null ));
    }

    public static void main(String[] args) throws InterruptedException {
        DisplayRender ex = new DisplayRender();
    }

    public class CustomPanel extends JPanel {

        Pillar[][] pillars;
        Camera cam;

        CustomPanel(Camera cam, Pillar[][] pillars) {
            this.cam = cam;
            this.pillars = pillars;
            MouseAdapter ma = new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    cam.toTurnX += e.getX() + 8 - (cam.width / 2);
                    cam.toTurnY += e.getY() + 7 - (cam.height / 2);
                    try{
                        Robot robot = new Robot();
                        robot.mouseMove(cam.width, cam.height);
                    }catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

            };
            addMouseListener(ma);
            addMouseMotionListener(ma);
        }

        public void sortPillars(Pillar[][] pillarsCopy) {
            boolean sorting = true;
            while (sorting) {
                sorting = false;
                Pillar temp;
                for (int i = 0; i < n * m - 1; i++) {
                    for (int j = 0; j < n * m - 1 - i; j++) {
                        if (ThreeDPoint.distance((pillarsCopy[j / m][j % m].getSide(0)[0]), cam.getPos()) < ThreeDPoint.distance((pillarsCopy[(j + 1) / m][(j + 1) % m].getSide(0)[0]), cam.getPos())) {
                            temp = pillarsCopy[(j + 1) / m][(j + 1) % m];
                            pillarsCopy[(j + 1) / m][(j + 1) % m] = pillarsCopy[j / m][j % m];
                            pillarsCopy[j / m][j % m] = temp;
                        }
                    }
                }
            }
        }

        public void drawStuff(Graphics2D g2d) {

            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, cam.width, cam.height);
            g2d.setStroke(new BasicStroke((float) 0.5));

            Pillar[][] pillarsCopy = new Pillar[pillars.length][pillars[0].length];
            for (int i = 0; i < pillars.length; i++) {
                System.arraycopy(pillars[i], 0, pillarsCopy[i], 0, pillars[i].length);
            }
            sortPillars(pillarsCopy);

            for (Pillar[] pillars2 : pillarsCopy) {
                for (Pillar pillar : pillars2) {
                    int[][] sidesX = pillar.getSidesX(cam);
                    int[][] sidesY = pillar.getSidesY(cam);

                    for (int i = 0; i < 6; i++) {
                        g2d.setColor(Color.GRAY);
                        g2d.fillPolygon(sidesX[i], sidesY[i], sidesX[i].length);
                        g2d.setColor(Color.BLACK);
                        g2d.drawPolygon(sidesX[i], sidesY[i], sidesX[i].length);
                    }
                }
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            drawStuff(g2d);

            g2d.dispose();
        }
    }

    public class MyKeyListener extends KeyAdapter {
        Camera cam;
        Pillar[][] pillars;

        public MyKeyListener(Camera cam, Pillar[][] pillars) {
            this.cam = cam;
            this.pillars = pillars;
        }

        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == 'p') {
                SortThread sortThread = new SortThread(pillars, 0, 0);
                sortThread.start();
            }
        }

        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == 'w') {
                cam.w = true;
            }
            if (e.getKeyChar() == 'a') {
                cam.a = true;
            }
            if (e.getKeyChar() == 's') {
                cam.s = true;
            }
            if (e.getKeyChar() == 'd') {
                cam.d = true;
            }
            if (e.getKeyChar() == ' ') {
                cam.space = true;
            }
            if (e.getKeyCode() == 17) {
                cam.ctrl = true;
            }
            if (e.getKeyChar() == 16) {
                cam.shift = true;
            }
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyChar() == 'w') {
                cam.w = false;
            }
            if (e.getKeyChar() == 'a') {
                cam.a = false;
            }
            if (e.getKeyChar() == 's') {
                cam.s = false;
            }
            if (e.getKeyChar() == 'd') {
                cam.d = false;
            }
            if (e.getKeyChar() == ' ') {
                cam.space = false;
            }
            if (e.getKeyCode() == 17) {
                cam.ctrl = false;
            }
            if (e.getKeyChar() == 16) {
                cam.shift = false;
            }
        }
    }

    class SortThread extends Thread {
        private Pillar[][] pillars;
        private int sortDelay;
        private int sortDelayNano;

        public SortThread(Pillar[][] pillars, int sortDelay, int sortDelayNano) {
            this.pillars = pillars;
            this.sortDelay = sortDelay;
            this.sortDelayNano = sortDelayNano;
        }

        @Override
        public void run() {
            System.out.println("Starting Sorting Thread");
            bubbleSort(n, m);
            System.out.println("Sorting Thread Ending");
        }

        private void bubbleSort(int n, int m) {
            boolean sorting = true;
            while (sorting) {
                sorting = false;
                int temp;
                for (int i = 0; i < n * m - 1; i++) {
                    for (int j = 0; j < n * m - 1 - i; j++) {
                        if (pillars[j / m][j % m].getHeight() > pillars[(j + 1) / m][(j + 1) % m].getHeight()) {
                            temp = pillars[(j + 1) / m][(j + 1) % m].getHeight();
                            pillars[(j + 1) / m][(j + 1) % m].setHeight(pillars[j / m][j % m].getHeight());
                            pillars[j / m][j % m].setHeight(temp);
                        }
                    }
                    try {
                        Thread.sleep(0, 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}