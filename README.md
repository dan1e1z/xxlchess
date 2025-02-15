
# XXLChess - A Java Chess Game

XXLChess is a Java-based chess game that extends traditional chess with additional pieces and features. It is built using Gradle and includes a graphical interface for an interactive gaming experience.

---

## Table of Contents
1. [Features](#features)
2. [Project Structure](#project-structure)
3. [Setup Instructions](#setup-instructions)
4. [Running the Game](#running-the-game)
5. [Testing](#testing)
6. [Contributing](#contributing)
7. [License](#license)

---

## Features
- **Extended Chess Pieces**: Includes unique pieces like Amazon, Archbishop, Camel, Chancellor, and General.
- **Interactive GUI**: A graphical user interface for an intuitive gaming experience.
- **Customizable Levels**: Predefined levels (e.g., `level1.txt`, `level2.txt`) for testing and gameplay.
- **Gradle Build System**: Easy dependency management and build automation.
- **Unit Tests**: Includes test cases for checkmate, castling, and other chess rules.

---

## Project Structure
The project is organized as follows:

``` 
xxlchess_scaffold/
├── .git/                     # Git version control metadata
├── .gradle/                  # Gradle cache and configuration
├── .settings/                # IDE-specific settings
├── build/                    # Build output directory
│   ├── classes/              # Compiled classes
│   ├── libs/                 # Generated JAR files
│   └── ...                  # Other build artifacts
├── src/                      # Source code
│   ├── main/                 # Main application code
│   │   ├── java/             # Java source files
│   │   │   └── XXLChess/     # Chess game logic and GUI
│   │   └── resources/        # Resource files (e.g., images, configs)
│   └── test/                 # Unit tests
│       └── java/             # Test cases
├── test/                     # Additional test files
│   ├── CheckmateTest.java    # Test for checkmate scenarios
│   ├── DisableCastle.java    # Test for disabled castling
│   └── ...                  # Other test files
├── .project                  # IDE project file
├── build.gradle              # Gradle build configuration
├── config.json               # Configuration file for the game
├── level*.txt                # Predefined game levels
└── README.md                 # This file
```

---

## Setup Instructions

### Prerequisites
- **Java Development Kit (JDK)**: Ensure you have JDK 11 or later installed.
- **Gradle**: Install Gradle to build and run the project.

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/xxlchess.git
   cd xxlchess/xxlchess_scaffold
   ```

2. Install dependencies:
   ```bash
   gradle build
   ```

3. Ensure all required libraries are installed. For Linux systems, you may need:
   ```bash
   sudo apt install libx11-dev libxext-dev libxrender-dev libxtst-dev libxt-dev
   ```

---

## Running the Game
To run the game, use the following command:
```bash
gradle run
```

The game will launch with a graphical interface. Follow the on-screen instructions to play.

---

## Testing

Test cases include:
- Checkmate scenarios (`CheckmateTest.java`)
- Castling rules (`LeftCastlingTest.java`, `RightCastlingTest.java`)
- Disabled castling (`DisableCastle.java`)

---

## Contributing
Contributions are welcome! If you'd like to contribute:
1. Fork the repository.
2. Create a new branch for your feature or bugfix.
3. Submit a pull request with a detailed description of your changes.

---

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

Enjoy playing XXLChess! 🎉
