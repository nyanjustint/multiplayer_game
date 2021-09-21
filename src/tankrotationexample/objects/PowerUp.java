package tankrotationexample.objects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PowerUp extends GameEntity {
    public boolean isHealthBoost = false;
    public boolean isSpeedBoost = false;
    boolean isActive = true;
    static private BufferedImage health_img;
    static private BufferedImage speed_img;

    public PowerUp(int x, int y, boolean isHealth, boolean isSpeed){
        this.x = x;
        this.y = y;
        this.rectangle = new Rectangle(x, y, 40, 40);
        this.isHealthBoost = isHealth;
        this.isSpeedBoost = isSpeed;
    }
    public static void setHealth_img(BufferedImage img){
        PowerUp.health_img = img;
    }

    public static void setSpeed_img(BufferedImage speed_img) {
        PowerUp.speed_img = speed_img;
    }

    @Override
    public void update() {

    }

    @Override
    public void drawImage(Graphics2D g) {
        if(this.isHealthBoost){
            g.drawImage(health_img, x,y, 40,40 , null);
        }
        if(this.isSpeedBoost){
            g.drawImage(speed_img, x,y, 40,40 , null);
        }
    }

    @Override
    public void collision() {
        this.isActive = false;
    }
}
