package ezxpns;
import ezxpns.GUI.*;
import java.awt.EventQueue;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					/* Essentially the following code starts the GUI */
					MainScreen main = new MainScreen();
					main.showScreen();
					/* The following is for testing, GUI Trial and Error */
					FrameRecord recordMgr = new FrameRecord();
					recordMgr.showScreen();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

}
