import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Card {
    public static final int BORDER = 5;
    public static final int WIDTH_WITHOUT_BORDER = 180;
    public static final int HEIGHT_WITHOUT_BORDER = 270;
    public static final int HALF_WIDTH_WITHOUT_BORDER = WIDTH_WITHOUT_BORDER/2;
    public static final int HALF_HEIGHT_WITHOUT_BORDER = HEIGHT_WITHOUT_BORDER/2;
    public static final int WIDTH = WIDTH_WITHOUT_BORDER + BORDER*2;
    public static final int HEIGHT = HEIGHT_WITHOUT_BORDER + BORDER*2;
    public static final int HALF_WIDTH = WIDTH/2;
    public static final int HALF_HEIGHT = HEIGHT/2;

    private static final double ACCELERATION = 5000;

    public final int m_Value;
    public final JLabel m_Label;
    private double m_X, m_Y, m_TargetX;
    private double speed;
    public final BufferedImage m_Image;
    private boolean m_Moving;
    private boolean m_ShowCardFront;
    private double m_Scale;
    private static final double POP_SCALE = 1.2;
    private static final double SHRINK_SPEED = 0.5;

    private double m_PopRectangleScale;
    private double m_PopRectangleSpeed = 4;
//    private static final double POP_RECTANGLE_DECELERATION = 10;
    private float m_PopRectangleAlpha;
    private static final float POP_ALPHA_SPEED = 4;

    private double m_DelayBeforeReveal;

    public Card(int value, JLabel label, BufferedImage image) {
        m_Value = value;
        m_Label = label;
        m_Image = image;
        Reset();
    }

//    public void SetPos(int x, int y) {
//        m_X = x;
//        m_Y = y;
//        m_TargetX = x;
//        speed = 0;
//    }

    public void SetPos(double x, double y, double targetX) {
        m_X = x;
        m_Y = y;
        m_TargetX = targetX;
        m_Moving = true;
        speed = 0;
    }

    public void SetTargetPos(double x) {
        m_TargetX = x;
        m_Moving = true;
    }

    private void ShowCard() {
        m_ShowCardFront = true;
        m_Scale = POP_SCALE;
        m_PopRectangleAlpha = 0.75f;

        Pontoon.m_Pontoon.m_NumCardsBeingRevealed--;

        if (Pontoon.m_Pontoon.m_NumCardsBeingRevealed == 0) {
            Pontoon.m_Pontoon.m_PlayerCards.CheckIfBust();
        }
    }

    public void Update(double deltaTime) {
        if (m_DelayBeforeReveal > 0) {
            m_DelayBeforeReveal -= deltaTime;
            if (m_DelayBeforeReveal < 0) {
                ShowCard();
            }
            return;
        }

        if (m_Moving) {
            speed += ACCELERATION * deltaTime;
            double distance = speed * deltaTime;
            m_X -= distance;
            if (m_X <= m_TargetX) {
                m_X = m_TargetX;
                speed = 0;
                m_Moving = false;

                if (!m_ShowCardFront) {
                    ShowCard();
                }
            }
            return;
        }

        if (m_Scale > 1) {
            m_Scale -= SHRINK_SPEED * deltaTime;
        }

        if (m_PopRectangleAlpha > 0) {
            m_PopRectangleAlpha -= POP_ALPHA_SPEED * deltaTime;
//            m_PopRectangleSpeed -= POP_RECTANGLE_DECELERATION * deltaTime;
//            if (m_PopRectangleSpeed < 0) {
//                m_PopRectangleSpeed = 0;
//            }
            m_PopRectangleScale += m_PopRectangleSpeed * deltaTime;
        }
    }

    public void SnapToTargetPosWithDelay(double delay) {
        m_X = m_TargetX;
        m_Moving = false;
        m_DelayBeforeReveal = delay;
    }

    private int Scale(double value, double scale) {
        value *= scale;
        return (int)value;
    }

    public void Render(Graphics g) {
        BufferedImage image = m_ShowCardFront ? m_Image : Pontoon.m_Pontoon.m_Deck.m_CardBack;

        if (m_Scale > 1) {
            g.drawImage(
                    image,
                    (int)m_X - Scale(HALF_WIDTH_WITHOUT_BORDER, m_Scale),
                    (int)m_Y - Scale(HALF_HEIGHT_WITHOUT_BORDER, m_Scale),
                    Scale(Card.WIDTH_WITHOUT_BORDER, m_Scale),
                    Scale(Card.HEIGHT_WITHOUT_BORDER, m_Scale),
                    null
            );
        } else {
            g.drawImage(
                    image,
                    (int)m_X - HALF_WIDTH_WITHOUT_BORDER,
                    (int)m_Y - HALF_HEIGHT_WITHOUT_BORDER,
                    Card.WIDTH_WITHOUT_BORDER,
                    Card.HEIGHT_WITHOUT_BORDER,
                    null
            );
        }

        if (m_PopRectangleAlpha > 0) {
            Color col = new Color(1, 1, 1, m_PopRectangleAlpha);
            g.setColor(col);
            for (int i = 0; i <= 0; i += 1) {
                int size = i * 5;
                g.fillRoundRect(
                        (int)m_X - Scale(HALF_WIDTH_WITHOUT_BORDER - size, m_PopRectangleScale),
                        (int)m_Y - Scale(HALF_HEIGHT_WITHOUT_BORDER - size, m_PopRectangleScale),
                        Scale(Card.WIDTH_WITHOUT_BORDER - size*2, m_PopRectangleScale),
                        Scale(Card.HEIGHT_WITHOUT_BORDER - size*2, m_PopRectangleScale),
                        50 + size, 50 + size
                );
            }
        }
    }

    public void Reset() {
        m_ShowCardFront = false;
        m_Scale = 1;
        m_PopRectangleScale = POP_SCALE;
        m_PopRectangleAlpha = 0;
        m_DelayBeforeReveal = 0;
    }
}
