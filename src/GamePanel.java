import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GamePanel extends JPanel {

    public static final int SOLIDER_HEIGHT = 100;
    public static final int SOLIDER_WIDTH = 100;
    public static final int FLOOR_HEIGHT = GameWindow.screenY / 2;
    public static boolean IsGameOver;

    Characters character;

    int playerFrame, enemyFrame;
    int playerX, playerY;
    int enemyX, enemyY;
    int currentPlayerAnimation;

    boolean isPlayerOnTile;
    boolean isGameOver;
    boolean isPlayerFalling;

    int timer;
    boolean jumpAnimation;
    int enemyHealth = 5;
    int shootDistance;
    int tile3 = 1300;
    int tileWidth = 500;
    boolean isEnemyEnd;
    String leftOrRight;
    int[][] tiles = new int[3][2];  // 2D Array Initialization
    Assets assets;

    public GamePanel() {
        setFocusable(true);
        setBackground(Color.darkGray);

        assets = new Assets();
        character = new Characters();

        playerX = 100;
        playerY = FLOOR_HEIGHT;
        enemyY = FLOOR_HEIGHT;
        enemyX = tile3 + tileWidth - 100;
        shootDistance = playerX + 200;
        currentPlayerAnimation = Characters.IDLE;

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
                currentPlayerAnimation = Characters.SHOOT;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentPlayerAnimation = Characters.IDLE;
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
                if (isPlayerFalling) return;

                if (e.getKeyCode() == KeyEvent.VK_D) {
                    playerY = FLOOR_HEIGHT;
                    leftOrRight = "RIGHT";
                    currentPlayerAnimation = Characters.RUNNING;
                } else if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (jumpAnimation)
                        return;

                    timer = 0;
                    jumpAnimation = true;
                    currentPlayerAnimation = Characters.JUMP;
                } else if (e.getKeyCode() == KeyEvent.VK_A) {
                    leftOrRight = "LEFT";
                    currentPlayerAnimation = Characters.RUNNING;
                    playerY = FLOOR_HEIGHT;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (isPlayerFalling || jumpAnimation) return;
                currentPlayerAnimation = Characters.IDLE;
            }
        });
    }

    private void initTiles() {
        for (int i = 0; i <= 2; i++) {
            tiles[i][0] = (i * (tileWidth + 100)) + 100;
            tiles[i][1] = FLOOR_HEIGHT + SOLIDER_HEIGHT - 10;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        isPlayerOnTile = checkPlayerOnTile();
        //  System.out.println("Player Animation:" + currentPlayerAnimation);

        if (!isPlayerFalling) {
            if (!isPlayerOnTile) {
                isPlayerFalling = true;
                currentPlayerAnimation = Characters.FALL;
            } else if (playerX + SOLIDER_WIDTH >= enemyX) {
                currentPlayerAnimation = Characters.DEATH;
            }
        }
        playerAnimation(g, currentPlayerAnimation);
        enemyAnimation(g);

//        System.out.println("player on tile: " + isPlayerOnTile);
        /* begin falling animation */

        /* Bounding boxes for testing */
        g.setColor(Color.MAGENTA);
        g.drawRect(playerX, playerY, SOLIDER_WIDTH, SOLIDER_HEIGHT);
        g.setColor(Color.blue);
        g.drawRect(enemyX, enemyY, SOLIDER_WIDTH, SOLIDER_HEIGHT);
        for (int i = 0; i <= 2; i++) {
            g.setColor(Color.red);
            g.drawLine(tiles[i][0], tiles[i][1] - 50, tiles[i][0] + tileWidth, tiles[i][1] - 50);
            g.drawImage(character.getAnimation(Characters.TILES).get(0), tiles[i][0],
                    tiles[i][1], tileWidth, 100, null);
        }
    }

    private void playerAnimation(Graphics g, int animation) {
        //System.out.println("Player (x,y): " + playerX + "," + (playerY));

        if (playerFrame >= character.getAnimationSize(animation) - 1) {
            playerFrame = 0;
            if (animation == Characters.DEATH)
                playerFrame = character.getAnimationSize(animation) - 1;
        } else playerFrame = playerFrame + 1;

        if (animation == Characters.DEATH) {
            g.drawImage(character.getAnimation(Characters.DEATH).get(playerFrame), playerX, playerY + 50, 150,
                    50, null);
            return;
        }

        if (animation == Characters.RUNNING) {
            if (leftOrRight == "RIGHT") {
                g.drawImage(character.getAnimation(Characters.RUNNING).get(playerFrame), playerX += 8, playerY, SOLIDER_WIDTH,
                        SOLIDER_HEIGHT, null);
            } else {
                g.drawImage(character.getAnimation(Characters.RUNNING).get(playerFrame), playerX -= 5, playerY, SOLIDER_WIDTH,
                        SOLIDER_HEIGHT, null);
            }
        } else if (animation == Characters.JUMP) {
            System.out.println(timer);
            playerY = FLOOR_HEIGHT - timer;
            if (timer >= 70) {
                playerY = FLOOR_HEIGHT;
                jumpAnimation = false;
                currentPlayerAnimation = Characters.IDLE;
            } else {
                timer = timer + 8;
            }

            g.drawImage(character.getAnimation(Characters.JUMP).get(playerFrame), playerX, playerY, SOLIDER_WIDTH,
                    SOLIDER_HEIGHT, null);
        } else if (animation == Characters.SHOOT) {
            g.drawImage(assets.bullet, playerX + SOLIDER_WIDTH, playerY + 33, 25, 25, null);
            g.drawImage(character.getAnimation(Characters.SHOOT).get(playerFrame), playerX, playerY, SOLIDER_WIDTH,
                    SOLIDER_HEIGHT, null);
            if (enemyX <= shootDistance) {
                enemyHealth = enemyHealth - 1;
            }
            currentPlayerAnimation = Characters.IDLE;
        } else if (animation == Characters.IDLE) {
            g.drawImage(character.getAnimation(Characters.IDLE).get(playerFrame), playerX, playerY, SOLIDER_WIDTH,
                    SOLIDER_HEIGHT, null);
        } else if (animation == Characters.FALL) {
            if (playerY >= getHeight() - SOLIDER_HEIGHT - 25) {
                currentPlayerAnimation = Characters.DEATH;
            } else
                g.drawImage(character.getAnimation(Characters.FALL).get(playerFrame), playerX, playerY += 50, SOLIDER_WIDTH,
                        SOLIDER_HEIGHT, null);
        }

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

    }

    private boolean checkPlayerOnTile() {
        for (int i = 0; i < tiles.length; i++) {
//            System.out.println(tiles[i][0]);

            if ((playerX + SOLIDER_WIDTH / 2) >= tiles[i][0]
                    && (playerX + SOLIDER_WIDTH / 2) <= (tiles[i][0] + tileWidth)) {
                return true;
            }
        }
        return false;
    }

//    private void changePlayerAnimation(String action) {
//        isPlayerRunning = false;
//        isPlayerShooting = false;
//        isPlayerJumping = false;
//        isPlayerDead = false;
//
//        if(action.equals("LEFT")) isPlayerRunning = true;
//        else if(action.equals("JUMP")) isPlayerJumping = true;
//        else if(action.equals("DEAD")) isPlayerDead = true;
//        else if(action.equals("SHOOT")) isPlayerShooting = true;
//    }
}
