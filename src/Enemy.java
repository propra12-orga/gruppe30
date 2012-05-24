
import java.awt.Image;
import java.awt.Point;

public class Enemy {
	Point position;
	Bomberman bomberman;
	Image image;
	boolean exists;
	
	public Enemy(Bomberman bomberman) {
		this.position = new Point(0, 0);
		this.bomberman = bomberman;
		this.image = bomberman.imageMap.get("cage");
	}

	/**
	 * Prüft ob der Gegner sich zu dem Punkt z bewegen darf
	 * @param z Punkt
	 * @return
	 */
	public boolean canPlayerMoveTo(Point z) {
		if (bomberman.stage.isPointOnField(new Point(z.x / 50, z.y / 50), 'a') ||
			bomberman.stage.isPointOnField(new Point((z.x + 49) / 50, z.y / 50), 'a') ||
			bomberman.stage.isPointOnField(new Point((z.x + 49) / 50, (z.y + 49) / 50), 'a') ||
			bomberman.stage.isPointOnField(new Point(z.x / 50, (z.y + 49) / 50), 'a') || 
			
			bomberman.stage.isPointOnField(new Point(z.x / 50, z.y / 50), 'b') ||
			bomberman.stage.isPointOnField(new Point((z.x + 49) / 50, z.y / 50), 'b') ||
			bomberman.stage.isPointOnField(new Point((z.x + 49) / 50, (z.y + 49) / 50), 'b') ||
			bomberman.stage.isPointOnField(new Point(z.x / 50, (z.y + 49) / 50), 'b'))
			return false;
		return true;
	}
	
	/**
	 * Gibt die Feld-Position des Gegners zurück
	 * @return
	 */
	public Point getStagePosition() {
		return new Point((position.x + 25) / 50, (position.y + 25) / 50);
	}
	
	/**
	 * Setzt einen Gegner auf die Feld-Position g
	 * @param g Feld-Position
	 */
	public void setStagePosition(Point z) {
		this.position = new Point(z.x * 50, z.y * 50);
	}
}

