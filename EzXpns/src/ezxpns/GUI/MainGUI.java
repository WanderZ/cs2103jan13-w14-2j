package ezxpns.GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainGUI extends JFrame implements UpdateNotifyee {
	
	private EzNavigator navi;
	
	public MainGUI() {
		super();
		this.instantiateUI();
		
		// TODO: InstantiateNavi
		// TODO: CardLayout
		// TODO: InstantiateContent
		// TODO: Iterate through the list of JLayeredPane/JPanel to be displayed
		
		navi = new EzNavigator(null, null);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		JButton btn = new JButton("Menu Placeholder");
		gbc.weightx = 0.15;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(btn, gbc);
		
		btn = new JButton("Content Placeholder");
		gbc.weightx = 0.85;
		gbc.weighty = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		this.add(btn, gbc);
		this.setVisible(true);
	}
	
	private void instantiateUI() {
		this.setBounds(0, 0, 1000, 600);
		this.setLocationRelativeTo(null);
		this.setLayout(new GridBagLayout());
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		// this.setUndecorated(true); // Remove Title Bar 
	}

	@Override
	public void updateAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addUndoAction(AbstractAction action, String name) {
		// TODO Auto-generated method stub
		
	}
}
