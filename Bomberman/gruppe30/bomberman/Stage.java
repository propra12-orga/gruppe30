/**
 * @author Pavel Kopylov
 */
package gruppe30.bomberman;

import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Stage extends JPanel implements ActionListener, Runnable {
	
	JFrame f;
	AudioClip click;
	Sequencer sequencer;
	Sequencer sequencer2;
	Timer time;
	Image img;//stage
	Image img2; //gate
	Image img3;//bomb
	Image img4;//explosion
	Image img5;//block
	Bomberman b;
	Block block;
	Rectangle r = new Rectangle(760, 550, 60, 60);//gate
	
	public static boolean bomb;
	public static boolean explosion;
	public static int bx;
	public static int by;
	public static boolean bombInProgress;
	public static boolean explosionInProgress;
	
	//Rectangle bl = new Rectangle(board.length, board.length, 50, 50);//block
	
	public Stage(JFrame f) {
		
		bombInProgress = false;
		explosionInProgress = false;
		this.f = f;
		time = new Timer(5, this);
		addKeyListener(new AL());
		b = new Bomberman();
		ImageIcon i = new ImageIcon("res/Stage_ohne_Rand.png");
		img = i.getImage();
		ImageIcon i2 = new ImageIcon("res/Gate2.png");
		img2 = i2.getImage();
		ImageIcon i3 = new ImageIcon("res/block.jpg");
		img5 = i3.getImage();		
		time.start();
		setFocusable(true);
		
		img3 = new ImageIcon("res/b.png").getImage();
		img4 = new ImageIcon("res/b2.png").getImage();
		
		try {
			Sequence sequence = MidiSystem.getSequence(new File("res/play.mid"));
	        sequencer = MidiSystem.getSequencer();
	        sequencer.open();
	        sequencer.setSequence(sequence);
			} catch(IOException e){}
			catch (InvalidMidiDataException e){}
			catch (MidiUnavailableException e){}
		
		try {
			Sequence sequence = MidiSystem.getSequence(new File("res/play.mid"));
	        sequencer2 = MidiSystem.getSequencer();
	        sequencer2.open();
	        sequencer2.setSequence(sequence);
			} catch(IOException e){}
			catch (InvalidMidiDataException e){}
			catch (MidiUnavailableException e){}
		
		
	}
	
	public void actionPerformed(ActionEvent e) {
		
		b.move();
		testBomb();
		repaint();
		testWin();
		//testBlock();
	}
	
	public void paint(Graphics g) {
		g = (Graphics2D) g;
		///Hintergrund
		g.drawImage(img, 0, 0, null);
		/////Bomberman
		g.drawImage(b.getImage(), b.getX(), b.getY(), null);
		
		//////gate
		g.drawImage(img2, 750, 540, 60, 60, null);
		
		//// Rahmen
		for(int b = 0; b < 800; b+=50){
			
			g.drawImage(img5, b, 0, 50, 50, null);
			g.drawImage(img5, b, 600, 50, 50, null);
		}
		for(int b = 0; b < 650; b+=50){
			
			g.drawImage(img5, 0, b, 50, 50, null);
			g.drawImage(img5, 800, b, 50, 50, null);
		}
		
		/////Blöcke in der Mitte
		for(int a = 100; a < 800; a+=100)  
			for(int b = 100; b < 600; b+=100){
				g.drawImage(img5, a, b, 50, 50, null);
			}
		/////Bombe
		
		if(bomb){
			
			g.drawImage(img3, bx, by, 35, 35, null);
			//sequencer2.start();
			
			//*
		}
		if(explosion){
			g.drawImage(img4, bx-140, by-150, 300, 300, null);
			
			//sequencer.start();
			//*
			
		}
		
	}
	
	private void testWin(){
	
		if(b.getRect().intersects(r)){int eingabe = JOptionPane.showConfirmDialog(null ,"Noch ein Spiel?","GEWONNEN!",JOptionPane.YES_NO_CANCEL_OPTION);
		if(eingabe == 0) {
		
		/////new Frame: Stage
		f.dispose();
		f = new JFrame();
		f.add(new Stage(f));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setVisible(true);
		f.setSize(850+(f.getInsets().bottom*2),650+f.getInsets().top+f.getInsets().bottom);
		f.setLocationRelativeTo(null);

		}
		if(eingabe == 1) System.exit(0);
		if(eingabe == 2) {
		
		///// new Frame: Menu
		f.dispose();
		f = new JFrame();
		f.add(new Menu(f));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setVisible(true);
		f.setSize(850+(f.getInsets().bottom*2),650+f.getInsets().top+f.getInsets().bottom);
		f.setLocationRelativeTo(null);
		
		
		}
		} //*
	}
	
	private void testBlock(){//*
		
		//if(b.getRect().intersects(bl)) b.dx = 0; b.dy = 0;
	}
	
	private void testBomb(){
		if(bomb && !bombInProgress){
			bombInProgress = true;
			Bomb.explosion();
		}
		if(explosion && !explosionInProgress){
			explosionInProgress = true;
			Bomb.reset();
			
		}	
	}
	
	private class AL extends KeyAdapter {
		public void keyReleased(KeyEvent e){
			b.keyReleased(e);
		}
		public void keyPressed(KeyEvent e){
			b.keyPressed(e);
			}
		}

	public void run() {
		
		
	}
		
}