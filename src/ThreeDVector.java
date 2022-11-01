public class ThreeDVector {
    private ThreeDPoint origin;
    public double i;
    public double j;
    public double k;

    public ThreeDVector(ThreeDPoint origin, double i, double j, double k) {
        this.origin = origin;                // Making vector from a starting point in space, with i j and k hats for direction
        this.i = i;
        this.j = j;
        this.k = k;
    }

    public ThreeDVector(double i, double j, double k) {
        this.i = i;            // Making vector from just i j and k hats
        this.j = j;
        this.k = k;
    }

    public ThreeDVector() {}

    public ThreeDVector(ThreeDPoint a, ThreeDPoint b) {
        this.origin = a;             // Making vector from two points
        i = b.vec.i - a.vec.i;
        j = b.vec.j - a.vec.j;
        k = b.vec.k - a.vec.k;
    }

    public ThreeDVector(ThreeDVector v) {
        this.origin = v.origin;  // For making copies of vectors
        this.i = v.i;
        this.j = v.j;
        this.k = v.k;
    }

    public ThreeDVector multiply(double x) {
        return new ThreeDVector(i * x, j * x, k * x);
    }

    public double length() {
        return Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2) + Math.pow(k, 2));
    }

    public void add(ThreeDVector v) {
        i += v.i;
        j += v.j;
        k += v.k;
    }

    public void moveTo(ThreeDPoint origin) {
        this.origin = origin;
    }

    public void move(ThreeDVector vec) {
        origin.move(vec);
    }

    public void multByMatrix(ThreeMatrix matrix) {
        double x = this.dot(matrix.getRow(0));
        double y = this.dot(matrix.getRow(1));
        double z = this.dot(matrix.getRow(2));
        this.changeDirection(new ThreeDVector(x, y, z));
    }

    public void changeDirection(ThreeDVector vec) {
        i = vec.i;
        j = vec.j;
        k = vec.k;
    }

    public double getRow(int i) {
        if (i == 0) return this.i;
        if (i == 1) return j;
        else return k;
    }

    public ThreeDVector cross(ThreeDVector vec) {
        ThreeDVector ret = new ThreeDVector();
        ret.i = this.j * vec.k - this.k * vec.j;
        ret.j = this.k * vec.i - this.i * vec.k;
        ret.k = this.i * vec.j - this.j * vec.i;
        return ret;
    }

    public ThreeDPoint getOrigin() {
        return origin;
    }

    public double dot(ThreeDVector a) {
        return a.i * i + a.j * j + a.k * k;
    }

    public double distToPlanePerpendicular(ThreeDPoint pt) {
        return this.getUnit().dot(new ThreeDVector(origin, pt));
    }

    public ThreeDVector getUnit() {
        double scale = this.length(); // Magnitude of vector
        if (scale == 0) return new ThreeDVector(0, 0, 0);
        return new ThreeDVector(origin, i / scale, j / scale, k / scale);    // Divides by length to get unit vector
    }

    public ThreeDVector toUnit() {
        double scale = ThreeDPoint.distance(new ThreeDPoint(i, j, k), new ThreeDPoint(0, 0, 0));   // Same thing as getUnit function, but changes current vector as well
        i /= scale;
        j /= scale;
        k /= scale;
        return this;
    }

    public String print() {
        return origin.print() + " -> (" + String.valueOf(i) + ", " + String.valueOf(j) + ", " + String.valueOf(k) + ")";
    }

    public String printNoPt() {
        return "(" + String.valueOf(i) + ", " + String.valueOf(j) + ", " + String.valueOf(k) + ")";
    }
}
