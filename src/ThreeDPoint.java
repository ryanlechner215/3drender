public class ThreeDPoint {
    public ThreeDVector vec;

    public ThreeDPoint(double x, double y, double z) {
        vec = new ThreeDVector(x, y, z);
    }

    public TwoDPoint ThreeDToTwoDPoint(Camera cam) {
        ThreeMatrix matrix = cam.getMatrix();
        ThreeDPoint adjThis = this.multByMatrix(matrix);
        ThreeDPoint camPt = cam.getOriginNew();
        double distBtwnCamNPt = adjThis.vec.i - camPt.vec.i;
        if (distBtwnCamNPt < 0) {
            //System.out.println("Behind cam point!");
            // Handle behind screen points
            return null;
        }
        double scaleX = (distBtwnCamNPt) / cam.getDistScale();
        double scaleY = scaleX / cam.getAspectRatio();
        TwoDPoint convertedPt = new TwoDPoint(((adjThis.vec.j - camPt.vec.j) / scaleX + 0.5) * cam.width, ((camPt.vec.k - adjThis.vec.k) / scaleY + 0.5) * cam.height);
        if (convertedPt.x < 0 || convertedPt.x > cam.width || convertedPt.y < 0 || convertedPt.y > cam.height) {
            //System.out.println("Off-Screen Point!");
            // Handle off-screen points
            //return null;
        }
        return convertedPt;
    }

    public static double distance(ThreeDPoint a, ThreeDPoint b) {
        return Math.sqrt(Math.pow(b.vec.i - a.vec.i, 2) + Math.pow(b.vec.j - a.vec.j, 2) + Math.pow(b.vec.k - a.vec.k, 2));   // Normal point to point calculation
    }

    public ThreeDPoint multByMatrix(ThreeMatrix matrix) {
        return new ThreeDPoint(vec.dot(matrix.getColumn(0)), vec.dot(matrix.getColumn(1)), vec.dot(matrix.getColumn(2)));
    }

    public void move(ThreeDVector v) {
        vec.i += v.i;        // Moves point along vector
        vec.j += v.j;
        vec.k += v.k;
    }

    public ThreeDPoint midPoint(ThreeDPoint pt) {
        return new ThreeDPoint((vec.i + pt.vec.i) / 2, (vec.j + pt.vec.j) / 2, (vec.k + pt.vec.k) / 2);
    }

    public ThreeDPoint moveNew(ThreeDVector v) {
        return new ThreeDPoint(vec.i + v.i, vec.j + v.j, vec.k + v.k);
    }

    public String print() {
        return "(" + String.valueOf(vec.i) + ", " + String.valueOf(vec.j) + ", " + String.valueOf(vec.k) + ")";
    }
}
