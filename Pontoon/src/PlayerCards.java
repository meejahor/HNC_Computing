import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerCards {
    private int m_Width, m_Height;
    private int m_MidX;
    public int m_PlayerCardsMidY;

    private JPanel m_Panel;
    private final List<Card> m_PlayerHand = new ArrayList<Card>();
    private final List<Card> m_OpponentHand = new ArrayList<Card>();

    private int m_MaxWidth;
    private double m_LeftCardPos;
    private double m_RightCardPos;
    private double m_CardWidth;

    private static final int MAX_SCORE = 100;
    private int m_CurrentScore;

    private static final int OPPONENT_SCORE = 18;

    public PlayerCards(JPanel panel, int width, int height) {
        m_Panel = panel;
        m_MaxWidth = width;
        m_Width = width;
        m_Height = height;
        m_MidX = m_Width/2;
//        m_PlayerCardsMidY = (int)Math.round(m_Height * 0.55);
        m_PlayerCardsMidY = m_Height / 2;
        SetupPanel();
    }

    public void DrawOpponentCards() {
        Random random = new Random();
        int leftToFind = OPPONENT_SCORE;
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

    public void DrawOpeningHand() {
        m_CurrentScore = 0;

        DrawCard();
//        DrawCard();
//        DrawCard();
//        DrawCard();
//        DrawCard();
//        DrawCard();

        m_PlayerHand.get(0).SnapToTargetPosWithRevealDelay(1);
//        m_PlayerHand.get(1).SnapToTargetPosWithDelay(1.25);
    }

    private Card DrawOpponentCard(int value, int skip) {
        Card newCard = Pontoon.m_Pontoon.m_Deck.FindAndDrawCard(value, skip);
        if (newCard == null) {
            return null;
        }
        newCard.SetStartPositionAndTargetPosition(m_Width + Card.HALF_WIDTH, m_PlayerCardsMidY, m_Width + Card.HALF_WIDTH);
        newCard.InstantRevealNoPop();
        m_OpponentHand.add(newCard);
        return newCard;
    }

    private void DrawCard() {
        Card newCard = Pontoon.m_Pontoon.m_Deck.DrawCard();
        if (newCard == null) {
            return;
        }

        int numCards = m_PlayerHand.size() + 1;
        FindCardPositionsAndWidth(numCards);
//        double x = FindLeftPos(numCards);
//        double cardWidth = FindCardWidth(numCards);
//        System.out.println(x);
//        System.out.println(cardWidth);

        double x = m_LeftCardPos;

        for(Card card : m_PlayerHand) {
            card.SetTargetPos(x);
//            System.out.println(x);
            x += m_CardWidth;
        }

        newCard.SetStartPositionAndTargetPosition(m_Width + Card.HALF_WIDTH, m_PlayerCardsMidY, (int)Math.round(m_RightCardPos));
        m_PlayerHand.add(newCard);
        Pontoon.m_Pontoon.m_NumCardsBeingRevealed++;

        CalculateCurrentScore();

//        System.out.println(m_RightCardPos);
//        System.out.println();
    }

    private void SetupPanel() {
        m_Panel.setOpaque(false);

        // bit of a hack (?) that allows us to do absolute layout
        // there doesn't seem to be any documented way to achieve this?
        m_Panel.setLayout(null);
        m_Panel.updateUI();
    }

//    private void FindCardWidth(int numCards) {
//        double width = Card.WIDTH * numCards;
//        if (width > MAX_WIDTH) {
//            return MAX_WIDTH / numCards;
//        } else {
//            return Card.WIDTH;
//        }
//    }
//
//    private double FindTotalWidth(int numCards) {
//        double cardWidth = FindCardWidth(numCards);
//        return cardWidth * numCards;
//    }
//
//    private void FindLeftPos(int numCards) {
//        double width = FindTotalWidth(numCards);
//        width /= 2;
//        m_LeftCardPos = m_MidX - width;
//    }

    private void FindCardPositionsAndWidth(int numCards) {
        double width = Card.WIDTH * numCards;
        double half = width/2;

        if (width > m_MaxWidth) {
            half = m_MaxWidth / 2;
        }

        m_LeftCardPos = m_MidX - half + Card.HALF_WIDTH;
        m_RightCardPos = m_MidX + half - Card.HALF_WIDTH;

        m_CardWidth = Card.WIDTH;

        if (width > m_MaxWidth) {
            m_CardWidth -= (width - m_MaxWidth) / (numCards - 1);
        }

//        System.out.println(width);
//        System.out.println(m_CardWidth);
//        System.out.println(numCards);
//        System.out.println();
    }

//    private int FindLastCardPos(int numCards) {
//        int left = FindLeftPos(numCards);
//        int width = FindTotalWidth(numCards);
//        int right = left + width;
//        right -= Card.WIDTH;
//        return right;
//    }

//    private void AddCard(JLabel card, int x, int y) {
//        card.setBounds(x, y, Card.WIDTH, Card.HEIGHT);
////        m_Panel.add(card);
//    }

    public void UpdatePlayerCards(double m_DeltaTime) {
        m_Panel.removeAll();
        for (Card card: m_PlayerHand) {
            card.Update(m_DeltaTime);
        }
        for (Card card: m_OpponentHand) {
            card.Update(m_DeltaTime);
        }
        m_Panel.updateUI();
    }

    private void CalculateCurrentScore() {
        m_CurrentScore = 0;
        for(Card card: m_PlayerHand) {
            m_CurrentScore += card.m_Value;
        }
    }

    public void CheckIfBust() {
        if (m_CurrentScore > MAX_SCORE) {
            Pontoon.m_Pontoon.PlayerHasBust();
        }
    }

    public void DrawCardButtonPressed() {
//        if (!Pontoon.m_Pontoon.m_GameInProgress) {
//            return;
//        }

//        if (m_PlayerHand.size() == 2) {
//            for (Card card: m_PlayerHand) {
//                card.RevealWithoutDelay();
//            }
//        }

        if (m_PlayerHand.size() == 1) {
            m_PlayerHand.get(0).RevealWithoutDelay();
        }

        if (m_CurrentScore > MAX_SCORE) {
            return;
        }

        DrawCard();
    }

    public void StickButtonPressed() {
        double delay = HandStack.CalculatePositions(m_PlayerHand, m_Width / 4, m_PlayerCardsMidY, 0);
        HandStack.CalculatePositions(m_OpponentHand, (m_Width / 4) * 3, m_PlayerCardsMidY, delay);

        int score = 0;
        for(Card card: m_PlayerHand) {
            score += card.m_Value;
        }

        for (Card card: m_PlayerHand) {
            if (card.m_Value == 1) {
                if (score+10 <= MAX_SCORE) {
                    score += 10;
                }
            }
        }

        if (score > OPPONENT_SCORE) {
            Pontoon.m_Pontoon.PlayerHasWon();
        } else if (score < OPPONENT_SCORE) {
            Pontoon.m_Pontoon.ComputerHasWon();
        } else {
            Pontoon.m_Pontoon.PlayerAndComputerHaveSameScores();
        }
    }

    public void RenderCards(Graphics2D g) {
        for (Card card: m_PlayerHand) {
            card.Render(g);
        }

        for (Card card: m_OpponentHand) {
            card.Render(g);
        }
    }

    public void ReturnCardsToDeck() {
        for (Card card: m_PlayerHand) {
            Pontoon.m_Pontoon.m_Deck.ReturnCard(card);
        }

        m_PlayerHand.clear();

        for (Card card: m_OpponentHand) {
            Pontoon.m_Pontoon.m_Deck.ReturnCard(card);
        }

        m_OpponentHand.clear();
    }
}
