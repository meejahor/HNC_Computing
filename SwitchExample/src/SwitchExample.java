import java.util.Scanner;

public class SwitchExample {

    public static void FunctionOne() {
    }

    public static void FunctionTwo() {
    }

    public static void main(String[] args) {
        final Scanner keyboard = new Scanner(System.in);
        String userInput;
        char firstChar;

        // 'do' marks the start of a loop that will keep repeating
        // until the 'while' condition at the end is untrue
        do {
            // print the menu options
            System.out.println("1. Menu option 1");
            System.out.println("2. Menu option 2");

            // read a line from the keyboard
            userInput = keyboard.next();
            // get the first character of that line
            firstChar = userInput.charAt(0);

            // switch(something) means we're going to compare 'something'
            // to various other values, and if it matches any of them
            // then we'll do something
            switch (firstChar) {
                // case '1' means if firstChar is equal to '1' then...
                case '1':
                    // ...we do this:
                    FunctionOne();
                    break;
                // case '2' means if firstChar is equal to '2' then...
                case '2':
                    // ...we do this:
                    FunctionTwo();
                    break;
            }
            // while (something) means "keep repeating everything between
            // 'do' and 'while' until something is true. We're using
            // "while (true)" to make the loop keep going infinitely
        } while (true);
    }
}
