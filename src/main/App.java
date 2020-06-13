package main;

import main.gameLogic.board.Board;
import main.gameLogic.board.Clients.HumanClient;
import test.Test;

import javax.swing.*;
import java.awt.*;

/**
 * Builds UI and starts the game.
 */
public class App {

    public static final String TITLE = "";
    public static final int BORDER_SIZE = 25;

    private static final boolean IS_TEST = false;

    public static void main(String[] args) {
        if (IS_TEST) Test.test();
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

        Board board = Board.getInstance();
        HumanClient player1 = new HumanClient();
        HumanClient player2 = new HumanClient();
        container.add(board);

        f.pack();
        f.setResizable(false);
        f.setLocationByPlatform(true);
        f.setVisible(true);
        board.startGame(player1, player2);
    }
}
