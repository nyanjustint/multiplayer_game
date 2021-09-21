package tankrotationexample.UI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class KeyReader implements MouseListener {


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mouse_x_coord = e.getX();
        int mouse_y_coord = e.getY();


        if (mouse_x_coord >= 360 && mouse_x_coord <= 360 + 210) {
            if (mouse_y_coord >= 416 && mouse_y_coord <= 416 + 70 ) {
                GameLauncher.gameState = GameLauncher.Stage.GAME_STAGE;
            }
            else if (mouse_y_coord >= 416 + 90 + 90 && mouse_y_coord <= 416 + 90 + 90 + 70 && GameLauncher.gameState == GameLauncher.Stage.START_STAGE) {
                GameLauncher.gameState = GameLauncher.Stage.EXIT_STAGE;
            }
        }



    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
