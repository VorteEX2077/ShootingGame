import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomPanel extends JPanel {

    public static final int SOLIDER_HEIGHT = 180;
    public static final int SOLIDER_WIDTH = 180;
    public static final int FLOOR_HEIGHT = GameWindow.screenY * 50 / 100;

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

    public CustomPanel() {
        setFocusable(true);
        enemyY = FLOOR_HEIGHT + 100;
        enemyX = GameWindow.screenX - 100;
        setBackground(Color.darkGray);
        character = new Characters();
        shootDistance = playerX + 200;

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
                    playerX = playerX + 3;
                    playerY = FLOOR_HEIGHT;
                    isPlayerRunning = true;
                } else if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    isPlayerJumping = true;
                    jumpAnimation = true;
                    timer = 0;
                } else if (e.getKeyCode() == KeyEvent.VK_G) {
                    playerFrame = 0;
                    isPlayerDead = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                isPlayerRunning = false;
            }
        });
    }

    private void soliderAnimation(Graphics g) {
        /* SOLIDER ANIMATION */
        if (isPlayerRunning) {
            if (playerFrame >= character.getAnimationSize(Characters.RUNNING) - 1) playerFrame = 0;
            else playerFrame = playerFrame + 1;
            g.drawImage(character.getAnimation(Characters.RUNNING).get(playerFrame), playerX, playerY, SOLIDER_WIDTH,
                    SOLIDER_HEIGHT, null);
        } else if (isPlayerJumping) {
            if (playerFrame >= character.getAnimationSize(Characters.JUMP) - 1) playerFrame = 0;
            else playerFrame = playerFrame + 1;
            playerY = FLOOR_HEIGHT - timer;
            playerX = playerX + timer / 3;
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
            g.drawImage(character.getAnimation(Characters.DEATH).get(playerFrame), playerX, playerY, SOLIDER_WIDTH,
                    SOLIDER_HEIGHT, null);
        } else if (isPlayerShooting) {
            if (playerFrame >= character.getAnimationSize(Characters.SHOOT) - 1) {
                playerFrame = 0;
            } else playerFrame = playerFrame + 1;
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
        System.out.println("enemyX: " + enemyX + " playerX: " + playerX + " enemy health: " + enemyHealth);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        soliderAnimation(g);

        if (enemyFrame >= character.getAnimationSize(Characters.ENEMY_WALKING) - 1) {
            enemyFrame = 0;
        }
        if(enemyHealth > 0) {
            g.drawImage(character.getAnimation(Characters.ENEMY_WALKING).get(enemyFrame), enemyX, enemyY, 80,
                    80, null);
            enemyX = enemyX - 3;
            enemyFrame = enemyFrame + 1;
        }

        // The floor line
        g.setColor(Color.red);
        g.drawLine(0, FLOOR_HEIGHT + SOLIDER_HEIGHT, GameWindow.screenX, FLOOR_HEIGHT + SOLIDER_HEIGHT);

    }
}
