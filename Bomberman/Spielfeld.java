import java.awt.Color;
import java.awt.Rectangle;

public class Spielfeld {
	
	public static boolean bombe;
	public static boolean explosion;
	public static int bx;
	public static int by;
	public static Rectangle oben = new Rectangle(0, 920, 1000, 1);
	public static Rectangle unten = new Rectangle(0, 80, 1000, 1);
	public static Rectangle links = new Rectangle(80, 0, 1, 1000);
	public static Rectangle rechts = new Rectangle(920, 0, 1, 1000);
	public static Rectangle ziel = new Rectangle(875, 75, 50, 50);

	public static void start() {
        StdDraw.clear(Color.BLACK);
        //StdDraw.show();
        //feld();
        //mauer();
        //ziel();
        Spieler1.start();
        StdDraw.show();
	}
	
    public static void feld() {
        StdDraw.clear(Color.BLACK);
        StdDraw.text(500, 1000, "Bomberman");
        int i = 50;
    	while(i < 975) {
        StdDraw.rectangle(50, i, 25, 25);
        StdDraw.rectangle(950, i, 25, 25);
        StdDraw.rectangle(i, 50, 25, 25);
        StdDraw.rectangle(i, 950, 25, 25);
        i = i+50;
        }
    	if (bombe) {
    		StdDraw.filledCircle(bx+15, by+15, 10);
    		Bombe.explosion();
    	}
    	if (explosion) {
    		StdDraw.filledRectangle(bx+15, by+15, 60, 15);
    		StdDraw.filledRectangle(bx+15, by+15, 15, 60 );
    		Bombe.reset();
    	}
    	/*int j = 150;
    	while(j < 975) {
        StdDraw.rectangle(150, j, 25, 25);
        StdDraw.rectangle(250, j, 25, 25);
        StdDraw.rectangle(350, j, 25, 25);
        StdDraw.rectangle(450, j, 25, 25);
        StdDraw.rectangle(550, j, 25, 25);
        StdDraw.rectangle(650, j, 25, 25);
        StdDraw.rectangle(750, j, 25, 25);
        StdDraw.rectangle(850, j, 25, 25);
        j = j+100;
        }*/
    	
    	
    }
    
    public static void mauer() {
    	StdDraw.filledRectangle(500, 500, 25, 25);
    }
    
    public static void ziel() {
    	StdDraw.filledRectangle(900, 100, 25, 25);
    }
    
}
