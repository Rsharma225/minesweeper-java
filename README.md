![Minesweeper Banner](screenshots/minesweeper.jpg)

# Minesweeper (Java)

A classic text-based Minesweeper game written in pure Java.

Clear the board without hitting any mines. Place flags to mark suspected mines. Difficulty increases the more you win!

## Features

- 10 × 12 grid
- Flag placement and removal
- Adjacent mine count shown on revealed cells
- Increasing difficulty (more mines after each win)
- Play-again loop
- Clean console board rendering

## Controls

| Key | Action              |
|-----|---------------------|
| `g` | Make a guess        |
| `f` | Place / remove flag |
| `q` | Quit the current game |

## How to Run

```bash
# Compile
javac -d out src/SharmaRaghavA5.java

# Run
java -cp out SharmaRaghavA5
```

## Project Structure

```
minesweeper-java/
├── src/
│   └── SharmaRaghavA5.java   # Full game implementation
├── .gitignore
└── README.md
```

## Game Rules 

```
1. The board contains a number of hidden mines.
2. Enter g and then a row & column to reveal a cell.
3. Enter f and then a row & column to place or remove a flag.
4. Numbers show how many mines are adjacent to that cell.
5. Clear every non-mine cell to win.
6. Hitting a mine ends the game.
```

Author
Raghav Sharma
