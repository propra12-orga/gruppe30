
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
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

public class Bomberman extends JFrame implements KeyListener, ActionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	boolean isRunning;
	boolean isFinished;
	boolean music = true;
	boolean sound = true;
	boolean inLevelEditor;
	int playerCount, player1win = 0, player2win = 0, Continue;

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
	Map<String, String> levelMap;
	int level = 1;
	int timeout;
	
	public Bomberman() {
		setTitle("MONSTERS");
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
		loadLevels();
		
		// Spieler hinzufügen
		player = new Player(this,1);
		player2 = new Player(this,2);
		
		// Bomben hinzufügen
		bombList = new ArrayList<Bomb>();
		for (int i = 0; i < 8; i++)
			bombList.add(new Bomb(this));
		
		// Sequencer für Hintergrundmusik
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
		addMouseListener(this);
		
		// 25ms Timer hinzufügen
		timer = new Timer(25, this);
		timer.start();
	}
	
	

	/**
	 * Zeigt den Startbildschirm an
	 */
	public void showStartScreen() {
		inLevelEditor = false;
		isRunning = false;
		stopMusic();
		if(music)playMusic(musicMap.get("start"));
		else stopMusic();
	}
	
	/**
	 * Lädt alle Bilder in eine Map
	 */
	public void loadImages() {
		imageMap = new HashMap<String, Image>();

		imageMap.put("start", getToolkit().getImage(getClass().getClassLoader().getResource("res/Bom.jpg")));
		imageMap.put("board", getToolkit().getImage(getClass().getClassLoader().getResource("res/board.jpg")));
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
	 * Lädt alle Musikdateien in eine Map
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
	 * Lädt alle Sounddateien in eine Map
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
	 * Lädt alle Levels in eine Map
	 */
	public void loadLevels() {
		levelMap = new HashMap<String, String>();
		
		try{
			File dir = new File("src/Levels");
			String[] levels = dir.list();
			int name=0;
			for(String s : levels){
				levelMap.put("" + name, "src/Levels/" +s);
				name++;

			}
			
		}
		catch(Exception ex){}
		
		
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
	 * @param welches level soll gestartet werden
	 */
	public void startGame(int playerCount, int level) {
		inLevelEditor = false;
		
		this.level = level;
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
		Bomb.radius1 = 4;     // setzt Power-ups zurüxk
		Bomb.radius2 = 4; 	  // "
		player.maxBombs = 1;  // "
		player2.maxBombs = 1; // "
		
		for (Bomb bomb : bombList) {
			bomb.isVisible = false;
			bomb.isExploded = false;
		}
		
		keyCodes.clear();
		keyReleasedCodes.clear();
		if(level == 999999999){
			stage.loadStage("src/res/savegame");
		}
		else{
			System.out.println(levelMap.get(""+level));
			stage.loadStage(levelMap.get(""+level));
		}
		stage.repaint();
	}
	
	/**
	 * Beendet das Spiel
	 * @param playerID des Spielers der gewonnen hat
	 */
	public void endGame(int winner) {
		isFinished = true;	
		if(playerCount == 1){
		if(stage.isPointOnField(player.getStagePosition(), Stage.GATE)){
		playMusic(musicMap.get("win"));
		}else playMusic(musicMap.get("defeat"));
		}
		if(playerCount == 2){
			playMusic(musicMap.get("win"));
		}
		Bomb.radius1 = 4;     // setzt Power-ups zurüxk
		Bomb.radius2 = 4; 	  // "
		player.maxBombs = 1;  // "
		player2.maxBombs = 1; // "
		if(playerCount == 2){
		Object[] options = { "Yes!", "No" };
		int dialogResult = JOptionPane.showOptionDialog(this, "Continue?", "Player " + winner + " wins!" , JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
		if (dialogResult == JOptionPane.YES_OPTION) {
			if(level < 5){
				level++;
				playSound(soundMap.get("laugh"));
				startGame(playerCount,level);
			}
			else{
				
				if(player1win == player2win){
					JOptionPane.showMessageDialog(this, player1win + " to " + player2win + " ...", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
				}
					
				else if(player1win > player2win){
					level++;
					JOptionPane.showMessageDialog(this, "Player 2, you lose! Player 1 won the game: " + player1win + " to " + player2win, "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
				}
				else{
					level++;
					JOptionPane.showMessageDialog(this, "Player 1, you lose! Player 2 won the game: "  + player2win + " to " + player1win, "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
					}
				player1win = 0;
				player2win = 0;
				showStartScreen();
			
			}
		}
		/*else if (dialogResult == JOptionPane.NO_OPTION) {
			Object[] levels = { "Level 1", "Level 2", "Level 3", "Level 4", "Level 5"};
			int levelSelect = level;
			JOptionPane.showInputDialog(this, "Level Selection", "Choose level!",JOptionPane.QUESTION_MESSAGE, null, levels, levelSelect);
			if(playerCount == 2 && levelSelect < 5){
				playSound(soundMap.get("laugh"));
				}
			startGame(playerCount, level);
		} */
		else {
			
			if(player1win == player2win){
				JOptionPane.showMessageDialog(this, player1win + " to " + player2win + " ...", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
			}
			
			else if(player1win > player2win){
				level++;
				JOptionPane.showMessageDialog(this, "Player 2, you lose! Player 1 won the game: " + player1win + " to " + player2win, "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
			}
			else{
				level++;
				JOptionPane.showMessageDialog(this, "Player 1, you lose! Player 2 won the game: "  + player2win + " to " + player1win, "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
				}
			player1win = 0;
			player2win = 0;
			showStartScreen();
		}
		
		}
		if(playerCount == 1){
			Object[] options = { "Yes!", "No" };
			int dialogResult = JOptionPane.showOptionDialog(this, "Continue?",stage.isPointOnField(player.getStagePosition(), Stage.GATE)? "Victory!" : "Defeat..", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
			if (dialogResult == JOptionPane.YES_OPTION) {
				if(level < 5 && Continue > 0){
					if(!player.isDead){
					level++;
					startGame(playerCount,level);
					}else{
						startGame(playerCount,level);
					}
				}
				else{
					if(Continue != 0) {
					JOptionPane.showMessageDialog(this, "You win!", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
					}else{JOptionPane.showMessageDialog(this, "You lose..", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);}
					showStartScreen();
				
				}
			}
			else {
				showStartScreen();
			}
			
		}
		
		stage.repaint();
	}
	
	/**
	 * startet den leveleditor
	 */
	public void startEditor(String filename){
		stage.focused = 1;
		isRunning = true;
		inLevelEditor = true;
		player.isDead = true;
		player2.isDead = true;
		stage.loadStage(filename);
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
	
	/**
	 * Prüft ob die Taste keyCode gedrückt und losgelassen wurde
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
			if(playerCount == 1){
				if (stage.isPointOnField(player.getStagePosition(), Stage.GATE)) {
					endGame(1);
				}
			}
			/*else if(stage.isPointOnField(player2.getStagePosition(), Stage.GATE)) {
				endGame(2);
			}*/
			
			
			
			// Alle Bomben prüfen
			for (Bomb bomb : bombList) {
				bomb.process();
				if (bomb.isVisible && bomb.isExploded) {
					
					for (Point p : bomb.explosionArray) {
						// Wenn der Spieler in der Explosion ist
						if(playerCount == 1){
						if (player.getStagePosition().equals(p)) {
							player.isDead = true;
							Continue--;
							playSound(soundMap.get("scream"));
							endGame(1);
							break;

						}
						
						}
						
						if(playerCount == 2){
							if (player.getStagePosition().equals(p) && !player.isDead && !isFinished) {
								player2win++;
								player.isDead = true;
								playSound(soundMap.get("scream"));
								endGame(2);
								
								break;
							}
							if (player2.getStagePosition().equals(p) && !player2.isDead && !isFinished) {
								player1win++;
								player2.isDead = true;
								playSound(soundMap.get("scream"));
								endGame(1);
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
			if(!player.isDead && !inLevelEditor){
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
			
				
			if(!player2.isDead && !inLevelEditor){
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
	
		
	
	
	// Mouse Listener

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseReleased(MouseEvent e) {
		Point Click = e.getPoint();
		if(inLevelEditor == true){
		// Klick auf die menuleiste unten
		if(Click.y >= 655 && Click.y <= 695){
			if(Click.x >= 5 && Click.x <=45 ){
				stage.focused = 1;
			}
			if(Click.x >= 55 && Click.x <=95 ){
				stage.focused = 2;
			}
			if(Click.x >= 105 && Click.x <=145 ){
				stage.focused = 3;
			}
			if(Click.x >= 155 && Click.x <=195 ){
				stage.focused = 4;
			}
			if(Click.x >= 205 && Click.x <=245 ){
				stage.focused = 5;
			}
			if(Click.x >= 255 && Click.x <=295 ){
				stage.focused = 6;
			}
			if(Click.x >=760 && Click.x <= 820){
				if(stage.validateField()){
					JOptionPane.showMessageDialog(null, "Level meets requirements!","Success!", JOptionPane.INFORMATION_MESSAGE);
					String name = JOptionPane.showInputDialog(null, "Insert level name!", "Saving", JOptionPane.PLAIN_MESSAGE);
					if(name != null){
					String filename = "src/Levels/" + name;
					int newName = 1;
					try{
						stage.saveLevel(filename);
						newName = levelMap.size() -1;
						levelMap.put("" + newName, filename);
					}catch(Exception ex){}
					Object[] options = {"Singleplayer", "2 Players", "Edit Level","Exit"};
					int selected = JOptionPane.showOptionDialog(null,"Play it now?", "Level saved!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
					switch(selected){
					case 0: Continue = 3;
							inLevelEditor = false;
							startGame(1, newName);
							break;
					case 1: inLevelEditor = false;
							startGame(2, newName);
							break;
					case 3: showStartScreen();
							break;
					}
					
					
					}
				}else{
					JOptionPane.showMessageDialog(null, "Something is missing! \nMake sure you have set the players starting point and the exit!\nPlayers must be able to reach exit, too!","Error!", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		}
		
		Point position = new Point((int)(Click.x/50),(int)(Click.y/50));
		// click ins feld rein
		if(position.x >0 && position.x <16 && position.y >1 && position.y <12){
			if(stage.focused == 1){
				if(stage.stageArray[position.x][position.y-1] == 'g'){
					stage.stageArray[position.x][position.y-1] = 'x';
				}else
					stage.stageArray[position.x][position.y-1] = 'b';
				
			}
			if(stage.focused == 2){
				stage.stageArray[position.x][position.y-1] = 'a';
			}
			if(stage.focused == 3){
				stage.stageArray[position.x][position.y-1] = 0;
			}
			if(stage.focused == 4){
				for(int i = 1; i < 17 ; i++){
					for(int j = 1; j < 12 ; j++){
						if(stage.stageArray[i][j] == 'p'){
							stage.stageArray[i][j] = 0;
							break;
						}
					}
				}
				stage.stageArray[position.x][position.y-1] = 'p';
			}
			if(stage.focused == 5){
				for(int i = 1; i < 17 ; i++){
					for(int j = 1; j < 12 ; j++){
						if(stage.stageArray[i][j] == 'l'){
							stage.stageArray[i][j] = 0;
							break;
						}
					}
				}
				stage.stageArray[position.x][position.y-1] = 'l';
			}
			if(stage.focused == 6){
				for(int i = 1; i < 17 ; i++){
					for(int j = 1; j < 12 ; j++){
						if(stage.stageArray[i][j] == 'g' || stage.stageArray[i][j] == 'x' ){
							stage.stageArray[i][j] = 0;
							break;
						}
					}
				}
				stage.stageArray[position.x][position.y-1] = 'g';
			}
		}
		}
	}
	
	public static void main(String args[]) {
		new Bomberman();
	}
}
