package guiTree.Components;

import guiTree.Helper.Point2;
import guiTree.Visual;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CheckBoxList extends Visual {
    private List<CheckBox> checkBoxList;
    int spacing;
    private BufferedImage icon;
    private Point2<Integer> checkBoxSize;

    public CheckBoxList() {
        this(20);
    }

    public CheckBoxList(int spacing) {
        checkBoxList = new ArrayList<>();
        this.spacing = spacing;
    }

    @Override
    public void addVisual(Visual v) {
        if(!(v instanceof CheckBox)) {
            System.out.println("Trying to insert into checkbox list something different from checkbox");
            return;
        }
        CheckBox checkbox = (CheckBox)v;
        if(checkBoxList.size() == 0) {
            checkbox.setLocation(0, 0);

        }
        else {
            checkbox.setLocation(0, checkBoxList.get(checkBoxList.size() - 1).getLocationY() + spacing);
        }
        if(icon != null) {
            checkbox.setIcon(icon);
        }
        if(checkBoxSize != null) {
            checkbox.setSize(checkBoxSize.x, checkBoxSize.y);
        }
        checkbox.setForegroundColor(getForegroundColor());
        checkbox.setBackgroundColor(getBackgroundColor());
        checkbox.setAccentColor(getAccentColor());
        checkbox.setFontColor(getFontColor());
        checkBoxList.add(checkbox);
        super.addVisual(checkbox);
    }

    @Override
    public void removeVisual(Visual v) {
        if(v instanceof CheckBox) {
            checkBoxList.remove(v);
            super.removeVisual(v);
        }
    }

    public void setIcon(BufferedImage icon) {
        this.icon = icon;
        for(CheckBox cb: checkBoxList) {
            cb.setIcon(icon);
        }
    }

    public void setIcon(String url) {
        try {
            InputStream iconStream = getClass().getClassLoader().getResourceAsStream("icons/" + url + ".png");
            assert iconStream != null;
            icon = ImageIO.read(iconStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(CheckBox cb: checkBoxList) {
            cb.setIcon(icon);
        }
    }

    public void setCheckBoxSize(Integer width, Integer height) {
        for(CheckBox cb: checkBoxList) {
            cb.setSize(width, height);
        }
        checkBoxSize = new Point2<>(width, height);
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        for(CheckBox cb: checkBoxList) {
            cb.setFont(font);
        }
    }

    @Override
    public void setBackgroundColor(Color backgroundColor) {
        super.setBackgroundColor(backgroundColor);
        for(CheckBox cb: checkBoxList) {
            cb.setBackgroundColor(backgroundColor);
        }
    }

    @Override
    public void setForegroundColor(Color foregroundColor) {
        super.setForegroundColor(foregroundColor);
        for(CheckBox cb: checkBoxList) {
            cb.setForegroundColor(foregroundColor);
        }
    }

    @Override
    public void setFontColor(Color fontColor) {
        super.setFontColor(fontColor);
        for(CheckBox cb: checkBoxList) {
            cb.setFontColor(fontColor);
        }
    }

    @Override
    public void setAccentColor(Color accentColor) {
        super.setAccentColor(accentColor);
        for(CheckBox cb: checkBoxList) {
            cb.setAccentColor(accentColor);
        }
    }

    public void setSpacing(Integer spacing) {
        this.spacing = spacing;
        if(checkBoxList == null) {
            return;
        }
        int offsetY = 0;
        for(CheckBox cb: checkBoxList) {
            cb.setLocationY(offsetY);
            offsetY += spacing;
        }
    }

    public List<CheckBox> getActiveBoxes() {
        List<CheckBox> markedBoxes = new ArrayList<>();
        for(CheckBox cb: checkBoxList) {
            if(cb.getPressed()) {
                markedBoxes.add(cb);
            }
        }
        return markedBoxes;
    }
}
