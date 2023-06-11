import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game {

    public static void main(String[] args) {
        Runnable game = new org.cis120.minesweeper.RunMinesweeper();
        SwingUtilities.invokeLater(game);
    }
}
