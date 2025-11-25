import java.util.ArrayList;

public abstract class Player {
    protected String name;
    protected ArrayList<Card> hand;

    public Player(String name) {
        this.name = name;
        hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
        System.out.println(name + " draws: " + card);
    }
    
    public void addCardSilent(Card card) {
        hand.add(card);
    
    }
    
    public void resetHand() {
        hand.clear();
    }

    public int calculateHandValue() {
        int total = 0;
        int aces = 0;
        for(Card c : hand) {
            total += c.getValue();
            if(c.getRank().equals("Ace")) aces++;
        }
        while(total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }
        return total;
    }

    public abstract void takeTurn(Deck deck);
}
