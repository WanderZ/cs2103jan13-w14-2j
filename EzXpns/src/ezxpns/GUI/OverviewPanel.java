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

/**
 * This is the overview a.k.a summary panel that display the user's balance, in and out cash flow.
 */
@SuppressWarnings("serial")
public class OverviewPanel extends JPanel {
	
	private JLabel lblBalance;
	private JLabel lblIncome;
	private JLabel lblExpense;
	private JLabel lblTimeFrame;
	private SummaryType[] summaryTab = {SummaryType.TODAY, SummaryType.MONTH, SummaryType.YEAR, SummaryType.ALLTIME};
	private int index = 1;
	
	
	private SummaryGenerator sumGen;
	
	public OverviewPanel(SummaryGenerator sumGenRef) {
		super();
		
		this.sumGen = sumGenRef;
		
		lblBalance = new JLabel("Balance:");
		lblBalance.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		
		lblIncome = new JLabel("Income:");
		
		lblExpense = new JLabel("Expense:");
		
		lblTimeFrame = new JLabel("This Month");

		
		JButton buttonBack = new JButton("<");
		buttonBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				index -= 1;
				if (index < 0)
					index = 0;
				getSummaryData();
			}
		});
		
		JButton buttonNext = new JButton(">");
		buttonNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				index += 1;
				if (index > 3)
					index = 3;
				getSummaryData();
			}
		});
		
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
							.addComponent(buttonBack)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblTimeFrame)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(buttonNext)))
					.addContainerGap(186, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(26)
					.addComponent(lblBalance)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonBack)
						.addComponent(buttonNext)
						.addComponent(lblTimeFrame))
					.addGap(26)
					.addComponent(lblIncome)
					.addGap(18)
					.addComponent(lblExpense)
					.addContainerGap(126, Short.MAX_VALUE))
		);
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
