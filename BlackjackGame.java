import java.util.Scanner;
import java.io.File;

public class BlackjackGame {
    private Scanner scanner;
    private Deck deck;
    private HumanPlayer player;
    private Dealer dealer;
    private int balance;
    private String username;
    private String password;

    public BlackjackGame(Scanner scanner) {
        this.scanner = scanner;
    }

    public void startGame() {
        showRules();
        mainMenu();
    }

    private void showRules() {
        System.out.println("=== Welcome to Blackjack ===");
        System.out.println("Rules:");
        System.out.println("1. Try to get as close to 21 without going over.");
        System.out.println("2. Number cards are worth their number.");
        System.out.println("3. Face cards (Jack, Queen, King) are worth 10.");
        System.out.println("4. Ace can be 1 or 11.");
        System.out.println("5. Dealer hits until 17 or higher.");
        System.out.println("6. Start with 500 credits (or your saved balance). Place bets each round.");
    }

    private void mainMenu() {
        while(true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Show existing profiles");
            System.out.println("2. Load existing profile");
            System.out.println("3. Create new profile");
            System.out.println("4. Delete profile");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            if(choice.equals("1")) showProfiles();
            else if(choice.equals("2")) loadProfile();
            else if(choice.equals("3")) newProfile();
            else if(choice.equals("4")) deleteProfile();
            else System.out.println("Invalid choice.");

            if(player != null) break; // profile loaded or created
        }
        playRounds();
    }

    private void showProfiles() {
        String[] profiles = listProfiles();
        if(profiles == null) {
            System.out.println("No profiles found.");
            return;
        }
        System.out.println("Existing profiles:");
        for(String p : profiles) System.out.println("- " + p);
    }

    private String[] listProfiles() {
        File folder = new File(".");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if(files == null || files.length == 0) return null;

        String[] profiles = new String[files.length];
        for(int i=0;i<files.length;i++) profiles[i] = files[i].getName().replace(".txt", "");
        return profiles;
    }

    private void loadProfile() {
        while(true) {
            String[] profiles = listProfiles();
            if(profiles == null) {
                System.out.println("No profiles found. Create a new profile first.");
                return;
            }

            System.out.println("Existing profiles:");
            for(String p : profiles) System.out.println("- " + p);
            System.out.println("Type 'back' to return to the main menu.");

            System.out.print("Enter username to load: ");
            String selectedUser = scanner.nextLine().trim();

            if(selectedUser.equalsIgnoreCase("back")) {
                return;
            }

            boolean exists = false;
            for(String p : profiles) {
                if(p.equals(selectedUser)) {
                    exists = true;
                    break;
                }
            }

            if(!exists) {
                System.out.println("Profile not found. Try again.");
                continue;
            }

            SaveSystem.ProfileData data = SaveSystem.loadProfile(selectedUser);
            if(data == null) {
                System.out.println("Unable to load profile. Try again.");
                continue;
            }

            if(data.password == null || data.password.isEmpty()) {
                System.out.println("This profile does not have a password yet. Please set one now.");
                String newPassword = promptForNewPassword();
                if(newPassword == null) {
                    return;
                }
                username = selectedUser;
                password = newPassword;
                balance = data.balance;
                SaveSystem.saveProfile(username, password, balance);
                System.out.println("Password set successfully. Balance: " + balance);
            } else {
                boolean authenticated = authenticateUser(data);
                if(!authenticated) {
                    return;
                }
                username = data.username;
                password = data.password;
                balance = data.balance;
                System.out.println("Profile loaded. Balance: " + balance);
            }

            player = new HumanPlayer(username, scanner);
            dealer = new Dealer();
            return;
        }
    }

    private void newProfile() {
        while(true) {
            System.out.print("Enter new username (or type 'back' to return): ");
            String newUsername = scanner.nextLine().trim();

            if(newUsername.equalsIgnoreCase("back")) {
                return;
            }

            if(newUsername.isEmpty()) {
                System.out.println("Username cannot be empty.");
                continue;
            }

            if(SaveSystem.profileExists(newUsername)) {
                System.out.println("That username already exists. Choose another.");
                continue;
            }

            String newPassword = promptForNewPassword();
            if(newPassword == null) {
                return;
            }

            username = newUsername;
            password = newPassword;
            balance = 500;
            SaveSystem.saveProfile(username, password, balance);
            System.out.println("New profile created. Balance: " + balance);
            player = new HumanPlayer(username, scanner);
            dealer = new Dealer();
            return;
        }
    }

    private void deleteProfile() {
        String[] profiles = listProfiles();
        if(profiles == null) {
            System.out.println("No profiles found to delete.");
            return;
        }

        while(true) {
            System.out.println("Existing profiles:");
            for(String p : profiles) System.out.println("- " + p);
            System.out.println("Type 'back' to return to the main menu.");

            System.out.print("Enter username to delete: ");
            String name = scanner.nextLine().trim();

            if(name.equalsIgnoreCase("back")) {
                return;
            }

            boolean exists = false;
            for(String p : profiles) {
                if(p.equals(name)) {
                    exists = true;
                    break;
                }
            }

            if(!exists) {
                System.out.println("Profile not found. Try again.");
                continue;
            }

            System.out.print("Are you sure you want to delete " + name + "? (y/n): ");
            String confirm = scanner.nextLine();
            if(confirm.equalsIgnoreCase("y")) {
                if(SaveSystem.deleteProfile(name)) System.out.println("Profile deleted successfully.");
                else System.out.println("Error deleting profile.");
            } else {
                System.out.println("Deletion canceled.");
            }
            return;
        }
    }

    private void playRounds() {
        while(true) {
            if(balance <= 0) {
                SaveSystem.saveProfile(username, password, balance);
                if(!handleZeroBalance()) {
                    return;
                }
                continue;
            }

            System.out.println("\nYour current balance: " + balance);
            int bet = 0;
            while(true) {
                System.out.print("Enter your bet (or 0 to quit): ");
                try {
                    bet = Integer.parseInt(scanner.nextLine());
                    if(bet == 0) {
                        SaveSystem.saveProfile(username, password, balance);
                        System.out.println("Game saved. Goodbye!");
                        return;
                    }
                    if(bet > 0 && bet <= balance) break;
                    else System.out.println("Invalid bet.");
                } catch(Exception e) {
                    System.out.println("Enter a number.");
                }
            }

            deck = new Deck();
            player.resetHand();
            dealer.resetHand();

            player.addCard(deck.drawCard());
            player.addCard(deck.drawCard());
            dealer.addCard(deck.drawCard());
            dealer.addCard(deck.drawCard());

            System.out.println("\nDealer shows: " + dealer.hand.get(0));
            player.takeTurn(deck);

            if(player.calculateHandValue() > 21) {
                System.out.println("Busted! Dealer wins this round.");
                balance -= bet;
                continue;
            }

            dealer.takeTurn(deck);

            int playerTotal = player.calculateHandValue();
            int dealerTotal = dealer.calculateHandValue();

            System.out.println("\nRound Result:");
            System.out.println("Your total: " + playerTotal);
            System.out.println("Dealer total: " + dealerTotal);

            if(dealerTotal > 21 || playerTotal > dealerTotal) {
                System.out.println("You win this round!");
                balance += bet;
            } else if(playerTotal == dealerTotal) {
                System.out.println("Tie! No balance change.");
            } else {
                System.out.println("Dealer wins this round.");
                balance -= bet;
            }
        }
    }

    private boolean handleZeroBalance() {
        System.out.println("You ran out of credits.");
        while(true) {
            System.out.println("1. Delete profile and exit");
            System.out.println("2. Create a new profile");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            if(choice.equals("1")) {
                if(SaveSystem.deleteProfile(username)) {
                    System.out.println("Profile deleted successfully. Goodbye!");
                } else {
                    System.out.println("Unable to delete profile. Exiting game.");
                }
                player = null;
                dealer = null;
                username = null;
                password = null;
                return false;
            } else if(choice.equals("2")) {
                player = null;
                dealer = null;
                newProfile();
                if(player != null) {
                    return true;
                }
                System.out.println("Profile creation canceled. Please choose an option.");
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private String promptForNewPassword() {
        while(true) {
            System.out.print("Enter password (or type 'back' to cancel): ");
            String firstEntry = scanner.nextLine();
            if(firstEntry.equalsIgnoreCase("back")) {
                return null;
            }
            if(firstEntry.trim().isEmpty()) {
                System.out.println("Password cannot be empty.");
                continue;
            }
            System.out.print("Confirm password: ");
            String confirmEntry = scanner.nextLine();
            if(!firstEntry.equals(confirmEntry)) {
                System.out.println("Passwords do not match. Try again.");
                continue;
            }
            return firstEntry;
        }
    }

    private boolean authenticateUser(SaveSystem.ProfileData data) {
        final int attemptsAllowed = 3;
        for(int attempt = 1; attempt <= attemptsAllowed; attempt++) {
            System.out.print("Enter password (or type 'back' to cancel): ");
            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("back")) {
                System.out.println("Authentication canceled.");
                return false;
            }
            if(data.password.equals(input)) {
                return true;
            }
            int remaining = attemptsAllowed - attempt;
            if(remaining > 0) {
                System.out.println("Incorrect password. Attempts remaining: " + remaining);
            }
        }
        System.out.println("Too many incorrect attempts. Returning to main menu.");
        return false;
    }
}
