public class TwoDPoint {
    public double x;
    public double y;

    public TwoDPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String print() {
        return "(" + String.valueOf(x) + ", " + String.valueOf(y) + ")";
    }

}
