/**
 * @author Pavel Kopylov
 */
package gruppe30.bomberman;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Block {
	
	int x, y;
	Image img;
	Stage stage;

	public Rectangle getRect() {
		return new Rectangle(x, y, 50, 50);
	}
	
	public Block(int x, int y, int v, Stage stage) {
		this.x = x;
		this.y = y;
		this.stage = stage;
		
		ImageIcon i = new ImageIcon("res/block.jpg");
		img = i.getImage();
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public Image getImage(){
		return img;

	}
	
}
