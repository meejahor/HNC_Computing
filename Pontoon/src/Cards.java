import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Cards {
    final public static int WIDTH = 180;
    final public static int HEIGHT = 270;

    private final JLabel[] cards = new JLabel[52];
    private int cardLoadingIndex = 0;

    private JLabel LoadCard(String filename) {
        try {
            BufferedImage bufferedImage = ImageIO.read(getClass().getResource(filename));
            BufferedImage scaledImage = new BufferedImage(WIDTH, HEIGHT, bufferedImage.getType());
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.drawImage(bufferedImage, 0, 0, WIDTH, HEIGHT, null);
            g2d.dispose();
            ImageIcon icon = new ImageIcon(scaledImage);
            return new JLabel(icon);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String BuildResourceName(String prefix, String suffix) {
        return prefix + suffix + ".png";
    }

    private void LoadSuite(String name) {
        String prefix = "/resources/cards/" + name + "/" + name + "_";
        String resourceName;
        for (int i = 1; i <= 10; i++) {
            resourceName = BuildResourceName(prefix, Integer.toString(i));
            cards[cardLoadingIndex++] = LoadCard(resourceName);
        }

        cards[cardLoadingIndex++] = LoadCard(BuildResourceName(prefix, "j"));
        cards[cardLoadingIndex++] = LoadCard(BuildResourceName(prefix, "k"));
        cards[cardLoadingIndex++] = LoadCard(BuildResourceName(prefix, "q"));
    }

    public void LoadCards() {
        LoadSuite("clubs");
        LoadSuite("diamonds");
        LoadSuite("hearts");
        LoadSuite("spades");
    }

    public JLabel GetCard(int index) {
        return cards[index];
    }
}
