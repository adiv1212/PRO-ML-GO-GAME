package main.java.gameLogic.board.Clients;

import main.java.gameLogic.board.Board;
import main.java.gameLogic.stone.Point;
import java.util.Random;

public class CpuClient extends Client {
	public CpuClient() {
	}

	//private static Random rnd = new Random();

	public void action( boolean pass,int x, int y) {
		Board board = Board.getInstance();
        int row = x;
        int col = y;
        if (pass) {
        	board.grid.passTurn();
        	board.grid.passTurn();
            //if (board.grid.over()) {
                System.out.println(board.grid.winner() + " wins.");
            //}
        } 
        else {
        	/*boolean flag = true;
        	do {
        		row = rnd.nextInt(9);
        		col = rnd.nextInt(9);
                System.out.println(col + " and " + row);
        		flag=!board.grid.addStone(new Point(row, col), board.getCurrentPlayer().getColor());
        	}while(flag);*/
        	board.grid.addStone(new Point(row, col), board.getCurrentPlayer().getColor());
        	board.lastMove = new Point(row, col);
            board.repaint();
        }
        /*else if (!board.grid.addStone(new Point(row, col), board.getCurrentPlayer().getColor())) {
        		System.out.println("2");
        		row = rnd.nextInt(9);
        		col = rnd.nextInt(9);
        }  
        else {
            board.lastMove = new Point(col, row);
           // board.switchPlayer();
            board.repaint();
        }*/
    }
	
	@Override
	public void play() {
    	//System.out.println("cpu client play was called");

		//action(false);//example for input from the network.
	}
	
	@Override
	public void play(boolean pass, int x, int y) {
    	//System.out.println("cpu client play was called");

		action(pass,x,y);//example for input from the network.
	}
	
}
