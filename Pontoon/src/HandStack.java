import java.util.List;

public class HandStack {

    private static final double SPACING = 20;
    private static final double ROTATION_PER_CARD = 10;
    private static final double DELAY = 0.05;

    public static double CalculatePositions(List<Card> cards, double x, double y, double delay) {
        int numCards = cards.size();
        x -= (numCards-1) * (SPACING*0.5);
        double startAngle = (numCards-1) * (ROTATION_PER_CARD*-0.5);
        for (int i = 0; i < numCards; i++) {
            Card card = cards.get(i);
            card.SetStackTargetPosition(x + (SPACING*i), y, startAngle + ROTATION_PER_CARD*i, delay);
            delay += DELAY;
        }

        return delay;
    }
}
