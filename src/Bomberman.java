
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import javax.swing.JFrame;

public class Bomberman extends JFrame implements KeyListener, ActionListener {
	private static final long serialVersionUID = 1L;
	boolean isRunning;
	boolean isFinished;
	Stage stage;
	Player player;
	List<Bomb> bombList;
	Timer timer;
	List<Integer> keyCodes;
	Sequencer backgroundSequencer;
	Map<String, Image> imageMap;
	Map<String, URL> musicMap;
	Map<String, URL> soundMap;
	public Bomberman() {
		setTitle("Bomberman");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		setJMenuBar(new MenuBar(this));
		
		stage = new Stage(this);
		add(stage);

		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		// Daten laden
		loadImages();
		loadMusic();
		loadSounds();
		
		// Spieler hinzufügen
		player = new Player(this);
		
		// Bomben hinzufügen
		bombList = new ArrayList<Bomb>();
		//for (int i = 0; i < 8; i++)
			bombList.add(new Bomb(this));
		
		// 25ms Timer hinzufügen
		timer = new Timer(25, this);
		timer.start();
		
		// Sequencer für Hintergrundmusik
		try {
			backgroundSequencer = MidiSystem.getSequencer();
			backgroundSequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			backgroundSequencer.open();
		}
		catch (Exception ex) {}
		
		showStartScreen();
		
		keyCodes = new ArrayList<Integer>();
		
		addKeyListener(this);
	}
	
	/**
	 * Zeigt den Startbildschirm an
	 */
	public void showStartScreen() {
		isRunning = false;
		stopMusic();
		playMusic(musicMap.get("start"));
	}
	
	/**
	 * Lädt alle Bilder in eine Map
	 */
	public void loadImages() {
		imageMap = new HashMap<String, Image>();

		imageMap.put("start", getToolkit().getImage(getClass().getClassLoader().getResource("res/Bom.jpg")));
		imageMap.put("block", getToolkit().getImage(getClass().getClassLoader().getResource("res/block.jpg")));
		imageMap.put("floor", getToolkit().getImage(getClass().getClassLoader().getResource("res/Level.jpg")));
		imageMap.put("player_left", getToolkit().getImage(getClass().getClassLoader().getResource("res/m2.jpg")));
		imageMap.put("player_right", getToolkit().getImage(getClass().getClassLoader().getResource("res/m1.jpg")));
		imageMap.put("bomb", getToolkit().getImage(getClass().getClassLoader().getResource("res/bomb.png")));
		imageMap.put("explosion", getToolkit().getImage(getClass().getClassLoader().getResource("res/b2.png")));
		imageMap.put("gate", getToolkit().getImage(getClass().getClassLoader().getResource("res/Gate2.png")));
		imageMap.put("box", getToolkit().getImage(getClass().getClassLoader().getResource("res/box.jpg")));
		
		// Bilder vorladen
		MediaTracker mTracker = new MediaTracker(this);
		int i = 0;
		for (Image image : imageMap.values()) {
			mTracker.addImage(image, i);
			i++;
		}
		try {
			mTracker.waitForAll();
		}
		catch (Exception ex) {}
	}
	
	/**
	 * Lädt alle Musikdateien in eine Map
	 */
	public void loadMusic() {
		musicMap = new HashMap<String, URL>();
		
		try {
			musicMap.put("start", getClass().getClassLoader().getResource("res/play3.mid"));
			musicMap.put("game", getClass().getClassLoader().getResource("res/play2.mid"));
		}
		catch (Exception ex) {}
	}
	
	/**
	 * Lädt alle Sounddateien in eine Map
	 */
	public void loadSounds() {
		soundMap = new HashMap<String, URL>();
		
		try {			
			soundMap.put("bomb", getClass().getClassLoader().getResource("res/bomb.wav"));
			soundMap.put("ticktock", getClass().getClassLoader().getResource("res/ticktock.wav"));
		}
		catch (Exception ex) {}
	}
	
	/**
	 * Spielt eine eine Musikdatei im Hintergrund ab
	 * @param file Musikdatei
	 */
	public void playMusic(URL url) {
		try {
			backgroundSequencer.setSequence(MidiSystem.getSequence(url));
			backgroundSequencer.start();
	        
		} catch(Exception ex) {}
	}
	
	/**
	 * Stopt die Hintergrundmusik
	 */
	public void stopMusic() {
		backgroundSequencer.stop();
	}
	
	/**
	 * Spielt eine Sounddatei ab
	 * @param file Sounddatei
	 */
	public void playSound(URL url) {
		try {
	        Clip clip = AudioSystem.getClip();
	        clip.stop();
	        clip.open(AudioSystem.getAudioInputStream(url));
	        clip.setFramePosition(0);
	        clip.start();
	        
		}
		catch (Exception ex) {}
	}
	
	/**
	 * Startet das Spiel
	 */
	public void startGame() {		
		playMusic(musicMap.get("game"));
		
		isFinished = false;
		isRunning = true;
		player.isDead = false;
		for (Bomb bomb : bombList) {
			bomb.isVisible = false;
			bomb.isExploded = false;
		}
		
		stage.loadStage("res/level02.txt");
		
		stage.repaint();
	}
	
	/**
	 * Beendet das Spiel
	 */
	public void endGame() {
		keyCodes.clear();
		stopMusic();
		stage.repaint();
		isFinished = true;
		int dialogResult = JOptionPane.showConfirmDialog(this, "Noch ein Spiel?", (player.isDead)?"Verloren ...":"Gewonnen!", JOptionPane.YES_NO_OPTION);
		if (dialogResult == JOptionPane.YES_OPTION) {
			startGame();
		}
		else {
			showStartScreen();
		}
	}
	
	public void keyPressed(KeyEvent e) {
		Integer key = new Integer(e.getKeyCode());
		if (!keyCodes.contains(key))
			keyCodes.add(key);
	}
	
	public void keyReleased(KeyEvent e) {
		Integer key = new Integer(e.getKeyCode());
		if (keyCodes.contains(key))
			keyCodes.remove(key);
	}
	
	public void keyTyped(KeyEvent e) {
	}
	
	/**
	 * Prüft ob die Taste keyCode gerade gedrückt wird
	 * @param keyCode Taste
	 * @return
	 */
	public boolean isPressing(int keyCode) {
		for (Integer key : keyCodes) {
			if (key.equals(keyCode)) {
				return true;
			}
		}
		return false;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer && isRunning && !isFinished) {
			// Wenn das Gate erreicht wurde
			if (stage.isPointOnField(player.getStagePosition(), Stage.GATE)) {
				endGame();
			}
			// Alle Bomben prüfen
			for (Bomb bomb : bombList) {
				bomb.process();
				if (bomb.isVisible && bomb.isExploded) {
					for (Point p : bomb.explosionArray) {
						// Wenn der Spieler in der Explosion ist
						if (player.getStagePosition().equals(p)) {
							player.isDead = true;
							endGame();
							break;
						}
					}
				}
			}
			if (isPressing(KeyEvent.VK_B)) {
				/// Bombe legen
				for (Bomb bomb : bombList) {
					if (!bomb.isVisible) {
						bomb.setToPlayerPos();
						break;
					}
				}
			}
			/*
			 *  Spieler Steuerung
			 */
			if (isPressing(KeyEvent.VK_LEFT)) {
				player.moveLeft();
			}
			if (isPressing(KeyEvent.VK_RIGHT)) {
				player.moveRight();
			}
			if (isPressing(KeyEvent.VK_UP)) {
				player.moveUp();
			}
			if (isPressing(KeyEvent.VK_DOWN)) {
				player.moveDown();
			}
			// Feld neu zeichnen
			stage.repaint();
		}
	}
	
	public static void main(String args[]) {
		new Bomberman();
	}
}
