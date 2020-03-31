package guiTree.Helper;

public class Point4<T> {
    public T a;
    public T b;
    public T c;
    public T d;

    public Point4(T a, T b, T c, T d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public boolean equals(Point4<T> point4) {
        return a == point4.a && b == point4.b && c == point4.c && d == point4.d;
    }

    @Override
    public String toString() {
        return "Point2 a:" + a + " b: " + b + " c: " + c + " d: " + d;
    }
}
