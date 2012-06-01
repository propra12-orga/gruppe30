
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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Bomberman extends JFrame implements KeyListener, ActionListener {
	private static final long serialVersionUID = 1L;
	boolean isRunning;
	boolean isFinished;
	boolean music = true;
	boolean sound = true;
	int playerCount;
	Stage stage;
	Player player;
	List<Bomb> bombList;
	Player player2;
	Timer timer;
	List<Integer> keyCodes;
	List<Integer> keyReleasedCodes;
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
		
		// Spieler hinzuf�gen
		player = new Player(this,1);
		player2 = new Player(this,2);
		
		// Bomben hinzuf�gen
		bombList = new ArrayList<Bomb>();
		for (int i = 0; i < 8; i++)
			bombList.add(new Bomb(this));
		
		// Sequencer f�r Hintergrundmusik
		try {
			backgroundSequencer = MidiSystem.getSequencer();
			backgroundSequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			backgroundSequencer.open();
		}
		catch (Exception ex) {}
		
		showStartScreen();
		
		keyCodes = new ArrayList<Integer>();
		keyReleasedCodes = new ArrayList<Integer>();
		
		addKeyListener(this);
		
		// 25ms Timer hinzuf�gen
		timer = new Timer(25, this);
		timer.start();
	}
	
	/**
	 * Zeigt den Startbildschirm an
	 */
	public void showStartScreen() {
		isRunning = false;
		stopMusic();
		if(music)playMusic(musicMap.get("start"));
		else stopMusic();
	}
	
	/**
	 * L�dt alle Bilder in eine Map
	 */
	public void loadImages() {
		imageMap = new HashMap<String, Image>();

		imageMap.put("start", getToolkit().getImage(getClass().getClassLoader().getResource("res/Bom.jpg")));
		imageMap.put("block", getToolkit().getImage(getClass().getClassLoader().getResource("res/block.png")));
		imageMap.put("floor", getToolkit().getImage(getClass().getClassLoader().getResource("res/Level.jpg")));
		imageMap.put("player_left", getToolkit().getImage(getClass().getClassLoader().getResource("res/Green-Monster2.png")));
		imageMap.put("player_right", getToolkit().getImage(getClass().getClassLoader().getResource("res/Green-Monster.png")));
		imageMap.put("player2_left", getToolkit().getImage(getClass().getClassLoader().getResource("res/Blue-Monster2.png")));
		imageMap.put("player2_right", getToolkit().getImage(getClass().getClassLoader().getResource("res/Blue-Monster.png")));
		imageMap.put("bomb", getToolkit().getImage(getClass().getClassLoader().getResource("res/bomb2.png")));
		imageMap.put("explosion", getToolkit().getImage(getClass().getClassLoader().getResource("res/expl.png")));
		imageMap.put("gate", getToolkit().getImage(getClass().getClassLoader().getResource("res/gate.png")));
		imageMap.put("box", getToolkit().getImage(getClass().getClassLoader().getResource("res/box.jpg")));
		imageMap.put("bup", getToolkit().getImage(getClass().getClassLoader().getResource("res/bup.png")));
		imageMap.put("pup", getToolkit().getImage(getClass().getClassLoader().getResource("res/pup.png")));
		
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
	 * L�dt alle Musikdateien in eine Map
	 */
	public void loadMusic() {
		musicMap = new HashMap<String, URL>();
		
		try {
			musicMap.put("start", getClass().getClassLoader().getResource("res/menu.mid"));
			musicMap.put("defeat", getClass().getClassLoader().getResource("res/defeat.mid"));
			musicMap.put("game", getClass().getClassLoader().getResource("res/game.mid"));
			musicMap.put("win", getClass().getClassLoader().getResource("res/win.mid"));
		}
		catch (Exception ex) {}
	}
	
	/**
	 * L�dt alle Sounddateien in eine Map
	 */
	public void loadSounds() {
		soundMap = new HashMap<String, URL>();
		
		try {			
			soundMap.put("bomb", getClass().getClassLoader().getResource("res/bomb.wav"));
			soundMap.put("ticktock", getClass().getClassLoader().getResource("res/ticktock.wav"));
			soundMap.put("scream", getClass().getClassLoader().getResource("res/scream.wav"));
			soundMap.put("laugh", getClass().getClassLoader().getResource("res/laugh.wav"));
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
	 * @param mit wie vielen spielern das spiel gestartet wird(max 2 im moment)
	 */
	public void startGame(int playerCount) {
		this.playerCount = playerCount;
		if(music) playMusic(musicMap.get("game"));
		else stopMusic();
		isFinished = false;
		isRunning = true;
		player.setActiveBombs(0);
		player2.setActiveBombs(0);
		
		player.isDead = false;
		player2.isDead = true;
		if(playerCount == 2){
		player2.isDead = false;
		}
		
		Bomb.radius1 = 4;     // setzt Power-ups zur�xk
		Bomb.radius2 = 4; 	  // "
		player.maxBombs = 1;  // "
		player2.maxBombs = 1; // "
		
		for (Bomb bomb : bombList) {
			bomb.isVisible = false;
			bomb.isExploded = false;
		}
		
		keyCodes.clear();
		keyReleasedCodes.clear();
		
		stage.loadStage("res/level02x.txt");
		
		stage.repaint();
	}
	
	/**
	 * Beendet das Spiel
	 */
	public void endGame() {
		isFinished = true;
		if(stage.isPointOnField(player.getStagePosition(), Stage.GATE) || stage.isPointOnField(player2.getStagePosition(), Stage.GATE)){
		playMusic(musicMap.get("win"));
		}else playMusic(musicMap.get("defeat"));
		Bomb.radius1 = 4;     // setzt Power-ups zur�xk
		Bomb.radius2 = 4; 	  // "
		player.maxBombs = 1;  // "
		player2.maxBombs = 1; // "
		Object[] options = { "1 Player", "2 Players", "Main Menu" };
		int dialogResult = JOptionPane.showOptionDialog(this, "New game?", (stage.isPointOnField(player.getStagePosition(), Stage.GATE) || stage.isPointOnField(player2.getStagePosition(), Stage.GATE))?"Victory!":"Defeat ...", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
		if (dialogResult == JOptionPane.YES_OPTION) {
			startGame(1);
		}
		else if (dialogResult == JOptionPane.NO_OPTION) {
			startGame(2);
		}
		else {
			
			showStartScreen();
		}
		stage.repaint();
	}
	
	public void keyPressed(KeyEvent e) {
		Integer key = new Integer(e.getKeyCode());
		if (!keyCodes.contains(key))
			keyCodes.add(key);
	}
	
	public void keyReleased(KeyEvent e) {
		Integer key = new Integer(e.getKeyCode());
		if (keyCodes.contains(key)){
			keyCodes.remove(key);
			if (!keyReleasedCodes.contains(key))
			keyReleasedCodes.add(key);
		}
		
	}
	
	public void keyTyped(KeyEvent e) {
	}
	
	/**
	 * Pr�ft ob die Taste keyCode gerade gedr�ckt wird
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
	
	/**
	 * Pr�ft ob die Taste keyCode gedr�ckt und losgelassen wurde
	 * @param keyCode Taste
	 * @return
	 */
	 public boolean hasReleased(int keyCode){
		 for(Integer key : keyReleasedCodes){
			 if(key.equals(keyCode)){
				 keyReleasedCodes.remove(key);
				 return true;
			 }
				 
		 }
		 return false;
	 }
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer && isRunning && !isFinished) {
			// Wenn das Gate erreicht wurde
			if (stage.isPointOnField(player.getStagePosition(), Stage.GATE) || stage.isPointOnField(player2.getStagePosition(), Stage.GATE)) {
				endGame();
			}
			
			
			
			// Alle Bomben pr�fen
			for (Bomb bomb : bombList) {
				bomb.process();
				if (bomb.isVisible && bomb.isExploded) {
					for (Point p : bomb.explosionArray) {
						// Wenn der Spieler in der Explosion ist
						if(playerCount == 1){
						if (player.getStagePosition().equals(p)) {
							player.isDead = true;
							playSound(soundMap.get("scream"));
							endGame();
							break;

						}
						
						}
						if(playerCount == 2){
							if (player.getStagePosition().equals(p) && !player.isDead) {
								player.isDead = true;
								playSound(soundMap.get("scream"));
							}
							if (player2.getStagePosition().equals(p) && !player2.isDead) {
								player2.isDead = true;
								playSound(soundMap.get("scream"));
							}

							if(player.isDead && player2.isDead){
							endGame();
							break;
							}
						}
						// Kettenreaktion der bomben
						for (Bomb bomb2 : bombList) {
							if(bomb2.isVisible && !bomb2.isExploded && p.equals(bomb2.position)){
								bomb2.count = 2000/25;
							}
						}
						
					}
				}
			}
			
			if (hasReleased(KeyEvent.VK_B) && !player.isDead) {
				/// Bombe legen
					for (Bomb bomb : bombList) {
						if (!bomb.isVisible && player.getActiveBombs() < player.maxBombs && bomb.canLayOn(player.getStagePosition())) {
							bomb.setToPlayerPos();
							bomb.setOwner(player);
							player.setActiveBombs(player.getActiveBombs()+1);
							break;
						}
					}
				
			}
			
			if (hasReleased(KeyEvent.VK_L) && !player2.isDead) {
				/// Bombe legen
					for (Bomb bomb : bombList) {
						if (!bomb.isVisible && player2.getActiveBombs() < player2.maxBombs && bomb.canLayOn(player2.getStagePosition())) {
							bomb.setToPlayer2Pos();
							bomb.setOwner(player2);
							player2.setActiveBombs(player2.getActiveBombs()+1);
							break;
						}
					}
				}
			}
			/*
			 *  Spieler Steuerung
			 */
			if(!player.isDead){
				if (isPressing(KeyEvent.VK_A)) {
					player.moveLeft();
				}
				if (isPressing(KeyEvent.VK_D)) {
					player.moveRight();
				}
				if (isPressing(KeyEvent.VK_W)) {
					player.moveUp();
				}
				if (isPressing(KeyEvent.VK_S)) {
					player.moveDown();
				}
			}
			//player 2
			
				
			if(!player2.isDead){
				if (isPressing(KeyEvent.VK_LEFT)) {
					player2.moveLeft();
				}
				if (isPressing(KeyEvent.VK_RIGHT)) {
					player2.moveRight();
				}
				if (isPressing(KeyEvent.VK_UP)) {
					player2.moveUp();
				}
				if (isPressing(KeyEvent.VK_DOWN)) {
					player2.moveDown();
					
				}
			}
			// Feld neu zeichnen
			stage.repaint();
		}
		
	
	public static void main(String args[]) {
		new Bomberman();
	}
}
