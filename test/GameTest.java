import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void testCorrectBoardValues() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        assertEquals(2, m.getCell(0, 1));
        assertEquals(3, m.getCell(1, 1));
        assertEquals(2, m.getCell(2, 1));
        assertEquals(1, m.getCell(3, 1));
        assertEquals(1, m.getCell(3, 0));
    }

    @Test
    public void testBoardWithZeroMines() {
        Minesweeper m = new Minesweeper(9, 9, 0, true);
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                count += m.getCell(i, j);
            }
        }
        assertEquals(0, count);
    }

    @Test
    public void testBoardFullOfMines() {
        Minesweeper m = new Minesweeper(9, 9, 81, false);
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                count += m.getCell(i, j);
            }
        }
        assertEquals(-81, count);
    }

    @Test
    public void testConstructBoardWithMoreMinesThanSquares() {
        Minesweeper m = new Minesweeper(9, 9, 82, false);
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                count += m.getCell(i, j);
            }
        }
        assertEquals(-81, count);
    }

    @Test
    public void testPlayTurn() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        m.playTurn(0, 1);
        assertTrue(m.isCellRevealed(0, 1));
        assertFalse(m.isCellRevealed(0, 0));
        assertFalse(m.isCellRevealed(0, 2));
    }

    @Test
    public void testPlayTurnRevealsAZero() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        m.playTurn(0, 2);
        assertEquals(78, m.numRevealed());
    }

    @Test
    public void testPlayTurnRevealsAMine() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        m.playTurn(0, 0);
        assertTrue(m.isCellRevealed(0, 0));
        assertEquals("Lost", m.checkWinner());
    }

    @Test
    public void testPlayTurnAfterYouLost() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        m.playTurn(0, 0);
        assertTrue(m.isCellRevealed(0, 0));

        m.playTurn(0, 1);
        assertFalse(m.isCellRevealed(0, 1));

        assertEquals("Lost", m.checkWinner());
    }

    @Test
    public void testCheckStatusAfterWinning() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        m.playTurn(0, 1);

        m.playTurn(3, 0);

        m.playTurn(1, 1);

        m.playTurn(0, 2);

        m.playTurn(2, 1);

        m.playTurn(3, 1);

        assertEquals("Won", m.checkWinner());
    }

    @Test
    public void testPlayTurnAfterWinning() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        m.playTurn(0, 1);
        m.playTurn(3, 0);
        m.playTurn(1, 1);
        m.playTurn(0, 2);
        m.playTurn(2, 1);
        m.playTurn(3, 1);

        m.playTurn(0, 0);
        assertFalse(m.isCellRevealed(0, 0));
        assertEquals("Won", m.checkWinner());
    }

    @Test
    public void testRestAfterWin() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        m.playTurn(0, 1);
        m.playTurn(3, 0);
        m.playTurn(1, 1);
        m.playTurn(0, 2);
        m.playTurn(2, 1);
        m.playTurn(3, 1);
        assertEquals("Won", m.checkWinner());

        m.reset(9, 9, 3, true);

        assertEquals("Still Playing", m.checkWinner());
        assertFalse(m.isCellRevealed(0, 1));
        assertFalse(m.isCellRevealed(3, 0));
        m.playTurn(0, 1);
        assertTrue(m.isCellRevealed(0, 1));
        m.playTurn(3, 0);
        m.playTurn(1, 1);
        m.playTurn(0, 2);
        m.playTurn(2, 1);
        m.playTurn(3, 1);
        assertEquals("Won", m.checkWinner());
    }

    @Test
    public void testAddFlag() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        m.addFlag(0, 1);
        m.playTurn(0, 1);
        assertFalse(m.isCellRevealed(0, 1));
    }

    @Test
    public void testRemoveFlag() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        m.addFlag(0, 1);
        m.removeFlag(0, 1);
        m.playTurn(0, 1);
        assertTrue(m.isCellRevealed(0, 1));
    }

    @Test
    public void testPlayTurnDoesNotWorkOnFlagedZeroSquare() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        m.addFlag(0, 2);
        m.playTurn(0, 2);
        assertFalse(m.isCellRevealed(0, 2));
        assertEquals(0, m.numRevealed());
    }

    @Test
    public void testPlayTurnDoesNotWorkOnFlagedMine() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        m.addFlag(0, 0);
        m.playTurn(0, 0);
        assertFalse(m.isCellRevealed(0, 0));
        assertEquals("Still Playing", m.checkWinner());
    }

    @Test
    public void testUndo() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        m.playTurn(0, 1);
        m.undo();
        assertFalse(m.isCellRevealed(0, 1));
    }

    @Test
    public void testUndoAfterTwoMoves() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        m.playTurn(0, 1);
        m.playTurn(1, 2);
        m.undo();
        assertTrue(m.isCellRevealed(0, 1));
        assertFalse(m.isCellRevealed(1, 2));
    }

    @Test
    public void testUndoTwice() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        m.playTurn(0, 1);
        m.playTurn(1, 2);
        m.undo();
        m.undo();
        assertFalse(m.isCellRevealed(0, 1));
        assertFalse(m.isCellRevealed(1, 2));
    }

    @Test
    public void testUndoOnAMine() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        m.playTurn(0, 0);
        m.undo();
        assertEquals("Still Playing", m.checkWinner());
    }

    @Test
    public void testUndoOnAZeroSquare() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        m.playTurn(0, 2);
        m.undo();
        assertEquals(0, m.numRevealed());
    }

    @Test
    public void testUndoAfterWinning() {
        Minesweeper m = new Minesweeper(9, 9, 3, true);
        m.playTurn(0, 2);
        m.undo();
        assertEquals("Still Playing", m.checkWinner());
    }
}
