import java.awt.Rectangle;

public class Spieler1{
	
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
    		if (spieler1.intersects(Spielfeld.ziel)) Menu.end();
    		StdDraw.filledRectangle(spieler1.x+15, spieler1.y+15, 15, 15);
    		StdDraw.show(20);
    		}
    	}
    }