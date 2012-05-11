/**
 * @author Pavel Kopylov
 */
package gruppe30.bomberman;


import javax.swing.*;

public class Main {

	public static void main(String[] args) {

		JFrame f = new JFrame("BOMBERMAN");
		f.add(new Menu(f));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		f.setResizable(false);
		
		f.setVisible(true);
		f.setSize(850+(f.getInsets().bottom*2),650+f.getInsets().top+f.getInsets().bottom);
		f.setLocationRelativeTo(null);
		//////check window size////////
		/*System.out.println("Frame Size   : " + f.getSize() );
        System.out.println("Frame Insets : " + f.getInsets() );
        System.out.println("Content Size : " + f.getContentPane().getSize() );*/

	}

}
