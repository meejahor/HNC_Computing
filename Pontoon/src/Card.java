import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Card {
    public static final int BORDER = 5;
    public static final int FULL_SIZE_WIDTH = 360;
    public static final int FULL_SIZE_HEIGHT = 540;
    public static final int FULL_SIZE_HALF_WIDTH = FULL_SIZE_WIDTH/2;
    public static final int FULL_SIZE_HALF_HEIGHT = FULL_SIZE_HEIGHT/2;
    public static final int WIDTH_WITHOUT_BORDER = 180;
    public static final int HEIGHT_WITHOUT_BORDER = 270;
    public static final int HALF_WIDTH_WITHOUT_BORDER = WIDTH_WITHOUT_BORDER/2;
    public static final int HALF_HEIGHT_WITHOUT_BORDER = HEIGHT_WITHOUT_BORDER/2;
    public static final int WIDTH = WIDTH_WITHOUT_BORDER + BORDER*2;
    public static final int HEIGHT = HEIGHT_WITHOUT_BORDER + BORDER*2;
    public static final int HALF_WIDTH = WIDTH/2;
    public static final int HALF_HEIGHT = HEIGHT/2;
    public static final double SCALE = (double)WIDTH_WITHOUT_BORDER / (double)FULL_SIZE_WIDTH;
    public static final double STACK_SCALE = SCALE * 0.75;

    private static final double ACCELERATION = 5000;
    private static final double LERP_ACCELERATION = 10;

    public final int m_Value;
    public final JLabel m_Label;
    private double m_X, m_Y, m_TargetX, m_TargetY;
    private double m_StartX, m_StartY;
    private double m_Rotation, m_TargetRotation;
    private double m_Speed;
    private double m_Lerp, m_LerpSpeed;
    public final BufferedImage m_Image;
//    private boolean m_Moving;
    private boolean m_ShowCardFront;
    private boolean m_MoveToStackPosition;
    private double m_Scale;
//    private double m_StartStackScale, m_TargetStackScale;
    private static final double POP_SCALE = SCALE * 1.2;
    private static final double SHRINK_SPEED = 0.5;

    private double m_PopRectangleScale;
    private double m_PopRectangleSpeed = 4;
//    private static final double POP_RECTANGLE_DECELERATION = 10;
    private float m_PopRectangleAlpha;
    private static final float POP_ALPHA_SPEED = 4;

    private double m_DelayBeforeReveal;
    private double m_DelayBeforeStack;

    enum CurrentStage {
        MOVING_IN_LINEUP,
        REVEALING,
        IN_LINEUP,
        MOVING_TO_STACK,
        IN_STACK
    }
    private CurrentStage m_CurrentStage;

    public Card(int value, JLabel label, BufferedImage image) {
        m_Value = value;
        m_Label = label;
        m_Image = image;
//        Reset();
    }

    public void SetStartPositionAndTargetPosition(double x, double y, double targetX) {
        m_X = x;
        m_Y = y;
        m_TargetX = targetX;
        m_Rotation = 0;
        m_Speed = 0;
        m_ShowCardFront = false;
        m_Scale = SCALE;
        m_PopRectangleAlpha = 0;
        m_CurrentStage = CurrentStage.MOVING_IN_LINEUP;
    }

    public void SetTargetPos(double x) {
        m_TargetX = x;
        m_CurrentStage = CurrentStage.MOVING_IN_LINEUP;
    }

    private void BeginReveal(double delay) {
        if (m_ShowCardFront) {
            return;
        }

        m_DelayBeforeReveal = delay;
        m_CurrentStage = CurrentStage.REVEALING;
    }

    private void RevealNow() {
        if (m_ShowCardFront) {
            return;
        }

        m_ShowCardFront = true;
        m_Scale = POP_SCALE;
        m_PopRectangleScale = 1;
        m_PopRectangleAlpha = 0.75f;

        Pontoon.m_Pontoon.m_NumCardsBeingRevealed--;

        if (Pontoon.m_Pontoon.m_NumCardsBeingRevealed == 0) {
            Pontoon.m_Pontoon.m_PlayerCards.CheckIfBust();
        }

        m_CurrentStage = CurrentStage.REVEALING;
    }

    private boolean LerpTowardsStack(double deltaTime) {
        m_LerpSpeed += LERP_ACCELERATION * deltaTime;
        m_Lerp += m_LerpSpeed * deltaTime;
        m_Lerp = Math.min(m_Lerp, 1);

        double dx = m_TargetX - m_StartX;
        double dy = m_TargetY - m_StartY;
        dx *= m_Lerp;
        dy *= m_Lerp;

        m_X = m_StartX + dx;
        m_Y = m_StartY + dy;

        m_Rotation = m_TargetRotation * m_Lerp;

        double sx = STACK_SCALE - SCALE;
        sx *= m_Lerp;
        m_Scale =  SCALE + sx;

        return m_Lerp == 1;
    }

//    public boolean MoveAndRotateTowardsStackPos(double distance) {
//        double dx = m_TargetX - m_X;
//        double dy = m_TargetY - m_Y;
//        double h = Math.sqrt(dx*dx + dy*dy);
//
//        double scale = distance / h;
//
//        if (scale > 1) {
//            m_X = m_TargetX;
//            m_Y = m_TargetY;
//            m_Rotation = m_TargetRotation;
//            return true;
//        }
//
//        double mx = dx * scale;
//        double my = dy * scale;
//        m_X += mx;
//        m_Y += my;
//
//        double rx = m_TargetRotation - m_Rotation;
//
//        return false;
//    }

    private void MoveInLineup(double deltaTime) {
        m_Speed += ACCELERATION * deltaTime;
        double distance = m_Speed * deltaTime;
        m_X -= distance;
        if (m_X <= m_TargetX) {
            m_X = m_TargetX;
            m_Speed = 0;
            RevealNow();
        }
    }

    private void Update_Reveal(double deltaTime) {
        if (m_DelayBeforeReveal > 0) {
            m_DelayBeforeReveal -= deltaTime;

            if (m_DelayBeforeReveal <= 0) {
                RevealNow();
            }

            return;
        }
    }

    private void Update_MoveToStack(double deltaTime) {
        if (m_DelayBeforeStack > 0) {
            m_DelayBeforeStack -= deltaTime;
            return;
        }

        boolean finished = LerpTowardsStack(deltaTime);

        if (finished) {
            m_CurrentStage = CurrentStage.IN_STACK;
        }
    }

    public void Update(double deltaTime) {
        switch(m_CurrentStage) {
            case MOVING_IN_LINEUP:
                MoveInLineup(deltaTime);
                break;

            case REVEALING:
                Update_Reveal(deltaTime);
                break;

            case MOVING_TO_STACK:
                Update_MoveToStack(deltaTime);
                break;
        }

        if (m_Scale > SCALE) {
            m_Scale -= SHRINK_SPEED * deltaTime;
        }

        if (m_PopRectangleAlpha > 0) {
            m_PopRectangleAlpha -= POP_ALPHA_SPEED * deltaTime;
            m_PopRectangleScale += m_PopRectangleSpeed * deltaTime;

//            if ((m_PopRectangleAlpha <= 0) && (m_Scale <= 1)) {
//                m_CurrentStage = CurrentStage.IN_LINEUP;
//            }
        }

//        if (m_MoveToStackPosition) {
//            LerpTowardsStack(deltaTime);
////            m_Speed += LERP_ACCELERATION * deltaTime;
////            double distance = m_Speed * deltaTime;
////
////            if (MoveAndRotateTowardsStackPos(distance)) {
//////                m_MoveToStackPosition = false;
////            };
//            return;
//        }
    }

    public void SnapToTargetPosWithRevealDelay(double delay) {
        m_X = m_TargetX;
        m_DelayBeforeReveal = delay;
        m_CurrentStage = CurrentStage.REVEALING;
    }

    public void RevealWithoutDelay() {
        RevealNow();
    }

    private int Scale(double value, double scale) {
        value *= scale;
        return (int)value;
    }

//    enum CurrentStage {
//        MOVING_IN_LINEUP,
//        REVEALING,
//        IN_LINEUP,
//        MOVING_TO_STACK,
//        IN_STACK
//    }

//    private void RenderNormal(Graphics2D g) {
//        BufferedImage image = m_ShowCardFront ? m_Image : Pontoon.m_Pontoon.m_Deck.m_CardBack;
//
//        AffineTransform backup = g.getTransform();
//        AffineTransform transform = new AffineTransform();
//        transform.translate(m_X, m_Y);
//        transform.scale(m_Scale, m_Scale);
//        transform.translate(-Card.FULL_SIZE_HALF_WIDTH, -Card.FULL_SIZE_HALF_HEIGHT);
//        transform.rotate(Math.toRadians(m_Rotation), FULL_SIZE_HALF_WIDTH, FULL_SIZE_HEIGHT);
//        g.drawImage(image, transform, null);
//        g.setTransform(backup);
//
////        if (m_Scale > 1) {
////            g.drawImage(
////                    image,
////                    (int)m_X - Scale(HALF_WIDTH_WITHOUT_BORDER, m_Scale),
////                    (int)m_Y - Scale(HALF_HEIGHT_WITHOUT_BORDER, m_Scale),
////                    Scale(Card.WIDTH_WITHOUT_BORDER, m_Scale),
////                    Scale(Card.HEIGHT_WITHOUT_BORDER, m_Scale),
////                    null
////            );
////        } else {
////            g.drawImage(
////                    image,
////                    (int)m_X - HALF_WIDTH_WITHOUT_BORDER,
////                    (int)m_Y - HALF_HEIGHT_WITHOUT_BORDER,
////                    Card.WIDTH_WITHOUT_BORDER,
////                    Card.HEIGHT_WITHOUT_BORDER,
////                    null
////            );
////        }
//    }

    public void Render(Graphics2D g) {
//        switch(m_CurrentStage) {
//            case MOVING_IN_LINEUP:
//            case REVEALING:
//            case IN_LINEUP:
//                RenderInStack(g);
//                break;
//
//            case MOVING_TO_STACK:
//            case IN_STACK:
//                RenderInStack(g);
//                break;
//        }

        BufferedImage image = m_ShowCardFront ? m_Image : Pontoon.m_Pontoon.m_Deck.m_CardBack;
        AffineTransform backup = g.getTransform();
        AffineTransform transform = new AffineTransform();
        transform.translate(m_X, m_Y);
        transform.scale(m_Scale, m_Scale);
        transform.translate(-Card.FULL_SIZE_HALF_WIDTH, -Card.FULL_SIZE_HALF_HEIGHT);
        transform.rotate(Math.toRadians(m_Rotation), FULL_SIZE_HALF_WIDTH, FULL_SIZE_HEIGHT);
        g.drawImage(image, transform, null);
        g.setTransform(backup);

        if (m_PopRectangleAlpha > 0) {
            Color col = new Color(1, 1, 1, m_PopRectangleAlpha);
            g.setColor(col);
            for (int i = 0; i <= 0; i += 1) {
                int size = i * 5;
                g.fillRect(
                        (int)m_X - Scale(HALF_WIDTH_WITHOUT_BORDER - size, m_PopRectangleScale),
                        (int)m_Y - Scale(HALF_HEIGHT_WITHOUT_BORDER - size, m_PopRectangleScale),
                        Scale(Card.WIDTH_WITHOUT_BORDER - size*2, m_PopRectangleScale),
                        Scale(Card.HEIGHT_WITHOUT_BORDER - size*2, m_PopRectangleScale)
                );
            }
        }
    }

//    public void Reset() {
//        m_ShowCardFront = false;
//        m_MoveToStackPosition = false;
//        m_Scale = 1;
//        m_PopRectangleScale = POP_SCALE;
//        m_PopRectangleAlpha = 0;
//        m_DelayBeforeReveal = 0;
//    }

    public void SetStackTargetPosition(double x, double y, double rotation, double delay) {
        m_StartX = m_X;
        m_StartY = m_Y;
        m_TargetX = x;
        m_TargetY = y;
        m_Rotation = 0;
        m_TargetRotation = rotation;
        m_Scale = SCALE;
        m_DelayBeforeStack = delay;
        m_Lerp = 0;
        m_LerpSpeed = 0;
        m_CurrentStage = CurrentStage.MOVING_TO_STACK;
    }
}
