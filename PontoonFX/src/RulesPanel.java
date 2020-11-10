import javax.swing.*;
import java.awt.*;

/***
 * RulesPanel
 * Bound class for the rules panel
 *
 * @author      Andrew Smith <meejahor@gmail.com>
 * @version     1.0
 * @since       1.0
 */
public class RulesPanel {
    public JPanel m_Panel;
    private JLabel m_RulesLabel;

    public void Init() {
        Font f = m_RulesLabel.getFont();
        f = new Font(f.getName(), Font.PLAIN, 18);
        m_RulesLabel.setFont(f);
        m_RulesLabel.setText("<html><p><b>Pontoon rules:</b></p><p></p><p>You are playing against a computer opponent.</p><p></p><p>You are dealt a card. You can then choose to draw more cards, or you can 'stick' and not draw any more.</p><p></p><p>The aim of the game is to score as close to 21 as possible. If you score more than 21 then you go 'bust' and lose the game.</p><p></p><p>If you stick, the winner is the player with the highest score.</p></html>");
    }
}
