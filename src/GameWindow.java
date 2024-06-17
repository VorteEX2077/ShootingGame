import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    CustomPanel customPanelObject;
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
                while (!customPanelObject.isGameOver) {
                    if (System.currentTimeMillis() - lastTimeInMillis >= 100) {

                        customPanelObject.repaint();
                        lastTimeInMillis = System.currentTimeMillis();
                    }
                }
            }
        };

        screenX = Toolkit.getDefaultToolkit().getScreenSize().width;
        screenY = Toolkit.getDefaultToolkit().getScreenSize().height;
        screenX = 1280;
        screenY = 720;

        customPanelObject = new CustomPanel();
        add(customPanelObject);

//        Dimension dim;
//        Toolkit myToolkit = Toolkit.getDefaultToolkit();
//        dim = myToolkit.getScreenSize();
//        System.out.println(dim.width);
        // OR

        setSize(screenX, screenY);
        //setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      //  f.setLocationRelativeTo(null);

        // Start the game thread
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void gameOver(){
        customPanelObject.isGameOver = true;
    }

    public static void main(String[] args) {
        new GameWindow();
    }

}