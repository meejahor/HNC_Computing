import java.util.Scanner;

public class DiedSurvived {

    private static Scanner m_Keyboard;

    private static char ReadOneCharacterLowerCase() {
        return m_Keyboard.next().toLowerCase().charAt(0);
    }

    private static void SIBSP() {
        while (true) {
            System.out.print("Is SIBSP over 2.5? (y/n) ");
            switch (ReadOneCharacterLowerCase()) {
                case 'y':
                    System.out.println("Died.");
                    return;
                case 'n':
                    System.out.println("Survived.");
                    return;
                default:
                    System.out.println("Invalid response.");
            }
        }
    }

    private static void Age() {
        while (true) {
            System.out.print("Is age over 9.5 yrs? (y/n) ");
            switch (ReadOneCharacterLowerCase()) {
                case 'y':
                    System.out.println("Died.");
                    return;
                case 'n':
                    SIBSP();
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
            System.out.print("Male or female? (m/f) ");
            switch (ReadOneCharacterLowerCase()) {
                case 'm':
                    Age();
                    return;
                case 'f':
                    System.out.println("Survived.");
                    return;
                default:
                    System.out.println("Invalid response.");
            }
        }
    }
}
