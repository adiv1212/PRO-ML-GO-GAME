package main.java.gameLogic.board.Clients;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import main.java.gameLogic.board.Board;
import main.java.gameLogic.stone.Point;

public class HumanClient extends Client {
    public HumanClient() {
    }

    private boolean actionPerformed;
    
    
	public boolean isActionPerformed() {
		return actionPerformed;
	}


	public void setActionPerformed(boolean actionPerformed) {
		this.actionPerformed = actionPerformed;
	}


	@Override
    public void play() {
    	//System.out.println("human client play was called");
        Board board = Board.getInstance();
        actionPerformed=true;
        //System.out.println(actionPerformed);
        board.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
            	if(!actionPerformed)return;
                int row = Math.round((float) (e.getY() - Board.BORDER_SIZE)
                        / Board.TILE_SIZE);
                int col = Math.round((float) (e.getX() - Board.BORDER_SIZE)
                        / Board.TILE_SIZE);
                if (row >= Board.SIZE || col >= Board.SIZE || row < 0 || col < 0) {
                    return;
                } else if (!board.grid.addStone(new Point(row, col), board.getCurrentPlayer().getColor())) {
                    return;
                } else {
                    board.lastMove = new Point(row, col);
                   // board.switchPlayer();
                    actionPerformed = false; 
                   // System.out.println(actionPerformed + "e");
                    board.repaint();
                }
            }
        });
        
        board.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "pass");
        board.getActionMap().put("pass", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
	        	board.grid.passTurn();
	        	board.grid.passTurn();
	        //  board.switchPlayer();
	            //if (board.grid.over()) {
	                System.out.println(board.grid.winner() + " wins.");
	            //}
        	}
        });
        /*
        board.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
            	System.out.println("key :" +actionPerformed);
            	if(actionPerformed!=true) {
            		System.out.println("plz");
            		return;
            	}
            	if (e.getKeyCode() == KeyEvent.VK_SPACE) {//error
                    board.grid.passTurn();
                    board.switchPlayer();
                    if (board.grid.over()) {
                        System.out.println(board.grid.winner() + " wins.");
                    }
                    actionPerformed = false;
                }
            }
        });
        */
    }
}
