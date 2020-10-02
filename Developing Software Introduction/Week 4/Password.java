import java.util.Scanner;

public class Password {

    private static Scanner m_Keyboard;
    private static final String m_CorrectPassword = "1234";

    private static boolean CheckForCorrectPassword(String message) {
        System.out.print(message);
        return m_Keyboard.next().equals(m_CorrectPassword);
    }

    public static void main(String[] args)
    {
        m_Keyboard = new Scanner(System.in);

        boolean result = CheckForCorrectPassword("Please enter a numeric password: ");

        if (!result) {
            result = CheckForCorrectPassword("Please try again: ");
        }

        if (!result) {
            System.out.println("INCORRECT PASSWORD");
        } else {
            System.out.println("ENTER");
        }
    }
}
