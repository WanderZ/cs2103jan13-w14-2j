package ezxpns.GUI;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

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
		
		CardLayout contentMgr = new CardLayout();
		JLayeredPane panContent = new JLayeredPane();
		panContent.setLayout(contentMgr);
		navi = new EzNavigator(contentMgr, panContent);
		
		GridBagConstraints gbc = new GridBagConstraints();
		// Setup for Navigator
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.10;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(navi, gbc);
		// Setup for Content 
		gbc.weightx = 0.90;
		gbc.weighty = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		
		this.add(panContent, gbc);
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
