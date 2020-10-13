import javax.swing.*;

public class Card {
    public static final int BORDER = 5;
    public static final int WIDTH = 180 + BORDER * 2;
    public static final int HEIGHT = 270 + BORDER * 2;
    public static final int HALF_WIDTH = WIDTH/2;
    public static final int HALF_HEIGHT = HEIGHT/2;

    public final int m_Value;
    public final JLabel m_Label;
    public int m_CurrentX, m_TargetX;
    private double speed;

    public Card(int value, JLabel label) {
        m_Value = value;
        m_Label = label;
    }

    public void SetPos(int x) {
        m_CurrentX = x;
        m_TargetX = x;
        speed = 0;
    }

    public void SetPos(int x, int targetX) {
        m_CurrentX = x;
        m_TargetX = targetX;
        speed = 0;
    }

    public void SetTargetPos(int x) {
        m_TargetX = x;
    }

    public void MoveCard(double deltaTime, double acceleration) {
        speed += acceleration * deltaTime;
        double distance = speed * deltaTime;
        if (m_CurrentX < m_TargetX) {
            m_CurrentX += distance;
            if (m_CurrentX >= m_TargetX) {
                m_CurrentX = m_TargetX;
                speed = 0;
            }
        } else {
            m_CurrentX -= distance;
            if (m_CurrentX <= m_TargetX) {
                m_CurrentX = m_TargetX;
                speed = 0;
            }
        }
    }

    public void SnapToTargetPos() {
        m_CurrentX = m_TargetX;
    }
}
