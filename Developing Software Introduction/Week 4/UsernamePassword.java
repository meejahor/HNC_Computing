import java.util.Scanner;

public class UsernamePassword {

    public static void main(String[] args)
    {
        final Scanner keyboard = new Scanner(System.in);
        final String m_CorrectUsername = "PE123";
        final String m_CorrectPassword = "3456";

        System.out.print("Please enter username: ");
        String username = keyboard.next();
        System.out.print("Please enter password: ");
        String password = keyboard.next();

        if (!username.equals(m_CorrectUsername)) {
            System.out.println("WRONG DETAILS ENTERED");
            return;
        }

        if (!password.equals(m_CorrectPassword)) {
            System.out.println("WRONG DETAILS ENTERED");
            return;
        }

        System.out.println("PROCEED");
    }
}
