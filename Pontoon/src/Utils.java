import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Random;

public class Utils {

    private final Random random = new Random();

    public BufferedImage LoadBufferedImage(String filename) {
        try {
            return ImageIO.read(getClass().getResource(filename));
        } catch (IOException e) {
            return null;
        }
    }

    public JLabel LoadImageLabel(String filename) {
        BufferedImage image = LoadBufferedImage(filename);
        ImageIcon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon);
        return label;
    }

    public int Random(int max) {
        return random.nextInt(max);
    }
}
