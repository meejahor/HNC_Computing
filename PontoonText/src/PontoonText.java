import java.util.Random;
import java.util.Scanner;

public class PontoonText {

    // we need to initialise a Random object to generate random numbers
    // private = only members of this class can access the object
    // static = even if we create multiple copies of the class, we'll only have one of these objects
    // final = once it's assigned (which we do here) it can't be changed
    private static final Random m_Random = new Random();

    // function to generate a random number between 1 and 10 inclusive, and return it
    private static int DrawCard() {
        // nextInt(10) gives a value in the range 0-9 so we need to add 1 to get it in the range 1-10
        return m_Random.nextInt(10) + 1;
    }

    // program entry point
    public static void main(String[] args) {

        // Scanner object for reading keyboards
        final Scanner keyboard = new Scanner(System.in);

        int playerScore = 0;
        int opponentScore = 18; // the assignment said to always give the opponent a score of 18
        int newCardValue;

        // the player gets two cards in their opening hand, so we need to draw one card before the game loop starts

        // get the first card value
        newCardValue = DrawCard();
        // tell the player what it was
        System.out.println("You drew card: " + newCardValue);
        // add it to the player's current score
        playerScore += newCardValue;

        // start of repeating loop, see 'while' statement below for end point of loop
        do {
            // get the new card value
            newCardValue = DrawCard();
            // tell the player what it was
            System.out.println("You drew card: " + newCardValue);
            // add it to the player's current score
            playerScore += newCardValue;
            // tell the player their current score
            System.out.println("Current score: " + playerScore);
            // check if they've gone bust
            if (playerScore > 21) {
                System.out.println("You went bust!");
                // end the game
                return;
            }
            System.out.print("Draw another card? (y/n) ");
        // get some input from the player and repeat the loop if the first character is 'y'
        } while (keyboard.next().charAt(0) == 'y');

        // at this point the player has chosen to 'stick' so tell them the opponent's score
        System.out.println("Opponent score: " + opponentScore);

        // compare player vs opponent scores to check for win, lose, or draw
        if (playerScore > opponentScore) {
            System.out.println("You won!");
        } else if (opponentScore > playerScore) {
            System.out.println("Opponent won!");
        } else {
            System.out.println("It's a draw.");
        }
    }
}
