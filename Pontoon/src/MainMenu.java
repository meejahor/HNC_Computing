import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/***
 * MainMenu
 * Bound class for the menu panel
 *
 * @author      Andrew Smith <meejahor@gmail.com>
 * @version     1.0
 * @since       1.0
 */
public class MainMenu {
    public JPanel m_Panel;
    private JButton m_PlayButton;
    private JButton m_RulesButton;

    public MainMenu() {
        m_PlayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pontoon.m_Pontoon.NewGame();
            }
        });
        m_RulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pontoon.m_Pontoon.ShowRules();
            }
        });
    }
}
