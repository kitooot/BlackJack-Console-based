public class Dealer extends Player {

    public Dealer() {
        super("Dealer");
    }

    @Override
    public void takeTurn(Deck deck) {
        while(calculateHandValue() < 17) {
            System.out.println("Dealer hits.");
            addCard(deck.drawCard());
        }
        System.out.println("Dealer stands.");
    }

    @Override
    public String decideAction(Deck deck) {
        return calculateHandValue() < 17 ? "h" : "s";
    }
}
