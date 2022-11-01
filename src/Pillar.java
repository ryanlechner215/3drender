public class Pillar {
    private int[] coords = new int[6];
    private ThreeDPoint[] points = new ThreeDPoint[8];

    public Pillar(int x, int y, int z, int breadth, int height) {
        coords[0] = x;   // Origin vertex
        coords[1] = y;
        coords[2] = z;
        coords[3] = x + breadth;   // Opposite vertex
        coords[4] = y + breadth;
        coords[5] = z + height;
        makePoints();
    }

    private void makePoints() {
        points[0] = new ThreeDPoint(coords[0], coords[1], coords[2]);    // Probably inefficient, but assigns coordinates to all of the vertices
        points[1] = new ThreeDPoint(coords[3], coords[1], coords[2]);
        points[2] = new ThreeDPoint(coords[3], coords[4], coords[2]);
        points[3] = new ThreeDPoint(coords[0], coords[4], coords[2]);
        points[4] = new ThreeDPoint(coords[0], coords[1], coords[5]);
        points[5] = new ThreeDPoint(coords[3], coords[1], coords[5]);
        points[6] = new ThreeDPoint(coords[3], coords[4], coords[5]);
        points[7] = new ThreeDPoint(coords[0], coords[4], coords[5]);
    }

    public void setHeight(int height) {
        coords[5] = coords[2] + height;
        makePoints();
    }

    public int getHeight() {
        return coords[5] - coords[2];
    }

    public void moveBase(int moveX, int moveY) {
        coords[0] += moveX;
        coords[1] += moveY;
        coords[3] += moveX;
        coords[4] += moveY;
        makePoints();
    }

    public void moveBaseTo(int x, int y) {
        int breadth = coords[3] - coords[0];
        coords[0] = x;
        coords[1] = y;
        coords[3] = x + breadth;
        coords[4] = y + breadth;
        makePoints();
    }

    public ThreeDPoint[] getSide(int side) {
        if (side > 5 || side < 0) return null;
        if (side == 0) return new ThreeDPoint[]{points[0], points[1], points[2], points[3]};    // Also probably inefficient, but returns 4 vertices of a side when requested
        if (side == 1) return new ThreeDPoint[]{points[0], points[3], points[7], points[4]};
        if (side == 2) return new ThreeDPoint[]{points[0], points[1], points[5], points[4]};
        if (side == 3) return new ThreeDPoint[]{points[2], points[3], points[7], points[6]};
        if (side == 4) return new ThreeDPoint[]{points[1], points[2], points[6], points[5]};
        else return new ThreeDPoint[]{points[4], points[5], points[6], points[7]};
    }

    public int[] getSideX(int side, Camera cam) {
        if (side > 5 || side < 0) return null;
        TwoDPoint[] pts = new TwoDPoint[8];
        pts[0] = points[0].ThreeDToTwoDPoint(cam);
        pts[1] = points[1].ThreeDToTwoDPoint(cam);
        pts[2] = points[2].ThreeDToTwoDPoint(cam);
        pts[3] = points[3].ThreeDToTwoDPoint(cam);
        pts[4] = points[4].ThreeDToTwoDPoint(cam);
        pts[5] = points[5].ThreeDToTwoDPoint(cam);
        pts[6] = points[6].ThreeDToTwoDPoint(cam);
        pts[7] = points[7].ThreeDToTwoDPoint(cam);
        for (TwoDPoint pt : pts) {
            if (pt == null) {
                //System.out.println("Behind cam point!");
                return new int[]{0, 0, 0, 0};
            }
        }
        if (side == 0) return new int[]{(int) pts[0].x, (int) pts[1].x, (int) pts[2].x, (int) pts[3].x};    // Also probably inefficient, but returns 4 vertices of a side when requested
        if (side == 1) return new int[]{(int) pts[0].x, (int) pts[3].x, (int) pts[7].x, (int) pts[4].x};
        if (side == 2) return new int[]{(int) pts[0].x, (int) pts[1].x, (int) pts[5].x, (int) pts[4].x};
        if (side == 3) return new int[]{(int) pts[2].x, (int) pts[3].x, (int) pts[7].x, (int) pts[6].x};
        if (side == 4) return new int[]{(int) pts[1].x, (int) pts[2].x, (int) pts[6].x, (int) pts[5].x};
        else return new int[]{(int) pts[4].x, (int) pts[5].x, (int) pts[6].x, (int) pts[7].x};
    }

    public int[] getSideY(int side, Camera cam) {
        if (side > 5 || side < 0) return null;
        TwoDPoint[] pts = new TwoDPoint[8];
        pts[0] = points[0].ThreeDToTwoDPoint(cam);
        pts[1] = points[1].ThreeDToTwoDPoint(cam);
        pts[2] = points[2].ThreeDToTwoDPoint(cam);
        pts[3] = points[3].ThreeDToTwoDPoint(cam);
        pts[4] = points[4].ThreeDToTwoDPoint(cam);
        pts[5] = points[5].ThreeDToTwoDPoint(cam);
        pts[6] = points[6].ThreeDToTwoDPoint(cam);
        pts[7] = points[7].ThreeDToTwoDPoint(cam);
        for (TwoDPoint pt : pts) {
            if (pt == null) {
                //System.out.println("Behind cam point!");
                return new int[]{0, 0, 0, 0};
            }
        }
        if (side == 0) return new int[]{(int) pts[0].y, (int) pts[1].y, (int) pts[2].y, (int) pts[3].y};    // Also probably inefficient, but returns 4 vertices of a side when requested
        if (side == 1) return new int[]{(int) pts[0].y, (int) pts[3].y, (int) pts[7].y, (int) pts[4].y};
        if (side == 2) return new int[]{(int) pts[0].y, (int) pts[1].y, (int) pts[5].y, (int) pts[4].y};
        if (side == 3) return new int[]{(int) pts[2].y, (int) pts[3].y, (int) pts[7].y, (int) pts[6].y};
        if (side == 4) return new int[]{(int) pts[1].y, (int) pts[2].y, (int) pts[6].y, (int) pts[5].y};
        else return new int[]{(int) pts[4].y, (int) pts[5].y, (int) pts[6].y, (int) pts[7].y};
    }

    public int[][] getSidesX(Camera cam) {
        int[] order = {0, 1, 2, 3, 4, 5};
        double[] dists = {ThreeDPoint.distance(cam.getPos(), points[0].midPoint(points[2])),
                ThreeDPoint.distance(cam.getPos(), points[0].midPoint(points[7])),
                ThreeDPoint.distance(cam.getPos(), points[0].midPoint(points[5])),
                ThreeDPoint.distance(cam.getPos(), points[2].midPoint(points[7])),
                ThreeDPoint.distance(cam.getPos(), points[1].midPoint(points[6])),
                ThreeDPoint.distance(cam.getPos(), points[4].midPoint(points[6]))};
        for (int i = 0; i < 6; i++) {
            double maxDist = Double.MIN_VALUE;
            int max = i;
            for(int j = i; j < 6; j++) {
                if (j == i) maxDist = dists[j];
                else if (dists[j] > maxDist) {
                    maxDist = dists[j];
                    max = j;
                }
            }
            int tempInt = order[i];
            order[i] = order[max];
            order[max] = tempInt;
            double tempDouble = dists[i];
            dists[i] = dists[max];
            dists[max] = tempDouble;
        }
        int[][] ret = new int[6][4];
        for (int i = 0; i < 6; i++) {
            ret[i] = getSideX(order[i], cam);
        }

        return ret;
    }

    public int[][] getSidesY(Camera cam) {
        int[] order = {0, 1, 2, 3, 4, 5};
        double[] dists = {ThreeDPoint.distance(cam.getPos(), points[0].midPoint(points[2])),
                ThreeDPoint.distance(cam.getPos(), points[0].midPoint(points[7])),
                ThreeDPoint.distance(cam.getPos(), points[0].midPoint(points[5])),
                ThreeDPoint.distance(cam.getPos(), points[2].midPoint(points[7])),
                ThreeDPoint.distance(cam.getPos(), points[1].midPoint(points[6])),
                ThreeDPoint.distance(cam.getPos(), points[4].midPoint(points[6]))};
        for (int i = 0; i < 6; i++) {
            double maxDist = Double.MIN_VALUE;
            int max = i;
            for(int j = i; j < 6; j++) {
                if (j == i) maxDist = dists[j];
                else if (dists[j] > maxDist) {
                    maxDist = dists[j];
                    max = j;
                }
            }
            int tempInt = order[i];
            order[i] = order[max];
            order[max] = tempInt;
            double tempDouble = dists[i];
            dists[i] = dists[max];
            dists[max] = tempDouble;
        }
        int[][] ret = new int[6][4];
        for (int i = 0; i < 6; i++) {
            ret[i] = getSideY(order[i], cam);
        }

        return ret;
    }
}
