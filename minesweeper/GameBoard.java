import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class instantiates a Minesweeper object, which is the model for the
 * game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Minesweeper m; // model for the game
    private JLabel status; // current status text

    // Game constants
    public static final int BOARD_WIDTH = 700;
    public static final int BOARD_HEIGHT = 700;

    /**
     * Because I give the option for the player to create their own sized board,
     * I need to know how many boxes to draw. These variables represent how to
     * divide the board up based on the width and height of the desired board
     * size.
     */

    private int wSection;
    private int hSection;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit, int w, int h, int mines) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        m = new Minesweeper(w, h, mines, false); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        // defines how large each square should based on the number of cells on the
        // board
        wSection = BOARD_WIDTH / m.width();
        hSection = BOARD_HEIGHT / m.height();
        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (m.getStatus().equals("Still Playing")) {

                    Point p = e.getPoint();
                    int x = p.x / (BOARD_WIDTH / m.width());
                    int y = p.y / (BOARD_HEIGHT / m.height());

                    if (SwingUtilities.isLeftMouseButton(e)) {

                        // left mouseclick reveals a square so the model is updated accordingly
                        m.playTurn(x, y);

                        updateStatus(); // updates the status JLabel
                        repaint(); // repaints the game board
                    } else if (SwingUtilities.isRightMouseButton(e)) {

                        // right mouse click flags a square so the model is updated accordingly
                        if (m.flags().contains(new Point(x, y))) {
                            m.removeFlag(x, y);
                        } else {
                            m.addFlag(x, y);
                        }

                        repaint();
                    }
                }
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        m.reset(m.width(), m.height(), m.mines(), false);
        status.setText(m.getStatus());
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void undo() {
        m.undo();
        status.setText(m.getStatus());
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        status.setText(m.getStatus());

        String won = m.checkWinner();
        if (won.equals("Lost")) {
            status.setText("Congratulations You Lost");
        } else if (won.equals("Won")) {
            status.setText("Congratulations You Won");
        }
    }

    /**
     * Draws the game board.
     * 
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 1; i < m.height(); i++) {
            for (int j = 1; j < m.width(); j++) {
                g.drawLine(wSection * j, 0, wSection * j, BOARD_HEIGHT);
                g.drawLine(0, hSection * i, BOARD_WIDTH, hSection * i);
            }
        }

        int shiftX = wSection / 2;
        int shiftY = hSection / 2;

        for (int i = 0; i < m.height(); i++) {
            for (int j = 0; j < m.width(); j++) {
                boolean state = m.isCellRevealed(j, i);
                if (state && m.getCell(j, i) != -1) {
                    g.drawString(
                            Integer.toString(m.getCell(j, i)), shiftX + wSection * j,
                            hSection * (i + 1) - shiftY
                    );
                } else if (state) {
                    g.drawString("(*)", shiftX + wSection * j, hSection * (i + 1) - shiftY);
                }
            }
        }

        for (Point p : m.flags()) {
            g.setColor(Color.red);
            g.fillRect(wSection * p.x, hSection * p.y, wSection, hSection);
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
