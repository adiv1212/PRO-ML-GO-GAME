package main.java.gameLogic.board.Clients;

import main.java.gameLogic.board.Board;
import main.java.gameLogic.stone.Point;

public class CpuClient extends Client {
	public CpuClient() {
	}


	public void action( boolean pass,int x, int y) {
		Board board = Board.getInstance();
        int row = y;
        int col = x;
        
        if (pass) {
        	board.grid.passTurn();
            board.switchPlayer();
            if (board.grid.over()) {
                System.out.println(board.grid.winner() + " wins.");
            }
        }else if (row >= Board.SIZE || col >= Board.SIZE || row < 0 || col < 0) {
        	return;
        } else if (!board.grid.addStone(new Point(row, col), board.getCurrentPlayer().getColor())) {
            return;
        }  
         else {
            board.lastMove = new Point(col, row);
            board.switchPlayer();
            board.repaint();
        }
    }
	
	@Override
	public void play() {
    	System.out.println("cpu client play was called");

		action(false,1,2);//example for input from the network.
	}
	
}
