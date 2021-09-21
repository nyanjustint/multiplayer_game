package tankrotationexample.objects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends GameEntity {
    private boolean isBG;
    private static BufferedImage background_img;
    private static BufferedImage unbreakable_wall_img;


    public static void set_unbreakable_wall_img(BufferedImage image) {
        unbreakable_wall_img = image;
    }


    public static void setBackground_img(BufferedImage image) {
        background_img = image;
    }

    public Wall(int x, int y, boolean isBG) {
        this.x = x;
        this.y = y;
        this.isBG = isBG;
        this.rectangle = new Rectangle(x, y, 32, 32);

    }

    @Override
    public void update() {

    }

    @Override
    public void collision() {

    }

    @Override
    public void drawImage(Graphics2D g2d) {

        if (this.isBG) {

            g2d.drawImage(background_img, x, y, null);

        } else {
            g2d.drawImage(unbreakable_wall_img, x, y, null);
        }

    }
}
