package ezxpns.GUI;

import javax.swing.JPanel;

import ezxpns.data.SummaryGenerator;


/**
 * This is the overview a.k.a summary panel that display the user's balance, in and out cash flow.
 */
@SuppressWarnings("serial")
public class OverviewPanel extends JPanel {
	
	private SummaryGenerator sumGen;
	
	public OverviewPanel(SummaryGenerator sumGenRef) {
		super();
		
		this.sumGen = sumGenRef;
		this.setLayout(new java.awt.BorderLayout());
		JPanel temp = new JPanel(new java.awt.GridLayout(0, 1, 0, 0));
		this.add(temp, java.awt.BorderLayout.CENTER);
		temp.add(new javax.swing.JLabel("Overview - Under Construction"));
		temp.add(new javax.swing.JLabel("Balance: $00.00"));
		temp.add(new javax.swing.JLabel("IN: $00.00"));
		temp.add(new javax.swing.JLabel("Out: $00.00"));
	}
}
