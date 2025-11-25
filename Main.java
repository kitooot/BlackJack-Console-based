import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BlackjackGame game = new BlackjackGame(scanner);
        game.startGame();
        scanner.close();
    }
}
