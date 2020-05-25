package guiTree.Helper;

public class Point2<T extends Comparable<T>> implements Comparable<Point2<T>>{
    public T x;
    public T y;

    public Point2(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public Point2(Point2<T> point2) {
        x = point2.x;
        y = point2.y;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Point2<?>)) {
            return false;
        }
        Point2<?> point2 = (Point2<?>)obj;
        return point2.x.equals(x) && point2.y.equals(y);
    }

    @Override
    public String toString() {
        return "Point2 x:" + x + " y: " + y;
    }

    @Override
    public int hashCode() {
        return (x.toString() + ", " + y.toString()).hashCode();
    }

    @Override
    public int compareTo(Point2<T> tPoint2) {
        int cmp = y.compareTo(tPoint2.y);
        if(cmp == 0) {
            return x.compareTo(tPoint2.x);
        }
        return cmp;
    }
}
