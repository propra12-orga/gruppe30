package gruppe30.bomberman;

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
	List<Point> explosionArray;
	public Bomb(Bomberman bomberman) {
		this.bomberman = bomberman;
	}
	
	/**
	 * Verarbeitet die Aktionen der Bombe
	 */
	public void process() {
		if (isVisible) {
			if (count == 0) {
				bomberman.playSound(bomberman.soundMap.get("ticktock"));
			}
			if (count == 2000 / 25 /* 2 Sekunden */) {
				explosionArray = getExplosionArray();
				isExploded = true;
				bomberman.playSound(bomberman.soundMap.get("bomb"));
			}
			if (count == 3000 / 25 /* 3 Sekunden */) {
				isVisible = false;
			}
			count++;
		}
	}
	
	/**
	 * Legt eine Bombe auf die Feld-Position auf der der Spieler steht
	 */
	public void setToPlayerPos() {
		position = new Point(bomberman.player.getStagePosition());
		count = 0;
		isVisible = true;
		isExploded = false;
		radius = 4;
	}
	
	/**
	 * Pr�ft ob an der Feld-Position p eine Explosion sichtbar sein darf
	 * @param p Feld-Position
	 * @return
	 */
	public boolean canExplodeTo(Point p) {
		if (bomberman.stage.isPointOnField(p, Stage.BLOCK)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Erstellt ein Array der Feld-Positionen an denen eine Explosion sichtbar sein wird
	 * @return
	 */
	public List<Point> getExplosionArray() {
		List<Point> explosionArray = new ArrayList<Point>();
		if (radius > 0)
			explosionArray.add(position);
		for (int i = 1; i < radius; i++) {
			if (canExplodeTo(new Point(position.x + i, position.y))) {
				explosionArray.add(new Point(position.x + i, position.y));
			}
			else {
				break;
			}
		}
		
		for (int i = 1; i < radius; i++) {
			if (canExplodeTo(new Point(position.x - i, position.y))) {
				explosionArray.add(new Point(position.x - i, position.y));
			}
			else {
				break;
			}
		}
		
		for (int i = 1; i < radius; i++) {
			if (canExplodeTo(new Point(position.x, position.y + i))) {
				explosionArray.add(new Point(position.x, position.y + i));
			}
			else {
				break;
			}
		}
		
		for (int i = 1; i < radius; i++) {
			if (canExplodeTo(new Point(position.x, position.y - i))) {
				explosionArray.add(new Point(position.x, position.y - i));
			}
			else {
				break;
			}
		}
		
		return explosionArray;
	}
}
