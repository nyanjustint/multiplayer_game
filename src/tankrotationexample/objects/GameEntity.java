package tankrotationexample.objects;
import java.awt.*;

public abstract class GameEntity {
    int x;
    int y;
    int vx;
    int vy;
    int angle;
    int height;
    int width;

    public Rectangle rectangle;

    public void setX(int x_to_set) {
        this.x = x_to_set;
    }

    public int getX() {
        return this.x;
    }

    public void setY(int y_to_set) {
        this.y = y_to_set;
    }

    public int getY() {
        return this.y;
    }

    int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public abstract void update();

    public abstract void drawImage(Graphics2D g);

    public abstract void collision();

}
