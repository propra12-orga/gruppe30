
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

import javax.swing.JPanel;

public class Stage extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static final char BLOCK = 'a';
	public static final char PLAYER = 'p';
	public static final char PLAYER2 = 'l';
	public static final char GATE = 'g';
	public static final char BOX = 'b';
	public static final char BOXGATE = 'x';
	public static final char BOMBUP = 'z';
	public static final char POWERUP = 'u';
	int k, p1, p2, r1, r2;
	
	Bomberman bomberman;
	private char[][] stageArray;
	public Stage(Bomberman bomberman) {
		this.bomberman = bomberman;
		setSize(850, 650);
		setPreferredSize(getSize());
		
		stageArray = new char[17][13];
	}
	
	/**
	 * Lädt ein Spielfeld aus einer Datei
	 * @param filename
	 */
	public void loadStage(String filename) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename)));
			for (int y = 0; y < 13; y++) {
				String line = in.readLine();
				for (int x = 0; x < 17; x++) {
					switch (line.charAt(x)) {
					case Stage.BLOCK: case Stage.GATE: case Stage.BOX: case Stage.BOXGATE:
						stageArray[x][y] = line.charAt(x);
						break;
					case Stage.PLAYER:
						bomberman.player.setStagePosition(new Point(x, y));
						stageArray[x][y] = ' ';
						break;
					case Stage.PLAYER2:
						bomberman.player2.setStagePosition(new Point(x, y));
						stageArray[x][y] = ' ';
						break;
					default:
						Random rand = new Random();
						if(rand.nextInt(100) < 50 && !new Point(x,y).equals(new Point(1,2)) &&!new Point(x,y).equals(new Point(2,1))){
						stageArray[x][y] = 0;  	//////// für zufällig erzeugte Blöcke  ='b' und bs aus Textdatei entfernen
						}
						else{
						stageArray[x][y] = 0;
						}
					}
				}
			}
			repaint();
		}
		catch(Exception ex) {}
	}
	
	/**
	 * Prüft ob auf der Feld-Position p das Feld field ist
	 * @param p Feld-Position
	 * @param field Feld
	 * @return
	 */
	public boolean isPointOnField(Point p, char field) {
		if (stageArray[p.x][p.y] == field) return true;
		return false;
	}
	
	/**
	 * Zerstört Box an Feld-Position p, prüft ob es ein Power-Up gibt
	 * @param p Feld-Position
	 */
	public void destroyBox(Point p){
		if(isPointOnField(p, BOX)){
			stageArray[p.x][p.y] = 0;
			if(Chance()) k = Choose();
			if(k == 1) Bombup(p);
			else if(k == 2) Powerup(p);
		}
		if(isPointOnField(p, BOXGATE)){
			stageArray[p.x][p.y] = 'g';
		}
	}
	
	/**
	 * 
	 * @param p
	 */
	public void destroyPU(Point p){
		if(isPointOnField(p, BOMBUP)){
			stageArray[p.x][p.y] = 0;
		}
		if(isPointOnField(p, POWERUP)){
			stageArray[p.x][p.y] = 0;
		}
	}
	
	/**
	 * prüft ob es ein Power-Up gibt
	 */
	
	public boolean  Chance() {
		int i = (int)(Math.random()*10);
		if(i > 7) return true;
		else return false;
	}
	
	/**
	 *  prüft welches Power-Up es gibt
	 */
	 public int Choose() {
		int j = (int)(Math.random()*10);
		if(j <= 6) return 1;
		else return 2;
	}
	
	/**
	 *  Bombe +1
	 */
	public void Bombup(Point p) {
		k = 0;
		stageArray[p.x][p.y] = 'z';
	}
	
	/**
	 *  Radius +1
	 */
	public void Powerup(Point p) {
		k = 0;
		stageArray[p.x][p.y] = 'u';
	}
	
	/**
	 * Power-Up aufnehmen und entfernen
	 */
	public void PlayerpickPowerup() {
		if (bomberman.stage.isPointOnField(new Point(bomberman.player.position.x/50, bomberman.player.position.y/50), 'z') && !bomberman.player.isDead) {
			stageArray[bomberman.player.position.x/50][bomberman.player.position.y/50] = 0;
			bomberman.player.maxBombs++;	
		}
		if (bomberman.stage.isPointOnField(new Point(bomberman.player2.position.x/50, bomberman.player2.position.y/50), 'z') && !bomberman.player2.isDead) {
			stageArray[bomberman.player2.position.x/50][bomberman.player2.position.y/50] = 0;
			bomberman.player2.maxBombs++;	
		}
		if (bomberman.stage.isPointOnField(new Point(bomberman.player.position.x/50, bomberman.player.position.y/50), 'u') && !bomberman.player.isDead) {
			stageArray[bomberman.player.position.x/50][bomberman.player.position.y/50] = 0;
			Bomb.radius1++;
		}
		if (bomberman.stage.isPointOnField(new Point(bomberman.player2.position.x/50, bomberman.player2.position.y/50), 'u') && !bomberman.player2.isDead) {
			stageArray[bomberman.player2.position.x/50][bomberman.player2.position.y/50] = 0;
			Bomb.radius2++;
		}
	}
	
	
	private Image buffer;
	public void paint(Graphics graphics) {
		if (buffer == null) {
			buffer = this.createImage(getWidth(), getHeight());
		}
		Graphics2D g = (Graphics2D)buffer.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.clearRect(0, 0, getWidth(), getHeight());
		if (!bomberman.isRunning) {
			// Startbildschirm zeichnen
			g.drawImage(bomberman.imageMap.get("start"), 0, 0, getWidth(), getHeight(), this);
		}
		else {
			// Spielfeld
			for (int x = 0; x < 17; x++) {
				for (int y = 0; y < 13; y++) {
					switch (stageArray[x][y]) {
					case Stage.BLOCK:
						g.drawImage(bomberman.imageMap.get("block"), x * 50, y * 50, 50, 50, this);
						break;
					case Stage.GATE:
						g.drawImage(bomberman.imageMap.get("floor"), x * 50, y * 50, 50, 50, this);
						g.drawImage(bomberman.imageMap.get("gate"), x * 50, y * 50, 50, 50, this);
						break;
					case Stage.BOX:
						g.drawImage(bomberman.imageMap.get("box"), x * 50, y * 50, 50, 50, this);
						break;
					case Stage.BOXGATE:
						g.drawImage(bomberman.imageMap.get("box"), x * 50, y * 50, 50, 50, this);
						break;
					case Stage.BOMBUP:
						g.drawImage(bomberman.imageMap.get("floor"), x * 50, y * 50, 50, 50, this);
						g.drawImage(bomberman.imageMap.get("bup"), x * 50, y * 50, 40, 40, this);
						break;
					case Stage.POWERUP:
						g.drawImage(bomberman.imageMap.get("floor"), x * 50, y * 50, 50, 50, this);
						g.drawImage(bomberman.imageMap.get("pup"), x * 50, y * 50, 40, 40, this);
						break;
					default:
						g.drawImage(bomberman.imageMap.get("floor"), x * 50, y * 50, 50, 50, this);
					}
				}
			}
			
			// Bomben
			for (Bomb bomb : bomberman.bombList) {
				if (bomb.isVisible && !bomb.isExploded) {
					g.drawImage(bomberman.imageMap.get("bomb"), bomb.position.x * 50, bomb.position.y * 50, 50, 50, this);
				}
			}
			
			// Spieler
			if (!bomberman.player.isDead)
				g.drawImage(bomberman.player.image, bomberman.player.position.x, bomberman.player.position.y, 50, 50, this);
			
			if (!bomberman.player2.isDead)
				g.drawImage(bomberman.player2.image, bomberman.player2.position.x, bomberman.player2.position.y, 50, 50, this);
			
			
			// Explosionen
			for (Bomb bomb : bomberman.bombList) {
				if (bomb.isVisible && bomb.isExploded) {
					for (Point p : bomb.explosionArray) {
						g.drawImage(bomberman.imageMap.get("explosion"), p.x * 50, p.y * 50, 50, 50, this);
					}
				}
			}
			if(bomberman.playerCount == 1){
				int p1 = (bomberman.player.maxBombs);
				int r1 = (Bomb.radius1);
				g.setColor(Color.GREEN);
				Font font = new Font("Arial", Font.BOLD, 15);
				g.setFont(font);
				g.drawString("Player 1", 5, 625);
				g.drawString("Bombs: " + p1 + ", Radius: " + r1, 5, 645);
				}
			
			if(bomberman.playerCount == 2){
			p1 = (bomberman.player.maxBombs);
			p2 = (bomberman.player2.maxBombs);
			r1 = (Bomb.radius1);
			r2 = (Bomb.radius2);
			Font font = new Font("Arial", Font.BOLD, 15);
			g.setFont(font);
			if(!bomberman.player.isDead){
			g.setColor(Color.GREEN);
			g.drawString("Player 1", 5, 625);
			g.drawString("Bombs: " + p1 + ", Radius: " + r1, 5, 645);
			}else {
				g.setColor(Color.RED);
				g.drawString("Player 1 is Dead", 5, 625);
				}

			if(!bomberman.player2.isDead){
			g.setColor(Color.CYAN);
			g.drawString("Player 2", 690, 625);
			g.drawString("Bombs: " + p2 + ", Radius: " + r2, 690, 645);
			}else {
				g.setColor(Color.RED);
				g.drawString("Player 2 is Dead", 690, 625);
				}
			}
		}

		
		g.dispose();
		
		// Doublebuffering
		graphics.drawImage(buffer, 0, 0, getWidth(), getHeight(), this);
	}
	
	
}
