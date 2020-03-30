package guiTree.Helper;

public class Point2<T> {
    public T x;
    public T y;

    public Point2(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Point2<T> point2) {
        return x == point2.y && y == point2.y;
    }
}
