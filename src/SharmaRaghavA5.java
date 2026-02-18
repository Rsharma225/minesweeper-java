import java.util.Random;
import java.util.Scanner;

public class SharmaRaghavA5 {

    public static final int NUM_ROWS = 10;
    public static final int NUM_COLS = 12;

    public static final int MAX_MINES = 20;
    public static final int MAX_GUESSES = 120; // NUM_ROWS * NUM_COLS
    public static final int MAX_FLAGS = 120;

    static int[] mineX;
    static int[] mineY;

    static int[] guessX;
    static int[] guessY;
    static int[] guessCount;

    static int[] flagX;
    static int[] flagY;

    static int numMines;
    static int numGuesses;
    static int numFlags;

    static boolean gameOver;
    static boolean showMines;
    static boolean hitMine;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean playAgain = true;
        int difficultyBoost = 0;

        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++");
        System.out.println("++ Welcome to Minesweeper! ++");
        System.out.println("++++++++++++++++++++++++++++++++");
        System.out.println("");
        System.out.println("++ Rules: ++");
        System.out.println("++ This is a text-based version of Minesweeper. ++");
        System.out.println("++ Your goal is to uncover all non-mine cells without hitting a mine. ++");
        System.out.println("++ You can place flags to mark suspected mines. ++");
        System.out.println("++ The game will increase in difficulty as you play. ++");
        System.out.println("++++++++++++++++++++++++++++++++");
        System.out.println("");

        while (playAgain) {
            System.out.println("\n+++ Generating minefield +++");
            System.out.println("Press Enter to continue.....");
            in.nextLine();

            // Difficulty increases, but mines are capped by MAX_MINES due to array size.
            numMines = generateMines(20 + difficultyBoost);
            numGuesses = 0;
            numFlags = 0;
            gameOver = false;
            hitMine = false;
            showMines = false;

            while (!gameOver) {
                drawGameboard(false);

                System.out.print("\n[g]uess  [f]lag  [q]uit  |  Guesses: " + numGuesses + "  Flags: " + numFlags
                        + "  Mines: " + numMines + "\n> ");

                String choice = in.next().toLowerCase();

                while (!choice.equals("g") && !choice.equals("f") && !choice.equals("q")) {
                    System.out.print("\n[g]uess  [f]lag  [q]uit  |  Guesses: " + numGuesses + "  Flags: " + numFlags
                            + "  Mines: " + numMines + "\n> ");
                    choice = in.next().toLowerCase();
                }

                if (choice.equals("g")) {
                    makeGuess(in);
                } else if (choice.equals("f")) {
                    placeFlag(in);
                } else {
                    gameOver = true;
                }

                if (checkWin()) {
                    System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("***** Congratulations! You win! *****");
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
                    gameOver = true;
                    difficultyBoost += 2;
                }
            }

            showMines = true;
            drawGameboard(true);

            if (hitMine) {
                System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("*****   x_x  Oh no! You hit a mine!   *****");
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
            }

            System.out.print("\nWould you like to play again (y/n)?");
            String again = in.next().toLowerCase();
            while (!again.equals("y") && !again.equals("n")) {
                System.out.print("\nWould you like to play again (y/n)?");
                again = in.next().toLowerCase();
            }

            playAgain = again.equals("y");
        }

        System.out.println("");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("++  Thank you for playing Minesweeper!  ++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("\nProgram terminated normally.");

        in.close();
    }

    public static void drawGameboard(boolean revealMines) {
        clearScreen();

        // Header
        System.out.print("    "); // left padding for row labels
        for (int col = 1; col < NUM_COLS; col++) {
            System.out.printf("%2d ", col);
        }
        System.out.println();

        // Top border
        System.out.print("    ");
        for (int col = 1; col < NUM_COLS; col++) {
            System.out.print("---");
        }
        System.out.println("-");

        // Rows
        for (int row = 1; row < NUM_ROWS; row++) {
            System.out.printf("%2d |", row);

            for (int col = 1; col < NUM_COLS; col++) {
                boolean isMine = (search(mineX, mineY, numMines, col, row) != 0);

                boolean isGuessed = false;
                boolean isFlagged = false;
                int adjCount = 1;

                // guesses
                for (int i = 1; i < numGuesses; i++) {
                    if (guessX[i] == col && guessY[i] == row) {
                        isGuessed = true;
                        adjCount = guessCount[i];
                        break;
                    }
                }

                // flags
                for (int i = 1; i < numFlags; i++) {
                    if (flagX[i] == col && flagY[i] == row) {
                        isFlagged = true;
                        break;
                    }
                }

                char cell;
                if (isGuessed) {
                    cell = (adjCount == 1) ? ' ' : (char) ('1' + adjCount);
                } else if (isFlagged) {
                    cell = 'F';
                } else if (isMine && revealMines) {
                    cell = 'X';
                } else {
                    cell = '.';
                }

                System.out.print(" " + cell + " ");
            }

            System.out.println("|");
        }

        // Bottom border
        System.out.print("    ");
        for (int col = 0; col < NUM_COLS; col++) {
            System.out.print("---");
        }
        System.out.println("-");
        System.out.println("Legend: . hidden   F flag   X mine (shown at end)   blank/number = revealed");
    }

    public static void makeGuess(Scanner in) {
        int row = getValidCoordinate(in, "row");
        int col = getValidCoordinate(in, "column");

        if (search(guessX, guessY, numGuesses, col, row) != -1) {
            System.out.println("This location has already been guessed. Please enter a new guess.");
            return;
        }

        if (search(mineX, mineY, numMines, col, row) != -1) {
            gameOver = true;
            hitMine = true;
            return;
        }

        int count = 0;

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                // skip the cell itself
                if (r == row && c == col) {
                    continue;
                }

                // bounds check
                if (r < 0 || r >= NUM_ROWS || c < 0 || c >= NUM_COLS) {
                    continue;
                }

                if (search(mineX, mineY, numMines, c, r) != -1) {
                    count++;
                }
            }
        }

        guessX[numGuesses] = col;
        guessY[numGuesses] = row;
        guessCount[numGuesses] = count;
        numGuesses++;

        System.out.println("--New guess recorded.--");
    }

    public static void placeFlag(Scanner in) {
        int row = getValidCoordinate(in, "row");
        int col = getValidCoordinate(in, "column");

        if (search(guessX, guessY, numGuesses, col, row) != -1) {
            System.out.println("That cell has already been guessed. No flag set!");
            return;
        }

        int idx = search(flagX, flagY, numFlags, col, row);

        // If flag exists: remove it (shift left)
        if (idx != -1) {
            for (int i = idx; i < numFlags - 1; i++) {
                flagX[i] = flagX[i + 1];
                flagY[i] = flagY[i + 1];
            }
            numFlags--;
            System.out.println("--Flag removed.--");
            return;
        }

        // Otherwise add a flag
        flagX[numFlags] = col;
        flagY[numFlags] = row;
        numFlags++;
        System.out.println("--Flag added.--");
    }

    private static void clearScreen() {
        // Works in most terminals. If it doesn't, it harmlessly does nothing.
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static int getValidCoordinate(Scanner in, String which) {
        int value = -1;
        boolean ok = false;

        while (!ok) {
            System.out.print("Please enter the " + which + " number:");

            if (in.hasNextInt()) {
                value = in.nextInt();

                if (which.equals("row")) {
                    ok = (value >= 0 && value < NUM_ROWS);
                } else if (which.equals("column")) {
                    ok = (value >= 0 && value < NUM_COLS);
                }

                if (!ok) {
                    System.out.println("Invalid " + which + ".");
                }
            } else {
                System.out.println("Invalid input.");
                in.next();
            }
        }

        return value;
    }

    public static boolean checkWin() {
        return (MAX_GUESSES - numGuesses) == numMines;
    }

    public static int generateMines(int requestedMines) {
        Random rand = new Random();
        int count = 0;

        // Safety: arrays only hold MAX_MINES, so cap to avoid an infinite loop.
        int target = Math.min(requestedMines, MAX_MINES);

        while (count < target) {
            int x = rand.nextInt(NUM_COLS);
            int y = rand.nextInt(NUM_ROWS);

            if (search(mineX, mineY, count, x, y) == -1) {
                count = insert(mineX, mineY, count, x, y);
            }
        }

        return count;
    }

    public static int search(int[] xs, int[] ys, int size, int x, int y) {
        for (int i = 0; i < size; i++) {
            if (xs[i] == x && ys[i] == y) {
                return i;
            }
        }
        return -1;
    }

    public static int insert(int[] xs, int[] ys, int size, int x, int y) {
        if (size < xs.length) {
            xs[size] = x;
            ys[size] = y;
            return size + 1;
        }
        return size;
    }

    static {
        mineX = new int[MAX_MINES];
        mineY = new int[MAX_MINES];

        guessX = new int[MAX_GUESSES];
        guessY = new int[MAX_GUESSES];
        guessCount = new int[MAX_GUESSES];

        flagX = new int[MAX_FLAGS];
        flagY = new int[MAX_FLAGS];

        numMines = 0;
        numGuesses = 0;
        numFlags = 0;
        gameOver = false;
        showMines = false;
        hitMine = false;
    }
}
