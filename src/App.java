import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Builds UI and starts the game.
 *
 */
public class App {

	public static final String TITLE = "";
	public static final int BORDER_SIZE = 25;

	public static void main(String[] args) {
		Test.test();
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
