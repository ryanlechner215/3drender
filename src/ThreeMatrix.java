public class ThreeMatrix {
    private ThreeDVector[] values = new ThreeDVector[3];

    public ThreeMatrix(double[][] values) {
        this.values[0] = new ThreeDVector(values[0][0], values[0][1], values[0][2]);
        this.values[1] = new ThreeDVector(values[1][0], values[1][1], values[1][2]);
        this.values[2] = new ThreeDVector(values[2][0], values[2][1], values[2][2]);
    }

    public ThreeMatrix(ThreeDVector x, ThreeDVector y, ThreeDVector z) {
        values[0] = new ThreeDVector(x.i, y.i, z.i);
        values[1] = new ThreeDVector(x.j, y.j, z.j);
        values[2] = new ThreeDVector(x.k, y.k, z.k);
    }

    public ThreeDVector getRow(int i) {
        return values[i];
    }

    public ThreeDVector getColumn(int j) {
        return new ThreeDVector(values[0].getRow(j), values[1].getRow(j), values[2].getRow(j));
    }
}
