import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Card object
 * Stores the value and image for one playing card.
 * Handles animation and rendering of cards.
 *
 * @author      Andrew Smith <meejahor@gmail.com>
 * @version     1.0
 * @since       1.0
 */
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
    public static final int WIDTH_WITH_BORDER = WIDTH_WITHOUT_BORDER + BORDER*2;
    public static final int HEIGHT_WITH_BORDER = HEIGHT_WITHOUT_BORDER + BORDER*2;
    public static final int HALF_WIDTH = WIDTH_WITH_BORDER /2;
    public static final int HALF_HEIGHT = HEIGHT_WITH_BORDER /2;
    public static final double SCALE = (double)WIDTH_WITHOUT_BORDER / (double)FULL_SIZE_WIDTH;
    public static final double STACK_SCALE = SCALE * 0.75;

    public final int m_Value;

    private static final double ACCELERATION = 5000;
    private static final double LERP_ACCELERATION = 10;
    private static final double LERP_ACCELERATION_MAX = 2;

    private double m_X, m_Y;
    private double m_StartX, m_StartY;
    private double m_TargetX, m_TargetY;
    private double m_Rotation, m_TargetRotation;
    private double m_Speed;
    private double m_Lerp, m_LerpSpeed;
    public final BufferedImage m_Image;
    private boolean m_ShowCardFront;
    private double m_Scale;
    private static final double POP_SCALE = SCALE * 1.25;
    private static final double SHRINK_SPEED = 0.5;

    private double m_PopX, m_PopY;
    private double m_PopRectangleScale;
    private double m_PopRectangleSpeed = 4;
    private float m_PopRectangleAlpha;
    private static final float POP_ALPHA_SPEED = 4;

    private double m_DelayBeforeReveal;
    private double m_DelayBeforeStack;

    // Used for keeping track of what the card is currently doing
    enum CurrentStage {
        MOVING_IN_LINEUP,
        WAITING_TO_REVEAL,
        STATIC_IN_LINEUP,
        MOVING_TO_STACK,
        IN_STACK
    }
    private CurrentStage m_CurrentStage;

    public Card(int value, String filename) {
        m_Value = value;
        m_Image = Pontoon.m_Utils.LoadBufferedImage(filename);
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

    // reveal the card immediately without the reveal delay or animation
    public void InstantRevealNoPop() {
        m_ShowCardFront = true;
        m_CurrentStage = CurrentStage.STATIC_IN_LINEUP;
    }

    private void RevealNow() {
        if (m_ShowCardFront) {
            return;
        }

        m_ShowCardFront = true;
        m_Scale = POP_SCALE;
        m_PopRectangleScale = 1;
        m_PopRectangleAlpha = 0.75f;
        // popx,y store the location when the pop animation starts. This is because the animation can
        // start while the card is moving, and it looks weird if the animation moves with the card
        m_PopX = m_X;
        m_PopY = m_Y;

        // we keep track of how many cards are waiting to be revealed. We only check if the player
        // has gone bust when all cards have been revealed. This is because it feels wrong to tell the player
        // they've gone bust before showing them the card that made them go bust.
        Pontoon.m_Pontoon.m_NumCardsBeingRevealed--;

        if (Pontoon.m_Pontoon.m_NumCardsBeingRevealed == 0) {
            Pontoon.m_Pontoon.m_PlayerCards.CheckIfBust();
        }

        m_CurrentStage = CurrentStage.STATIC_IN_LINEUP;
    }

    private boolean LerpTowardsStack(double deltaTime) {
        m_LerpSpeed += LERP_ACCELERATION * deltaTime;
        m_LerpSpeed = Math.min(m_LerpSpeed, LERP_ACCELERATION_MAX);
        m_Lerp += m_LerpSpeed * deltaTime;
        m_Lerp = Math.min(m_Lerp, 1);

        double dx = m_TargetX - m_StartX;
        double dy = m_TargetY - m_StartY;
        dx *= m_Lerp;
        dy *= m_Lerp;

        m_X = m_StartX + dx;
        m_Y = m_StartY + dy;

        m_Rotation = m_TargetRotation * m_Lerp;

        // split stage lerp to make the card go bigger before lerping to target size
        if (m_Lerp <= 0.5) {
            double sx = POP_SCALE - SCALE;
            sx *= m_Lerp*2;
            m_Scale = SCALE + sx;
        } else {
            double sx = STACK_SCALE - POP_SCALE;
            sx *= (m_Lerp-0.5)*2;
            m_Scale = POP_SCALE + sx;
        }

        return m_Lerp == 1;
    }

    private void MoveInLineup(double deltaTime) {
        m_Speed += ACCELERATION * deltaTime;
        double distance = m_Speed * deltaTime;
        m_X -= distance;
        // cards always appear at the right of the screen so they'll only ever move left
        if (m_X <= m_TargetX) {
            m_X = m_TargetX;
            m_Speed = 0;
            RevealNow();
        }
    }

    private void Update_Reveal(double deltaTime) {
        if (m_DelayBeforeReveal > 0) {
            m_DelayBeforeReveal -= deltaTime;
        }

        if (m_DelayBeforeReveal <= 0) {
            RevealNow();
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
        if (m_PopRectangleAlpha > 0) {
            m_PopRectangleAlpha -= POP_ALPHA_SPEED * deltaTime;
            m_PopRectangleScale += m_PopRectangleSpeed * deltaTime;
        }

        switch(m_CurrentStage) {
            case MOVING_IN_LINEUP:
                MoveInLineup(deltaTime);
                break;

            case WAITING_TO_REVEAL:
                Update_Reveal(deltaTime);
                break;

            case MOVING_TO_STACK:
                Update_MoveToStack(deltaTime);
                // we return here because we're using m_Scale in the stack animation, so we don't want m_Scale
                // to be modified after the switch block
                return;
        }

        if (m_Scale > SCALE) {
            m_Scale -= SHRINK_SPEED * deltaTime;
        }
    }

    public void SnapToTargetPosWithRevealDelay(double delay) {
        m_X = m_TargetX;
        m_DelayBeforeReveal = delay;
        m_CurrentStage = CurrentStage.WAITING_TO_REVEAL;
    }

    public void RevealWithoutDelay() {
        RevealNow();
    }

    private int Scale(double value, double scale) {
        value *= scale;
        return (int)value;
    }

    public void Render(Graphics2D g) {
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
                        (int)m_PopX - Scale(HALF_WIDTH_WITHOUT_BORDER - size, m_PopRectangleScale),
                        (int)m_PopY - Scale(HALF_HEIGHT_WITHOUT_BORDER - size, m_PopRectangleScale),
                        Scale(Card.WIDTH_WITHOUT_BORDER - size*2, m_PopRectangleScale),
                        Scale(Card.HEIGHT_WITHOUT_BORDER - size*2, m_PopRectangleScale)
                );
            }
        }
    }

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
