/**
 * Simple automated player that hits until reaching a conservative threshold.
 */
public class AIPlayer extends Player {

    public AIPlayer(String name) {
        super(name);
    }

    @Override
    public void takeTurn(Deck deck) {
        while(calculateHandValue() < 16) {
            System.out.println(getName() + " (AI) decides to hit.");
            addCard(deck.drawCard());
        }
        System.out.println(getName() + " (AI) stands.");
    }

    @Override
    public String decideAction(Deck deck) {
        return calculateHandValue() < 16 ? "h" : "s";
    }
}
