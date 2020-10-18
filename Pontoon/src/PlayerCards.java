import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerCards {

    public final List<Card> m_Hand = new ArrayList<Card>();

    private double m_LeftCardPos;
    private double m_RightCardPos;
    private double m_CardWidth;

    private int m_CurrentScore;

    public void DrawOpeningHand() {
        m_CurrentScore = 0;
        DrawCard();
        m_Hand.get(0).SnapToTargetPosWithRevealDelay(1);
    }

    private void DrawCard() {
        Card newCard = Pontoon.m_Pontoon.m_Deck.DrawCard();
        if (newCard == null) {
            return;
        }

        int numCards = m_Hand.size() + 1;
        FindCardPositionsAndWidth(numCards);

        double x = m_LeftCardPos;

        for(Card card : m_Hand) {
            card.SetTargetPos(x);
            x += m_CardWidth;
        }

        newCard.SetStartPositionAndTargetPosition(
                Pontoon.m_Pontoon.m_Width + Card.HALF_WIDTH,
                Pontoon.m_Pontoon.m_PlayerCardsMidY,
                (int)Math.round(m_RightCardPos)
        );

        m_Hand.add(newCard);
        Pontoon.m_Pontoon.m_NumCardsBeingRevealed++;

        CalculateCurrentScore();
    }

    private void FindCardPositionsAndWidth(int numCards) {
        double width = Card.WIDTH_WITH_BORDER * numCards;
        double half = width/2;

        if (width > Pontoon.m_Pontoon.m_MaxWidth) {
            half = Pontoon.m_Pontoon.m_MaxWidth / 2;
        }

        m_LeftCardPos = Pontoon.m_Pontoon.m_MidX - half + Card.HALF_WIDTH;
        m_RightCardPos = Pontoon.m_Pontoon.m_MidX + half - Card.HALF_WIDTH;

        m_CardWidth = Card.WIDTH_WITH_BORDER;

        if (width > Pontoon.m_Pontoon.m_MaxWidth) {
            m_CardWidth -= (width - Pontoon.m_Pontoon.m_MaxWidth) / (numCards - 1);
        }
    }

    public void UpdatePlayerCards(double m_DeltaTime) {
//        m_Panel.removeAll();
        for (Card card: m_Hand) {
            card.Update(m_DeltaTime);
        }
//        m_Panel.updateUI();
    }

    private void CalculateCurrentScore() {
        m_CurrentScore = 0;
        for(Card card: m_Hand) {
            m_CurrentScore += card.m_Value;
        }
    }

    public void CheckIfBust() {
        if (m_CurrentScore > Pontoon.MAX_SCORE) {
            Pontoon.m_Pontoon.PlayerHasBust();
        }
    }

    public void DrawCardButtonPressed() {
//        System.out.println("pressed");
        if (m_Hand.size() == 1) {
            m_Hand.get(0).RevealWithoutDelay();
        }

        if (m_CurrentScore > Pontoon.MAX_SCORE) {
            return;
        }

        DrawCard();
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
