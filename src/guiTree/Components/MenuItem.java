package guiTree.Components;

import guiTree.Helper.Point2;
import guiTree.Visual;

public abstract class MenuItem extends Visual {
    public abstract Point2<Integer> getClosedSize();

    public abstract Point2<Integer> getOpenedSize();

    public abstract void setClosedSize(Integer width, Integer height);

    public abstract void setOpenedSize(Integer width, Integer height);

    public abstract int getClosedWidth();

    public abstract int getClosedHeight();

    public abstract int getOpenedWidth();

    public abstract int getOpenedHeight();

    public void setClosedWidth(Integer width) {
        setClosedSize(width, getClosedHeight());
    }

    public void setClosedHeight(Integer height) {
        setClosedSize(getClosedWidth(), height);
    }

    public void setOpenedWidth(Integer width) {
        setOpenedSize(width, getOpenedHeight());
    }

    public void setOpenedHeight(Integer height) {
        setOpenedSize(getOpenedWidth(), height);
    }
}
