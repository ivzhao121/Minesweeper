import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 */
public class RunMinesweeper implements Runnable {
    public void run() {

        // Jframe that shows the instructions to the game
        final JFrame instructions = new JFrame("Instructions");
        JOptionPane.showMessageDialog(
                instructions,
                "Welcome to Minesweeper! To play Minesweeper, "
                        + "left click squares to reveal them "
                        + "and right click squares to mark them for mines. \n"
                        + "A square can either hold a value telling how many "
                        + "mines there are adjacent to the square "
                        + "or it can hold a mine. Reveal all the non- \n"
                        + "mine squares to win! If you click a mine you lose! "
                        + "If you want to restart hit reset and if you want to "
                        + "undo your move hit undo! \n Note undo does not remove "
                        + "flags."
        );

        // JFrame that asks for the desired width of the game board
        final JFrame width = new JFrame("width");
        int w = Integer.parseInt(
                JOptionPane.showInputDialog(
                        width, "Standard Minesweeper boards are: \n"
                                + "Easy- 10x10 with 10 mines \n"
                                + "Intermediate- 16x16 with 40 mines \n"
                                + "Hard - 16x30 with 99 mines \n \n"
                                + "Please enter your desired width."
                )
        );

        // JFrame that asks for the desired height of the game board
        final JFrame height = new JFrame("height");
        int h = Integer
                .parseInt(JOptionPane.showInputDialog(height, "Please enter your desired height."));

        final JFrame mines = new JFrame("mines");
        int m = Integer.parseInt(
                JOptionPane.showInputDialog(mines, "Please enter your desired number of mines.")
        );

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Minesweeper");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status, w, h, m);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);

        final JButton undo = new JButton("undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.undo();
            }
        });
        control_panel.add(undo);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}