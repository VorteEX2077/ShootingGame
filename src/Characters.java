import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
* This class only responsibility is to load all the game assets from the
* resource directory into the program 0.
*/
public class Characters {

    public static final int RUNNING = 0;
    public static final int IDLE = 1;
    public static final int JUMP = 2;
    public static final int DEATH = 3;
    public static  final int SHOOT = 4;
    public static  final int ENEMY_IDLE = 5;
    public static final int ENEMY_WALKING = 6;

    public static int max_images;
    public static final int NUM_ANIMATION_COUNT = 10;
    List<List<BufferedImage>> images = new ArrayList<>(NUM_ANIMATION_COUNT);

    public Characters() {
        loadImages("run", RUNNING);
        loadImages("idle", IDLE);
        loadImages("jump", JUMP);
        loadImages("Death", DEATH);
        loadImages("Shooting", SHOOT);
        loadImages("EnemyIdle", ENEMY_IDLE);
        loadImages("EnemyWalking", ENEMY_WALKING);
    }

    /**
     * This method will load all the images from a particular folder from the resource directory.
     * @param folderName this is the name of the folder from which u want to load all the images.
     * @param idx        this is the sequence of the folder stored inside the big List<List> images variable.
     * */
    public void loadImages(String folderName, int idx) {
        try {
            URL folderURL = getClass().getResource(folderName);

            if (folderURL == null) {
                System.err.println(folderName + " does not exist!");
                return;
            }

            File folderFile = Paths.get(folderURL.toURI()).toFile();
            File[] files = folderFile.listFiles();
            List<BufferedImage> bufferedImage = new ArrayList<>();

            // iterate (Loop) over the files array
            for (File file : files) {
                bufferedImage.add(ImageIO.read(file));
                //System.out.println(file);
            }

            images.add(idx, bufferedImage);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public List<BufferedImage> getAnimation(int animation) {
        max_images = images.get(animation).size();
        return images.get(animation);
    }

    public int getAnimationSize(int animation) {
        return images.get(animation).size();
    }
}
