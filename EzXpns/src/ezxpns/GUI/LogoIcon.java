package ezxpns.GUI;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The Logo Object for EzXpns
 * @author A0097973
 */
@SuppressWarnings("serial")
public class LogoIcon extends JPanel {

	private final String LOGO_PATH = "src/images/logo.png"; 
	private ImageIcon logo;
	private JLabel lblLogo;
	
	public LogoIcon() {
		super(new java.awt.BorderLayout());
		logo = new ImageIcon(LOGO_PATH);
		lblLogo = new JLabel(logo);
		this.add(lblLogo, BorderLayout.CENTER);
//		System.out.println("Trying to create a logo :)");
	}
}
