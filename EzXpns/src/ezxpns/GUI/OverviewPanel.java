package ezxpns.GUI;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import ezxpns.data.SummaryDetails;
import ezxpns.data.SummaryGenerator;
import ezxpns.data.SummaryGenerator.SummaryType;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;

/**
 * This is the overview a.k.a summary panel that display the user's balance, in and out cash flow.
 */
@SuppressWarnings("serial")
public class OverviewPanel extends JPanel {
	
	private JLabel lblBalance;
	private JLabel lblIncome;
	private JLabel lblExpense;
	private SummaryType[] summaryTab = {SummaryType.TODAY, SummaryType.MONTH, SummaryType.YEAR, SummaryType.ALLTIME};
	private int index = 1;
	
	
	private SummaryGenerator sumGen;
	private JLabel lblTimeFrame;
	private JButton buttonBack;
	private JButton buttonNext;
	
	public OverviewPanel(SummaryGenerator sumGenRef) {
		super();
		
		this.sumGen = sumGenRef;
		
		lblBalance = new JLabel("Balance:");
		lblBalance.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		
		lblIncome = new JLabel("Income:");
		
		lblExpense = new JLabel("Expense:");
		
		JPanel panel = new JPanel();
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addComponent(lblBalance))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblExpense)
								.addComponent(lblIncome)))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 251, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(315, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(26)
					.addComponent(lblBalance)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblIncome)
					.addGap(18)
					.addComponent(lblExpense)
					.addContainerGap(120, Short.MAX_VALUE))
		);
		panel.setLayout(new BorderLayout(0, 0));
		
		buttonBack = new JButton("<");
		buttonBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				index -= 1;
				if (index < 0)
					index = 0;
				getSummaryData();
			}
		});
		panel.add(buttonBack, BorderLayout.WEST);
		
		lblTimeFrame = new JLabel("");
		lblTimeFrame.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblTimeFrame, BorderLayout.CENTER);
		
		buttonNext = new JButton(">");
		buttonNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				index += 1;
				if (index > 3)
					index = 3;
				getSummaryData();
			}
		});
		panel.add(buttonNext, BorderLayout.EAST);
		setLayout(groupLayout);
		getSummaryData();
	}
	
	private void getSummaryData(){
		SummaryDetails mySummaryDetails = sumGen.getSummaryDetails(summaryTab[index]);
		
		lblBalance.setText("Balance:\t"+mySummaryDetails.getBalance());
		lblIncome.setText("Income:\t"+mySummaryDetails.getIncome());
		lblExpense.setText("Expense:\t"+mySummaryDetails.getExpense());
		lblTimeFrame.setText(mySummaryDetails.getSummaryType().getName());
	}
}
