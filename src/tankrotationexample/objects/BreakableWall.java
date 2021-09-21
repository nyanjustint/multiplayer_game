package tankrotationexample.objects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends GameEntity {
    private int health = 100;
    private static BufferedImage breakable_wall_img;
    private boolean dead = false;

    public BreakableWall(int x, int y) {
        this.x = x;
        this.y = y;
        this.rectangle = new Rectangle(x, y, breakable_wall_img.getWidth(), breakable_wall_img.getHeight());
    }


    private void removeHealth(int val) {
        if (health - val < 0) {
            health = 0;
            dead = true;
        } else {
            health -= val;
        }
    }

    public int getHealth() {
        return this.health;
    }



    public static void set_breakable_wall_img(BufferedImage image) {
        BreakableWall.breakable_wall_img = image;
    }

    @Override
    public void update() {

    }

    @Override
    public void collision() {
        this.removeHealth(40);

    }

    @Override
    public void drawImage(Graphics2D g2d) {

        if (!dead) {
            g2d.drawImage(breakable_wall_img, x, y, null);
        }

    }
}
