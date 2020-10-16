import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Pontoon {
    private JFrame m_Frame;
    private MainMenu m_MainMenu;
    private JPanel m_PontoonPanel;
    private PlayerCardsPanel m_PlayerCardsPanel;
    private CardsBackgroundPanel m_CardsBackgroundPanel;
    private GameInterfaceUpperPanel m_GameInterfaceUpperPanel;
    private GameInterfaceLowerPanel m_GameInterfaceLowerPanel;
    private JLabel m_WinTextPanel;
    private JLayeredPane m_LayeredPane;
    private YouLose m_YouLose;
//    private WinText m_WinText;
    private JLabel m_PlayerWinsLabel, m_OpponentWinsLabel, m_DrawLabel;

    private static final Integer LAYER_PLAYER_CARDS_PANEL = Integer.valueOf(0);
    private static final Integer LAYER_CARDS_BACKGROUND = Integer.valueOf(1);
    private static final Integer LAYER_UPPER_UI = Integer.valueOf(2);
    private static final Integer LAYER_LOWER_UI = Integer.valueOf(3);
    private static final Integer LAYER_YOU_LOSE = Integer.valueOf(4);
    private static final Integer LAYER_PLAYER_WINS = Integer.valueOf(5);
    private static final Integer LAYER_OPPONENT_WINS = Integer.valueOf(6);
    private static final Integer LAYER_DRAW = Integer.valueOf(7);
    private static final Integer LAYER_MAIN_MENU = Integer.valueOf(8);

    private static final int CARD_BACKGROUND_HEIGHT = 270;
    private static final int CARD_BACKGROUND_VERTICAL_OFFSET = 70;

    public PlayerCards m_PlayerCards;

    public static final Utils m_Utils = new Utils();
    public Deck m_Deck;

    enum GameState {
        MAIN_MENU,
        GAME_IN_PROGRESS,
        BUST,
        PLAYER_WINS,
        OPPONENT_WINS,
        DRAW
    }
    public GameState m_GameState;

    public int m_NumCardsBeingRevealed;

    public static Pontoon m_Pontoon;

    public Pontoon() {
        m_Deck = new Deck();
        m_Deck.LoadCards();
    }

    private void Init() {
        m_PontoonPanel = new JPanel();
        m_PontoonPanel.setLayout(null);
        m_PontoonPanel.updateUI();

        m_Frame = new JFrame("Pontoon");
        m_Frame.setResizable(false);
        m_Frame.setContentPane(m_PontoonPanel);
        m_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m_Frame.setSize(1000, 800);
        m_Frame.setLocationRelativeTo(null);
        m_Frame.setVisible(true);

        m_PlayerCardsPanel = new PlayerCardsPanel();
        m_PlayerCardsPanel.setLayout(null);
        m_PlayerCardsPanel.updateUI();
        m_PlayerCards = new PlayerCards(m_PlayerCardsPanel, m_Frame.getWidth(), m_Frame.getHeight());

        m_LayeredPane = new JLayeredPane();

        m_CardsBackgroundPanel = new CardsBackgroundPanel();
        m_LayeredPane.add(m_CardsBackgroundPanel.m_Panel, LAYER_CARDS_BACKGROUND);
        m_CardsBackgroundPanel.m_Panel.setBounds(0, m_PontoonPanel.getHeight() - CARD_BACKGROUND_HEIGHT + CARD_BACKGROUND_VERTICAL_OFFSET, m_PontoonPanel.getWidth(), CARD_BACKGROUND_HEIGHT);

        m_PontoonPanel.add(m_LayeredPane);
        m_LayeredPane.setBounds(0, 0, m_Frame.getWidth(), m_Frame.getHeight());

        m_LayeredPane.add(m_PlayerCardsPanel, LAYER_PLAYER_CARDS_PANEL);
        m_PlayerCardsPanel.setBounds(0, 0, m_Frame.getWidth(), m_Frame.getHeight());

        SetupGameInterface();
        SetupYouLose();
        SetupResultLabels();
        SetupMenu();

        SetBackground();

        m_GameState = GameState.MAIN_MENU;
        UpdateUIStates();
    }

    private void PositionLabel(JLabel label, int x, int y) {
        int width = label.getIcon().getIconWidth();
        int height = label.getIcon().getIconHeight();
        label.setBounds(x - (width/2), y - (height/2), width, height);
        System.out.println(width);
    }

    private void SetupResultLabels() {
        m_PlayerWinsLabel = m_Utils.LoadImageLabel("/resources/player_wins.png");
        m_LayeredPane.add(m_PlayerWinsLabel, LAYER_PLAYER_WINS);
        PositionLabel(m_PlayerWinsLabel, m_PontoonPanel.getWidth() / 4, m_PlayerCards.m_PlayerCardsMidY);

        m_OpponentWinsLabel = m_Utils.LoadImageLabel("/resources/opponent_wins.png");
        m_LayeredPane.add(m_OpponentWinsLabel, LAYER_OPPONENT_WINS);
        PositionLabel(m_OpponentWinsLabel, (m_PontoonPanel.getWidth() / 4) * 3, m_PlayerCards.m_PlayerCardsMidY);
    }

    private void SetupMenu() {
        m_MainMenu = new MainMenu();
//        m_MainMenu.m_Panel.setVisible(false);
        m_LayeredPane.add(m_MainMenu.m_Panel, LAYER_MAIN_MENU);
        m_MainMenu.m_Panel.setBounds(0, m_PlayerCards.m_PlayerCardsMidY - Card.HALF_HEIGHT, m_PontoonPanel.getWidth(), Card.HEIGHT);
    }

    private void SetupYouLose() {
        m_YouLose = new YouLose();
        m_YouLose.m_Panel.setVisible(false);
        m_YouLose.m_Panel.setBackground(new Color(0, 0, 0, 63));
        m_LayeredPane.add(m_YouLose.m_Panel, LAYER_YOU_LOSE);
        m_YouLose.m_Panel.setBounds(0, m_PlayerCards.m_PlayerCardsMidY - Card.HALF_HEIGHT, m_PontoonPanel.getWidth(), Card.HEIGHT);
    }

    private void SetupGameInterface() {
        m_GameInterfaceUpperPanel = new GameInterfaceUpperPanel();
//        m_GameInterfaceUpperPanel.m_Panel.setOpaque(false);
        m_LayeredPane.add(m_GameInterfaceUpperPanel.m_Panel, LAYER_UPPER_UI);
        int upperIUHeight = m_PlayerCards.m_PlayerCardsMidY - Card.HALF_HEIGHT;
        m_GameInterfaceUpperPanel.m_Panel.setBounds(0, 0, m_PontoonPanel.getWidth(), upperIUHeight);

        m_GameInterfaceLowerPanel = new GameInterfaceLowerPanel();
//        m_GameInterfaceLowerPanel.m_Panel.setOpaque(false);
        m_LayeredPane.add(m_GameInterfaceLowerPanel.m_Panel, LAYER_LOWER_UI);
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

    public void PlayerHasBust() {
        m_GameState = GameState.BUST;
        UpdateUIStates();
    }

    private void UpdateUIStates() {
        m_MainMenu.m_Panel.setVisible(m_GameState == GameState.MAIN_MENU);
        m_PlayerCardsPanel.setVisible(m_GameState != GameState.MAIN_MENU);
        m_GameInterfaceUpperPanel.SetUIStates();
        m_GameInterfaceLowerPanel.SetUIStates();
        m_YouLose.m_Panel.setVisible(m_GameState == GameState.BUST);
        m_PlayerWinsLabel.setVisible(m_GameState == GameState.PLAYER_WINS);
        m_OpponentWinsLabel.setVisible(m_GameState == GameState.OPPONENT_WINS);
    }

    public void PlayerHasWon() {
        m_GameState = GameState.PLAYER_WINS;
        UpdateUIStates();
    }

    public void ComputerHasWon() {
        m_GameState = GameState.OPPONENT_WINS;
        UpdateUIStates();
    }

    public void PlayerAndComputerHaveSameScores() {
    }

    public static void main(String[] args) {
        m_Pontoon = new Pontoon();
        m_Pontoon.Init();
        m_Pontoon.NewGame();
    }

    public void NewGame() {
        m_PlayerCards.ReturnCardsToDeck();
        m_NumCardsBeingRevealed = 0;
        m_PlayerCards.DrawOpponentCards();
        m_PlayerCards.DrawOpeningHand();
        m_GameState = GameState.GAME_IN_PROGRESS;
        UpdateUIStates();
    }

    public boolean GameHasEnded() {
        boolean ret = m_GameState == GameState.BUST;
        ret |= m_GameState == GameState.PLAYER_WINS;
        ret |= m_GameState == GameState.OPPONENT_WINS;
        ret |= m_GameState == GameState.DRAW;
        return ret;
    }

    public void BackToMenu() {
        m_GameState = GameState.MAIN_MENU;
        UpdateUIStates();
    }
}
