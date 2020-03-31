package guiTree.Helper;

public class Point3<T> {
    public T x;
    public T y;
    public T z;

    public Point3(T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(Point3<T> point3) {
        return x == point3.x && y == point3.y && z == point3.z;
    }

    @Override
    public String toString() {
        return "Point2 x:" + x + " y: " + y + " z: " + z;
    }
}
