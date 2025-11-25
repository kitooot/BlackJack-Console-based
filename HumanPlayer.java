import java.util.Scanner;

public class HumanPlayer extends Player {
    private Scanner scanner;

    public HumanPlayer(String name, Scanner scanner) {
        super(name);
        this.scanner = scanner;
    }

    @Override
    public void takeTurn(Deck deck) {
        while(true) {
            System.out.print("Do you want to Hit or Stand? (h/s): ");
            String choice = scanner.nextLine();
            if(choice.equalsIgnoreCase("h")) {
                addCard(deck.drawCard());
                if(calculateHandValue() > 21) {
                    System.out.println("You might have busted!");
                    break;
                }
            } else if(choice.equalsIgnoreCase("s")) {
                System.out.println("You chose to stand.");
                break;
            } else {
                System.out.println("Invalid input, enter h or s.");
            }
        }
    }
}
