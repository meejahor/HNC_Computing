import java.util.Random;
import java.util.Scanner;

public class PontoonConsole {

    private static final Random rand = new Random();

    private static int GetRandomCardValue() {
        return rand.nextInt(10) + 1;
    }

    public static void main(String[] args) {
        final Scanner keyboard = new Scanner(System.in);

        int playerTotal = 0;
        playerTotal += GetRandomCardValue();

        System.out.println("Welcome to Pontoon!");

        do {
            playerTotal += GetRandomCardValue();
            System.out.println("Your current total is: " + playerTotal);

            if (playerTotal > 21) {
                System.out.println("You went bust!");
                return;
            }

            System.out.println("Do you want to (d)raw another card or (s)tick?");

        } while (keyboard.next().toLowerCase().charAt(0) == 'd');

        System.out.println("Your opponent scored: 18");

        if (playerTotal > 18) {
            System.out.println("You won!");
        } else if (playerTotal < 18) {
            System.out.println("You lost!");
        } else {
            System.out.println("It's a draw!");
        }
    }
}
