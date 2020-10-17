import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameInterfaceLowerPanel {
    private JButton m_DrawCardButton;
    public JPanel m_Panel;
    private JButton m_StickButton;
    private JButton m_PlayAgainButton;

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

        m_PlayAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pontoon.m_Pontoon.NewGame();
            }
        });
    }

    public void SetUIStates() {
        m_Panel.setVisible(!Pontoon.m_Pontoon.MenuOrRules());
        m_DrawCardButton.setEnabled(Pontoon.m_Pontoon.m_GameState == Pontoon.GameState.GAME_IN_PROGRESS);
        m_StickButton.setEnabled(Pontoon.m_Pontoon.m_GameState == Pontoon.GameState.GAME_IN_PROGRESS);
        m_PlayAgainButton.setVisible(Pontoon.m_Pontoon.GameHasEnded());
    }
}
