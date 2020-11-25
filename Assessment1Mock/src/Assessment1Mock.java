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

import java.util.Scanner;

public class Assessment1Mock {

    /**
     * Scanner object for keyboard input
     */
    private static final Scanner keyboard = new Scanner(System.in);

    private static String GetValidName() {
        String name;
        boolean valid;
        do {
            name = keyboard.nextLine();

            // regex to check the string is alphabetic
            // the '+' makes sure the string isn't empty
            valid = name.matches("[a-zA-Z]+");

            if (!valid) {
                System.out.print("Invalid name. Please enter a valid name: ");
            }
        } while (!valid);

        return name;
    }

    /**
     * Get the user's forename
     * @return the user's input to be used as their forename in password generation
     */
    private static String GetForename() {
        String forename;
        System.out.print("\nPlease enter your first name: ");
        forename = GetValidName();
        return forename;
    }

    /**
     * Get the user's surname
     * @return the user's input to be used as their surname in password generation
     */
    private static String GetSurname() {
        String surname;
        System.out.print("Please enter your last name: ");
        surname = GetValidName();
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

        System.out.println("Your username is: " + username + "\n");
    }

    /**
     * Get the user's forename and surname and display a username based on that information
     */
    private static void GenerateUsername() {
        System.out.print("\nThis function will generate a username.");
        final String forename = GetForename();
        final String surname = GetSurname();
        DisplayUsername(forename, surname);
    }

    /**
     * Get a value from the user to be used as either the width or height of a shape
     * @return the value entered by the user
     */
    private static double GetSide() {
        String input;
        double length = 0;
        boolean valid;

        do {
            input = keyboard.nextLine();
            valid = true;

            try {
                // note this will allow 'd' suffix, but if someone's using that then fine
                // it doesn't affect the validity of the output
                length = Double.parseDouble(input);
            } catch (NumberFormatException nfe) {
                valid = false;
                System.out.print("Invalid value. Please enter a valid value: ");
            }
        } while (!valid);

        return Math.abs(length);
    }

    /**
     * Tell the user if a shape is square based on its width and height
     * @param width the width entered by the user
     * @param height the height entered by the user
     */
    private static void DisplayIfSquare(double width, double height) {
        // Double.compare() is used to avoid direct floating point comparison producing a false negative
        if (Double.compare(width, height) == 0) {
            System.out.println("The shape is square.\n");
        } else {
            System.out.println("The shape is not square.\n");
        }
    }

    /**
     * Ask the user for the width and height of an object, and tell them if it's square or not
     */
    private static void CheckIfShapeIsSquare() {
        double width, height;
        System.out.print("\nThis function will determine if a shape is square.");
        System.out.print("\nPlease enter the width: ");
        width = GetSide();
        System.out.print("Please enter the height: ");
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
            userInput = keyboard.nextLine();

            if (userInput.isEmpty()) {
                continue;
            }

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
                    keyboard.close();
                    return;
                default:
                    // if an invalid choice has been entered then tell the user before the do...while loop repeats
                    System.out.println("Invalid choice.\n");
            }
            // do...while is an infinite loop, which is exited when the user selects option 3
        } while (true);
    }
}
