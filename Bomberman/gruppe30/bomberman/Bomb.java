/**
 * @author Pavel Kopylov
 */
package gruppe30.bomberman;

import java.util.*;


public class Bomb {
	
	public static void explosion(){
		Stage.bombInProgress = true;
		Timer t1 = new Timer();
		t1.schedule(new Explosion(), 2000);
	}
	
	public static void reset(){
		Stage.explosionInProgress = true;
		Timer t2 = new Timer();
		t2.schedule(new Bombreset(), 2000);
	}

}

class Explosion extends TimerTask {
	public void run() {
		Stage.explosion = true;
		Stage.bomb = false;
	}
}

class Bombreset   extends TimerTask {
    public void run() {
    	Stage.bombInProgress = false;
    	Stage.explosionInProgress = false;
    	Stage.bomb = false;
    	Stage.explosion = false;
  }
}
