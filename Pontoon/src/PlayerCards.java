import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PlayerCards {
    private static final int MS_60_FPS = 17;

    private int m_Width, m_Height;
    private int m_MidX;
    public int m_PlayerCardsMidY;

    private JPanel m_Panel;
    private final List<Card> m_PlayerHand = new ArrayList<Card>();

    private long m_LastTime;
    private double m_DeltaTime;
    private double MILLISECONDS_TO_SECONDS = 0.001;
    private static final double ACCELERATION = 5000;

    private static int MAX_WIDTH = Card.WIDTH * 3;
    private double m_LeftCardPos;
    private double m_RightCardPos;
    private double m_CardWidth;

    ActionListener m_AnimationLoop;

    public PlayerCards(JPanel panel) {
        m_Panel = panel;
        m_Width = m_Panel.getWidth();
        m_Height = m_Panel.getHeight();
        m_MidX = m_Width/2;
        m_PlayerCardsMidY = (int)Math.round(m_Height * 0.55);
        SetupPanel();

        m_AnimationLoop = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long time = System.currentTimeMillis();
                m_DeltaTime = (double)time - m_LastTime;
                m_DeltaTime *= MILLISECONDS_TO_SECONDS;
                UpdatePlayerCards();
                m_LastTime = time;
            }
        };

        m_LastTime = System.currentTimeMillis();

        Timer timer = new Timer(MS_60_FPS, m_AnimationLoop);
        timer.setRepeats(true);
        timer.start();
    }

    public void DrawFirstTwoCards() {
        DrawCard();
        DrawCard();

        for(Card card : m_PlayerHand) {
            card.SnapToTargetPos();
        }
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
            card.SetTargetPos((int)Math.round(x));
//            System.out.println(x);
            x += m_CardWidth;
        }

        newCard.SetPos(m_Width, (int)Math.round(m_RightCardPos));
        m_PlayerHand.add(newCard);
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

        if (width > MAX_WIDTH) {
            half = MAX_WIDTH / 2;
        }

        m_LeftCardPos = m_MidX - half;
        m_RightCardPos = m_MidX + half - Card.WIDTH;

        m_CardWidth = Card.WIDTH;

        if (width > MAX_WIDTH) {
            m_CardWidth -= (width - MAX_WIDTH) / (numCards - 1);
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

    private void AddCard(JLabel card, int x, int y) {
        card.setBounds(x, y, Card.WIDTH, Card.HEIGHT);
        m_Panel.add(card);
    }

    private void UpdatePlayerCards() {
        m_Panel.removeAll();
        int numCards = m_PlayerHand.size();
        Card card;
        for (int i = numCards-1; i >= 0; i--) {
            card = m_PlayerHand.get(i);
            card.MoveCard(m_DeltaTime, ACCELERATION);
            AddCard(card.m_Label, card.m_CurrentX, m_PlayerCardsMidY -Card.HALF_HEIGHT);
        }
        m_Panel.updateUI();
    }

    private void CheckIfBust() {
        int score = 0;
        for(Card card: m_PlayerHand) {
            score += card.m_Value;
        }

        if (score > 21) {
            Pontoon.m_Pontoon.PlayerHasLost();
        }
    }

    public void DrawCardButtonPressed() {
        if (!Pontoon.m_Pontoon.m_PlayerCanDrawCards) {
            return;
        }

        DrawCard();
//        CheckIfBust();
    }

    public void StickButtonPressed() {
        int score = 0;
        for(Card card: m_PlayerHand) {
            score += card.m_Value;
        }

        for(Card card: m_PlayerHand) {
            if (card.m_Value == 1) {
                if (score+10 <= 21) {
                    score += 10;
                }
            }
        }

        if (score > 18) {
            Pontoon.m_Pontoon.PlayerHasWon();
        } else if (score < 18) {
            Pontoon.m_Pontoon.ComputerHasWon();
        } else {
            Pontoon.m_Pontoon.PlayerAndComputerHaveSameScores();
        }
    }
}
