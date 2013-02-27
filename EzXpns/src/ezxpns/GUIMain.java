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

					FrameRecord recordMgr = new FrameRecord();
					recordMgr.showScreen();
					MainScreen main = new MainScreen();
					main.showScreen();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

}
