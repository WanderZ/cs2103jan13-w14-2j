package ezxpns;
import ezxpns.GUI.*;
import java.awt.EventQueue;

public class GUIMain {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					/* Main GUI */
					MainScreen main = new MainScreen();
					main.showScreen();
					
					/* Record Frame GUI - For testing */
					RecordFrame recordMgr = new RecordFrame();
					recordMgr.showScreen();
					
					/* Search Frame GUI - For testing */
					SearchFrame searchMgr = new SearchFrame();
					searchMgr.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
