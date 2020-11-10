import javax.swing.*;
import java.awt.*;

public class PlayerCardsPanel extends JPanel {

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Pontoon.m_Pontoon.Render((Graphics2D)g);
    }
}
