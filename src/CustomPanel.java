import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomPanel extends JPanel {

    public static final int SOLIDER_HEIGHT = 100;
    public static final int SOLIDER_WIDTH = 100;
    public static final int FLOOR_HEIGHT = GameWindow.screenY / 2;

    Characters character;


    boolean isGameOver;
    int placeHolder = 0;
    int playerFrame;
    int enemyFrame;
    int playerX = 100;
    int playerY = FLOOR_HEIGHT;
    boolean isPlayerRunning;
    boolean isPlayerJumping;
    boolean isPlayerDead;
    boolean isPlayerShooting;
    int enemyX;
    int enemyY;

    int timer;
    boolean jumpAnimation;
    int enemyHealth = 5;
    int shootDistance;
    int tile3 = 1300;
    int tileWidth = 500;
    boolean isEnemyEnd;
    boolean isPlayerOnTile = true;
    String leftOrRight;
    int[][] tiles = new int[3][2];  // 2D Array Initialization
    Assets assets;

    public CustomPanel() {
        setFocusable(true);
        enemyY = FLOOR_HEIGHT;
        enemyX = tile3 + tileWidth - 100;
        setBackground(Color.darkGray);
        assets = new Assets();
        character = new Characters();
        shootDistance = playerX + 200;

        initTiles();
        initListeners();
    }

    private void initListeners() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPlayerShooting = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPlayerShooting = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                isPlayerRunning = false;
                isPlayerJumping = false;
                isPlayerShooting = false;
                isPlayerDead = false;

                if (e.getKeyCode() == KeyEvent.VK_D) {
                    // playerX = playerX + 3;
                    playerY = FLOOR_HEIGHT;
                    isPlayerRunning = true;
                    leftOrRight = "RIGHT";
                } else if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    isPlayerJumping = true;
                    jumpAnimation = true;
                    timer = 0;
                } else if (e.getKeyCode() == KeyEvent.VK_G) {
                    playerFrame = 0;
                    isPlayerDead = true;
                }
                else if (e.getKeyCode() == KeyEvent.VK_A){
                    leftOrRight = "LEFT";
                    isPlayerRunning = true;
                    playerY = FLOOR_HEIGHT;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                isPlayerRunning = false;
            }
        });
    }

    private void initTiles() {
        for (int i = 0; i <= 2; i++) {
            tiles[i][0] = (i * (tileWidth + 100)) + 100;
            tiles[i][1] = FLOOR_HEIGHT + SOLIDER_HEIGHT - 10;
        }
    }

    private void soliderAnimation(Graphics g) {
        /* SOLIDER ANIMATION */
        System.out.println("PlayerX: " + playerX);
        if (isPlayerRunning) {
            if (playerFrame >= character.getAnimationSize(Characters.RUNNING) - 1) playerFrame = 0;
            else playerFrame = playerFrame + 1;
            if(leftOrRight == "RIGHT") {
                g.drawImage(character.getAnimation(Characters.RUNNING).get(playerFrame), playerX += 4, playerY, SOLIDER_WIDTH,
                        SOLIDER_HEIGHT, null);
            }
            else{
                g.drawImage(character.getAnimation(Characters.RUNNING).get(playerFrame), playerX -= 4, playerY, SOLIDER_WIDTH,
                        SOLIDER_HEIGHT, null);
            }
        } else if (isPlayerJumping) {
            if (playerFrame >= character.getAnimationSize(Characters.JUMP) - 1) playerFrame = 0;
            else playerFrame = playerFrame + 1;
            playerY = FLOOR_HEIGHT - timer;
            playerX = playerX + timer - 7;
            if (timer == 60)
                jumpAnimation = false;
            else if (timer <= -10)
                isPlayerJumping = false;
            if (jumpAnimation) timer = timer + 10;
            else timer = timer - 10;

            //System.out.println(playerY + " " + (FLOOR_HEIGHT + SOLIDER_HEIGHT));
            if (playerY >= FLOOR_HEIGHT) playerY = FLOOR_HEIGHT;

            g.drawImage(character.getAnimation(Characters.JUMP).get(playerFrame), playerX, playerY, SOLIDER_WIDTH,
                    SOLIDER_HEIGHT, null);
        } else if (isPlayerDead) {
            if (playerFrame >= character.getAnimationSize(Characters.DEATH) - 1) {
                playerFrame = 4;
            } else
                playerFrame = playerFrame + 1;
            g.drawImage(character.getAnimation(Characters.DEATH).get(playerFrame), playerX, playerY + 50, 150,
                    50, null);
        } else if (isPlayerShooting) {
            if (playerFrame >= character.getAnimationSize(Characters.SHOOT) - 1) {
                playerFrame = 0;
            } else {
                playerFrame = playerFrame + 1;
            }
            g.drawImage(assets.bullet, playerX + SOLIDER_WIDTH, playerY + 33, 25, 25, null);

            g.drawImage(character.getAnimation(Characters.SHOOT).get(playerFrame), playerX, playerY, SOLIDER_WIDTH,
                    SOLIDER_HEIGHT, null);
            if (enemyX <= shootDistance) {
                enemyHealth = enemyHealth - 1;
            }

            isPlayerShooting = false;
        } else {
            if (playerFrame >= character.getAnimationSize(Characters.IDLE) - 1) playerFrame = 0;
            else playerFrame = playerFrame + 1;
            g.drawImage(character.getAnimation(Characters.IDLE).get(playerFrame), playerX, playerY, SOLIDER_WIDTH,
                    SOLIDER_HEIGHT, null);
        }
        /* END OF SOLIDER ANIMATION */
        shootDistance = playerX + 400;
        //System.out.println("enemyX: " + enemyX + " playerX: " + playerX + " enemy health: " + enemyHealth);
    }

    private void enemyAnimation(Graphics g) {

        if (enemyFrame >= character.getAnimationSize(Characters.ENEMY_WALKING) - 1) {
            enemyFrame = 0;
        }
        if (enemyHealth > 0) {
            g.drawImage(character.getAnimation(Characters.ENEMY_WALKING).get(enemyFrame),
                    enemyX, enemyY, SOLIDER_WIDTH, SOLIDER_HEIGHT, null);

            if (enemyX <= tile3) {
                isEnemyEnd = true;
            }
            if (isEnemyEnd) {
                enemyX = enemyX + 3;
                if (enemyX >= tileWidth + tile3 - 100) {
                    isEnemyEnd = false;
                }
            } else {
                enemyX = enemyX - 3;
            }

            enemyFrame = enemyFrame + 1;
        }
        /* enemy health bar */
        if (enemyHealth < 5 && enemyHealth > 0) {
            g.setColor(Color.green);
            g.fillRect(enemyX, enemyY - 40, enemyHealth * 16, 10);
            g.setColor(Color.black);
            g.drawRect(enemyX, enemyY - 40, 80, 10);
        }
        if(playerX + SOLIDER_WIDTH >= enemyX){
            isPlayerDead = true;
        }
    }

    private boolean checkPlayerOnTile() {
        for (int i = 0; i < tiles.length; i++) {
//            System.out.println(tiles[i][0]);

            if ((playerX + SOLIDER_WIDTH) >= tiles[i][0] && (playerX + SOLIDER_WIDTH) <= (tiles[i][0] + tileWidth)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        soliderAnimation(g);
        enemyAnimation(g);
        isPlayerOnTile = checkPlayerOnTile();
        System.out.println("player on tile: " + isPlayerOnTile);
        /* TODO: DETECT IF THE PLAYER IS ON THE TILE OR NOT */

        /* Bounding boxes for testing */
        g.setColor(Color.MAGENTA);
        g.drawRect(playerX, playerY, SOLIDER_WIDTH, SOLIDER_HEIGHT);
        g.setColor(Color.blue);
        g.drawRect(enemyX, enemyY, SOLIDER_WIDTH, SOLIDER_HEIGHT);
//        System.out.println(FLOOR_HEIGHT +" "+ playerY +" "+ enemyY);
        for (int i = 0; i <= 2; i++) {
            g.setColor(Color.red);
            g.drawLine(tiles[i][0], tiles[i][1] - 50, tiles[i][0] + tileWidth, tiles[i][1] - 50);
            g.drawImage(character.getAnimation(Characters.TILES).get(0), tiles[i][0],
                    tiles[i][1], tileWidth, 100, null);
        }
    }
}
