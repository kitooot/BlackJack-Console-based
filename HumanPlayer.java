import java.util.Scanner;

public class HumanPlayer extends Player {
    private Scanner scanner;

    public HumanPlayer(String name, Scanner scanner) {
        super(name);
        this.scanner = scanner;
    }

    public String promptAction() {
        while(true) {
            System.out.print("Do you want to Hit or Stand? (h/s): ");
            String choice = scanner.nextLine().trim();
            if(choice.equalsIgnoreCase("h") || choice.equalsIgnoreCase("s")) {
                return choice.toLowerCase();
            }
            System.out.println("Invalid input, enter h or s.");
        }
    }

    @Override
    public void takeTurn(Deck deck) {
        while(true) {
            String choice = promptAction();
            if(choice.equals("h")) {
                addCard(deck.drawCard());
                if(calculateHandValue() > 21) {
                    System.out.println("You might have busted!");
                    break;
                }
            } else if(choice.equals("s")) {
                System.out.println("You chose to stand.");
                break;
            }
        }
    }

    @Override
    public String decideAction(Deck deck) {
        return promptAction();
    }
}
