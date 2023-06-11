import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

public class Minesweeper {

    /**
     * Represents the board of the Minesweeper grid.
     * Revealed tells if a cell has been revealed by
     * the player.
     */
    private int[][] board;
    private boolean[][] revealed;

    // Represents the state of the game; can be "Still Playing", "Lost", or "Won"
    private String gameState;

    // Parameters of the game board; mines is the number of mines
    private int width;
    private int height;
    private int mines;

    /**
     * Collections to implement the flag and undo function.
     * Flags hold each point of square that has been flag.
     * Undo holds the sequential moves that the user makes. Each time a move is made
     * the square is added to the end.
     * The numChange collection is used to track how many squares were revealed per
     * move. This is used to help implement the undo function.
     */
    private LinkedList<Point> flags;
    private LinkedList<Point> undo;
    private ArrayList<Integer> numChange;

    /**
     * Constructor sets up game state.
     */
    public Minesweeper(int c, int r, int m, boolean fixed) {
        int tempMines = m;
        if (m > c * r) {
            tempMines = c * r;
        }
        reset(c, r, tempMines, fixed);
    }

    /**
     * revealZero is a helper function of playTurn. This is called when a
     * cell containing the value 0 (meaning no mines are around) is revealed.
     * As a result, this method will reveal all nearby zeros around the original
     * zero. This method uses recursion because all zero cells touching another
     * should be revealed. The base case is that the square is not zero and in
     * that case that square is revealed too and the recursion will end. If a square
     * is flagged then the square will not be revealed.
     * 
     * @param c column to play in
     * @param r row to play in
     */
    private void revealZero(int c, int r) {
        if (!revealed[r][c] && board[r][c] == 0 && !flags.contains(new Point(c, r))) {
            revealed[r][c] = true;
            undo.addLast(new Point(c, r));
            if (c - 1 >= 0) {
                revealZero(c - 1, r);
            }
            if (c + 1 < board[0].length) {
                revealZero(c + 1, r);
            }
            if (r - 1 >= 0) {
                revealZero(c, r - 1);
                if (c - 1 >= 0) {
                    revealZero(c - 1, r - 1);
                }
                if (c + 1 < board[0].length) {
                    revealZero(c + 1, r - 1);
                }
            }
            if (r + 1 < board.length) {
                revealZero(c, r + 1);
                if (c - 1 >= 0) {
                    revealZero(c - 1, r + 1);
                }
                if (c + 1 < board[0].length) {
                    revealZero(c + 1, r + 1);
                }
            }
        }
        if (!revealed[r][c] && board[r][c] != 0 && !flags.contains(new Point(c, r))) {
            revealed[r][c] = true;
            undo.addLast(new Point(c, r));
        }
    }

    /**
     * playTurn allows the player to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is
     * taken or after the game has ended.
     *
     * @param c column to play in
     * @param r row to play in
     * @return whether the turn was successful
     */
    public boolean playTurn(int c, int r) {
        if (!flags.contains(new Point(c, r))) {
            if (board[r][c] == 0) {
                int initial = numRevealed();
                revealZero(c, r);
                numChange.add(numRevealed() - initial);
            } else if (gameState.equals("Still Playing")) {
                revealed[r][c] = true;
                undo.addLast(new Point(c, r));
                numChange.add(1);
                checkWinner();
                return true;
            }
        }
        return false;
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     * checkWinner only looks for horizontal wins.
     *
     * @return "Still Playing" if the game is not finished, "Won" if the player won,
     *         and
     *         "Lost" if the player lost.
     */
    public String checkWinner() {
        int total = (board.length * board[0].length) - this.mines;
        int moves = 0;
        for (int i = 0; i < board.length; i++) {
            if (!gameState.equals("Lost")) {
                for (int j = 0; j < board[0].length; j++) {
                    if (!gameState.equals("Lost")) {
                        if (board[i][j] != -1 && revealed[i][j]) {
                            moves++;
                        }
                        if (board[i][j] != -1 && !revealed[i][j]) {
                            gameState = "Still Playing";
                        }
                        if (board[i][j] == -1 && revealed[i][j]) {
                            gameState = "Lost";
                        }
                    }
                }
            }
        }
        if (total == moves) {
            gameState = "Won";
        }
        return gameState;
    }

    /**
     * printGameState prints the grid of the board
     * for debugging.
     */
    public void printGameState() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (revealed[i][j]) {
                    if (board[i][j] == -1) {
                        System.out.print("* ");
                    } else {
                        System.out.print(Integer.toString(board[i][j]) + " ");
                    }
                } else {
                    System.out.print("# ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * numRevealed returns the number of revealed squares in the game.
     * This is useful for add the number of changes per move and thus
     * useful to our undo function.
     * 
     * @return number of revealed squares in the game
     */
    public int numRevealed() {
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (revealed[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Undo's the last move by removing the last element from undo
     * and changing the correct cell in revealed to false. If a
     * zero square is called for undo each square the zero had
     * revealed is also changed to hidden with the use of
     * the numChange ArrayList. Undo does not undo a flag from
     * the user and only undos the move.
     */
    public void undo() {
        if (numRevealed() != 0) {
            int end = numChange.size() - 1;
            for (int i = 0; i < numChange.get(end); i++) {
                Point last = undo.getLast();
                undo.removeLast();
                revealed[last.y][last.x] = false;

                if (!checkWinner().equals("Still Playing")) {
                    gameState = "Still Playing";
                }
            }
            numChange.remove(end);
        }
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset(int c, int r, int m, boolean fixed) {
        board = new int[r][c];
        if (!fixed) {
            for (int i = 0; i < m; i++) {
                int x = (int) (Math.random() * c);
                int y = (int) (Math.random() * r);
                if (board[y][x] == -1) {
                    i -= 1;
                } else {
                    board[y][x] = -1;
                }
            }
        } else {
            int i = 0;
            int x = 0;
            int y = 0;
            while (i < m) {
                board[x][y] = -1;
                i++;
                y++;
                if (y >= board[0].length) {
                    y = 0;
                    x++;
                }
            }
        }

        for (int row = 0; row < r; row++) {
            for (int col = 0; col < c; col++) {
                int value = 0;

                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        int x = col + j;
                        int y = row + i;
                        if (x >= 0 && y >= 0 && x < c && y < r) {
                            if (board[y][x] == -1) {
                                value++;
                            }
                        }
                    }
                }
                if (board[row][col] != -1) {
                    board[row][col] = value;
                }
            }
        }

        revealed = new boolean[r][c];
        for (int row = 0; row < r; row++) {
            for (int col = 0; col < c; col++) {
                revealed[row][col] = false;
            }
        }

        gameState = "Still Playing";
        mines = m;
        width = c;
        height = r;
        flags = new LinkedList<Point>();
        undo = new LinkedList<Point>();
        numChange = new ArrayList<Integer>();
    }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board. 0 = empty, 1 = Player 1, 2 = Player 2
     */
    public int getCell(int c, int r) {
        return board[r][c];
    }

    /**
     * isCellRevealed is a getter that tells if the cell is supposed to be
     * revealed.
     * 
     * @param c column to retrieve
     * @param r row to retrieve
     * @return boolean that tells wheter the cell is revealed or not
     */
    public boolean isCellRevealed(int c, int r) {
        return revealed[r][c];
    }

    /**
     * getStatus is a getter function that tells the status of the game.
     * 
     * @return "Still PLaying" if the game has not ended, "Won" if the user won,
     *         or "Lost" if the user lost.
     */
    public String getStatus() {
        String state = gameState;
        return state;
    }

    /**
     * getter for the width of the board
     * 
     * @return int that represents the width of the board
     */
    public int width() {
        int w = width;
        return w;
    }

    /**
     * getter for the height of the board
     * 
     * @return int that represents the height of the board
     */
    public int height() {
        int h = height;
        return h;
    }

    /**
     * getter for the number of mines on the board
     * 
     * @return int that represents the number of mines on the board
     */
    public int mines() {
        int m = mines;
        return m;
    }

    /**
     * flags is a getter function that returns all the squares that are flagged
     * 
     * @return LinkedList of all the flags' coordinates in the form of points
     */
    public LinkedList<Point> flags() {
        LinkedList<Point> copy = new LinkedList<Point>();
        for (int i = 0; i < flags.size(); i++) {
            copy.add(flags.get(i));
        }
        return copy;
    }

    /**
     * addFlag flags a square making it immune to playTurn
     * 
     * @param c column of the square to be flagged
     * @param r row of the square to be flagged
     */
    public void addFlag(int c, int r) {
        if (!isCellRevealed(c, r)) {
            Point p = new Point(c, r);
            flags.add(p);
        }
    }

    /**
     * removeFlag removes a square from being flagged so that
     * it can now be revealed by playTurn
     * 
     * @param c column of the square to be flagged
     * @param r row of the square to be flagged
     */
    public void removeFlag(int c, int r) {
        Point p = new Point(c, r);
        flags.remove(p);
    }

    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     */
    public static void main(String[] args) {
        Minesweeper t = new Minesweeper(9, 9, 3, true);

        t.playTurn(0, 1);
        t.printGameState();

        t.addFlag(0, 0);
        t.playTurn(0, 0);
        t.printGameState();

        t.removeFlag(0, 0);
        t.playTurn(0, 0);
        t.printGameState();

        t.undo();
        t.printGameState();

        t.playTurn(3, 0);
        t.printGameState();

        t.playTurn(1, 1);
        t.printGameState();

        t.addFlag(0, 2);

        t.playTurn(0, 2);
        t.printGameState();

        t.removeFlag(0, 2);

        t.playTurn(0, 2);
        t.printGameState();

        System.out.println();
        System.out.println("Congratulations You " + t.checkWinner());
    }
}
