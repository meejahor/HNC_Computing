import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/***
 * GameInterfaceUpperPanel
 * Bound class for the panel with the Pontoon logo and the back to menu icon
 *
 * @author      Andrew Smith <meejahor@gmail.com>
 * @version     1.0
 * @since       1.0
 */
public class GameInterfaceUpperPanel {
    public JPanel m_Panel;
    private JLabel Logo;
    private JButton m_BackButton;

    public GameInterfaceUpperPanel() {
        m_BackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pontoon.m_Pontoon.BackToMenu();
            }
        });
    }

    public void SetUIStates() {
        m_BackButton.setVisible(Pontoon.m_Pontoon.m_GameState != Pontoon.GameState.MAIN_MENU);
    }
}
