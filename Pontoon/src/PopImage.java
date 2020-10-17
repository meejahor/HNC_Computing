import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class PopImage {
    private BufferedImage m_Image;
    private double m_HalfWidth, m_HalfHeight;
    private int m_X, m_Y;
    private double m_Scale;
    private double m_Lerp;
    private double m_LerpSpeed;
    private static final double LERP_SPEED = 1;
    private static final double LERP_ACCELERATION = 5;

    public PopImage(BufferedImage image, int x, int y) {
        m_Image = image;
        m_HalfWidth = image.getWidth() / 2;
        m_HalfHeight = image.getHeight() / 2;
        m_X = x;
        m_Y = y;
    }

    public void StartPop() {
        m_Lerp = 0;
        m_LerpSpeed = 0;
    }

    public void Update(double deltaTime) {
        m_LerpSpeed += LERP_ACCELERATION * deltaTime;
        m_Lerp += m_LerpSpeed * deltaTime;
        m_Lerp = Math.min(m_Lerp, 1);

        if (m_Lerp <= 0.8) {
            double sx = 1.1 - 1;
            m_Scale = 1.2 * (m_Lerp * 1.25);
        } else {
            double sx = 1 - 1.1;
            sx *= (m_Lerp - 0.8) * 5;
            m_Scale = 1 + sx;
        }
    }

    public void Render(Graphics2D g) {
        AffineTransform transform = new AffineTransform();
        transform.translate(m_X, m_Y);
        transform.scale(m_Scale, m_Scale);
        transform.translate(-m_HalfWidth, -m_HalfHeight);
        transform.rotate(Math.toRadians(360 * m_Lerp), m_HalfWidth, m_HalfHeight);
        g.drawImage(m_Image, transform, null);
    }
}
