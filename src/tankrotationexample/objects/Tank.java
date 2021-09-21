package tankrotationexample.objects;
import tankrotationexample.UI.GameConstants;
import tankrotationexample.UI.GameLauncher;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author anthony-pc
 */

//based upon starter code given by professor souza
public class Tank extends GameEntity {
    private int vx;
    private int vy;
    private int angle;

    private final int R = 3;
    private final int ROTATIONSPEED = 3;
    private int health = 100;


    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;
    private long lastFired = 0;
    private long speedBoost = 0;
    private boolean isSpBoosted;
    private String tag;


    private boolean noMove = false;
    private GameLauncher launcher;

    public void setGW(GameLauncher game) { //this is needed for spawning bullets
        this.launcher = game;
    }

    public void setHealth(int h) {
        this.health = h;
    }


    public Tank(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.height = 50;
        this.width = 50;
        this.rectangle = new Rectangle(x, y, img.getWidth(), img.getHeight());
    }

    public void setTag(String t) {
        this.tag = t;
    }

    public String getTag() {
        return tag;
    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void ShootPressed() {
        this.ShootPressed = true;
    }

    void unShootPressed() {
        this.ShootPressed = false;
    }


    public void update() {
        this.rectangle.setLocation(x, y);

        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }


        if (this.ShootPressed && (System.currentTimeMillis() - lastFired > 1000)) {

            this.getShell( x , y , vx, vy, angle, launcher);
            lastFired = System.currentTimeMillis();

        }
        this.noMove = false;

    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle))) * -1;
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle))) * -1;
        if (System.currentTimeMillis() - speedBoost < 5000 && this.isSpBoosted) {
            vx = (int) Math.round(4 * R * Math.cos(Math.toRadians(angle))) * -1;
            vy = (int) Math.round(4 * R * Math.sin(Math.toRadians(angle))) * -1;

        } else if (this.isSpBoosted && (System.currentTimeMillis() - speedBoost < 5000)) {
            speedBoost = 0;
            this.isSpBoosted = false;
        }
        if (!noMove) {
            x += vx;
            y += vy;
        }

        checkBorder();
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        if (System.currentTimeMillis() - speedBoost < 5000 && isSpBoosted) {
            vx = (int) Math.round(4 * R * Math.cos(Math.toRadians(angle)));
            vy = (int) Math.round(4 * R * Math.sin(Math.toRadians(angle)));
        } else if (this.isSpBoosted && (System.currentTimeMillis() - speedBoost < 5000)) {
            speedBoost = 0;
            this.isSpBoosted = false;
        }
        if (!noMove) {
            x += vx;
            y += vy;
        }

        checkBorder();
    }

    @Override
    public void setAngle(int angle) {
        this.angle = angle;
    }

    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.WORLD_SCREEN_WIDTH - 88) {
            x = GameConstants.WORLD_SCREEN_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= GameConstants.WORLD_SCREEN_HEIGHT - 80) {
            y = GameConstants.WORLD_SCREEN_HEIGHT - 80;
        }
    }


    private void getShell(int x, int y, int vx, int vy, int angle, GameLauncher gw) {
        Shell blt = new Shell(x, y, angle);
        blt.setOwner(tag);
        gw.addGame_object(blt);

    }

    public void collision() {
        this.removeHealth(10);

    }

    void addHealth(int val) {
        if (health + val > 100) {
            health = 100;
        } else {
            health += val;
        }

    }

    private void removeHealth(int val) {
        if (health - val < 0) {
            health = 0;
        } else {
            health -= val;
        }
    }

    public int getHealth() {
        return this.health;
    }


    public void drawImage(Graphics2D g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        if (this.health != 0) {
            g.drawImage(this.img, rotation, null);
        }
    }

    public Rectangle getOffsetBounds() {
        return new Rectangle(x + vx, y + vy, 50, 50);
    }

    public void setSpeedBoost(long speedBoost) {
        this.speedBoost = speedBoost;
    }

    public void setSpBoost(boolean boost) {
       this.isSpBoosted = boost;
    }
    public void setNoMove(boolean dont_move){
        this.noMove = dont_move;
    }




}
