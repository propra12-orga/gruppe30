

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Bomb {
	Point position;
	int count;
	Bomberman bomberman;
	boolean isVisible;
	boolean isExploded;
	int radius;
	static int radius1 = 4;
	static int radius2 = 4;
	List<Point> explosionArray;
	Player owner;
	
	public Bomb(Bomberman bomberman) {
		this.bomberman = bomberman;
	}
	
	/**
	 * Verarbeitet die Aktionen der Bombe
	 */
	public void process() {
		if (isVisible) {
			if (count == 0) {
				if(bomberman.sound) bomberman.playSound(bomberman.soundMap.get("ticktock"));
			}
			if (count == 2000 / 25 /* 2 Sekunden */) {
				explosionArray = getExplosionArray();
				isExploded = true;
				if(bomberman.sound) bomberman.playSound(bomberman.soundMap.get("bomb"));
			}
			if (count == 3000 / 25 /* 3 Sekunden */) {
				isVisible = false;
				owner.setActiveBombs(owner.getActiveBombs()-1);
				Point origin = new Point(getExplosionArray().get(0));
				
				// Bomben kollision
				for (int angle = 0; angle < 360; angle += 90) {
					double radian = Math.toRadians(angle);
					int x = (int)Math.cos(radian);
					int y = (int)Math.sin(radian);
					for(int i = 1; i <= radius; i++) {
						Point p = new Point(origin.x + x * i, origin.y + y * i);
						if (origin.x + x * i >= 0 && origin.x + x * i <= 16 && origin.y + y * i >= 0 && origin.y + y * i<= 12) {
							if(bomberman.stage.isPointOnField(new Point(p), Stage.BLOCK)){
								break;
							}
							if(bomberman.stage.isPointOnField(new Point(p), Stage.BOX)){
								bomberman.stage.destroyBox(new Point(p));
								break;
							}
							if(bomberman.stage.isPointOnField(new Point(p), Stage.BOXGATE)){
								bomberman.stage.destroyBox(new Point(p));
								break;
							}
							if(bomberman.stage.isPointOnField(new Point(p), Stage.BOMBUP)){
								bomberman.stage.destroyPU(new Point(p));
								break;
							}
							if(bomberman.stage.isPointOnField(new Point(p), Stage.POWERUP)){
								bomberman.stage.destroyPU(new Point(p));
								break;
							}
						}
					}
				}
			}
			count++;
		}
	}
	
	/**
	 * Legt eine Bombe auf die Feld-Position auf der der Spieler1 steht
	 */
	public void setToPlayerPos() {
		position = new Point(bomberman.player.getStagePosition());
		count = 0;
		isVisible = true;
		isExploded = false;
		radius = radius1;
	}
	
	/**
	 * Legt eine Bombe auf die Feld-Position auf der der Spieler2 steht
	 */
	public void setToPlayer2Pos() {
		position = new Point(bomberman.player2.getStagePosition());
		count = 0;
		isVisible = true;
		isExploded = false;
		radius = radius2;
	}
	
	/**
	 * pr�ft ob die Bombe auf dem feld an Punkt p gesetzt werden kann oder ob dort schon eine bombe liegt
	 * @param p Punkt an dem grpr�ft wird
	 * @return
	 */
	public boolean canLayOn(Point p){
		boolean erg = true;
		for (Bomb bomb : bomberman.bombList) {
			if (p.equals(bomb.position) && bomb.isVisible) {
				erg = false;
				break;
			}
		}
		return erg;
	}
	
	/**
	 * Pr�ft ob an der Feld-Position p eine Explosion sichtbar sein darf
	 * @param p Feld-Position
	 * @return
	 */
	
	public boolean canExplodeTo(Point p) {
		if (
				bomberman.stage.isPointOnField(p, Stage.BLOCK) || 
				bomberman.stage.isPointOnField(p, Stage.BOX) || 
				bomberman.stage.isPointOnField(p, Stage.BOXGATE) ||
				bomberman.stage.isPointOnField(p, Stage.BOMBUP) ||
				bomberman.stage.isPointOnField(p, Stage.POWERUP)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Setzt den Besitzer der Bombe
	 * @param player Player, der die Bombe gelegt hat
	 */
	
	public void setOwner(Player player){
	this.owner = player;	
	}
	
	/**
	 * Erstellt ein Array der Feld-Positionen an denen eine Explosion sichtbar sein wird
	 * @return
	 */
	public List<Point> getExplosionArray() {
		List<Point> explosionArray = new ArrayList<Point>();
		if (radius > 0)
			explosionArray.add(position);
		
		for (int angle = 0; angle < 360; angle += 90) {
			double radian = Math.toRadians(angle);
			int x = (int)Math.cos(radian);
			int y = (int)Math.sin(radian);
		
			for (int i = 1; i < radius; i++) {
				Point p = new Point(position.x + x * i, position.y + y * i);
				if (canExplodeTo(new Point(p))) {
					explosionArray.add(new Point(p));
				}
				else {
					break;
				}
			}
		}
	
		return explosionArray;
	}
}
