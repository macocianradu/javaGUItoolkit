package guiTree.Components;

import guiTree.Visual;

public class RadioButtonList extends CheckBoxList{

    public void handleNotification(Visual v, int notify) {
        if(notify == CheckBox.CHECKBOX_CLICKED) {
            for(CheckBox cb: getActiveBoxes()) {
                if(!cb.equals(v)) {
                    cb.setPressed(false);
                }
            }
        }
        update();
    }

    public int getActiveButton() {
        for(CheckBox rb: getActiveBoxes()) {
            if(rb.getPressed()) {
                return getActiveBoxes().indexOf(rb);
            }
        }
        return -1;
    }
}
