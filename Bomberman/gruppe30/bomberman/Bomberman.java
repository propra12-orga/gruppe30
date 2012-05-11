/**
 * @author Pavel Kopylov
 */
package gruppe30.bomberman;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Bomberman {
	int x, dx, y, dy;
	int schritt;
	Image monster;
	Image monster1;
	Image monster2;
	
	
	public static final int MAX_TOP = 54;
	public static final int MAX_BOTTOM = 554;
	public static final int MAX_LEFT = 55;
	public static final int MAX_RIGHT = 755;
	
	public Rectangle getRect() {
		return new Rectangle(x, y, 40, 46);
		
	}
	
	public Bomberman(){
		
		ImageIcon i = new ImageIcon("res/m1.jpg");
		monster = i.getImage();
		
		ImageIcon i2 = new ImageIcon("res/m2.jpg");
		monster2 = i2.getImage();
		
		ImageIcon i3 = new ImageIcon("res/m1.jpg");
		monster1 = i3.getImage();
		
		schritt = 50;
		
		x = 55;
		y = 54;
	}
	
	public void move(){	
		if (x <= MAX_LEFT) x = MAX_LEFT;
		if (x >= MAX_RIGHT) x = MAX_RIGHT;
		
		if (y <= MAX_TOP) y = MAX_TOP;
		if (y >= MAX_BOTTOM) y = MAX_BOTTOM;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public Image getImage(){
		return monster;

	}
	
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_LEFT && checkMiddle("left")) { 
			x -=schritt;
			monster = monster2;
		}
		
		if(key == KeyEvent.VK_RIGHT && checkMiddle("right")) {		
			x+=schritt;
			monster = monster1;
		}
		
		if(key == KeyEvent.VK_UP && checkMiddle("up")){ 
			y-=schritt;

		
		}
		
		if(key == KeyEvent.VK_DOWN && checkMiddle("down")){ 
			y+=schritt;
		
		
		}
		
		}
	
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_B && Stage.bomb == false && Stage.explosion == false){
			Stage.bomb = true;
			Stage.bx = x;
			Stage.by = y;
			}
		/*
		if(key == KeyEvent.VK_LEFT) dx = 0;
		
		if(key == KeyEvent.VK_RIGHT) dx = 0;
		
		if(key == KeyEvent.VK_UP) dy = 0;
		
		if(key == KeyEvent.VK_DOWN) dy = 0;
		*/
	}
	
	public boolean checkMiddle(String a){
		if(a.equals("up") ){
			if((x-5) % 100 == 50) return true;
			else return false;
			
		}
		if(a.equals("down") ){
			if((x-5) % 100 == 50) return true;
			else return false;
		}else
		if(a.equals("left")){
			if((y-4) % 100 == 50) return true;
			else return false;
		}else
		if(a.equals("right")){
			if((y-4) % 100 == 50) return true;
			else return false;
		}
		else return true;
		
	}
	
}