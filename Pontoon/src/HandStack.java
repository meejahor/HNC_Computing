import java.util.List;

public class HandStack {

    private static final double SPACING = 50;

    public static void CalculatePositions(List<Card> cards, double x, double y) {
        int numCards = cards.size();
        x -= (numCards-1) * (SPACING*0.5);
        for (int i = 0; i < numCards; i++) {
            Card card = cards.get(i);
            card.SetStackTargetPosition(x + (SPACING*i), y, 10*i);
        }
    }
}
