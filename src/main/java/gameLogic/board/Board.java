package main.java.gameLogic.board;

import java.awt.*;
import java.net.*;
import java.io.*;


import javax.swing.JPanel;

import main.java.gameLogic.Grid;
import main.java.gameLogic.board.Clients.Client;
import main.java.gameLogic.board.Clients.CpuClient;
import main.java.gameLogic.stone.Point;
import main.java.gameLogic.stone.StoneColor;



public class Board extends JPanel {
    private static final Board instance = new Board();
    /**
     * Number of rows/columns.
     */
    public static final int SIZE = 9;

    /**
     * Number of tiles in row/column. (Size - 1)
     */
    public static final int N_OF_TILES = SIZE - 1;
    public static final int TILE_SIZE = 40;
    public static final int BORDER_SIZE = TILE_SIZE;

    public Client getPlayer1() {
        return player1;
    }

    public Client getPlayer2() {
        return player2;
    }

    public Client getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Black/white player/stone
     */
    private Client player1, player2, currentPlayer;
    public Grid grid;
    public Point lastMove = null;
	private Socket s;
	private PrintWriter pr;
	private InputStreamReader in;
	private BufferedReader bf;
	

    private Board() {
    }

    public void startGame(Client player1, Client player2) throws UnknownHostException, IOException {
    	
        this.setBackground(Color.ORANGE);
        this.setFocusable(true);
        grid = new Grid(SIZE);
        this.player1 = player1;
        this.player2 = player2;
        // Black always starts
        this.player1.setColor(StoneColor.BLACK);
        this.player2.setColor(StoneColor.WHITE);
        this.currentPlayer = player1;
        if(player2 instanceof CpuClient) {
        createSocket();
		}

        this.currentPlayer.play();
        while(true) {
        	System.out.println("");
        	if (!currentPlayer.isActionPerformed()) break;
        }
       // System.out.println(lastMove);

        while (!grid.over()) {
        	if (lastMove != null) {
                this.switchPlayer();
                if(this.currentPlayer==this.player2) {
            		char colConvert = (char)(lastMove.getCol() + 65);
            		char rowConvert = (char)(57 - lastMove.getRow());
            		pr.println(colConvert + "" + rowConvert);
            		pr.flush();
            		String str="";
            		while(true) {
                    	str = bf.readLine();
                    	//System.out.println("str:" + str);
            			if(str!="") break; 
            		}
            		//System.out.println("OUT");
            		/*if(str=="null") {
            			this.currentPlayer.play(true,0,0);
            		}*/
            		//else {
            			char rowBackConvert = (char)(str.charAt(1) - 48);
            			char colBackConvert = (char)(str.charAt(0) - 48);
            			this.lastMove = null;
            			this.currentPlayer.play(false,colBackConvert,rowBackConvert);            		
            	//	}
                }
                else{
                	this.lastMove = null;
                    this.currentPlayer.play();
                }
            }
        }
        pr.println("VIC");
        pr.flush();
    }

    public static Board getInstance() {
        return instance;
    }

    public void switchPlayer() {
        if (currentPlayer == player1) {
            this.currentPlayer = player2;
        } else {
            this.currentPlayer = player1;
        }
    }

    private void createSocket() throws UnknownHostException, IOException {
    	s = new Socket("localhost",12345);
		pr = new PrintWriter(s.getOutputStream());
//		pr.println("");
	//	pr.flush();
		
		in = new InputStreamReader(s.getInputStream());
		bf = new BufferedReader(in);
		
		//String str =bf.readLine();
		//System.out.println(str);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.BLACK);
        // Draw rows.
        for (int i = 0; i < SIZE; i++) {
            g2.drawLine(BORDER_SIZE, i * TILE_SIZE + BORDER_SIZE, TILE_SIZE
                    * N_OF_TILES + BORDER_SIZE, i * TILE_SIZE + BORDER_SIZE);
        }
        // Draw columns.
        for (int i = 0; i < SIZE; i++) {
            g2.drawLine(i * TILE_SIZE + BORDER_SIZE, BORDER_SIZE, i * TILE_SIZE
                    + BORDER_SIZE, TILE_SIZE * N_OF_TILES + BORDER_SIZE);
        }
        // Iterate over intersections
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                StoneColor stoneColor = grid.getStoneColor(row, col);
                if (stoneColor != null) {
                    if (stoneColor == StoneColor.BLACK) {
                        g2.setColor(Color.BLACK);
                    } else {
                        g2.setColor(Color.WHITE);
                    }
                    g2.fillOval(col * TILE_SIZE + BORDER_SIZE - TILE_SIZE / 2,
                            row * TILE_SIZE + BORDER_SIZE - TILE_SIZE / 2,
                            TILE_SIZE, TILE_SIZE);
                }
            }
        }
        // Highlight last move
        if (lastMove != null) {
            g2.setColor(Color.RED);
            g2.drawOval(lastMove.getCol() * TILE_SIZE + BORDER_SIZE - TILE_SIZE / 2,
                    lastMove.getRow() * TILE_SIZE + BORDER_SIZE - TILE_SIZE / 2,
                    TILE_SIZE, TILE_SIZE);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(N_OF_TILES * TILE_SIZE + BORDER_SIZE * 2,
                N_OF_TILES * TILE_SIZE + BORDER_SIZE * 2);
    }
}
