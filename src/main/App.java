package main;

import javax.swing.*;

import main.gameLogic.Board;
import test.Test;
import java.awt.*;

/**
 * Builds UI and starts the game.
 */
public class App {

    public static final String TITLE = "";
    public static final int BORDER_SIZE = 25;

    private static final boolean IS_TEST = false;
    
    public static void main(String[] args) {
    	if(IS_TEST) Test.test();
        new App().init();
    }

    private void init() {
        JFrame f = new JFrame();
        f.setTitle(TITLE);

        JPanel container = new JPanel();
        container.setBackground(Color.GRAY);
        container.setLayout(new BorderLayout());
        f.add(container);
        container.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));

        Board board = new Board();
        container.add(board);

        f.pack();
        f.setResizable(false);
        f.setLocationByPlatform(true);
        f.setVisible(true);
    }
}
