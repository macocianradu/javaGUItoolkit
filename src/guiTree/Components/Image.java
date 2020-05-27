package guiTree.Components;

import guiTree.Visual;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image extends Visual {
    private BufferedImage bufferedImage;

    public Image() {
    }

    public Image(String url) {
        setImage(url);
    }

    public Image(BufferedImage image) {
        setImage(image);
    }

    public void setImage(BufferedImage image) {
        this.bufferedImage = image;
    }

    public void setImage(String url) {
        try{
            bufferedImage = ImageIO.read(new File("resources\\icons\\" + url + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(BufferedImage imageBuffer) {
        Graphics2D g = imageBuffer.createGraphics();
        g.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), null);
    }
}
