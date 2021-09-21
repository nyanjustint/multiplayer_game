
package tankrotationexample.UI;


import tankrotationexample.objects.*;
import tankrotationexample.objects.Wall;

import javax.swing.*;
import java.awt.*;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static javax.imageio.ImageIO.read;


public class GameLauncher extends JPanel {
    private BufferedImage world;
    private Graphics2D buffer;
    private JFrame jFrame;
    private Tank t1;
    private Tank t2;
    private static BufferedImage startImg;
    private boolean player1_won = false;
    private boolean player2_won = false;
    private StartPanel startPanel;
    private ArrayList<GameEntity> entities;
    public void addGame_object(GameEntity obj) {
        this.gameEntities.add(obj);
    }

    private ArrayList<GameEntity> gameEntities = new ArrayList<>();
    private int Player1_num_lives = 2;
    private int Player2_num_lives = 2;

    //courtesy of Ahmad Shakoushy to show me to use enum when checking the game state
    //during the game or end game or start of the game in order to build the interfaces
    public enum Stage {
        START_STAGE("start"),
        GAME_STAGE("game"),
        EXIT_STAGE("exit");

        private final String stageName;
        Stage(String stageName) {
            this.stageName = stageName;
        }
        public String getStageName(){return this.stageName;}
    }

    static Stage gameState = Stage.START_STAGE;

    public static void main(String[] args) {
        GameLauncher launcher = new GameLauncher();
        launcher.init();
        try {
            while (true) {
                launcher.repaint();
                if (GameLauncher.gameState == Stage.GAME_STAGE) {
                    launcher.updateGame(launcher);
                    launcher.gameEntities = launcher.handleCollision(launcher.gameEntities);
                    launcher.t1.update();
                    launcher.t2.update();

                    Thread.sleep(1000 / 144);
                } else if (GameLauncher.gameState == Stage.EXIT_STAGE) {
                    launcher.jFrame.dispose();
                    System.exit(0);
                }
            }
        } catch (InterruptedException ignored) {

        }

    }


    private void init() {
        //given by professor souza in the starter codes
        this.jFrame = new JFrame("Tank War Game");
        this.world = new BufferedImage(GameConstants.WORLD_SCREEN_WIDTH, GameConstants.WORLD_SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        BufferedImage t1img = null, bullet_image, background_image, unbreakable_wall_img, breakable_wall_img, exp_img;

        try {

            t1img = read(Objects.requireNonNull(GameLauncher.class.getClassLoader().getResource("tank1.png")));

            unbreakable_wall_img = read(Objects.requireNonNull(GameLauncher.class.getClassLoader().getResource("Wall2.gif")));
            Wall.set_unbreakable_wall_img(unbreakable_wall_img);

            background_image = read(Objects.requireNonNull(GameLauncher.class.getClassLoader().getResource("Background.bmp")));
            Wall.setBackground_img(background_image);

            breakable_wall_img = read(Objects.requireNonNull(GameLauncher.class.getClassLoader().getResource("Wall1.gif")));
            BreakableWall.set_breakable_wall_img(breakable_wall_img);

            bullet_image = read(Objects.requireNonNull(GameLauncher.class.getClassLoader().getResource("Shell.gif")));
            Shell.setBufferedImage(bullet_image); 

            exp_img = read(Objects.requireNonNull(GameLauncher.class.getClassLoader().getResource("explosion.gif")));
            Shell.setExplosionImage(exp_img);

            GameLauncher.startImg = read(Objects.requireNonNull(GameLauncher.class.getClassLoader().getResource("title.png")));

            PowerUp.setHealth_img(read(Objects.requireNonNull(GameLauncher.class.getClassLoader().getResource("potion.png"))));
            PowerUp.setSpeed_img(read(Objects.requireNonNull(GameLauncher.class.getClassLoader().getResource("run.png"))));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        t1 = new Tank(GameConstants.TANK1_X_COORD, GameConstants.TANK1_Y_COORD, 0, 0, GameConstants.TANK1_ANGLE, t1img);
        t1.setTag("Tank1");
        t2 = new Tank(GameConstants.TANK2_X_COORD, GameConstants.TANK2_Y_COORD, 0, 0, GameConstants.TANK2_ANGLE, t1img);
        t2.setTag("Tank2");

        startPanel = new StartPanel();
        
        for (int i = 0; i <GameConstants.WORLD_SCREEN_WIDTH ; i = i + 320) {
            for (int j = 0; j < GameConstants.WORLD_SCREEN_HEIGHT; j = j + 240) {
                gameEntities.add(new Wall(i, j, true)); 
            }
        }
        
        createMap();

        gameEntities.add(t1);
        t1.setGW(this);
        gameEntities.add(t2);
        t2.setGW(this);

        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);

        addPowerUp();

        this.jFrame.setLayout(new BorderLayout());
        this.jFrame.add(this);


        this.jFrame.addKeyListener(tc1);
        this.jFrame.addKeyListener(tc2);
        this.addMouseListener(new KeyReader());

        this.jFrame.setSize(GameConstants.SPLIT_SCREEN_WIDTH + 20, GameConstants.SPLIT_SCREEN_HEIGHT + 40);
        this.jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);

        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jFrame.setVisible(true);


    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        buffer = world.createGraphics();
        super.paintComponent(g2);


        if (GameLauncher.gameState == Stage.START_STAGE) {
            (g).drawImage(startImg, 0, 0, GameConstants.SPLIT_SCREEN_WIDTH + 2, GameConstants.SPLIT_SCREEN_HEIGHT, null);
            startPanel.drawImage(g);
        }
        else if (GameLauncher.gameState == Stage.GAME_STAGE) {


            for (int i = 0; i < gameEntities.size(); i++) {

                gameEntities.get(i).drawImage(buffer);

            }
            int player1_x_Coord = t1.getX();
            int player2_x_Coord = t2.getX();
            int player1_y_Coord = t1.getY();
            int player2_y_Coord = t2.getY();


            if (player1_x_Coord < GameConstants.SPLIT_SCREEN_WIDTH / 4) {
                player1_x_Coord = GameConstants.SPLIT_SCREEN_WIDTH / 4;
            }
            if (player2_x_Coord < GameConstants.SPLIT_SCREEN_WIDTH / 4) {
                player2_x_Coord = GameConstants.SPLIT_SCREEN_WIDTH / 4;
            }
            if (player1_x_Coord > GameConstants.WORLD_SCREEN_WIDTH - GameConstants.SPLIT_SCREEN_WIDTH / 4) {
                player1_x_Coord = GameConstants.WORLD_SCREEN_WIDTH - GameConstants.SPLIT_SCREEN_WIDTH / 4;
            }
            if (player2_x_Coord > GameConstants.WORLD_SCREEN_WIDTH - GameConstants.SPLIT_SCREEN_WIDTH / 4) {
                player2_x_Coord = GameConstants.WORLD_SCREEN_WIDTH - GameConstants.SPLIT_SCREEN_WIDTH / 4;
            }
            if (player1_y_Coord < GameConstants.SPLIT_SCREEN_HEIGHT / 2) {
                player1_y_Coord = GameConstants.SPLIT_SCREEN_HEIGHT / 2;
            }
            if (player2_y_Coord < GameConstants.SPLIT_SCREEN_HEIGHT / 2) {
                player2_y_Coord = GameConstants.SPLIT_SCREEN_HEIGHT / 2;
            }
            if (player1_y_Coord > GameConstants.WORLD_SCREEN_HEIGHT - GameConstants.SPLIT_SCREEN_HEIGHT / 2) {
                player1_y_Coord = GameConstants.WORLD_SCREEN_HEIGHT - GameConstants.SPLIT_SCREEN_HEIGHT / 2;
            }
            if (player2_y_Coord > GameConstants.WORLD_SCREEN_HEIGHT - GameConstants.SPLIT_SCREEN_HEIGHT / 2) {
                player2_y_Coord = GameConstants.WORLD_SCREEN_HEIGHT - GameConstants.SPLIT_SCREEN_HEIGHT / 2;
            }


            BufferedImage left_split_screen = world.getSubimage(player1_x_Coord - GameConstants.SPLIT_SCREEN_WIDTH / 4, player1_y_Coord - GameConstants.SPLIT_SCREEN_HEIGHT / 2, GameConstants.SPLIT_SCREEN_WIDTH / 2, GameConstants.SPLIT_SCREEN_HEIGHT);
            BufferedImage right_split_screen = world.getSubimage(player2_x_Coord - GameConstants.SPLIT_SCREEN_WIDTH / 4, player2_y_Coord - GameConstants.SPLIT_SCREEN_HEIGHT / 2, GameConstants.SPLIT_SCREEN_WIDTH / 2, GameConstants.SPLIT_SCREEN_HEIGHT);

            g2.drawImage(left_split_screen, 0, 0, null);
            g2.drawImage(right_split_screen, GameConstants.SPLIT_SCREEN_WIDTH / 2 + 5, 0, null);

            g2.drawImage(world, GameConstants.SPLIT_SCREEN_WIDTH / 2 - GameConstants.WORLD_SCREEN_WIDTH / 6 / 2, GameConstants.SPLIT_SCREEN_HEIGHT - GameConstants.WORLD_SCREEN_HEIGHT / 6, GameConstants.WORLD_SCREEN_WIDTH / 6, GameConstants.WORLD_SCREEN_HEIGHT / 6, null);
            g2.setFont(new Font("Aerial", Font.BOLD, 30));
            g2.setColor(Color.WHITE);
            g2.drawString("Player1 Life: " + this.Player1_num_lives, 10, 28);
            g2.drawString("Player2 Life: " + this.Player2_num_lives, GameConstants.SPLIT_SCREEN_WIDTH / 2 + 10, 28);

            g2.drawString("HP: " + this.t1.getHealth() * 2, 10 , 58);
            g2.drawString("HP: " + this.t2.getHealth() * 2, GameConstants.SPLIT_SCREEN_WIDTH / 2 + 25 , 58);

        }
    }

    public void createMap(){
        MapLayout layout = new MapLayout();
        int[] mapArray = layout.getLayout();
        int column = 0;
        int index = 0;

        for (int i = 0; i < 60; i++) {

            for (int j = 0; j < 48; j++) {
                if (column == 60) {
                    column = 0;
                }
                int temp_val = mapArray[index];
                if (temp_val != 0) {
                    if (temp_val == 2) {
                        gameEntities.add(new BreakableWall(j * 32, i * 32));
                    } else {
                        gameEntities.add(new Wall(j * 32, i * 32, false));
                    }
                }
                column++;
                index++;
            }
        }
    }

    public void addPowerUp(){
        PowerUp p1 = new PowerUp(812, 852, true, false);
        PowerUp p2 = new PowerUp(90, 1856, true, false);
        PowerUp p3 = new PowerUp(730, 852, false, true);
        PowerUp p4 = new PowerUp(90, 1760, false, true);
        PowerUp p5 = new PowerUp(90, 1664, true, false);
        PowerUp p6 = new PowerUp(1465, 80, true, false);
        PowerUp p7 = new PowerUp(1465, 125, false, true);
        PowerUp p8 = new PowerUp(1465, 185, false, true);
        PowerUp[] powerArray = {p1,p2,p3,p4,p5,p6, p7, p8};
        for(int i=0; i<powerArray.length; i++){
            gameEntities.add(powerArray[i]);
        }

    }

    public ArrayList<GameEntity> handleCollision(ArrayList<GameEntity> object){

        this.entities = new ArrayList<>();
        this.entities = object;
        for (int i = 1; i < entities.size()-1; i++) {

            for (int j = i; j < entities.size(); j++) {
                GameEntity obj1 = entities.get(i);
                GameEntity obj2 = entities.get(j);

                if (i != j) {

                    if (obj1 instanceof Shell && obj2 instanceof Tank && !(((Shell) obj1).getOwner().equals(((Tank) obj2).getTag())) && !((Shell) obj1).collided) {
                        if ( obj1.rectangle.intersects(obj2.rectangle)) {
                            obj1.collision();
                            obj2.collision();
                        }


                    }
                    if (obj1 instanceof Tank && obj2 instanceof Shell && !((Shell) obj2).getOwner().equals(((Tank) obj1).getTag()) && !((Shell) obj2).collided) {
                        if (obj1.rectangle.intersects(obj2.rectangle)) {
                            obj2.collision();
                            obj1.collision();
                        }

                    }

                    if (((obj2 instanceof Shell && obj1 instanceof BreakableWall && !((Shell) obj2).collided))) {
                        if (obj1.rectangle.intersects(obj2.rectangle)) {
                            obj2.collision();
                            obj1.collision();
                        }

                    }


                    if (obj1 instanceof Tank && obj2 instanceof BreakableWall) {
                        Rectangle r1 = ((Tank) obj1).getOffsetBounds();
                        if (r1.intersects(obj2.rectangle)) {

                            ((Tank) obj1).setNoMove(true);

                        }

                    }


                    if (obj1 instanceof Tank && obj2 instanceof Tank) {
                        Rectangle r2 = ((Tank) obj1).getOffsetBounds();
                        if (r2.intersects(obj2.rectangle)) {

                            ((Tank) obj1).setNoMove(true);
                            ((Tank) obj2).setNoMove(true);
                            obj1.collision();
                            obj2.collision();

                        }

                    }

                    if (obj1 instanceof BreakableWall && obj2 instanceof Tank) {

                        Rectangle r3 = ((Tank) obj2).getOffsetBounds();
                        if (r3.intersects(obj1.rectangle)) {  //intersection occurs

                            ((Tank) obj2).setNoMove(true);

                        }

                    }



                    if (obj1 instanceof Tank && obj2 instanceof PowerUp) {
                        if (obj1.rectangle.intersects(obj2.rectangle)) {
                            if (((PowerUp) obj2).isHealthBoost) {
                                ((Tank) obj1).setHealth(100);
                                entities.remove(j);

                            }else if (((PowerUp) obj2).isSpeedBoost) {
                                ((Tank) obj1).setSpeedBoost(System.currentTimeMillis());
                                ((Tank) obj1).setSpBoost(true);
                                entities.remove(j);
                            }
                        }

                    }
                }

            }
        }


        return entities;
    }

    public void updateGame(GameLauncher launcher){
        for (int i = 0; i < launcher.gameEntities.size(); i++) {
            if (launcher.gameEntities.get(i) instanceof Shell) {
                if (((Shell) launcher.gameEntities.get(i)).getIsInactive()) {
                    launcher.gameEntities.remove(i);
                    i--;
                } else {
                    launcher.gameEntities.get(i).update();
                }
            }
            if (launcher.gameEntities.get(i) instanceof Tank) {
                if (((Tank) launcher.gameEntities.get(i)).getHealth() == 0) {
                    if ((((Tank) launcher.gameEntities.get(i)).getTag()).equals("Tank1")) {
                        if (launcher.Player1_num_lives > 1) {
                            launcher.Player1_num_lives--;


                            ((Tank) launcher.gameEntities.get(i)).setHealth(100);
                            launcher.gameEntities.get(i).setX(GameConstants.TANK1_X_COORD);
                            launcher.gameEntities.get(i).setY(GameConstants.TANK1_Y_COORD);
                            launcher.gameEntities.get(i).setAngle(GameConstants.TANK1_ANGLE);


                        } else {
                            launcher.Player1_num_lives = 0;
                            launcher.player2_won = true;
                            break;
                        }
                    }
                    if ((((Tank) launcher.gameEntities.get(i)).getTag()).equals("Tank2")) {
                        if (launcher.Player2_num_lives > 1) {
                            launcher.Player2_num_lives--;


                            ((Tank) launcher.gameEntities.get(i)).setHealth(100);
                            launcher.gameEntities.get(i).setX(GameConstants.TANK2_X_COORD);
                            launcher.gameEntities.get(i).setY(GameConstants.TANK2_Y_COORD);
                            launcher.gameEntities.get(i).setAngle(GameConstants.TANK2_ANGLE);


                        } else {
                            launcher.Player2_num_lives = 0;
                            launcher.player1_won = true;
                            Stage.valueOf("exit");
                            break;
                        }
                    }

                }
            }
            if (((launcher.gameEntities.get(i) instanceof BreakableWall) && ((BreakableWall) launcher.gameEntities.get(i)).getHealth() == 0)) {
                launcher.gameEntities.remove(i);
            }
        }
    }



}
