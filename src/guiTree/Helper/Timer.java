package guiTree.Helper;

public class Timer {
    private long now;
    private long prev;

    public void startTiming() {
        prev = System.currentTimeMillis();
    }

    public long stopTiming() {
        now = System.currentTimeMillis();
        return now - prev;
    }

    public long getTime() {
        return System.currentTimeMillis() - prev;
    }
}
