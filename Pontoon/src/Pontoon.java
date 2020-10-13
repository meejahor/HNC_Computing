import javax.swing.*;
import java.awt.image.BufferedImage;

public class Pontoon {
    private JFrame m_Frame;
    private JPanel m_PontoonPanel;
    private JPanel m_PlayerCardsPanel;
    private GameInterfaceUpperPanel m_GameInterfaceUpperPanel;
    private GameInterfaceLowerPanel m_GameInterfaceLowerPanel;
    private JLayeredPane m_LayeredPane;

    public PlayerCards m_PlayerCards;

    public static final Utils m_Utils = new Utils();
    public Deck m_Deck;

    public static Pontoon m_Pontoon;

    public Pontoon() {
        m_Deck = new Deck();
        m_Deck.LoadCards();

        m_PontoonPanel = new JPanel();
        m_PontoonPanel.setLayout(null);
        m_PontoonPanel.updateUI();

        m_Frame = new JFrame("Pontoon");
        m_Frame.setContentPane(m_PontoonPanel);
        m_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m_Frame.setSize(1000, 800);
        m_Frame.setLocationRelativeTo(null);
        m_Frame.setVisible(true);

        m_LayeredPane = new JLayeredPane();

        m_PontoonPanel.add(m_LayeredPane);
        m_LayeredPane.setBounds(0, 0, m_PontoonPanel.getWidth(), m_PontoonPanel.getHeight());

        m_PlayerCardsPanel = new JPanel();
        m_PlayerCardsPanel.setLayout(null);
        m_PlayerCardsPanel.updateUI();

        m_LayeredPane.add(m_PlayerCardsPanel, Integer.valueOf(0));
        m_PlayerCardsPanel.setBounds(0, 0, m_PontoonPanel.getWidth(), m_PontoonPanel.getHeight());

        m_PlayerCards = new PlayerCards(m_PlayerCardsPanel);
        SetupGameInterface();

        SetBackground();
    }

    private void SetupGameInterface() {
        m_GameInterfaceUpperPanel = new GameInterfaceUpperPanel();
        m_GameInterfaceUpperPanel.m_Panel.setOpaque(false);
        m_LayeredPane.add(m_GameInterfaceUpperPanel.m_Panel, Integer.valueOf(1));
        int upperIUHeight = (m_PontoonPanel.getHeight() / 2) - Card.HALF_HEIGHT;
        m_GameInterfaceUpperPanel.m_Panel.setBounds(0, 0, m_PontoonPanel.getWidth(), upperIUHeight);

        m_GameInterfaceLowerPanel = new GameInterfaceLowerPanel();
        m_GameInterfaceLowerPanel.m_Panel.setOpaque(false);
        m_LayeredPane.add(m_GameInterfaceLowerPanel.m_Panel, Integer.valueOf(2));
        int lowerIUHeight = m_PontoonPanel.getHeight() - m_PlayerCards.m_PlayerCardsMidY - Card.HALF_HEIGHT;
        m_GameInterfaceLowerPanel.m_Panel.setBounds(0, m_PontoonPanel.getHeight() - lowerIUHeight, m_PontoonPanel.getWidth(), lowerIUHeight);
    }

    private void SetBackground() {
        BufferedImage bufferedImage = Pontoon.m_Utils.LoadBufferedImage("/resources/felt_texture.jpg");
        ImageIcon icon = new ImageIcon(bufferedImage);

        int imageWidth = bufferedImage.getWidth();
        int imageHeight = bufferedImage.getHeight();
        int windowWidth = m_PontoonPanel.getWidth();
        int windowHeight = m_PontoonPanel.getHeight();

        m_PontoonPanel.setOpaque(false);
        m_PontoonPanel.setLayout(null);

        JLabel duplicate;
        for (int x = 0; x < windowWidth; x += imageWidth) {
            for (int y = 0; y < windowHeight; y += imageHeight) {
                duplicate = new JLabel(icon);
                duplicate.setBounds(x, y, imageWidth, imageHeight);
                m_PontoonPanel.add(duplicate);
            }
        }

        m_PontoonPanel.updateUI();
    }

    public static void main(String[] args) {
        m_Pontoon = new Pontoon();
        m_Pontoon.m_PlayerCards.DrawFirstTwoCards();
    }
}
