import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameInterfaceLowerPanel {
    private JButton m_DrawCardButton;
    public JPanel m_Panel;
    private JButton m_StickButton;

    public GameInterfaceLowerPanel() {

        m_DrawCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pontoon.m_Pontoon.m_PlayerCards.DrawCardButtonPressed();
            }
        });

        m_StickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pontoon.m_Pontoon.m_PlayerCards.StickButtonPressed();
            }
        });
    }
}
