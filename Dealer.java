public class Dealer extends Player {

    public Dealer() {
        super("Dealer");
    }

    @Override
    public void takeTurn(Deck deck) {
        while(calculateHandValue() < 17) {
            addCard(deck.drawCard());
        }
        System.out.println("Dealer stands.");
    }
}
