package tankrotationexample.UI;

import java.awt.*;

public class StartPanel {

    public void drawImage(Graphics grph) {
        Font f = new Font("arial", Font.BOLD, 55);
        grph.setColor(Color.GREEN);
        grph.setFont(f);
        grph.drawString("START", 380, 470);
        grph.drawString("EXIT", 395, 560);
        grph.setColor(Color.white);
        grph.drawRoundRect(360, 416, 210, 70, 20, 20);
        grph.drawRoundRect(360, 506, 210, 70, 20, 20);
    }
}
