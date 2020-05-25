package guiTree.Components;

import guiTree.Helper.Point2;
import guiTree.Visual;

public abstract class MenuItem extends Visual {
    public abstract Point2<Integer> getClosedSize();

    public abstract Point2<Integer> getOpenedSize();

    public abstract void setClosedSize(Integer width, Integer height);

    public abstract void setOpenedSize(Integer width, Integer height);

    public void setClosedWidth(Integer width) {
        setClosedSize(width, getClosedSize().y);
    }

    public void setClosedHeight(Integer height) {
        setClosedSize(getClosedSize().x, height);
    }

    public void setOpenedWidth(Integer width) {
        setOpenedSize(width, getOpenedSize().y);
    }

    public void setOpenedHeight(Integer height) {
        setOpenedSize(getOpenedSize().x, height);
    }
}
