import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Cards {
    final public static int WIDTH = 180;
    final public static int HEIGHT = 270;

    private final List<Card> cards = new ArrayList<Card>();

    private void LoadCard(String filename, int value) {
        try {
            BufferedImage bufferedImage = ImageIO.read(getClass().getResource(filename));
            BufferedImage scaledImage = new BufferedImage(WIDTH, HEIGHT, bufferedImage.getType());
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.drawImage(bufferedImage, 0, 0, WIDTH, HEIGHT, null);
            g2d.dispose();
            ImageIcon icon = new ImageIcon(scaledImage);
            JLabel label = new JLabel(icon);
            Card card = new Card(value, label);
            cards.add(card);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String BuildResourceName(String prefix, String suffix) {
        return prefix + suffix + ".png";
    }

    private void LoadSuite(String name) {
        String prefix = "/resources/cards/" + name + "/" + name + "_";
        String resourceName;
        for (int value = 1; value <= 10; value++) {
            resourceName = BuildResourceName(prefix, Integer.toString(value));
            LoadCard(resourceName, value);
        }

        LoadCard(BuildResourceName(prefix, "j"), 11);
        LoadCard(BuildResourceName(prefix, "q"), 12);
        LoadCard(BuildResourceName(prefix, "k"), 13);
    }

    public void LoadCards() {
        LoadSuite("clubs");
        LoadSuite("diamonds");
        LoadSuite("hearts");
        LoadSuite("spades");
    }

    public Card GetCard(int index) {
        return cards.get(index);
    }
}
