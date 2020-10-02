import java.util.Scanner;

public class Retirement {

    public static void main(String[] args)
    {
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Please enter your name: ");
        String name = keyboard.next();
        System.out.print("Please enter your age: ");
        int age = keyboard.nextInt();

        if (age > 65) {
            System.out.println("MAY RETIRE");
        } else {
            System.out.println("YOU MAY NOT RETIRE");
        }
    }
}
