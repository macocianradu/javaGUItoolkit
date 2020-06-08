package guiTree.Components;

import guiTree.Visual;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Picture extends Visual {
    private BufferedImage bufferedImage;

    public Picture() {
    }

    public Picture(String url) {
        setImage(url);
    }

    public Picture(BufferedImage image) {
        setImage(image);
    }

    public void setImage(BufferedImage image) {
        this.bufferedImage = image;
    }

    public void setImage(String url) {
        try{
            bufferedImage = ImageIO.read(new File("resources\\images\\" + url + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Image imageBuffer) {
        Graphics2D g = (Graphics2D)imageBuffer.getGraphics();
        g.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
    }
}
