package ezxpns.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JWindow;

/** 
 * Utility to do Minor Notification Messages
 * <br />To display responses to users when they perform an action,
 * <br />namely - Adding new data, editing existing data, removing existing data.
 */
public class UINotify {

	private UINotify() {}
	
	public static void createPopUp(String msg) {
		PopMsg popup = new PopMsg(msg);
		new Thread(popup).start();
	}
	
	public static void createErrMsg(String msg) {
		PopMsg popup = new PopMsg(msg, PopMsg.ERR_MSG);
		new Thread(popup).start();
	}
}

/**
 * Pop Up Message that disappear after 5 seconds 
 */
@SuppressWarnings("serial")
class PopMsg extends JWindow implements Runnable {
		
	private final int TIMEOUT = 5000;
	private final int WIDTH = 250;
	private final int HEIGHT = 21;
	
	private JLabel lblMsg;
	
	public static final int ERR_MSG = 01;
	
	public PopMsg(String msg) {
		this.setSize(WIDTH, HEIGHT);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		
		lblMsg = new JLabel("     " + msg);
		lblMsg.setFont(Config.TEXT_FONT);
		lblMsg.setBounds(5, 5, WIDTH-50, HEIGHT);
		this.add(lblMsg, BorderLayout.CENTER);
		
		JButton btnClose = new JButton("[X]");
		btnClose.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(final ActionEvent e) {
	               dispose();
	        }
		});
		btnClose.setContentAreaFilled(false);
		btnClose.setMargin(new Insets(1, 4, 1, 4));
		btnClose.setFocusable(false);
		
		this.add(btnClose, BorderLayout.EAST);
	}
	
	public PopMsg(String msg, int errType) {
		this(msg);
		lblMsg.setForeground(Color.RED);
	}

	@Override
	public void run() {
		try {
			this.setAlwaysOnTop(true);
			this.setVisible(true);
			Thread.sleep(TIMEOUT); // time after which pop up will be disappeared.
			this.dispose();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

/* 	
	Faking a pop up :)
	
	JWindow jWin = new JWindow();
	jWin.getContentPane().add(new JLabel("hello world!"));
	jWin.setSize(800,600);
	jWin.setLocationRelativeTo(null);
	jWin.setVisible(true);
	
	String message = "You got a new notification message. Isn't it awesome to have such a notification message.";
	String header = "This is header of notification message";
	JFrame frame = new JFrame();
	frame.setSize(300,125);
	frame.setLayout(new GridBagLayout());
	GridBagConstraints constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.weightx = 1.0f;
	constraints.weighty = 1.0f;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.fill = GridBagConstraints.BOTH;
	JLabel headingLabel = new JLabel(header);
	headingLabel .setIcon(headingIcon); // --- use image icon you want to be as heading image.
	headingLabel.setOpaque(false);
	frame.add(headingLabel, constraints);
	constraints.gridx++;
	constraints.weightx = 0f;
	constraints.weighty = 0f;
	constraints.fill = GridBagConstraints.NONE;
	constraints.anchor = GridBagConstraints.NORTH;
	JButton cloesButton = new JButton("X")(new AbstractAction("x") {
        @Override
        public void actionPerformed(final ActionEvent e) {
               frame.dispose();
        }
	});
	cloesButton.setMargin(new Insets(1, 4, 1, 4));
	cloesButton.setFocusable(false);
	frame.add(cloesButton, constraints);
	constraints.gridx = 0;
	constraints.gridy++;
	constraints.weightx = 1.0f;
	constraints.weighty = 1.0f;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.fill = GridBagConstraints.BOTH;
	JLabel messageLabel = new JLabel("<HtMl>"+message);
	frame.add(messageLabel, constraints);
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	frame.setUndecorated(true);
	frame.setAlwaysOnTop(true);
	frame.setVisible(true);

	// Disappear after a while
	new Thread(){
      @Override
      public void run() {
           try {
                  Thread.sleep(5000); // time after which pop up will be disappeared.
                  frame.dispose();
           } catch (InterruptedException e) {
                  e.printStackTrace();
           }
      };
	}.start();

	// Setting the location of the window
	Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();// size of the screen
	Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());// height of the task bar
	frame.setLocation(scrSize.width - frame.getWidth(), scrSize.height - toolHeight.bottom - frame.getHeight());
	// Multiple popups
	frame.setLocation(scrSize.width - frame.getWidth(), scrSize.height - toolHeight.bottom - (frame.getHeight() * (n+1)));
*/