import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Deck object
 * Loads and manages an array of Card objects
 *
 * @author      Andrew Smith <meejahor@gmail.com>
 * @version     1.0
 * @since       1.0
 */
public class Deck {
    private final List<Card> m_Cards = new ArrayList<Card>();

    public BufferedImage m_CardBack;

    private void LoadCardBack() {
        m_CardBack = Pontoon.m_Utils.LoadBufferedImage("/resources/cards/card_back.png");
    }

    private void LoadCard(String filename, int value) {
        Card card = new Card(value, filename);
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

        int index = Pontoon.m_Utils.Random(m_Cards.size());
        Card card = GetCard(index);
        m_Cards.remove(index);
        return card;
    }

    public Card FindAndDrawCard(int value, int skip) {
        for (Card card: m_Cards) {
            if (card.m_Value == value) {
                skip --;
                if (skip < 0) {
                    m_Cards.remove(card);
                    return card;
                }
            }
        }

        return null;
    }

    public void ReturnCard(Card card) {
        m_Cards.add(card);
    }
}
