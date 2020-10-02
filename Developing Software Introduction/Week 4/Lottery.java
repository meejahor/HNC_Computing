import java.util.Scanner;

public class Lottery {

    private static Scanner m_Keyboard;
    private static final int m_NumberOfNumbers = 6;

    private static final String[] m_Ordinals = {
        "first",
        "second",
        "third",
        "fourth",
        "fifth",
        "sixth"
    };

    private static int GetNumber(int index) {
        System.out.print("Please enter the " + m_Ordinals[index] + " number: ");
        return m_Keyboard.nextInt();
    }

    private static void LotteryNumbers() {
        int[] numbers = new int[m_NumberOfNumbers];

        for (int index = 0; index < m_NumberOfNumbers; index++) {
            numbers[index] = GetNumber(index);
        }

        System.out.print("You entered: ");

        for (int number : numbers) {
            System.out.print(number + " ");
        }

        System.out.println();
    }

    private static void CapitaliseName() {
        System.out.print("Please enter your name: ");

        // Scanner.nextLine() seems to be broken but this makes .next() read the full line
        m_Keyboard.useDelimiter("\n");

        String name = m_Keyboard.next();
        System.out.println("Name in upper case = " + name.toUpperCase());
    }

    public static void main(String[] args)
    {
        m_Keyboard = new Scanner(System.in);

        System.out.print("Press 1 to enter lottery numbers or 2 to enter name: ");
        switch (m_Keyboard.nextInt()) {
            case 1 -> LotteryNumbers();
            case 2 -> CapitaliseName();
        }
    }
}
