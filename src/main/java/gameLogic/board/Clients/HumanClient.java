package main.java.gameLogic.board.Clients;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import main.java.gameLogic.board.Board;
import main.java.gameLogic.stone.Point;

public class HumanClient extends Client {
    public HumanClient() {
    }

    private boolean mouselistiner;
    public boolean getMouselistiner() {
		return mouselistiner;
	}
	public void setMouselistiner(boolean mouselistiner) {
		this.mouselistiner = mouselistiner;
	}
	@Override
    public void play() {
    	System.out.println("human client play was called");
        Board board = Board.getInstance();
        mouselistiner=true;
        board.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
            	if(!mouselistiner)return;
                int row = Math.round((float) (e.getY() - Board.BORDER_SIZE)
                        / Board.TILE_SIZE);
                int col = Math.round((float) (e.getX() - Board.BORDER_SIZE)
                        / Board.TILE_SIZE);
                if (row >= Board.SIZE || col >= Board.SIZE || row < 0 || col < 0) {
                    return;
                } else if (!board.grid.addStone(new Point(row, col), board.getCurrentPlayer().getColor())) {
                    return;
                } else {
                    board.lastMove = new Point(col, row);
                    board.switchPlayer();
                    mouselistiner=!mouselistiner;
                    board.repaint();
                }
            }
        });
        board.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    board.grid.passTurn();
                    board.switchPlayer();
                    if (board.grid.over()) {
                        System.out.println(board.grid.winner() + " wins.");
                    }
                }
            }
        });
    }
}
