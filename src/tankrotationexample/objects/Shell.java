package tankrotationexample.objects;

import tankrotationexample.UI.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class Shell extends GameEntity {
    private String bulletOwner;
    private boolean isInActive = false;
    public boolean collided = false;
    static private BufferedImage bullet_img;
    static private BufferedImage explosion_img;
    private int iter = 0;

    public String getOwner() {
        return this.bulletOwner;
    }

    void setOwner(String owner) {
        this.bulletOwner = owner;
    }

    public boolean getIsInactive() {
        return this.isInActive;
    }

    public static void setBufferedImage(BufferedImage img) {
        bullet_img = img;
    }

    public static void setExplosionImage(BufferedImage exp) {
        explosion_img = exp;
    }


    Shell(int x, int y, int angle) {
        this.x = x;
        this.y = y;
        this.vx = (int) Math.round(3 * Math.cos(Math.toRadians(angle)));
        this.vy = (int) Math.round(3 * Math.sin(Math.toRadians(angle)));
        this.angle = angle;
        this.rectangle = new Rectangle(x, y, bullet_img.getWidth(), bullet_img.getHeight());
    }

    public void update() {
        if (!collided) {
            this.x = x + vx;
            this.y = y + vy;
            this.checkBorder();
        }
        else {
            iter++;
        }
        this.rectangle.setLocation(x,y);
    }


    public void drawImage(Graphics2D g2d) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), bullet_img.getWidth() / 2.0, bullet_img.getHeight() / 2.0);

        if(collided) {
            g2d.drawImage(explosion_img, rotation, null);

            if (iter >= 6) {
                this.isInActive = true;
            }

        }

        else {
            g2d.drawImage(bullet_img, rotation, null);
        }
    }

    public void collision() {
        collided = true;
    }

    private void checkBorder() {
        int left_limit = 30;
        if (x < left_limit) {
            this.isInActive = true;
        }
        int right_limit = GameConstants.WORLD_SCREEN_WIDTH - 65;
        if (x >= right_limit) {
            this.isInActive = true;
        }
        int lower_limit = 40;
        if (y < lower_limit) {
            this.isInActive = true;
        }
        int upper_limit = GameConstants.WORLD_SCREEN_HEIGHT - 60;
        if (y >= upper_limit) {
            this.isInActive = true;
        }
    }
}

