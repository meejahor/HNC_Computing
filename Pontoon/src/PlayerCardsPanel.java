import javax.swing.*;
import java.awt.*;

public class PlayerCardsPanel extends JPanel {

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Pontoon.m_Pontoon.m_PlayerCards.RenderCards((Graphics2D)g);
    }
}
