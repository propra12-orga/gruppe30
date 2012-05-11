/**
 * @author Pavel Kopylov
 */
package gruppe30.bomberman;

import java.applet.AudioClip;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Menu extends JPanel implements ActionListener {
	
	AudioClip click;
	Image img = Toolkit.getDefaultToolkit().getImage("res/bomb.jpg");
	
	
	

	private JMenuItem newGame = new JMenuItem("New Game");
	private JMenuItem exit = new JMenuItem("Exit");
	private JMenuItem gruppe30 = new JMenuItem("Gruppe30");
	JFrame f;

	Sequencer sequencer;
	
	public Menu(JFrame f) {
		
		this.f = f;
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setIconImage(img);
		
		
		try {
		f.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("res/Bom.jpg")))));
		
		
		Sequence sequence = MidiSystem.getSequence(new File("res/play.mid"));
		
        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.setSequence(sequence);
        
        sequencer.start();
        sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        
		
		} catch(IOException e){}
		catch (InvalidMidiDataException e){}
		catch (MidiUnavailableException e){}
		
		
		
		
		JMenuBar menuBar = new JMenuBar();
		f.setJMenuBar(menuBar);

		JMenu menu = new JMenu("Menu");
		JMenu about = new JMenu("About");
		menuBar.add(menu);
		menuBar.add(about);
		menu.add(newGame);
		menu.add(exit);
		about.add(gruppe30);
		
		newGame.addActionListener(this);
		exit.addActionListener(this);
		gruppe30.addActionListener(this);

		f.pack();
		f.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newGame){
			
			
			f.dispose();
			f = new JFrame();
			f.add(new Stage(f));
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setResizable(false);
			f.setVisible(true);
			f.setSize(850+(f.getInsets().bottom*2),650+f.getInsets().top+f.getInsets().bottom);
			f.setLocationRelativeTo(null);
			
			}
		if (e.getSource() == exit){System.exit(0);}
		if (e.getSource() == gruppe30){JOptionPane.showMessageDialog(null, "Message");}
	}
}