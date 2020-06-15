package main.java;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import main.java.gameLogic.board.Board;
import main.java.gameLogic.board.Clients.Client;
import main.java.gameLogic.board.Clients.CpuClient;
import main.java.gameLogic.board.Clients.HumanClient;

/**
 * Builds UI and starts the game.
 */
public class App {

    public static final String TITLE = "";
    public static final int BORDER_SIZE = 25;

    private static final boolean IS_TEST = false;

    public static void main(String[] args) {
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
        Client player1 = new HumanClient();
        Client player2 = new CpuClient();
        container.add(board);

        f.pack();
        f.setResizable(false);
        f.setLocationByPlatform(true);
        f.setVisible(true);
        board.startGame(player1, player2);
    }
}
