import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/***
 * Pontoon class and program entry point
 *
 * @author      Andrew Smith <meejahor@gmail.com>
 * @version     1.0
 * @since       1.0
 */
public class Pontoon {
    private JFrame m_Frame;
    private MainMenu m_MainMenu;
    private RulesPanel m_RulesPanel;
    private JPanel m_PontoonPanel;
    private PlayerCardsPanel m_PlayerCardsPanel;
    private CardsBackgroundPanel m_CardsBackgroundPanel;
    private GameInterfaceUpperPanel m_GameInterfaceUpperPanel;
    private GameInterfaceLowerPanel m_GameInterfaceLowerPanel;
    private JLabel m_WinTextPanel;
    private JLayeredPane m_LayeredPane;
    private YouLose m_YouLose;

    private PopImage m_PlayerWins;
    private PopImage m_OpponentWins;
    private PopImage m_ItsADraw;
    private PopImage m_GameStatePopImage = null;

    private static final Integer LAYER_PLAYER_CARDS_PANEL = Integer.valueOf(0);
    private static final Integer LAYER_CARDS_BACKGROUND = Integer.valueOf(1);
    private static final Integer LAYER_UPPER_UI = Integer.valueOf(2);
    private static final Integer LAYER_LOWER_UI = Integer.valueOf(3);
    private static final Integer LAYER_YOU_LOSE = Integer.valueOf(4);
    private static final Integer LAYER_MAIN_MENU = Integer.valueOf(5);
    private static final Integer LAYER_RULES = Integer.valueOf(6);

    private static final int CARD_BACKGROUND_HEIGHT = 270;
    private static final int CARD_BACKGROUND_VERTICAL_OFFSET = 70;

    public int m_Width, m_Height;
    public int m_MidX;
    public int m_PlayerCardsMidY;

    public int m_MaxWidth;

    public PlayerCards m_PlayerCards;
    public OpponentCards m_OpponentCards;

    public static final Utils m_Utils = new Utils();
    public Deck m_Deck;

    enum GameState {
        MAIN_MENU,
        RULES,
        GAME_IN_PROGRESS,
        BUST,
        PLAYER_WINS,
        OPPONENT_WINS,
        DRAW
    }
    public GameState m_GameState;

    public int m_NumCardsBeingRevealed;

    private long m_LastTime;
    private static final double MILLISECONDS_TO_SECONDS = 0.001;
    private static final int MS_60_FPS = 17;
    private ActionListener m_AnimationLoop;
    private Timer m_Timer;

    public static final int MAX_SCORE = 21;
    public static final int OPPONENT_SCORE = 18;

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

        m_Width = m_Frame.getWidth();
        m_Height = m_Frame.getHeight();
        m_MaxWidth = m_Frame.getWidth();
        m_MidX = m_Width/2;
        m_PlayerCardsMidY = m_Height / 2;

        m_PlayerCardsPanel = new PlayerCardsPanel();
        m_PlayerCardsPanel.setOpaque(false);
        m_PlayerCardsPanel.setLayout(null);
        m_PlayerCardsPanel.updateUI();
        m_PlayerCards = new PlayerCards();
        m_OpponentCards = new OpponentCards();

        m_LayeredPane = new JLayeredPane();

        m_CardsBackgroundPanel = new CardsBackgroundPanel();
        m_LayeredPane.add(m_CardsBackgroundPanel.m_Panel, LAYER_CARDS_BACKGROUND);
        m_CardsBackgroundPanel.m_Panel.setBounds(0, m_PontoonPanel.getHeight() - CARD_BACKGROUND_HEIGHT + CARD_BACKGROUND_VERTICAL_OFFSET, m_PontoonPanel.getWidth(), CARD_BACKGROUND_HEIGHT);

        m_PontoonPanel.add(m_LayeredPane);
        m_LayeredPane.setBounds(0, 0, m_Frame.getWidth(), m_Frame.getHeight());

        m_LayeredPane.add(m_PlayerCardsPanel, LAYER_PLAYER_CARDS_PANEL);
        m_PlayerCardsPanel.setBounds(0, 0, m_Width, m_Height);

        SetupGameInterface();
        SetupYouLose();
        SetupResultLabels();
        SetupMenu();
        SetupRules();

        SetBackground();

        InitUpdateTimer();

        m_GameState = GameState.MAIN_MENU;
        UpdateUIStates();
    }

    // timer function to update animation states and call rendering functions every 1/60s
    private void InitUpdateTimer() {
        m_AnimationLoop = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long time = System.currentTimeMillis();
                double deltaTime = (double)time - m_LastTime;
                deltaTime *= MILLISECONDS_TO_SECONDS;
                m_LastTime = time;

                m_PlayerCards.UpdatePlayerCards(deltaTime);
                m_OpponentCards.UpdateOpponentCards(deltaTime);
                m_PlayerCardsPanel.updateUI();

                if (m_GameStatePopImage != null) {
                    m_GameStatePopImage.Update(deltaTime);
                }
            }
        };

        m_Timer = new Timer(MS_60_FPS, m_AnimationLoop);
        m_Timer.setRepeats(true);
    }

    private void StartUpdateTimer() {
        m_LastTime = System.currentTimeMillis();
        m_Timer.start();
    }

    private void StopUpdateTimer() {
        m_Timer.stop();
    }

    private void SetupResultLabels() {
        int y = m_PlayerCardsMidY + (Card.HALF_HEIGHT / 4);

        m_PlayerWins = new PopImage(
                m_Utils.LoadBufferedImage("/resources/player_wins.png"),
                m_PontoonPanel.getWidth() / 4,
                y
        );

        m_OpponentWins = new PopImage(
                m_Utils.LoadBufferedImage("/resources/opponent_wins.png"),
                (m_PontoonPanel.getWidth() / 4) * 3,
                y
        );

        m_ItsADraw = new PopImage(
                m_Utils.LoadBufferedImage("/resources/its_a_draw.png"),
                (m_PontoonPanel.getWidth() / 2),
                y
        );
    }

    private void SetupMenu() {
        m_MainMenu = new MainMenu();
        m_LayeredPane.add(m_MainMenu.m_Panel, LAYER_MAIN_MENU);
        m_MainMenu.m_Panel.setBounds(0, m_PlayerCardsMidY - Card.HALF_HEIGHT, m_PontoonPanel.getWidth(), Card.HEIGHT_WITH_BORDER);
    }

    private void SetupRules() {
        m_RulesPanel = new RulesPanel();
        m_LayeredPane.add(m_RulesPanel.m_Panel, LAYER_RULES);
        m_RulesPanel.m_Panel.setBounds(0, m_PlayerCardsMidY - Card.HALF_HEIGHT, m_PontoonPanel.getWidth(), Card.HEIGHT_WITH_BORDER);
        m_RulesPanel.Init();
    }

    private void SetupYouLose() {
        m_YouLose = new YouLose();
        m_YouLose.m_Panel.setVisible(false);
        m_YouLose.m_Panel.setBackground(new Color(0, 0, 0, 63));
        m_LayeredPane.add(m_YouLose.m_Panel, LAYER_YOU_LOSE);
        m_YouLose.m_Panel.setBounds(0, m_PlayerCardsMidY - Card.HALF_HEIGHT, m_PontoonPanel.getWidth(), Card.HEIGHT_WITH_BORDER);
    }

    private void SetupGameInterface() {
        m_GameInterfaceUpperPanel = new GameInterfaceUpperPanel();
        m_LayeredPane.add(m_GameInterfaceUpperPanel.m_Panel, LAYER_UPPER_UI);
        int upperIUHeight = m_PlayerCardsMidY - Card.HALF_HEIGHT;
        m_GameInterfaceUpperPanel.m_Panel.setBounds(0, 0, m_PontoonPanel.getWidth(), upperIUHeight);

        m_GameInterfaceLowerPanel = new GameInterfaceLowerPanel();
        m_LayeredPane.add(m_GameInterfaceLowerPanel.m_Panel, LAYER_LOWER_UI);
        int lowerIUHeight = m_PontoonPanel.getHeight() - m_PlayerCardsMidY - Card.HALF_HEIGHT;
        m_GameInterfaceLowerPanel.m_Panel.setBounds(0, m_PontoonPanel.getHeight() - lowerIUHeight, m_PontoonPanel.getWidth(), lowerIUHeight);
    }

    // JPanel doesn't support tiled backgrounds so we set-up a JLabel with the background image and then
    // add enough copies to fill the width and height of the panel. These are child objects so the panel
    // handles drawing, we don't need to handle any rendering
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
        m_RulesPanel.m_Panel.setVisible(m_GameState == GameState.RULES);
        m_PlayerCardsPanel.setVisible(m_GameState != GameState.MAIN_MENU);
        m_GameInterfaceUpperPanel.SetUIStates();
        m_GameInterfaceLowerPanel.SetUIStates();
        m_YouLose.m_Panel.setVisible(m_GameState == GameState.BUST);
    }

    public void PlayerHasWon() {
        m_GameState = GameState.PLAYER_WINS;
        UpdateUIStates();
        m_PlayerWins.StartPop();
        m_GameStatePopImage = m_PlayerWins;
    }

    public void ComputerHasWon() {
        m_GameState = GameState.OPPONENT_WINS;
        UpdateUIStates();
        m_OpponentWins.StartPop();
        m_GameStatePopImage = m_OpponentWins;
    }

    public void PlayerAndComputerHaveSameScores() {
        m_GameState = GameState.DRAW;
        UpdateUIStates();
        m_ItsADraw.StartPop();
        m_GameStatePopImage = m_ItsADraw;
    }

    public static void main(String[] args) {
        m_Pontoon = new Pontoon();
        m_Pontoon.Init();
//        m_Pontoon.NewGame();
    }

    public void NewGame() {
        m_PlayerCards.ReturnCardsToDeck();
        m_OpponentCards.ReturnCardsToDeck();
        m_NumCardsBeingRevealed = 0;
        m_PlayerCards.DrawOpeningHand();
        m_OpponentCards.DrawOpeningHand();
        m_GameState = GameState.GAME_IN_PROGRESS;
        UpdateUIStates();
        m_GameStatePopImage = null;
        StartUpdateTimer();
    }

    public boolean MenuOrRules() {
        boolean ret = m_GameState == GameState.MAIN_MENU;
        ret |= m_GameState == GameState.RULES;
        return ret;
    }

    // return true if we're in any of the end-game states
    public boolean GameHasEnded() {
        boolean ret = m_GameState == GameState.BUST;
        ret |= m_GameState == GameState.PLAYER_WINS;
        ret |= m_GameState == GameState.OPPONENT_WINS;
        ret |= m_GameState == GameState.DRAW;
        return ret;
    }

    public void BackToMenu() {
        m_PlayerCards.ReturnCardsToDeck();
        m_OpponentCards.ReturnCardsToDeck();
        m_GameState = GameState.MAIN_MENU;
        UpdateUIStates();
        StopUpdateTimer();
    }

    public void Render(Graphics2D g) {
        m_PlayerCards.RenderCards(g);
        m_OpponentCards.RenderCards(g);

        if (m_GameStatePopImage != null) {
            m_GameStatePopImage.Render(g);
        }
    }

    public void ShowRules() {
        m_GameState = GameState.RULES;
        UpdateUIStates();
    }

    public void StickButtonPressed() {
        if (m_NumCardsBeingRevealed > 0) {
            return;
        }

//        m_PlayerCards.RevealAllCards();
//        if (m_PlayerCards.m_Hand.size() == 1) {
//            m_PlayerCards.m_Hand.get(0).RevealWithoutDelay();
//        }

        // we start the stacking animations with an incremental delay for each card.
        // When HandStack.CalculatePositions is called for the player cards, it returns the delay value for the
        // first of the opponent's cards, which is then passed back to the function
        double delay = HandStack.CalculatePositions(m_PlayerCards.m_Hand, Pontoon.m_Pontoon.m_Width / 4, Pontoon.m_Pontoon.m_PlayerCardsMidY, 0);
        HandStack.CalculatePositions(m_OpponentCards.m_Hand, (Pontoon.m_Pontoon.m_Width / 4) * 3, Pontoon.m_Pontoon.m_PlayerCardsMidY, delay);

        // this calculates the lowest possible score for the player's hand, assuming aces are low...
        int score = 0;
        for(Card card: m_PlayerCards.m_Hand) {
            score += card.m_Value;
        }
        // ...and this checks if any aces can be high without going bust, to give the player the
        // highest possible score
        for (Card card: m_PlayerCards.m_Hand) {
            if (card.m_Value == 1) {
                if (score+10 <= MAX_SCORE) {
                    score += 10;
                }
            }
        }

        if (score > OPPONENT_SCORE) {
            Pontoon.m_Pontoon.PlayerHasWon();
        } else if (score < OPPONENT_SCORE) {
            Pontoon.m_Pontoon.ComputerHasWon();
        } else {
            Pontoon.m_Pontoon.PlayerAndComputerHaveSameScores();
        }
    }
}
