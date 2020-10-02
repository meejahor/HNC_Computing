import java.util.Scanner;

public class WorldCupCountries {

    private static void OutputCountry(int year, String country) {
        System.out.println("In " + year + " the World Cup was played in " + country + ".");
    }

    public static void main(String[] args)
    {
        final Scanner keyboard = new Scanner(System.in);

        System.out.print("Please enter a year: ");
        int year = keyboard.nextInt();

        switch (year) {
            case 1934 -> OutputCountry(year, "Italy");
            case 1938 -> OutputCountry(year, "France");
            case 1954 -> OutputCountry(year, "Switzerland");
            case 1958 -> OutputCountry(year, "Sweden");
            case 1966 -> OutputCountry(year, "England");
            case 1974 -> OutputCountry(year, "Germany");
            case 1982 -> OutputCountry(year, "Spain");
            case 1990 -> OutputCountry(year, "Italy");
            case 1998 -> OutputCountry(year, "France");
            case 2006 -> OutputCountry(year, "Germany");
            case 2018 -> OutputCountry(year, "Russia");
            default -> System.out.println("The World Cup was not played in " + year + ".");
        }
    }
}
