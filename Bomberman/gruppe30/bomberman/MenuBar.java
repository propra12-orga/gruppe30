/**
 * Das Menü-Interface
 */
package gruppe30.bomberman;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

public class MenuBar extends JMenuBar implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Bomberman bomberman;
	JMenu game;
	JMenuItem newGame;
	JMenuItem exit;
	JMenu about;
	JMenuItem gruppe30;
	public MenuBar(Bomberman bomberman) {
		this.bomberman = bomberman;
		
		game = new JMenu("Game");

		newGame = new JMenuItem("New Game");
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
		newGame.addActionListener(this);
		game.add(newGame);
		
		game.add(new JSeparator());
		
		exit = new JMenuItem("Exit");
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));
		exit.addActionListener(this);
		game.add(exit);
		
		add(game);
		
		about = new JMenu("About");
		
		gruppe30 = new JMenuItem("Gruppe30");
		gruppe30.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, Event.CTRL_MASK));
		gruppe30.addActionListener(this);
		about.add(gruppe30);
		
		add(about);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newGame) {
			// Neues Spiel starten
			bomberman.startGame();
		}
		else if (e.getSource() == exit) {
			// Programm beenden
			System.exit(0);
		}
		else if(e.getSource() == gruppe30) {
			// Gruppe30 Info anzeigen
			String message = "Gruppe30 2012\n"
					+ "Heinrich Heine Universität Düsseldorf\n\n"
					+ "Pavel Kopylov\n"
					+ "Markus Schirmer\n"
					+ "Marc Neveling\n"
					+ "Sebastian Kreuß\n";
			JOptionPane.showMessageDialog(bomberman, message, "About Gruppe30", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
