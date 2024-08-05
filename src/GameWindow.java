import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    GamePanel gamePanelObject;
    JButton buttonNew = new JButton();
    Runnable runnable;
    Thread gameThread;
    long lastTimeInMillis;
    static int screenX, screenY;


    public GameWindow() {
         runnable = new Runnable() {
            @Override
            public void run() {
                lastTimeInMillis = System.currentTimeMillis();
                while (!GamePanel.IsGameOver) {
                    if (System.currentTimeMillis() - lastTimeInMillis >= 100) {

                        gamePanelObject.repaint();
                        lastTimeInMillis = System.currentTimeMillis();
                    }
                }
            }
        };

        screenX = Toolkit.getDefaultToolkit().getScreenSize().width;
        screenY = Toolkit.getDefaultToolkit().getScreenSize().height;

        gamePanelObject = new GamePanel();
        add(gamePanelObject);

//        Dimension dim;
//        Toolkit myToolkit = Toolkit.getDefaultToolkit();
//        dim = myToolkit.getScreenSize();
//        System.out.println(dim.width);
        // OR

        setSize(screenX, screenY);
       // setExtendedState(JFrame.MAXIMIZED_BOTH);
       // setUndecorated(true);
        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      //  f.setLocationRelativeTo(null);

        // Start the game thread
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void gameOver(){
        GamePanel.IsGameOver = true;
    }

    public static void main(String[] args) {
        new GameWindow();
    }

}