import java.util.Scanner;

public class VowelOrConsonant {

    private static boolean IsLetter(char letterLowerCase) {
        return ((letterLowerCase >= 'a') && (letterLowerCase <= 'z'));
    }

    public static void main(String[] args)
    {
        final Scanner keyboard = new Scanner(System.in);

        System.out.print("Please enter a letter: ");
        char letter = keyboard.next().charAt(0);

        final String letterWithQuotes = "\"" + letter + "\"";
        char letterLowerCase = Character.toLowerCase(letter);

        if ( !IsLetter(letterLowerCase) ) {
            System.out.println(letterWithQuotes + " is not a letter.");
            return;
        }

        switch (letterLowerCase) {
            case 'a', 'e', 'i', 'o', 'u' -> System.out.println(letterWithQuotes + " is a vowel.");
            default -> System.out.println(letterWithQuotes + " is a consonant.");
        }
    }
}
