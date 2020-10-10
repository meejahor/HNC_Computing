import javax.swing.*;

public class Pontoon {
    private final JFrame m_Frame;
    private JPanel m_PontoonPanel;
    private JPanel m_PlayerCardsPanel;

    private final Cards m_Cards;

    public Pontoon() {
        m_Cards = new Cards();
        m_Cards.LoadCards();

        m_Frame = new JFrame("Pontoon");
        m_Frame.setContentPane(m_PontoonPanel);
        m_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m_Frame.setSize(1000, 800);
        m_Frame.setLocationRelativeTo(null);
        m_Frame.setVisible(true);

        SetupPlayerCardsPanel();

        UpdatePlayerCards();
    }

    private void SetupPlayerCardsPanel() {
        m_PlayerCardsPanel.setOpaque(false);

        // bit of a hack (?) that allows us to do absolute layout
        // there doesn't seem to be any documented way to achieve this?
        m_PlayerCardsPanel.setLayout(null);
        m_PlayerCardsPanel.updateUI();
    }

    private void AddCard(JLabel card, int x, int y) {
        card.setBounds(x, y, Cards.WIDTH, Cards.HEIGHT);
        m_PlayerCardsPanel.add(card);
    }

    private void UpdatePlayerCards() {
        AddCard(m_Cards.GetCard(0), 0, 0);
        AddCard(m_Cards.GetCard(1), 0 + Cards.WIDTH, 0);
    }

    public static void main(String[] args) {
        new Pontoon();
    }
}
