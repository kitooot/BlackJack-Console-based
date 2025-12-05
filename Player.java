import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Player {
    protected String name;
    private ArrayList<Card> hand;

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

    public List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }

    public String getName() {
        return name;
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
    
    public abstract String decideAction(Deck deck);
}
