# Blackjack Console Game

## Quick Description
This is a simple console Blackjack game written in Java. Players can create password-protected profiles, place bets, and play rounds against a dealer. The project is also intended to show core OOP ideas in a small, working program.

## What the program does 
- You can create a profile (username + password) and the program remembers your balance between runs.
- When you play, you place a bet, receive cards, and decide to Hit or Stand.
- The dealer shows one card and keeps one face-down (the "hole" card). The dealer's hole card is revealed only when the round's outcome is being decided.
- The dealer announces every action (Hit or Stand) and shows any card it draws during play.
- You may choose to play as a Human (you decide) or as an AI player (the computer plays for you).
- If the deck runs out of cards it is automatically rebuilt and shuffled so the game continues.

## Important notes about profiles and passwords
- Profiles are stored as files named `username.txt` in the project folder.
- Passwords are stored in a hashed form (SHA-256). If you have an older profile with a plain-text password, the program will accept it once and then upgrade the stored password to a hashed value.

## Key features 
- Encapsulation: internal data like a player's hand is kept private and exposed through safe methods.
- Inheritance: a `Player` base class is extended by `HumanPlayer`, `AIPlayer`, and `Dealer`.
- Polymorphism: the game talks to players through the `Player` type, it doesn't need to know whether the player is human or AI.
- Abstraction: `Player` defines the required behavior (take a turn, decide an action) without specifying how it must be done.
- Defensive IO: file reads/writes and number parsing are guarded so bad input won't crash the program.

## How to run
OPTION 1:
Open PowerShell in the project folder and run: 
```powershell
javac *.java
java Main
```

If you add or change files, re-run `javac` before `java`.

OPTION 2:
Run or Debug Java

## Demo / Typical session 
- Program: "Welcome to Blackjack" and rules printed.
- Main menu: list, load, create, delete profiles.
- Create: enter a username and password, then choose `h` for Human or `a` for AI.
- During play: the dealer will show its first card and say the second is face-down. When you Hit the dealer may also take turns and will print what it does.
- At round end: hole card is revealed, winner decided, balance updated and saved. If balance becomes 0 you'll be offered options to delete the profile or create a new one (or return to the main menu).

## Sample Output
Here is a brief example of what a short session looks like in the terminal:

```
=== Welcome to Blackjack ===
Main Menu: 
1) Show profiles  
2) Load profile  
3) Create profile  
4) Delete profile
Choice: 3
Enter new username (or '0' to return): demo
Enter password (or '0' to cancel): 123
Confirm password: 123
Play as Human or AI? (h/a): h
Your balance: 500
Enter your bet (or 0 to quit): 50
Dealer shows: 9 of Hearts and a face-down card.
You: Hit -> drew 5 of Clubs (total 14)
Dealer: Hits and drew 6 of Diamonds
Dealer: Stands (total 17)
Dealer reveals hole card: Ace of Spades (total 17)
Round result: Dealer wins. New balance: 450
Profile saved.
```

## Files and short roles
`Main.java` — program entry point.
`BlackjackGame.java` — menus, profile handling, betting, and the core round loop (it coordinates player and dealer actions and reveals the dealer's hole card only at round end).
`Player.java` — abstract base class with shared player behavior.
`HumanPlayer.java` / `AIPlayer.java` / `Dealer.java` — specific player behaviors.
`Deck.java` / `Card.java` — card and deck logic (deck auto-refills when empty).
`SaveSystem.java` — saves/loads username, password hash, and balance.

## Program Structure 
This section expands the brief file list into the main classes, their responsibilities, and how they interact.

- `Main.java`
	- Role: program entry point. Creates required objects (Scanner / BlackjackGame) and starts the main loop.

- `BlackjackGame.java`
	- Role: orchestrates the application. Presents menus, handles profile create/load/delete, accepts bets, deals cards, coordinates player and dealer actions, applies game rules, and saves balances.
	- Notes: Uses a `Player` reference so a `HumanPlayer` or `AIPlayer` can be used interchangeably; controls dealer hole-card reveal at round end.

- `Player.java` (abstract)
	- Role: shared state and utility functions used by any participant (name, hand, add/clear cards, calculate hand value). Declares abstract behavior required by subclasses.
	- Key responsibilities: manage a player's hand safely, calculate totals (including Ace logic), and define `takeTurn(Deck)` / `decideAction(Deck)` which concrete players implement.

- `HumanPlayer.java`
	- Role: prompts the human user for actions (Hit/Stand) and implements interactive `takeTurn` behavior.

- `AIPlayer.java`
	- Role: automated player useful for demos/testing. Decides actions programmatically (for example, hits while below a fixed threshold).

- `Dealer.java`
	- Role: implements the house rules (dealer hits until reaching at least 17). Dealer announces actions and draws under the game's control.

- `Deck.java` / `Card.java`
	- Role: model a standard 52-card deck and card values. `Deck` manages cards inside an `ArrayList<Card>`, shuffles, and refills/shuffles automatically when empty.

- `SaveSystem.java`
	- Role: file-based persistence for player profiles. Reads/writes username, password hash, and balance. Handles legacy plain-text passwords by upgrading the stored value to a hash on first successful login.

Relationships: `BlackjackGame` composes `Player`, `Deck`, and `SaveSystem`. `Player` is the abstract superclass for `HumanPlayer`, `AIPlayer`, and `Dealer`.

## OOP Concepts Applied
Below are the four main OOP principles required by the assignment, with a plain-language explanation and short code examples showing where each is implemented.

- Encapsulation
	- Keep internal state hidden and expose only safe operations so other parts of the program cannot corrupt the object's data.
	- `Player.java` keeps the hand private and provides a controlled accessor.
	- Example (from `Player.java`):
		private ArrayList<Card> hand;

		public List<Card> getHand() {
				return Collections.unmodifiableList(hand);
		}

- Inheritance
	-Share common code by defining a base (super) class and create specialized versions (subclasses) that extend or customize the behavior.
	-`Player` is the abstract base class; concrete players (human, AI, dealer) extend it.
	- Example (class relationships):

		Player (abstract)
		├─ HumanPlayer
		├─ AIPlayer
		└─ Dealer

- Polymorphism
	-Use a superclass type to refer to objects of different subclasses and rely on overridden methods to provide the right runtime behavior.
	-`BlackjackGame` stores `private Player player;` and calls `player.decideAction(deck)` which dispatches to the concrete subclass method.
	- Example (from `BlackjackGame.java`):

		private Player player; // may be HumanPlayer or AIPlayer

		String action = player.decideAction(deck); // dynamic dispatch

- Abstraction
	-Define an abstract contract (abstract class or interface) that specifies required behavior without dictating how it's implemented.
	-`Player` declares abstract methods that each subclass implements to provide their turn logic.
	- Example (from `Player.java`):

		public abstract class Player {
				public abstract void takeTurn(Deck deck);
				public abstract String decideAction(Deck deck);
		}

## OOP Evidence
Use these short references to point a grader or reviewer to concrete code that proves each OOP principle was applied:

- **Encapsulation**: `Player.java` keeps the hand private and exposes a safe accessor:
	- `private ArrayList<Card> hand;` and `public List<Card> getHand()`
- **Inheritance**: `Player` is the abstract base and has at least three subclasses:
	- `Player` -> `HumanPlayer`, `AIPlayer`, `Dealer`
- **Polymorphism**: `BlackjackGame.java` uses a `Player` reference and calls overridden methods:
	- `private Player player;` then `player.decideAction(deck)` / `player.takeTurn(deck)`
- **Abstraction**: `Player` declares abstract methods that subclasses implement:
	- `public abstract void takeTurn(Deck deck);` and `public abstract String decideAction(Deck deck);`
- **Exception handling**: `SaveSystem.java` and input parsing in `BlackjackGame.java` use try/catch to avoid crashes on bad input.

## Acknowledgements
-First and foremost, we thank Ma'am Grace Alib for her guidance. 
-We also acknowledge our parents, siblings, classmates, and friends for their support. 
-Special thanks to Batangas State University - Alangilan Campus and the College of Informatics and Computing Sciences for providing the learning environment and resources.

## Author
- MB Hustlers
-(Psyvhin Nieva, Keith Mhartin Tambuli, Johan Cedrick Nabing)

## Next improvements (ideas)
- Use a stronger password storage method (salted KDF).
- Add unit tests and a dedicated setting to default to AI play.
