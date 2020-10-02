import java.util.Scanner;

public class DaysInMonth {

    public static void main(String[] args)
    {
        final Scanner keyboard = new Scanner(System.in);

        System.out.print("Please enter the number of days: ");
        int days = keyboard.nextInt();

        switch (days) {
            case 28 -> System.out.println("February has 28 days in a normal year.");
            case 29 -> System.out.println("February has 29 days in a leap year.");
            case 30 -> System.out.println("April, June, September, and November all have 30 days.");
            case 31 -> System.out.println("January, March, May, July, August, October, and December all have 31 days.");
            default -> System.out.println("No months have " + days + " days.");
        }
    }
}
