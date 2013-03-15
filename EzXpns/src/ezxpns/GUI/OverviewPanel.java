package ezxpns.GUI;

import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import ezxpns.data.SummaryGenerator;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This is the overview a.k.a summary panel that display the user's balance, in and out cash flow.
 */
@SuppressWarnings("serial")
public class OverviewPanel extends JPanel {
	
	private int TODAY = 0;
	private int MONTH = 1;
	private int YEAR = 2;
	private int ALLTIME = 3;
	private int index = 1;
	
	public OverviewPanel() {
		super();
		
		SummaryGenerator mySummary;
		
		JLabel lblBalance = new JLabel("Balance:");
		lblBalance.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		
		JLabel lblIncome = new JLabel("Income:");
		
		JLabel lblExpense = new JLabel("Expense:");
		
		JButton buttonBack = new JButton("<");
		buttonBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		
		JButton buttonNext = new JButton(">");
		buttonNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		
		JLabel lblThisMonth = new JLabel("This Month");
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
							.addComponent(lblThisMonth)
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
						.addComponent(lblThisMonth))
					.addGap(26)
					.addComponent(lblIncome)
					.addGap(18)
					.addComponent(lblExpense)
					.addContainerGap(126, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
		
		lblBalance.setText("Balance");
		lblIncome.setText("Income");
		lblExpense.setText("Expense");
	}
}
