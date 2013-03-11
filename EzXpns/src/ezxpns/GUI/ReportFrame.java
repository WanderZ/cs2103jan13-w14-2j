package ezxpns.GUI;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * The window to handle all the extensive record analysis and report
 * <br />Should contain a form for the user to enter the starting and the ending date of the records analysis
 */
@SuppressWarnings("serial")
public class ReportFrame extends JFrame implements ComponentListener{
	private JTextField startDateField;
	private JTextField endDateField;
	private JPanel cards;
	private JButton btnGeneral, btnExpense;
	public static final int DEFAULT_WIDTH = 600;
	public static final int DEFAULT_HEIGHT = 400; 
	private JPanel generateReport;
	private JPanel curtain;
	private JLabel startDateDisplay;
	private JLabel endDateDisplay;
	private JPanel cardGeneral;
	private JPanel cardExpense;
	JLayeredPane layeredPane;
	JPanel report;
	
	public ReportFrame() {
		super("EzXpns - Report");
		this.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		// http://stackoverflow.com/questions/852631/java-swing-how-to-show-a-panel-on-top-of-another-panel
		// resize issue http://geti.dcc.ufrj.br/cursos/fes_2008_1/javatutorial/uiswing/events/componentlistener.html
		// http://stackoverflow.com/questions/8792075/overlay-panel-above-another
		
		// Layaered Pane
		layeredPane = new JLayeredPane();
		getContentPane().add(layeredPane);
		
		// Generate a Report
		generateReport = new JPanel();
		layeredPane.add(generateReport);
		generateReport.setBackground(Color.CYAN.darker());
		generateReport.setBounds(0, 80, 600, 120); // x, y, width, height
		
		// White Curtain
		curtain = new JPanel();
		curtain.setBackground(new Color(255, 255, 255, 230));
		curtain.setBounds(0, 0, 600, 400);
		layeredPane.add(curtain);
		
		// Actual Report Panel
		report = new JPanel();
		layeredPane.add(report);	
		report.setBackground(Color.WHITE);
		report.setBounds(0, 0, 600, 400);
		
		// Report Header in Report Panel
		JLabel lblReport = new JLabel("Report");
		lblReport.setFont(new Font("Lucida Grande", Font.PLAIN, 21));
		
		// cards -> CardLayout
		cards = new JPanel();
		report.add(cards);
		
		// Buttons - "General", "Expense"
		btnGeneral = new JButton("General");
		btnExpense = new JButton("Expense");
		
		// Setting up the cards
		cards.setLayout(new CardLayout(0, 0));
		cardGeneral = new JPanel();		// General Card
		cardGeneral.setBackground(Color.PINK);
		cardGeneral.setForeground(Color.RED);
		cardExpense = new JPanel();		// Expense Card
		cardExpense.setBackground(Color.ORANGE);
		cardExpense.setForeground(Color.MAGENTA);
		cards.add(cardGeneral, "GeneralCard");
		cards.add(cardExpense, "ExpenseCard");
		
		// Stuff inside card
		JLabel lblGeneralTest = new JLabel("General test");
		
		JLabel lblTotalIncome = new JLabel("Income:");
		
		JLabel lblTotalExpense = new JLabel("Expense:");
		
		JLabel lblBalance = new JLabel("Balance:");
		
		JLabel lblUniquePieChart = new JLabel("Unique Pie chart");
		GroupLayout gl_cardGeneral = new GroupLayout(cardGeneral);
		gl_cardGeneral.setHorizontalGroup(
			gl_cardGeneral.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_cardGeneral.createSequentialGroup()
					.addGap(193)
					.addComponent(lblGeneralTest)
					.addGap(91))
				.addGroup(Alignment.TRAILING, gl_cardGeneral.createSequentialGroup()
					.addGap(37)
					.addComponent(lblUniquePieChart)
					.addPreferredGap(ComponentPlacement.RELATED, 215, Short.MAX_VALUE)
					.addGroup(gl_cardGeneral.createParallelGroup(Alignment.LEADING)
						.addComponent(lblBalance)
						.addComponent(lblTotalExpense)
						.addComponent(lblTotalIncome))
					.addGap(184))
		);
		gl_cardGeneral.setVerticalGroup(
			gl_cardGeneral.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_cardGeneral.createSequentialGroup()
					.addGap(5)
					.addComponent(lblGeneralTest)
					.addGap(42)
					.addComponent(lblTotalIncome)
					.addGap(18)
					.addGroup(gl_cardGeneral.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTotalExpense)
						.addComponent(lblUniquePieChart))
					.addGap(18)
					.addComponent(lblBalance)
					.addContainerGap(149, Short.MAX_VALUE))
		);
		cardGeneral.setLayout(gl_cardGeneral);
		
		
		JLabel lblExpenseTest = new JLabel("Expense Test");
		
		JLabel lblTables = new JLabel("Tables");
		
		JLabel lblPieChart = new JLabel("Pie Chart");
		GroupLayout gl_cardExpense = new GroupLayout(cardExpense);
		gl_cardExpense.setHorizontalGroup(
			gl_cardExpense.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_cardExpense.createSequentialGroup()
					.addGap(235)
					.addComponent(lblExpenseTest))
				.addGroup(Alignment.TRAILING, gl_cardExpense.createSequentialGroup()
					.addGap(48)
					.addComponent(lblPieChart)
					.addPreferredGap(ComponentPlacement.RELATED, 209, Short.MAX_VALUE)
					.addComponent(lblTables)
					.addGap(194))
		);
		gl_cardExpense.setVerticalGroup(
			gl_cardExpense.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_cardExpense.createSequentialGroup()
					.addGroup(gl_cardExpense.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_cardExpense.createSequentialGroup()
							.addGap(5)
							.addComponent(lblExpenseTest)
							.addGap(90)
							.addComponent(lblTables))
						.addGroup(gl_cardExpense.createSequentialGroup()
							.addGap(120)
							.addComponent(lblPieChart)))
					.addContainerGap(160, Short.MAX_VALUE))
		);
		cardExpense.setLayout(gl_cardExpense);
		
		btnGeneral.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
		        CardLayout cl = (CardLayout)(cards.getLayout());
		        cl.show(cards, "GeneralCard");
		    }
		});
		btnExpense.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
		        CardLayout cl = (CardLayout)(cards.getLayout());
		        cl.show(cards, "ExpenseCard");
		    }
		});
		
		// Remove buttons outline
		
		/*btnGeneral.setFocusPainted(false);
		btnGeneral.setMargin(new Insets(0, 0, 0, 0));
		btnGeneral.setContentAreaFilled(false);
		btnGeneral.setBorderPainted(false);
		btnGeneral.setOpaque(false);
		btnExpense.setFocusPainted(false);
		btnExpense.setMargin(new Insets(0, 0, 0, 0));
		btnExpense.setContentAreaFilled(false);
		btnExpense.setBorderPainted(false);
		btnExpense.setOpaque(false);*/
		
		JLabel fromDisplay = new JLabel("from");
		
		startDateDisplay = new JLabel("test start");
		
		JLabel toDisplay = new JLabel("to");
		
		endDateDisplay = new JLabel("test end");
		
		GroupLayout gl_report = new GroupLayout(report);
		gl_report.setHorizontalGroup(
			gl_report.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_report.createSequentialGroup()
					.addGap(20)
					.addGroup(gl_report.createParallelGroup(Alignment.LEADING)
						.addComponent(cards, GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
						.addGroup(gl_report.createSequentialGroup()
							.addComponent(lblReport)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(fromDisplay)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(startDateDisplay)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(toDisplay)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(endDateDisplay)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnGeneral)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnExpense)))
					.addGap(27))
		);
		gl_report.setVerticalGroup(
			gl_report.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_report.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_report.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_report.createSequentialGroup()
							.addGroup(gl_report.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblReport)
								.addComponent(fromDisplay)
								.addComponent(startDateDisplay)
								.addComponent(toDisplay)
								.addComponent(endDateDisplay))
							.addGap(24))
						.addGroup(gl_report.createSequentialGroup()
							.addGroup(gl_report.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnGeneral)
								.addComponent(btnExpense))
							.addPreferredGap(ComponentPlacement.UNRELATED)))
					.addComponent(cards, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
					.addGap(48))
		);
		
		
		
		report.setLayout(gl_report);
		
		
		
		JLabel lblGenerateAReport = new JLabel("Generate a Report");
		lblGenerateAReport.setFont(new Font("Lucida Grande", Font.PLAIN, 21));
		lblGenerateAReport.setForeground(Color.WHITE);
		
		JLabel lblStartDate = new JLabel("Start Date");
		lblStartDate.setForeground(Color.WHITE);
		
		// Start Date
		startDateField = new JTextField();
		startDateField.setColumns(10);
		
		JLabel lblEndDate = new JLabel("End Date");
		lblEndDate.setForeground(Color.WHITE);
		
		// End Date
		endDateField = new JTextField();
		endDateField.setColumns(10);
		
		// "Generate" Button
		JButton btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				generateReport.setVisible(false);
				curtain.setVisible(false);
				startDateDisplay.setText(startDateField.getText());
				endDateDisplay.setText(endDateField.getText());
			}
		});
		GroupLayout gl_generateReport = new GroupLayout(generateReport);
		gl_generateReport.setHorizontalGroup(
			gl_generateReport.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_generateReport.createSequentialGroup()
					.addContainerGap(352, Short.MAX_VALUE)
					.addComponent(btnGenerate)
					.addGap(49))
				.addGroup(gl_generateReport.createSequentialGroup()
					.addGap(20)
					.addGroup(gl_generateReport.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_generateReport.createSequentialGroup()
							.addComponent(lblStartDate)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(startDateField, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblEndDate)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(endDateField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblGenerateAReport))
					.addContainerGap(103, Short.MAX_VALUE))
		);
		gl_generateReport.setVerticalGroup(
			gl_generateReport.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_generateReport.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblGenerateAReport)
					.addPreferredGap(ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
					.addGroup(gl_generateReport.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStartDate)
						.addComponent(startDateField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEndDate)
						.addComponent(endDateField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnGenerate)
					.addContainerGap())
		);
		generateReport.setLayout(gl_generateReport);
		
		// Add Component Listener to identified component
		cards.addComponentListener(this);
		curtain.addComponentListener(this);
		report.addComponentListener(this);
		cardGeneral.addComponentListener(this);
		cardExpense.addComponentListener(this);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	
	
	public void showScreen() {this.setVisible(true);}



	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		Insets insets = this.getInsets();
        int w = this.getWidth() - insets.left - insets.right;
        int h = this.getHeight() - insets.top - insets.bottom;
		/*int widthDiff = getWidth() - DEFAULT_WIDTH;
		int heightDiff = getHeight() - DEFAULT_HEIGHT;
		cards.setBounds(cards.getX(),cards.getY(),getWidth()+widthDiff,getHeight()+heightDiff);
		curtain.setBounds(0,0,getWidth(),getHeight());
		generateReport.setBounds(0, 80, getWidth(), 120);
		cardGeneral.setBounds(cards.getX(),cards.getY(),cardGeneral.getWidth()+widthDiff,cardGeneral.getHeight()+heightDiff);
		cardExpense.setBounds(cards.getX(),cards.getY(),cardExpense.getWidth()+widthDiff,cardExpense.getHeight()+heightDiff);*/
        curtain.setSize(w, h);
        cards.setBounds(cards.getX(),cards.getY(),w,h);
        report.setSize(w,h);
        generateReport.setSize(w,120);
        //cardGeneral.setBounds(cards.getX(),cards.getY(),w,h);
        //cardExpense.setBounds(cards.getX(),cards.getY(),w,h);
        layeredPane.revalidate();
        layeredPane.repaint();
	}



	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
