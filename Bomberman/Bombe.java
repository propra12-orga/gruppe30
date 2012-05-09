import java.util.*;

class Explosion   extends TimerTask {
    public void run() {
    	Spielfeld.explosion = true;
  }
}

class Bombreset   extends TimerTask {
    public void run() {
    	Spielfeld.bombe = false;
    	Spielfeld.explosion = false;
  }
}

public class Bombe {

	public static void explosion() {
		Timer timer1 = new Timer();
		timer1.schedule (new Explosion(), 5000);
	}
	
	public static void reset() {
		Timer timer2 = new Timer();
		timer2.schedule (new Bombreset(), 2000);
	}
	
}
