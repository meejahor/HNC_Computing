import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Pontoon {
    private JFrame frame;
    private JPanel pontoonPanel;
    private JPanel playerCards;
    private JLabel cardImageLabel, cardImageLabel2;

    final private int CARD_WIDTH = 180;
    final private int CARD_HEIGHT = 270;

    public Pontoon() {
        frame = new JFrame("Pontoon");
        frame.setContentPane(pontoonPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        UpdatePlayerCards();
    }

    private JLabel LoadCard(String filename) {
        try {
            BufferedImage bufferedImage = ImageIO.read(getClass().getResource(filename));
            BufferedImage scaledImage = new BufferedImage(CARD_WIDTH, CARD_HEIGHT, bufferedImage.getType());
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.drawImage(bufferedImage, 0, 0, CARD_WIDTH, CARD_HEIGHT, null);
            g2d.dispose();
            ImageIcon icon = new ImageIcon(scaledImage);
            JLabel label = new JLabel(icon);
            return label;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void AddCard(JLabel card, int x, int y) {
        card.setBounds(x, y, CARD_WIDTH, CARD_HEIGHT);
        playerCards.add(card);
    }

    private void UpdatePlayerCards() {
        cardImageLabel = LoadCard("/resources/cards/card.png");
        cardImageLabel2 = LoadCard("/resources/cards/card2.png");
        playerCards.setBackground(Color.white);
        AddCard(cardImageLabel2, 150, 20);
        AddCard(cardImageLabel, 50, 10);
    }

    public static void main(String[] args) {
        new Pontoon();
    }
}
