import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class Assets {

    int a;
    public BufferedImage bullet;

    public Assets() {
        int a = 10;
        URL assets = getClass().getResource("other/muzzle.png");
        if(assets == null) {
            System.err.println("Game assets are missing!");
            return;
        }
        /* Convert this URL object into a FIle object */
        try {
            File file = new File(assets.toURI());
            bullet = ImageIO.read(file);
        }
        catch(Exception e){
            System.err.println("Game assets are missing:" + e.getMessage());
        }
    }
}
