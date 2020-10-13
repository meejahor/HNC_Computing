import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameInterfaceLowerPanel {
    private JButton m_DrawCardButton;
    public JPanel m_Panel;

    public GameInterfaceLowerPanel() {
        m_DrawCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.print("button pressed");
                Pontoon.m_Pontoon.m_PlayerCards.DrawCardButtonPressed();
            }
        });
    }
}
