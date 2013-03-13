package ezxpns.GUI;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import net.miginfocom.swing.MigLayout;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import ezxpns.data.Report;
import ezxpns.data.ReportCategory;
import java.awt.BorderLayout;

/**
 * The window to handle all the extensive record analysis and report
 * <br />Should contain a form for the user to enter the starting and the ending date of the records analysis
 */
@SuppressWarnings("serial")
public class ReportFrame extends JFrame implements ComponentListener{
	private JTextField startDateField;
	private JTextField endDateField;
	private JPanel cards;
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
	Report test;
	private JPanel expenseTable;
	private JLabel lblIncome;
	private JLabel lblExpense;
	private int PARAGRAPH_SPACE = 20;
	private JPanel button;
	private int WIDTH = 600;
	private JButton btnGeneral;
	private JButton btnExpense;
	private JTable table;
	private InteractiveTableModel tableModel;
	
	public static final String[] columnNames = {
        "Category", "Frequency", "Amount", "Percentage", "Amount/Frequency"
    };
	
	public ReportFrame() {
		super("EzXpns - Report");
		this.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		getContentPane().setLayout(new GridLayout(1, 0, 0, 0));

		tzReportData tz = new tzReportData();
		test = tz.createTestReport();
		
		// http://stackoverflow.com/questions/852631/java-swing-how-to-show-a-panel-on-top-of-another-panel
		// resize issue http://geti.dcc.ufrj.br/cursos/fes_2008_1/javatutorial/uiswing/events/componentlistener.html
		// http://stackoverflow.com/questions/8792075/overlay-panel-above-another
		// resizable font: http://java-sl.com/tip_adapt_label_font_size.html
		// resizable image: http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Resizeanimage.htm
		
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
		
		// test button panel
		button = new JPanel();
		button.setBounds(0, 0, WIDTH, 400);
		button.setOpaque(false);
		layeredPane.add(button);
		
		btnGeneral = new JButton("General");
		
		btnExpense = new JButton("Expense");
		GroupLayout gl_button = new GroupLayout(button);
		gl_button.setHorizontalGroup(
			gl_button.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_button.createSequentialGroup()
					.addContainerGap(300, Short.MAX_VALUE)
					.addComponent(btnGeneral, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnExpense)
					.addGap(60))
		);
		gl_button.setVerticalGroup(
			gl_button.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_button.createSequentialGroup()
					.addGap(78)
					.addGroup(gl_button.createParallelGroup(Alignment.BASELINE, false)
						.addComponent(btnGeneral)
						.addComponent(btnExpense))
					.addGap(293))
		);
		button.setLayout(gl_button);
		
		// Actual Report Panel
		report = new JPanel();
		layeredPane.add(report);	
		report.setBackground(Color.WHITE);
		report.setBounds(0, 0, WIDTH, 400);
		
		// Report Header in Report Panel
		JLabel lblReport = new JLabel("Report");
		lblReport.setFont(new Font("Lucida Grande", Font.PLAIN, 21));
		
		// cards -> CardLayout
		cards = new JPanel();
		report.add(cards);
		
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
		cardExpense.setLayout(new MigLayout("", "[262.00,grow,center][260.00,grow,right]", "[280.00,grow,fill]"));
		
		JPanel expensePieChart = new JPanel();
		cardExpense.add(expensePieChart, "cell 0 0,grow");
		expensePieChart.setLayout(new BoxLayout(expensePieChart, BoxLayout.X_AXIS));
		
		expenseTable = new JPanel();
		cardExpense.add(expenseTable, "cell 1 0,grow");
		
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
							.addComponent(endDateDisplay)))
					.addGap(27))
		);
		gl_report.setVerticalGroup(
			gl_report.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_report.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_report.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblReport)
						.addComponent(fromDisplay)
						.addComponent(startDateDisplay)
						.addComponent(toDisplay)
						.addComponent(endDateDisplay))
					.addGap(24)
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
			gl_generateReport.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_generateReport.createSequentialGroup()
					.addContainerGap(452, Short.MAX_VALUE)
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
					.addContainerGap(203, Short.MAX_VALUE))
		);
		gl_generateReport.setVerticalGroup(
			gl_generateReport.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_generateReport.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblGenerateAReport)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_generateReport.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStartDate)
						.addComponent(startDateField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEndDate)
						.addComponent(endDateField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnGenerate)
					.addGap(22))
		);
		generateReport.setLayout(gl_generateReport);
		
		// Add Component Listener to identified component
		cards.addComponentListener(this);
		curtain.addComponentListener(this);
		report.addComponentListener(this);
		cardGeneral.addComponentListener(this);
		cardExpense.addComponentListener(this);
		
		// PIE CHART
		// This will create the dataset 
        PieDataset dataset = createDataset();
        // based on the dataset we create the chart
        JFreeChart chart = createChart(dataset, "MY SUMMARY");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(250, 130));
        cardGeneral.setLayout(new MigLayout("", "[262.00,grow,center][260.00,grow,right]", "[280.00,grow,fill]"));
        
        JPanel generalPieChart = new JPanel();
        generalPieChart.add(chartPanel);
        cardGeneral.add(generalPieChart, "cell 0 0,grow");
        generalPieChart.setLayout(new BoxLayout(generalPieChart, BoxLayout.X_AXIS));
        
        JPanel generalSummary = new JPanel();
        cardGeneral.add(generalSummary, "cell 1 0,grow");
        generalSummary.setLayout(new BoxLayout(generalSummary, BoxLayout.PAGE_AXIS));
        
        lblIncome = new JLabel("Income:");
        lblIncome.setAlignmentX(0.4f);
        lblIncome.setAlignmentY(Component.TOP_ALIGNMENT);
        lblIncome.setHorizontalAlignment(SwingConstants.LEFT);
        generalSummary.add(Box.createVerticalGlue());
        generalSummary.add(lblIncome);
        generalSummary.add(Box.createRigidArea(new Dimension(0,PARAGRAPH_SPACE)));

        
        lblExpense = new JLabel("Expense:");
        lblExpense.setAlignmentX(0.4f);
        lblExpense.setAlignmentY(0.0f);
        lblExpense.setHorizontalAlignment(SwingConstants.LEFT);
        generalSummary.add(lblExpense);
        generalSummary.add(Box.createRigidArea(new Dimension(0,PARAGRAPH_SPACE)));
        
        lblExpense = new JLabel("Balance:");
        lblExpense.setAlignmentX(0.4f);
        lblExpense.setAlignmentY(0.0f);
        lblExpense.setHorizontalAlignment(SwingConstants.LEFT);
        generalSummary.add(lblExpense);
        generalSummary.add(Box.createVerticalGlue());
        
        
        // Table
        tableModel = new InteractiveTableModel(columnNames);
        expenseTable.setLayout(new BoxLayout(expenseTable, BoxLayout.Y_AXIS));
        table = new JTable();
        table.setModel(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension (20, 10));
        JScrollPane scrollPane = new JScrollPane(table);
        //table.setFillsViewportHeight(true);
        expenseTable.add(Box.createVerticalGlue());
        expenseTable.add(scrollPane);	
        expenseTable.add(Box.createVerticalGlue());
        
        // Pie Chart in Expense
        JFreeChart chart2 = createChart(dataset, "MY SUMMARY");
        ChartPanel chartPanel2 = new ChartPanel(chart2);
        expensePieChart.add(chartPanel2);



        
		
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
        button.setSize(w,h);
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
	
	/**
     * Creates a sample dataset 
     */

    private  PieDataset createDataset() {
        DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("Balance", 10);
        result.setValue("Expense", 40);
        result.setValue("Income", 50);
        return result;
        
    }
    
    
/**
     * Creates a chart
     */

    private JFreeChart createChart(PieDataset dataset, String title) {
        
        JFreeChart chart = ChartFactory.createPieChart(title,          // chart title
            dataset,                // data
            true,                   // include legend
            true,
            false);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setExplodePercent("Income", 0.2);
        plot.setStartAngle(0);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setOutlineVisible(false);
        plot.setBackgroundPaint(Color.white);
        plot.setForegroundAlpha(0.5f);
        return chart;
        
    }
    
    /** TableModel
     * 
     * @author yan
     *
     */
    public class InteractiveTableModel extends AbstractTableModel {
        public static final int CATEGORY_INDEX = 0;
        public static final int FREQUENCY_INDEX = 1;
        public static final int AMOUNT_INDEX = 2;
        public static final int PERCENTAGE_INDEX = 3;
        public static final int AMTFEQ_INDEX = 4;

        protected String[] columnNames;
        protected Vector<ReportCategory> dataVector;

        public InteractiveTableModel(String[] columnNames) {
            this.columnNames = columnNames;
            //dataVector = new Vector<ReportCategory>();
            dataVector = test.getExpenseCategory();
        }

        public String getColumnName(int column) {
            return columnNames[column];
        }


        public Class<?> getColumnClass(int column) {
            switch (column) {
                case CATEGORY_INDEX:
                case FREQUENCY_INDEX:
                case AMOUNT_INDEX:
                case PERCENTAGE_INDEX:
                case AMTFEQ_INDEX:
                   return String.class;
                default:
                   return Object.class;
            }
        }

        public Object getValueAt(int row, int column) {
            ReportCategory report = (ReportCategory)dataVector.get(row);
            switch (column) {
                case CATEGORY_INDEX:
                   return report.getCategory();
                case FREQUENCY_INDEX:
                   return report.getFrequency();
                case AMOUNT_INDEX:
                   return report.getAmount();
                case PERCENTAGE_INDEX:
                	return report.getPercentage();
                case AMTFEQ_INDEX:
                	return report.getAmtPerFreq();
                default:
                   return new Object();
            }
        }


        public int getRowCount() {
            return dataVector.size();
        }

        public int getColumnCount() {
            return columnNames.length;
        }
    }
}
