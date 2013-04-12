package ezxpns.GUI;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LogoIcon extends JPanel {

	private ImageIcon logo;
	private JLabel lblLogo;
	
	public LogoIcon() {
		super(new java.awt.BorderLayout());
		logo = new ImageIcon("src/images/logo.png");
		lblLogo = new JLabel(logo);
		this.add(lblLogo, BorderLayout.CENTER);
		
		System.out.println("Trying to create a logo :)");
	}
}
