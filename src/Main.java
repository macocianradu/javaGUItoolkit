import guiTree.Components.Button;
import guiTree.Components.Picture;
import guiTree.Window;
import guiTree.events.MouseAdapter;
import parser.XAMLParser;

import java.awt.event.MouseEvent;

public class Main {
    public static void main(String[] args) {
        Window window = null;
        try {
            window = XAMLParser.parse("ui.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert window != null;
        Button topLeft  = (Button) window.findByName("topLeft");
        Button topCenter  = (Button) window.findByName("topCenter");
        Button topRight  = (Button) window.findByName("topRight");
        Button middleLeft  = (Button) window.findByName("middleLeft");
        Button middleCenter  = (Button) window.findByName("middleCenter");
        Button middleRight  = (Button) window.findByName("middleRight");
        Button bottomLeft  = (Button) window.findByName("bottomLeft");
        Button bottomCenter  = (Button) window.findByName("bottomCenter");
        Button bottomRight  = (Button) window.findByName("bottomRight");
        Picture picture = (Picture) window.findByName("Image");

        topLeft.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                picture.setLocation("top_left");
                picture.update();
            }
        });

        topRight.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                picture.setLocation("top_right");
                picture.update();
            }
        });

        topCenter.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                picture.setLocation("top_center");
                picture.update();
            }
        });

        middleLeft.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                picture.setLocation("middle_left");
                picture.update();
            }
        });

        middleRight.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                picture.setLocation("middle_right");
                picture.update();
            }
        });

        middleCenter.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                picture.setLocation("middle_center");
                picture.update();
            }
        });

        bottomLeft.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                picture.setLocation("bottom_left");
                picture.update();
            }
        });

        bottomRight.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                picture.setLocation("bottom_right");
                picture.update();
            }
        });

        bottomCenter.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                picture.setLocation("bottom_center");
                picture.update();
            }
        });
    }
}
