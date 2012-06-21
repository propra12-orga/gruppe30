/**
 * Das Menü-Interface
 */

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

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
	JMenuItem newGame2;
	JMenuItem save;
	JMenuItem load;
	JMenuItem menu;
	JMenuItem exit;
	JMenu options;
	JMenuItem musicon;
	JMenuItem musicoff;
	JMenuItem soundon;
	JMenuItem soundoff;
	JMenu about;
	JMenu icons;
	JMenuItem gruppe30;
	public MenuBar(Bomberman bomberman) {
		this.bomberman = bomberman;
		
		game = new JMenu("Game");

		newGame = new JMenuItem("New Game");
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
		newGame.addActionListener(this);
		game.add(newGame);
		
		newGame2 = new JMenuItem("2 Players");
		newGame2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, Event.CTRL_MASK));
		newGame2.addActionListener(this);
		game.add(newGame2);
		
		game.add(new JSeparator());
		
		save = new JMenuItem("Save");
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		save.addActionListener(this);
		game.add(save);
		
		load = new JMenuItem("Load");
		load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Event.CTRL_MASK));
		load.addActionListener(this);
		game.add(load);
		
		game.add(new JSeparator());
		
		menu = new JMenuItem("Main Menu");
		menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK));
		menu.addActionListener(this);
		game.add(menu);
		
		game.add(new JSeparator());
		
		exit = new JMenuItem("Exit");
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));
		exit.addActionListener(this);
		game.add(exit);
		
		add(game);
		
		options = new JMenu("Options");
		
		musicon = new JMenuItem("Music on");
		musicon.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
		musicon.addActionListener(this);
		options.add(musicon);
		
		musicoff = new JMenuItem("Music off");
		musicoff.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
		musicoff.addActionListener(this);
		options.add(musicoff);
		
		options.add(new JSeparator());
		
		soundon = new JMenuItem("Sound on");
		soundon.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, Event.CTRL_MASK));
		soundon.addActionListener(this);
		options.add(soundon);
		
		soundoff = new JMenuItem("Sound off");
		soundoff.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, Event.CTRL_MASK));
		soundoff.addActionListener(this);
		options.add(soundoff);
		
		add(options);
		
		about = new JMenu("About");
		
		gruppe30 = new JMenuItem("Information");
		gruppe30.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, Event.CTRL_MASK));
		gruppe30.addActionListener(this);
		about.add(gruppe30);
		
		add(about);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newGame) {
			// Neues Spiel starten
			bomberman.Continue = 3;
			bomberman.startGame(1, 1);
		}
		
		else if (e.getSource() == newGame2) {
			// Neues Spiel starten
			bomberman.player1win = 0;
			bomberman.player2win = 0;
			bomberman.startGame(2, 1);
		}
		else if (e.getSource() == save) {
			// Spiel speichern
			try {
				bomberman.stage.save("src/Levels/99");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if (e.getSource() == load) {
			// Spiel laden;
			bomberman.startGame(1, 99);
		}
		else if (e.getSource() == menu) {
			// Programm beenden
			bomberman.showStartScreen();
			bomberman.stage.repaint();
		}
		else if (e.getSource() == exit) {
			// Programm beenden
			System.exit(0);
		}
		else if (e.getSource() == musicon) {
			// Musik anschalten (standart)
			bomberman.music = true;
			if(!bomberman.isRunning) bomberman.playMusic(bomberman.musicMap.get("start"));
			if(bomberman.isRunning) bomberman.playMusic(bomberman.musicMap.get("game"));
		}
		else if (e.getSource() == musicoff) {
			// Musik abschalten
			bomberman.music = false;
			bomberman.stopMusic();
		}
		else if (e.getSource() == soundon) {
			// Sound anschalten
			bomberman.sound = true;
		}
		else if (e.getSource() == soundoff) {
			// Sound abschalten
			bomberman.sound = false;
		}
		else if(e.getSource() == gruppe30) {
			// Gruppe30 Info anzeigen
			String message = "Gruppe30 2012\n"
					+ "Heinrich Heine Universität Düsseldorf\n\n"
					+ "Pavel Kopylov\n"
					+ "Markus Schirmer\n"
					+ "Marc Neveling\n"
					+ "Sebastian Kreuß\n\n"
					+ "Monsters: http://blog.spoongraphics.co.uk  \n\n";
			JOptionPane.showMessageDialog(bomberman, message, "About Gruppe30", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
