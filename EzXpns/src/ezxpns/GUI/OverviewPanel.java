package ezxpns.GUI;

import java.awt.Color;
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
import java.text.DecimalFormat;

import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This is the overview a.k.a summary panel that display the user's balance, in
 * and out cash flow.
 */
@SuppressWarnings("serial")
public class OverviewPanel extends JPanel {

	private JLabel lblBalance;
	private JLabel lblIncome;
	private JLabel lblExpense;
	private SummaryType[] summaryTab = { SummaryType.TODAY, SummaryType.MONTH,
			SummaryType.YEAR, SummaryType.ALLTIME };
	private int index = 1;

	private SummaryGenerator sumGen;
	private JLabel lblTimeFrame;
	private JButton buttonBack;
	private JButton buttonNext;

	DecimalFormat df = new DecimalFormat("#.##");

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
							.addContainerGap()
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblExpense)
								.addComponent(lblIncome))))
					.addContainerGap(211, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(26)
					.addComponent(lblBalance)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(26)
					.addComponent(lblIncome)
					.addGap(18)
					.addComponent(lblExpense)
					.addContainerGap(138, Short.MAX_VALUE))
		);
		panel.setLayout(new BorderLayout(0, 0));

		buttonBack = new JButton("<");
		buttonBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
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

	private void getSummaryData() {
		SummaryDetails mySummaryDetails = sumGen
				.getSummaryDetails(summaryTab[index]);

		lblBalance.setText("Balance:\t" + df.format(mySummaryDetails.getBalance()));
		lblIncome.setText("Income:\t" + df.format(mySummaryDetails.getIncome()));
		lblExpense.setText("Expense:\t" + df.format(mySummaryDetails.getExpense()));
		lblTimeFrame.setText(mySummaryDetails.getSummaryType().getName());
		
		if (mySummaryDetails.getBalance() > 0)
			lblBalance.setForeground(Color.GREEN.darker());
		else if (mySummaryDetails.getBalance() < 0)
			lblBalance.setForeground(Color.RED.darker());
	}
	
	public void updateOverview(){
		getSummaryData();
	}
}
