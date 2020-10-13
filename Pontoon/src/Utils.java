import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Utils {

    public BufferedImage LoadBufferedImage(String filename) {
        try {
            return ImageIO.read(getClass().getResource(filename));
        } catch (IOException e) {
            return null;
        }
    }
}
