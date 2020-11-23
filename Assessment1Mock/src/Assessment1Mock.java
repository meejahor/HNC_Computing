import java.util.Scanner;

/**
 * Assessment1Mock class
 * Contains:
 * <p>
 * Function to generate username, with supporting functions
 * Function to check if shape is square, with supporting functions
 * Program entry point with menu and user choice handling
 * </p>
 *
 * @author      Andrew Smith <20019208@uhi.ac.uk>
 * @version     1.0
 * @since       1.0
 */

public class Assessment1Mock {

    /**
     * Scanner object for keyboard input
     */
    private static final Scanner keyboard = new Scanner(System.in);

    /**
     * Get the user's forename
     * @return the user's input to be used as their forename in password generation
     */
    private static String GetForename() {
        String forename;
        System.out.println("\nPlease enter your first name:");
        forename = keyboard.next();
        return forename;
    }

    /**
     * Get the user's surname
     * @return the user's input to be used as their surname in password generation
     */
    private static String GetSurname() {
        String surname;
        System.out.println("\nPlease enter your last name:");
        surname = keyboard.next();
        return surname;
    }

    /**
     * Generate and output a password based on the user's forename and surname
     * @param forename the forename entered by the user
     * @param surname the surname entered by the user
     */
    private static void DisplayUsername(String forename, String surname) {
        String username;
        char forenameFirstCharacterLowerCase;
        String surnameUpperCase;

        forenameFirstCharacterLowerCase = forename.toLowerCase().charAt(0);
        surnameUpperCase = surname.toUpperCase();

        username = surnameUpperCase;
        username += forenameFirstCharacterLowerCase;

        System.out.println("\nYour username is: " + username + "\n");
    }

    /**
     * Get the user's forename and surname and display a username based on that information
     */
    private static void GenerateUsername() {
        final String forename = GetForename();
        final String surname = GetSurname();
        DisplayUsername(forename, surname);
    }

    /**
     * Get a value from the user to be used as either the width or height of a shape
     * @return the value entered by the user
     */
    private static double GetSide() {
        double length;
        length = keyboard.nextDouble();
        return length;
    }

    /**
     * Tell the user if a shape is square based on its width and height
     * @param width the width entered by the user
     * @param height the height entered by the user
     */
    private static void DisplayIfSquare(double width, double height) {
        // Double.compare() is used to avoid direct floating point comparison producing a false negative
        if (Double.compare(width, height) == 0) {
            System.out.println("\nThe shape is square.\n");
        } else {
            System.out.println("\nThe shape is not square.\n");
        }
    }

    /**
     * Ask the user for the width and height of an object, and tell them if it's square or not
     */
    private static void CheckIfShapeIsSquare() {
        double width, height;
        System.out.println("\nPlease enter the width:");
        width = GetSide();
        System.out.println("\nPlease enter the height:");
        height = GetSide();
        DisplayIfSquare(width, height);
    }

    /**
     * Program entry point
     * <p>
     *     Display the main menu
     *     Accept user input and call the selected function
     *     Repeat until the user chooses to exit
     * </p>
     * @param args commands line arguments
     */
    public static void main(String[] args)
    {
        String userInput;

        do {
            // display the main menu
            System.out.println("Main menu:");
            System.out.println("1. Generate username");
            System.out.println("2. Determine if shape is square");
            System.out.println("3. Exit");

            // get the user's choice
            userInput = keyboard.next();

            // only check the first character of the user's choice
            switch(userInput.charAt(0)) {
                case '1':
                    GenerateUsername();
                    break;
                case '2':
                    CheckIfShapeIsSquare();
                    break;
                case '3':
                    System.out.println("Bye!");
                    return;
                default:
                    // if an invalid choice has been entered then tell the user before the do...while loop repeats
                    System.out.println("Invalid choice.\n");
            }
            // do...while is an infinite loop, which is exited when the user selects option 3
        } while (true);
    }
}
