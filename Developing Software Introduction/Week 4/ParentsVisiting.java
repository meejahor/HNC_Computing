import java.util.Scanner;

public class ParentsVisiting {

    private static Scanner m_Keyboard;

    private static char ReadOneCharacterLowerCase() {
        return m_Keyboard.next().toLowerCase().charAt(0);
    }

    private static void Money() {
        while (true) {
            System.out.print("What is your financial situation? (1 = rich, 2 = poor) ");
            switch (ReadOneCharacterLowerCase()) {
                case '1':
                    System.out.println("Go shopping.");
                    return;
                case '2':
                    System.out.println("Go to the cinema.");
                    return;
                default:
                    System.out.println("Invalid response.");
            }
        }
    }

    private static void Weather() {
        while (true) {
            System.out.print("What is the weather like? (1 = sunny, 2 = windy, 3 = rainy) ");
            switch (ReadOneCharacterLowerCase()) {
                case '1':
                    System.out.println("Play tennis.");
                    return;
                case '2':
                    Money();
                    return;
                case '3':
                    System.out.println("Stay in.");
                    return;
                default:
                    System.out.println("Invalid response.");
            }
        }
    }

    public static void main(String[] args)
    {
        m_Keyboard = new Scanner(System.in);

        while (true) {
            System.out.print("Are your parents visiting? (y/n) ");
            switch (ReadOneCharacterLowerCase()) {
                case 'y':
                    System.out.println("Go to the cinema.");
                    return;
                case 'n':
                    Weather();
                    return;
                default:
                    System.out.println("Invalid response.");
            }
        }
    }
}
