
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.*;
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
	
	int focused;
	Bomberman bomberman;
	
	public char[][] stageArray;
	public boolean[][] way;
	public Stage(Bomberman bomberman) {
		this.bomberman = bomberman;
		setSize(850, 650);
		setPreferredSize(getSize());
		
		stageArray = new char[17][13];
		focused = 0;
	}
	
	/**
	 * Lädt ein Spielfeld aus einer Datei
	 * @param filename
	 */
	public void loadStage(String filename) {
		try {
			
			BufferedReader in = new BufferedReader(new FileReader(filename));//InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename)));
			for (int y = 0; y < 16; y++) {
				String line = in.readLine();
				if (y == 12) bomberman.player.maxBombs =  Integer.parseInt(line);
				else if (y == 13) Bomb.radius1 = Integer.parseInt(line);
				else if (y == 14) bomberman.Continue = Integer.parseInt(line);
				else if (y == 15) bomberman.level = Integer.parseInt(line);
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
			in.close();
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
		ps.println();
		ps.print(bomberman.Continue);
		ps.println();
		ps.print(bomberman.level);
		ps.flush();
		ps.close();	
		
	}
	/**
	 * Speichert das Level
	 * @param filename Name des Levels
	 * @throws IOException
	 */
	public void saveLevel(String filename) throws IOException {
		File save = new File(filename);
		FileWriter out = new FileWriter(save);
		Writer bw = new BufferedWriter(out);
		PrintWriter ps = new PrintWriter(bw);
		for (int y = 0; y < 12; y++) {
			for (int x = 0; x < 17; x++) {
					ps.print(stageArray[x][y]);
					if(x==16) ps.println();
			}		
		}
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
	
	
	/**
	 * Überprüft, ob alle vorraussetzungen für ein level erfüllt sind
	 * @return
	 */
	public boolean validateField(){
		boolean player1set = false;
		boolean player2set = false;
		boolean gateset = false;
		boolean exitReachable = false;
		for(int x = 0; x < 17; x++ ){
			for(int y = 0; y < 13; y++ ){
				if(stageArray[x][y] == 'p')
					player1set = true;
				else if(stageArray[x][y] == 'l')
					player2set = true;
				else if(stageArray[x][y] == 'g' || stageArray[x][y] == 'x' ){
					way = new boolean[17][13];
					int result = exitReachable(x,y,0);
					if(result >= 2){
						exitReachable = true;
					}
					gateset = true;
				}
			}
		}
		if(player1set == true && player2set == true && gateset == true && exitReachable == true)
			return true;
		else return false;
	}
	
	/**
	 * überprüft ob das exit von beiden spieler erreicht werden kann
	 * @param x, x position des gates im array feld
	 * @param y, y position des gates im array feld
	 * @param result, gibt an ob schon ein spieler gefunden wurde
	 * @return
	 */
	
	public int exitReachable(int x, int y, int result){
		way[x][y] = true;
		if(stageArray[x][y] == 'p' || stageArray[x][y] == 'l'){
			result++;
			return result;
		}else{
		for(int i=-1; i <=1 ; i+=2){
			int x2 = x+i;
			int y2 = y+i;
			if(y2 > 0 && y2 < 13 && stageArray[x][y2] != 'a' && way[x][y2] != true){
				result += exitReachable(x, y2, result);
					
			}
			if(x2>0 && x2 < 17 && stageArray[x2][y] != 'a' && way[x2][y] != true){
				result += exitReachable(x2, y, result);
				
			}
		}
		return result;
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
			if(bomberman.host){
				try {
					char stage1[] = new char[17];
					for(int y=0; y<12;y++){
						for(int x=0; x<17;x++){
							stage1[x]=stageArray[x][y];
							bomberman.sout.writeChar(stage1[x]);
						}
					}
					bomberman.sout.writeInt(bomberman.player.position.x);
					bomberman.sout.writeInt(bomberman.player.position.y);
					bomberman.sout.writeInt(bomberman.player.maxBombs);
					bomberman.sout.writeInt(Bomb.radius1);
					bomberman.sout.writeBoolean(bomberman.player.isDead);
					bomberman.sout.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					char stages[][] = new char[17][13];
					for(int y=0; y<12;y++){
						for(int x=0; x<17;x++){
							stages[x][y] = bomberman.sin.readChar();
							if(stages[x][y]==0 || stageArray[x][y]==0)
								stageArray[x][y] = 0;
							else if(stages[x][y]=='z' || stageArray[x][y]=='z')
								stageArray[x][y] = 'z';
							else if(stages[x][y]=='u' || stageArray[x][y]=='u')
								stageArray[x][y] = 'u';
							else stageArray[x][y] = stages[x][y];
						}
					}
					bomberman.player2.position.x = bomberman.sin.readInt();
					bomberman.player2.position.y = bomberman.sin.readInt();
					bomberman.player2.maxBombs = bomberman.sin.readInt();
					Bomb.radius2 = bomberman.sin.readInt();
					bomberman.player2.isDead = bomberman.sin.readBoolean();
					}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(bomberman.client){
				try {
					char stage2[] = new char[17];
					for(int y=0; y<12;y++){
						for(int x=0; x<17;x++){
							stage2[x]=stageArray[x][y];
							bomberman.cout.writeChar(stage2[x]);
						}
					}
					bomberman.cout.writeInt(bomberman.player2.position.x);
					bomberman.cout.writeInt(bomberman.player2.position.y);
					bomberman.cout.writeInt(bomberman.player2.maxBombs);
					bomberman.cout.writeInt(Bomb.radius2);
					bomberman.cout.writeBoolean(bomberman.player2.isDead);
					bomberman.cout.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					char stagec[][] = new char[17][13];
					for(int y=0; y<12;y++){
						for(int x=0; x<17;x++){
							stagec[x][y] = bomberman.cin.readChar();
							if(stagec[x][y]==0 || stageArray[x][y]==0)
								stageArray[x][y] = 0;
							else if(stagec[x][y]=='z' || stageArray[x][y]=='z')
								stageArray[x][y] = 'z';
							else if(stagec[x][y]=='u' || stageArray[x][y]=='u')
								stageArray[x][y] = 'u';
							else stageArray[x][y] = stagec[x][y];
						}
					}
					bomberman.player.position.x = bomberman.cin.readInt();
					bomberman.player.position.y = bomberman.cin.readInt();
					bomberman.player.maxBombs = bomberman.cin.readInt();
					Bomb.radius1 = bomberman.cin.readInt();
					bomberman.player.isDead = bomberman.cin.readBoolean();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
						if(bomberman.inLevelEditor){
							g.drawImage(bomberman.imageMap.get("gate"), x * 50, y * 50, 50, 50, this);
						}
						break;
					case Stage.BOX:
						g.drawImage(bomberman.imageMap.get("box"), x * 50, y * 50, 50, 50, this);
						break;
					case Stage.BOXGATE:
						if(bomberman.inLevelEditor){
							g.drawImage(bomberman.imageMap.get("boxgate"), x * 50, y * 50, 50, 50, this);
						}else
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
					case Stage.PLAYER:
						g.drawImage(bomberman.imageMap.get("floor"), x * 50, y * 50, 50, 50, this);
						g.drawImage(bomberman.imageMap.get("player_left"), x * 50, y * 50, 50, 50, this);
						break;
					case Stage.PLAYER2:
						g.drawImage(bomberman.imageMap.get("floor"), x * 50, y * 50, 50, 50, this);
						g.drawImage(bomberman.imageMap.get("player2_left"), x * 50, y * 50, 50, 50, this);
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
			// Untere Leiste während des leveleditors
			if(bomberman.inLevelEditor){
				g.drawImage(bomberman.imageMap.get("box"), 5 ,605 ,40 ,40 , null);
				g.drawImage(bomberman.imageMap.get("block"), 55 ,605 ,40 ,40 , null);
				g.drawImage(bomberman.imageMap.get("floor"), 105 ,605 ,40 ,40 , null);
				g.drawImage(bomberman.imageMap.get("player_left"), 155 ,605 ,40 ,40 , null);
				g.drawImage(bomberman.imageMap.get("player2_left"), 205 ,605 ,40 ,40 , null);
				g.drawImage(bomberman.imageMap.get("gate"), 255,605 ,40 ,40, null);
				
				g.fillRect(760, 605, 60, 40);
				g.setColor(Color.WHITE);
				Font font3 = new Font("Arial", Font.BOLD, 20);
				g.setFont(font3);
				g.drawString("Save",766 ,630);
				
				// Umwandung des ausgewählen buttons
				g.setColor(Color.RED);
				switch(focused){
				case 1:
					g.drawRect(5, 605, 40, 40);
					break;
				case 2:
					g.drawRect(55, 605, 40, 40);
					break;
				case 3:
					g.drawRect(105, 605, 40, 40);
					break;
				case 4:
					g.drawRect(155, 605, 40, 40);
					break;
				case 5:
					g.drawRect(205, 605, 40, 40);
					break;
				case 6:
					g.drawRect(255, 605, 40, 40);
					break;
					
			}
				g.setColor(Color.BLACK);
				
				
			}
		}

		
		g.dispose();
		
		// Doublebuffering
		graphics.drawImage(buffer, 0, 0, getWidth(), getHeight(), this);
	}
	
	
}
