package ezxpns.GUI;

import javax.swing.JPanel;

/**
 * This is the overview a.k.a summary panel that display the user's balance, in and out cash flow.
 */
@SuppressWarnings("serial")
public class OverviewPanel extends JPanel {
	
	public OverviewPanel() {
		super();
		this.setLayout(new java.awt.GridLayout(4,1));
		this.add(new javax.swing.JLabel("Overview - Under Construction"));
		this.add(new javax.swing.JLabel("Balance: $00.00"));
		this.add(new javax.swing.JLabel("IN: $00.00"));
		this.add(new javax.swing.JLabel("Out: $00.00"));
	}
}
