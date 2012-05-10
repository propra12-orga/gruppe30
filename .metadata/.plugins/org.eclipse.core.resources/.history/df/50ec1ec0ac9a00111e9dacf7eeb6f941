import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Spieler1 {
	
	static int key;

    static boolean up;
	static boolean down;
	static boolean right;
	static boolean left;
	static Rectangle spieler1 = new Rectangle(100, 900, 30, 30);

    public static void start(){
    	spieler1.x = 85;
    	spieler1.y = 885;
    	StdDraw.filledRectangle(spieler1.x+15, spieler1.y+15, 15, 15);
    	StdDraw.show();
    	move();
    }
    
    public static void move(){
    	while(true){
    		up = false;
    		down = false;
    		left = false;
    		right = false;
    		StdDraw.clear();
    		Spielfeld.feld();
    		Spielfeld.ziel();
    		//Spielfeld.mauer();
    		/////KeyListener neu mit Key pressed//////
    		if(StdDraw.isKeyPressed(KeyEvent.VK_W)) if(!spieler1.intersects(Spielfeld.oben) && intersectsUp()) spieler1.y = spieler1.y + 5; 
    		if(StdDraw.isKeyPressed(KeyEvent.VK_S)) if(!spieler1.intersects(Spielfeld.unten)&& intersectsDown()) spieler1.y = spieler1.y - 5;
    		if(StdDraw.isKeyPressed(KeyEvent.VK_A)) if(!spieler1.intersects(Spielfeld.links)&& intersectsLeft()) spieler1.x = spieler1.x - 5;
    		if(StdDraw.isKeyPressed(KeyEvent.VK_D)) if(!spieler1.intersects(Spielfeld.rechts)&& intersectsRight()) spieler1.x = spieler1.x + 5;
    		if (StdDraw.hasNextKeyTyped() && StdDraw.nextKeyTyped() == 'b' && Spielfeld.bombe == false && Spielfeld.explosion == false) {
            	Spielfeld.bombe = true;
            	Spielfeld.bx = spieler1.x;
            	Spielfeld.by = spieler1.y;
            	}
    		if (spieler1.intersects(Spielfeld.ziel)) Menu.end();
    			
    		///// KeyListener alt, mit Keytyped//////////
    		/*
    		if (StdDraw.hasNextKeyTyped()) {
                char ch = StdDraw.nextKeyTyped();
                if (ch == 's') { up = false; down = true;}
                if (ch == 'w') { up = true; down = false;}
                if (ch == 'a') { left = true; right = false;}
                if (ch == 'd') { left = false; right = true;}
                if (ch == 'b' && Spielfeld.bombe == false && Spielfeld.explosion == false) {
                	Spielfeld.bombe = true;
                	Spielfeld.bx = spieler1.x;
                	Spielfeld.by = spieler1.y;
                	}
                }
    		if (up && !spieler1.intersects(Spielfeld.oben)) spieler1.y = spieler1.y + 10;                    //wandkollision
    		if (down && !spieler1.intersects(Spielfeld.unten)) spieler1.y = spieler1.y - 10;
    		if (left && !spieler1.intersects(Spielfeld.links)) spieler1.x = spieler1.x - 10;
    		if (right && !spieler1.intersects(Spielfeld.rechts)) spieler1.x = spieler1.x + 10;
    		if (spieler1.intersects(Spielfeld.ziel)) Menu.end();*/
    		StdDraw.filledRectangle(spieler1.x+15, spieler1.y+15, 15, 15);
    		StdDraw.show(20);
    		}
    	
    	}
    	//checkt ob bewegung nach oben möglich ist
    	public static boolean intersectsUp(){
    		for(int y= 125 ; y<= 825; y +=100){
    			if(spieler1.y+30 >= y && spieler1.y+30 <= y+50 ){
    				for(int x = 125; x <= 825 ; x+=100  ){
    					if(spieler1.x  > x && spieler1.x  < (x+50) || spieler1.x + 30 > x && spieler1.x + 30 < (x+50)  ){
    						return false;
    					}
    				}
    			}
    		}
    		
    		return true;
    	}
    	//checkt ob bewegung nach unten möglich ist
    	public static boolean intersectsDown(){
    		for(int y= 125 ; y<= 825; y +=100){
    			if(spieler1.y <= y+50 && spieler1.y >= y ){
    				for(int x = 125; x <= 825 ; x+=100  ){
    					if(spieler1.x  > x && spieler1.x  < (x+50) || spieler1.x + 30 > x && spieler1.x + 30 < (x+50)  ){
    						return false;
    					}
    				}
    			}
    		}
    		return true;
    	}
    	//checkt ob bewegung nach rechts möglich ist
    	public static boolean intersectsRight(){
    		for(int x= 125 ; x<= 825; x +=100){
    			if(spieler1.x+30 >= x && spieler1.x+30 <= x+50 ){
    				for(int y = 125; y <= 825 ; y+=100  ){
    					if(spieler1.y  > y && spieler1.y  < (y+50) || spieler1.y + 30 > y && spieler1.y + 30 < (y+50)  ){
    						return false;
    					}
    				}
    			}
    		}
    		return true;
    	}
    	//checkt ob bewegung nach Links möglich ist
    	public static boolean intersectsLeft(){
    		for(int x= 125 ; x<= 825; x +=100){
    			if(spieler1.x >= x && spieler1.x <= x+50 ){
    				for(int y = 125; y <= 825 ; y+=100  ){
    					if(spieler1.y  > y && spieler1.y  < (y+50) || spieler1.y + 30 > y && spieler1.y + 30 < (y+50)  ){
    						return false;
    					}
    				}
    			}
    		}
    		return true;
    	}
    }