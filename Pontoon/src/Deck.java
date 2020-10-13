import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
    private final List<Card> m_Cards = new ArrayList<Card>();
    private final Random random = new Random();

    public BufferedImage m_CardBack;

    private void LoadCardBack() {
        m_CardBack = Pontoon.m_Utils.LoadBufferedImage("/resources/cards/card_back.png");
    }

    private void LoadCard(String filename, int value) {
        BufferedImage bufferedImage = Pontoon.m_Utils.LoadBufferedImage(filename);
        BufferedImage scaledImage = new BufferedImage(Card.WIDTH, Card.HEIGHT, bufferedImage.getType());
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(bufferedImage, Card.BORDER, Card.BORDER, Card.WIDTH - (Card.BORDER*2), Card.HEIGHT - (Card.BORDER*2), null);
        g2d.dispose();
        ImageIcon icon = new ImageIcon(scaledImage);
        JLabel label = new JLabel(icon);
        Card card = new Card(value, label, bufferedImage);
        m_Cards.add(card);
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
        LoadCardBack();
    }

    public Card GetCard(int index) {
        return m_Cards.get(index);
    }

    public Card DrawCard() {
        if (m_Cards.size() == 0) {
            return null;
        }

        int index = random.nextInt(m_Cards.size());
        Card card = GetCard(index);
        m_Cards.remove(index);
        return card;
    }
}
