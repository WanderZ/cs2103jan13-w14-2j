package ezxpns.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Calendar;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import ezxpns.data.SummaryDetails;
import ezxpns.data.SummaryGenerator;
import ezxpns.data.SummaryGenerator.SummaryType;

/**
 * This is the overview a.k.a summary panel that display the user's balance, in
 * and out cash flow.
 */
@SuppressWarnings("serial")
public class OverviewPanel extends JPanel {
	private SummaryType[] summaryTab = { SummaryType.TODAY, SummaryType.MONTH,
			SummaryType.YEAR, SummaryType.ALLTIME };
	private int index = 1;

	private SummaryGenerator sumGen;
	private JLabel lblSummary;
	private JLabel lblBalance;
	private JLabel lblIncomeNumber;
	private JLabel lblExpenseNumber;
	
	private JButton buttonToday;
	DecimalFormat df = new DecimalFormat("$###,###,##0.00");
	private JButton buttonMonth;
	private JButton buttonYear;
	private JButton buttonAllTime;
    Calendar cal = Calendar.getInstance();

	String[] monthName = {"January", "February",
            "March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"};
	DecimalFormat df = new DecimalFormat("0.00");
	
	public OverviewPanel(SummaryGenerator sumGenRef) {
		super();
		this.setOpaque(false);
//		setBackground(new Color(255, 255, 255));

		this.sumGen = sumGenRef;

		JPanel panelTime = new JPanel();
		panelTime.setOpaque(false);
//		panelTime.setBackground(Color.WHITE); // set color for buttons and timeline
		
		lblSummary = new JLabel("Balance: This" + monthName[cal.get(Calendar.MONTH)]);
		lblSummary.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		
		JPanel panelBigBalance = new JPanel();
		panelBigBalance.setOpaque(false);
//		panelBigBalance.setBackground(new Color(255, 255, 255));
		
		JPanel panelSub = new JPanel();
		panelSub.setOpaque(false);
//		panelSub.setBackground(new Color(255, 255, 255));
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panelTime, GroupLayout.PREFERRED_SIZE, 423, GroupLayout.PREFERRED_SIZE)
						.addComponent(panelSub, GroupLayout.PREFERRED_SIZE, 424, GroupLayout.PREFERRED_SIZE)
						.addComponent(panelBigBalance, GroupLayout.PREFERRED_SIZE, 428, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSummary))
					.addContainerGap(16, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(8)
					.addComponent(lblSummary)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelBigBalance, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelSub, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelTime, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(28, Short.MAX_VALUE))
		);
		
		// panelBigBalance
		// ===============
		panelBigBalance.setLayout(new MigLayout("", "0[125][300]", "0[70][40]0"));
		
		lblBalance = new JLabel("Balance");
		lblBalance.setFont(new Font("Lucida Grande", Font.PLAIN, 50));
		panelBigBalance.add(lblBalance, "cell 0 0 2 2,alignx center");
		
		// panelSub
		// ========
		panelSub.setLayout(new MigLayout("", "[80][10][80]", "0[20][20]0"));
		
		JLabel lblIncome = new JLabel("Income");
		panelSub.add(lblIncome, "cell 0 0, align center");
		
		JLabel lblExpense = new JLabel("Expense");
		panelSub.add(lblExpense, "cell 2 0,alignx center");
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		panelSub.add(separator, "cell 1 0 1 2, growy");
		
		lblIncomeNumber = new JLabel("income number");
		panelSub.add(lblIncomeNumber, "cell 0 1, align center");
		
		lblExpenseNumber = new JLabel("expense number");
		panelSub.add(lblExpenseNumber, "cell 2 1,alignx center");

		
		
		// TIME PANEL
		// ===========
		panelTime.setLayout(new FlowLayout());
		
		buttonToday = new JButton("Today");
		buttonToday.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				lblSummary.setText("Balance: Today");
				index = 0;
				getSummaryData();
			}
		});
		panelTime.add(buttonToday);
		
		buttonMonth = new JButton("This Month");
		buttonMonth.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				lblSummary.setText("Balance: This" + monthName[cal.get(Calendar.MONTH)]);
				index = 1;
				getSummaryData();
			}
		});
		panelTime.add(buttonMonth);
		
		buttonYear = new JButton("This Year");
		buttonYear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				lblSummary.setText("Balance: This Year");
				index = 2;
				getSummaryData();
			}
		});
		panelTime.add(buttonYear);
		
		buttonAllTime = new JButton("All Time");
		buttonAllTime.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				lblSummary.setText("Balance: All Time");
				index = 3;
				getSummaryData();
			}
		});
		panelTime.add(buttonAllTime);

		/*// Back Button
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
		panelTime.add(buttonBack, BorderLayout.WEST);

		// Next Button
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
		panelTime.add(buttonNext, BorderLayout.EAST);
		
		// Time Frame
		lblTimeFrame = new JLabel("");
		lblTimeFrame.setHorizontalAlignment(SwingConstants.CENTER);
		panelTime.add(lblTimeFrame, BorderLayout.CENTER);*/
		
		// Get Data
		// =========
		getSummaryData();
		
		setLayout(groupLayout);

	}

	private void getSummaryData() {
		SummaryDetails mySummaryDetails = sumGen
				.getSummaryDetails(summaryTab[index]);

		lblIncomeNumber.setText(df.format(mySummaryDetails.getIncome()));
		lblExpenseNumber.setText(df.format(mySummaryDetails.getExpense()));
		lblBalance.setText(df.format(mySummaryDetails.getBalance()));
		
		if (mySummaryDetails.getBalance() > 0)
			lblBalance.setForeground(Color.GREEN.darker());
		else if (mySummaryDetails.getBalance() < 0)
			lblBalance.setForeground(Color.RED.darker());
	}
	
	public void updateOverview(){
		getSummaryData();
	}
}
