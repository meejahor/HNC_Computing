import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * OpponentCards class
 * Handles the computer opponent's cards
 *
 * @author      Andrew Smith <meejahor@gmail.com>
 * @version     1.0
 * @since       1.0
 */
public class OpponentCards {
    public final List<Card> m_Hand = new ArrayList<Card>();

    // draw a random hand adding up to the value of OPPONENT_SCORE
    public void DrawOpeningHand() {
        int leftToFind = Pontoon.OPPONENT_SCORE;
        while (leftToFind > 0) {
            Card card;
            do {
                int value = Pontoon.m_Utils.Random(Math.min(leftToFind+1, 14));
                int skip = Pontoon.m_Utils.Random(3);
                card = DrawOpponentCard(value, skip);
            } while (card == null);
            leftToFind -= card.m_Value;
        }
    }

    private Card DrawOpponentCard(int value, int skip) {
        Card newCard = Pontoon.m_Pontoon.m_Deck.FindAndDrawCard(value, skip);
        if (newCard == null) {
            return null;
        }
        newCard.SetStartPositionAndTargetPosition(
                Pontoon.m_Pontoon.m_Width + Card.HALF_WIDTH,
                Pontoon.m_Pontoon.m_PlayerCardsMidY,
                Pontoon.m_Pontoon.m_Width + Card.HALF_WIDTH
        );

        newCard.InstantRevealNoPop();
        m_Hand.add(newCard);
        return newCard;
    }

    public void UpdateOpponentCards(double m_DeltaTime) {
        for (Card card: m_Hand) {
            card.Update(m_DeltaTime);
        }
    }

    public void RenderCards(Graphics2D g) {
        for (Card card: m_Hand) {
            card.Render(g);
        }
    }

    public void ReturnCardsToDeck() {
        for (Card card: m_Hand) {
            Pontoon.m_Pontoon.m_Deck.ReturnCard(card);
        }

        m_Hand.clear();
    }
}
