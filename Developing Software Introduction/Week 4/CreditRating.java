import java.util.Scanner;

public class CreditRating {

    public static void main(String[] args)
    {
        final Scanner keyboard = new Scanner(System.in);
        System.out.print("Please enter customer credit limit: ");
        double creditLimit = keyboard.nextDouble();

        if (creditLimit > 50000) {
            System.out.println("Customer level = PLATINUM");
        } else if (creditLimit > 10000) {
            System.out.println("Customer level = GOLD");
        } else {
            System.out.println("Customer level = SILVER");
        }
    }
}
