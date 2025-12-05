# Blackjack Console Game

Blackjack Console Game is a console-based Java application that lets players enjoy Blackjack directly in the terminal while keeping profiles, balances, and passwords safe.

It demonstrates practical Object-oriented Programming (OOP) concepts such as encapsulation, inheritance, polymorphism, and abstraction, together with file-based persistence and modular design.

Users can:
🎮 Start a round as a human player or let the AI play automatically
💰 Place bets, win or lose chips, and keep balances between sessions
🔐 Create, load, or delete password-protected profiles without leaving the app
🃏 Watch the dealer narrate every hit or stand while keeping the hole card hidden until showdown
🔄 Return to the main menu safely whenever they input `0` on a prompt that allows cancellation

Profile & Save Storage
💾 Each profile is saved as `username.txt` in the project root
🧾 Line 1 stores the SHA-256 password hash; line 2 stores the current balance
♻️ Legacy plaintext passwords are upgraded to hashes automatically after the next login
🛡️ Invalid or missing data defaults to 500 chips to keep the game stable

‧₊˚ ┊ Project Structure
📂 BlackJack-Console-based-main/
├── ☕ Main.java
├── ☕ BlackjackGame.java
├── ☕ Player.java
├── ☕ HumanPlayer.java
├── ☕ AIPlayer.java
├── ☕ Dealer.java
├── ☕ Deck.java
├── ☕ Card.java
├── ☕ SaveSystem.java
├── 📄 README.md
├── 📄 Script.md
└── 📄 CodeFunctions.md

Main.java — Entry point that wires the scanner and launches the game loop.
BlackjackGame.java — Menu navigation, profile management, betting flow, and round orchestration.
Player.java — Abstract base class that manages shared state and declares the turn contract.
HumanPlayer.java / AIPlayer.java / Dealer.java — Concrete behaviors for each participant.
Deck.java / Card.java — Card modeling, deck shuffling, and automatic replenishment.
SaveSystem.java — File I/O for saving and loading hashed passwords and balances.

How to Run the Program
Open Windows PowerShell in the project folder and compile:

```powershell
javac *.java
```

Run the game with:

```powershell
java Main
```

Recompile with `javac *.java` whenever you change any source files.

‧₊˚ ┊ Features
🎯 Profile Menu. List, create, load, or delete players without restarting the program.
🗣️ Dealer Narration. Dealer announces each decision and only reveals the hole card at round end.
🤖 AI Mode. `AIPlayer` demonstrates automated decision-making using the same polymorphic contracts as humans.
🃠 Auto Deck Reset. Deck refills and shuffles when empty so long sessions stay seamless.
🪙 Persistent Balance. Every round saves updated chip counts through `SaveSystem`.
🛑 Safe Input Handling. Defensive parsing prevents crashes on invalid menu choices or bets.

‧₊˚ ┊ Object-oriented Principles
💊 Encapsulation
`Player.java` keeps the hand private and exposes it through an unmodifiable list so other classes cannot mutate the cards directly.

```java
private final ArrayList<Card> hand = new ArrayList<>();

public List<Card> getHand() {
    return Collections.unmodifiableList(hand);
}
```

💡 Abstraction
`Player` defines abstract methods `takeTurn(Deck)` and `decideAction(Deck)`, allowing subclasses to supply their own implementations without revealing internal details.

🧬 Inheritance
`HumanPlayer`, `AIPlayer`, and `Dealer` extend `Player`, reusing shared code while tailoring decision logic for each role.

🎭 Polymorphism
`BlackjackGame.java` holds a `Player` reference (`private Player player;`) and calls `player.takeTurn(deck)` or `player.decideAction(deck)`, dynamically dispatching to the correct subclass at runtime.

‧₊˚ ┊ Example Output
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

‧₊˚ ┊ demo.txt Snippet
```
a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3
450
```

‧₊˚ ┊ Contributors
| Name | Role |
| --- | --- |
| Psyvhin Nieva | Developer |
| Keith Mhartin Tambuli | Developer |
| Johan Cedrick Nabing | Developer |

‧₊˚ ┊ Acknowledgment
We sincerely thank Ma'am Grace Alib for her guidance, plus our families, classmates, and the Batangas State University - Alangilan Campus (College of Informatics and Computing Sciences) for providing the support and resources that made this project possible.
