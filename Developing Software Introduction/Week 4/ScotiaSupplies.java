import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class ScotiaSupplies {

    private static class SockPair {
        public String m_Description;
        public double m_Price;
        public int m_NumOrdered = 0;

        SockPair(String description, double price) {
            m_Description = description;
            m_Price = price;
        }
    }

    private static SockPair[] m_SockPairs = new SockPair[] {
            null,
            new SockPair("Red Socks", 5),
            new SockPair("Yellow Socks", 10),
            new SockPair("Green Socks", 15),
    };

    private static final DecimalFormat df = new DecimalFormat("#####.00");

    private static final int indexWidth = 7;
    private static final int descriptionWidth = 15;
    private static final int priceWidth = 10;
    private static final int numOrderedWidth = 10;
    private static final int fourColumnWidth =  indexWidth + descriptionWidth + priceWidth + numOrderedWidth;

    private static double orderTotal = 0;

    private static String LeftAlignWithWhiteSpace(String string, int targetLength) {
        while (string.length() < targetLength) {
            string += " ";
        }

        return string;
    }

    private static String RightAlignWithWhiteSpace(String string, int targetLength) {
        while (string.length() < targetLength) {
            string = " " + string;
        }

        return string;
    }

    public static void ShowStock() {

        System.out.println();

        System.out.print(
                LeftAlignWithWhiteSpace(
                        "Index",
                        indexWidth
                )
        );

        System.out.print(
                LeftAlignWithWhiteSpace(
                        "Description",
                        descriptionWidth
                )
        );

        System.out.print(
                LeftAlignWithWhiteSpace(
                        "Price",
                        priceWidth
                )
        );

        System.out.print(
                LeftAlignWithWhiteSpace(
                        "Ordered",
                        numOrderedWidth
                )
        );

        System.out.println("Line");

        SockPair sockPair;
        orderTotal = 0;

        for (int index = 1; index < m_SockPairs.length; index++) {
            sockPair = m_SockPairs[index];

            System.out.print(
                    LeftAlignWithWhiteSpace(
                            index + ".",
                            indexWidth
                    )
            );

            System.out.print(
                    LeftAlignWithWhiteSpace(
                            sockPair.m_Description,
                            descriptionWidth
                    )
            );

            System.out.print(
                    LeftAlignWithWhiteSpace(
                            "£" + df.format(sockPair.m_Price),
                            priceWidth
                    )
            );

            System.out.print(
                    LeftAlignWithWhiteSpace(
                            String.valueOf(sockPair.m_NumOrdered),
                            numOrderedWidth
                    )
            );

            if (sockPair.m_NumOrdered > 0) {
                double lineTotal = sockPair.m_Price * sockPair.m_NumOrdered;
                orderTotal += lineTotal;
                System.out.print("£" + df.format(lineTotal));
            }

            System.out.println();
        }

        if (orderTotal > 0) {
            String spacer = LeftAlignWithWhiteSpace(
                    "",
                    fourColumnWidth
            );

            System.out.print(spacer);

            System.out.println("--------");

            System.out.print(
                    RightAlignWithWhiteSpace(
                            String.valueOf("Sub-total: "),
                            fourColumnWidth
                    )
            );

            System.out.println("£" + df.format(orderTotal));

            System.out.print(
                    RightAlignWithWhiteSpace(
                            String.valueOf("Discount: "),
                            fourColumnWidth
                    )
            );

            double discount = orderTotal > 100 ? orderTotal - 100 : 0;
            discount /= 10;
            discount = (int)Math.round(discount);
            discount *= 2.5;

            if (discount > 0) {
                System.out.print("£" + df.format(discount));
            }
            System.out.println();

            System.out.print(
                    RightAlignWithWhiteSpace(
                            String.valueOf("Total: "),
                            fourColumnWidth
                    )
            );

            System.out.println("£" + df.format(orderTotal - discount));
        }

        System.out.println();
    }

    private static void TakeOrder() {
        final Scanner keyboard = new Scanner(System.in);

        while (true) {
            ShowStock();
            System.out.print("Please enter index of item to order, or '0' to exit: ");
            int input = keyboard.nextInt();
            switch(input) {
                case 1, 2, 3:
                    m_SockPairs[input].m_NumOrdered++;
                    break;
                case 0:
                    return;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        TakeOrder();

        if (orderTotal > 0) {
            System.out.println("Thank you for your order. Enjoy your socks!");
        } else {
            System.out.println("Goodbye.");
        }
    }
}
