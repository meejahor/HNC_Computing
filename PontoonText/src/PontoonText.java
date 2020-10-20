import java.util.Random;
import java.util.Scanner;

public class PontoonText {

    private static final Random m_Random = new Random();

    private static int DrawCard() {
        // nextInt(10) gives a value in the range 0-9 so we need to add 1 to get it in the range 1-10
        return m_Random.nextInt(10) + 1;
    }

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        int m_PlayerScore = 0;
        int m_OpponentScore = 18;
        int newCardValue;

        do {
            newCardValue = DrawCard();
            System.out.println("You drew card: " + newCardValue);
            m_PlayerScore += newCardValue;
            System.out.println("Current score: " + m_PlayerScore);
            if (m_PlayerScore > 21) {
                System.out.println("You went bust!");
                return;
            }
            System.out.print("Draw another card? (y/n) ");
        } while (keyboard.next().charAt(0) == 'y');

        System.out.println("Opponent score: " + m_OpponentScore);

        if (m_PlayerScore > m_OpponentScore) {
            System.out.println("You won!");
        } else if (m_OpponentScore > m_PlayerScore) {
            System.out.println("Opponent won!");
        } else {
            System.out.println("It's a draw.");
        }
    }
}
