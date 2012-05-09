import java.awt.Color;
import java.awt.Font;

public class Menu {
	
	public static void start() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setXscale(0.0, 1000.0); 
        StdDraw.setYscale(0.0, 1000.0);
        StdDraw.setPenColor(Color.GREEN);
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 20));
        StdDraw.text(500, 550, "Bomberman");
        StdDraw.rectangle(500, 450, 60, 40);
        StdDraw.text(500, 450, "Start");
        StdDraw.text(900, 0, "© Gruppe 30");
        StdDraw.show();
        startcheck(); 
        }
	
	public static void startcheck() {
        while(true) {
                int mx = (int)StdDraw.mouseX();
                int my = (int)StdDraw.mouseY();
                if (mx>=440 && mx<=560 && my>=410 && my<=490 && StdDraw.mousePressed()){
                        StdDraw.show(100);
                        Spielfeld.start();
                        }}}

	public static void end() {
        StdDraw.clear(Color.BLACK); 
        StdDraw.text(500, 550, "Du hast gewonnen!"); 
        StdDraw.rectangle(400, 450, 100, 40);
        StdDraw.text(400, 450, "Nochmal"); 
        StdDraw.rectangle(600, 450, 100, 40);
        StdDraw.text(600, 450, "Beenden"); 
        StdDraw.text(900, 0, "© Gruppe 30");
        StdDraw.show();
        endcheck(); }
	
	public static void endcheck(){
        while(true) {
                int mx = (int)StdDraw.mouseX();
                int my = (int)StdDraw.mouseY();
                if (mx>=300 && mx<=500 && my>=410 && my<=490 && StdDraw.mousePressed()) {
                        StdDraw.show(100);
                        start(); }
                if (mx>500 && mx<=700 && my>=410 && my<=490 && StdDraw.mousePressed()) {
                        StdDraw.show(100);
                        System.exit(0);}}}
	
}
