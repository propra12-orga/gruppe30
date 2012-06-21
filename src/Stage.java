
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.*;
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
	int k, p1, p2, r1, r2, p1w, p2w, lvl, con;
	
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
			for (int y = 0; y < 14; y++) {
				String line = in.readLine();
				if (y == 12) bomberman.player.maxBombs =  Integer.parseInt(line);
				else if (y == 13) Bomb.radius1 = Integer.parseInt(line);
				else if (y < 13){
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
			}
			repaint();
		}
		catch(Exception ex) {}
	}
	
	/**
	 * Speichert das Spiel
	 * @param filename Name der Speicherdatei
	 */
	public void save(String filename) throws IOException {
		File save = new File(filename);
		FileWriter out = new FileWriter(save);
		PrintWriter ps = new PrintWriter(out);
		for (int y = 0; y < 12; y++) {
			for (int x = 0; x < 17; x++) {
				if(bomberman.player.position.x/50 == x && bomberman.player.position.y/50 == y) ps.print('p');
				else {
					ps.print(stageArray[x][y]);
					if(x==16) ps.println();
				}
			}		
		}
		ps.print(bomberman.player.maxBombs);
		ps.println();
		ps.print(Bomb.radius1);
		ps.flush();
		ps.close();	
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
		if(bomberman.playerCount == 2) {
			int i = (int)(Math.random()*30);
			if(i > 7) return true;
			else return false;
		}else {
			int i = (int)(Math.random()*10);
			if(i > 7) return true;
			else return false;
		}
	}
	
	/**
	 *  prüft welches Power-Up es gibt
	 */
	 public int Choose() {
		int j = (int)(Math.random()*10);
		if(j <= 5) return 1;
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
			g.drawImage(bomberman.imageMap.get("board"), 0, 0, getWidth(), getHeight(), this);
			// Spielfeld
			for (int x = 0; x < 17; x++) {
				for (int y = 0; y < 12; y++) {
					switch (stageArray[x][y]) {
					case Stage.BLOCK:
						g.drawImage(bomberman.imageMap.get("block"), x * 50, y * 50, 50, 50, this);
						break;
					case Stage.GATE:
							g.drawImage(bomberman.imageMap.get("floor"), x * 50, y * 50, 50, 50, this);
						if(bomberman.playerCount == 1 ){
							g.drawImage(bomberman.imageMap.get("gate"), x * 50, y * 50, 50, 50, this);
						}
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
			if(bomberman.playerCount == 1 && !bomberman.inLevelEditor){
				p1 = (bomberman.player.maxBombs);
				r1 = (Bomb.radius1);
				lvl = (bomberman.level);
				con = (bomberman.Continue);
				g.setColor(Color.GREEN);
				Font font = new Font("Arial", Font.BOLD, 15);
				g.setFont(font);
				g.drawImage(bomberman.imageMap.get("player_right"),5, 595, 40, 40 ,null);
				g.drawImage(bomberman.imageMap.get("bomb"),50, 615, 25, 25 ,null);
				g.drawString("x" + p1, 55, 615);
				g.drawImage(bomberman.imageMap.get("explosion"),75, 615, 25, 25 ,null);
				g.drawString("" + r1, 83, 615);
				g.drawString("Continue Left: " + con, 5, 650);
				
				g.setColor(Color.WHITE);
				Font font2 = new Font("Arial", Font.BOLD, 55);
				g.setFont(font2);
				g.drawString("Level " + lvl, 357, 645);
				
				}
			// Anzeige in den Ecken
			if(bomberman.playerCount == 2 && !bomberman.inLevelEditor){
			p1 = (bomberman.player.maxBombs);
			p2 = (bomberman.player2.maxBombs);
			r1 = (Bomb.radius1);
			r2 = (Bomb.radius2);
			p1w = (bomberman.player1win);
			p2w = (bomberman.player2win);
			g.setColor(Color.WHITE);
			Font font = new Font("Arial", Font.BOLD, 55);
			g.setFont(font);
			g.drawString(p1w + "|" + p2w, 387, 640);
			if(p1w > p2w){
				g.setColor(Color.BLACK);
				g.drawString("_", 388, 638);
			}
			if(p1w < p2w){
				g.setColor(Color.BLACK);
				g.drawString("_", 434, 638);
			}
			Font font2 = new Font("Arial", Font.BOLD, 15);
			g.setFont(font2);
			if(!bomberman.player.isDead && !bomberman.inLevelEditor){
			g.setColor(Color.GREEN);
			g.drawImage(bomberman.imageMap.get("player_right"),5, 605, 40, 40 ,null);
			g.drawImage(bomberman.imageMap.get("bomb"),50, 620, 25, 25 ,null);
			g.drawString("x" + p1, 55, 620);
			g.drawImage(bomberman.imageMap.get("explosion"),75, 620, 25, 25 ,null);
			g.drawString("" + r1, 83, 620);
			}else {
				g.setColor(Color.RED);
				g.drawString("Player 1 is Dead", 5, 625);
				}
			

			if(!bomberman.player2.isDead && !bomberman.inLevelEditor){
			g.setColor(Color.CYAN);
			g.drawImage(bomberman.imageMap.get("player2_left"),805, 605, 40, 40 ,null);
			g.drawImage(bomberman.imageMap.get("bomb"),770, 620, 25, 25 ,null);
			g.drawString("x" + p2, 775, 620);
			g.drawImage(bomberman.imageMap.get("explosion"),745, 620, 25, 25 ,null);
			g.drawString("" + r2, 753, 620);
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
