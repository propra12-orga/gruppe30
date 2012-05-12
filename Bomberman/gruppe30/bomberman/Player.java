package gruppe30.bomberman;

import java.awt.Image;
import java.awt.Point;

public class Player {
	Point position;
	Bomberman bomberman;
	Image image;
	boolean isDead;
	public Player(Bomberman bomberman) {
		this.position = new Point(0, 0);
		this.bomberman = bomberman;
		this.image = bomberman.imageMap.get("player_right");
	}
	
	/**
	 * Spieler nach links bewegen
	 */
	public void moveLeft() {
		this.image = bomberman.imageMap.get("player_left");
		Point point;
		for (int i : new int[] {0, -10, 10, -20, 20}) {
			point = new Point(this.position.x - 10, this.position.y + i);
			if (canPlayerMoveTo(point)) {
				this.position = point;
				break;
			}
		}
	}
	
	/**
	 * Spieler nach rechts bewegen
	 */
	public void moveRight() {
		Point point;
		for (int i : new int[] {0, -10, 10, -20, 20}) {
			point = new Point(this.position.x + 10, this.position.y + i);
			if (canPlayerMoveTo(point)) {
				this.position = point;
				break;
			}
		}
		this.image = bomberman.imageMap.get("player_right");
	}
	
	/**
	 * Spieler nach oben bewegen
	 */
	public void moveUp() {
		Point point;
		for (int i : new int[] {0, -10, 10, -20, 20}) {
			point = new Point(this.position.x + i, this.position.y - 10);
			if (canPlayerMoveTo(point)) {
				this.position = point;
				break;
			}
		}
	}
	
	/**
	 * Spieler nach unten bewegen
	 */
	public void moveDown() {
		Point point;
		for (int i : new int[] {0, -10, 10, -20, 20}) {
			point = new Point(this.position.x + i, this.position.y + 10);
			if (canPlayerMoveTo(point)) {
				this.position = point;
				break;
			}
		}
	}
	
	/**
	 * Prüft ob der Spieler sich zu dem Punkt p bewegen darf
	 * @param p Punkt
	 * @return
	 */
	public boolean canPlayerMoveTo(Point p) {
		if (bomberman.stage.isPointOnField(new Point(p.x / 50, p.y / 50), 'a') ||
			bomberman.stage.isPointOnField(new Point((p.x + 49) / 50, p.y / 50), 'a') ||
			bomberman.stage.isPointOnField(new Point((p.x + 49) / 50, (p.y + 49) / 50), 'a') ||
			bomberman.stage.isPointOnField(new Point(p.x / 50, (p.y + 49) / 50), 'a'))
			return false;
		return true;
	}
	
	/**
	 * Gibt die Feld-Position des Spielers zurück
	 * @return
	 */
	public Point getStagePosition() {
		return new Point((position.x + 25) / 50, (position.y + 25) / 50);
	}
	
	/**
	 * Setzt einen Spieler auf die Feld-Position p
	 * @param p Feld-Position
	 */
	public void setStagePosition(Point p) {
		this.position = new Point(p.x * 50, p.y * 50);
	}
}
