package ezxpns.GUI;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.AbstractTableModel;

import net.miginfocom.swing.MigLayout;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import ezxpns.data.Report;
import ezxpns.data.ReportCategory;
import ezxpns.data.ReportGenerator;
import ezxpns.data.ReportGenerator.DateOrderException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The window to handle all the extensive record analysis and report <br />
 * Should contain a form for the user to enter the starting and the ending date
 * of the records analysis
 * 
 * @author A0087091B
 */
@SuppressWarnings("serial")
public class ReportFrame extends JFrame implements ComponentListener {
	private JPanel cards;
	private JPanel generateReport;
	private JPanel curtain;
	private JLabel startDateDisplay;
	private JLabel endDateDisplay;
	private JPanel cardGeneral;
	private JPanel cardExpense;
	private JLayeredPane layeredPane;
	private JPanel report;
	private JPanel expenseTable;
	private JLabel lblIncome;
	private JLabel lblExpense;
	private JLabel lblBalance;
	private JPanel button;
	private JButton btnGeneral;
	private JButton btnExpense;
	private JTable table;
	private InteractiveTableModel tableModel;
	private Report myReportData;
	private JLabel lblErrorMsg;
	private int pieChartIndex = 0;
	private JDateChooser dateChooserStart;
	private JDateChooser dateChooserEnd;
	private ColorSquare myIncome;
	private ColorSquare myExpense;
	private ColorSquare myBalance;
	private JLabel lblNumRecords;
	
	DecimalFormat df = new DecimalFormat("#.##");

	// Date Format
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public static final int DEFAULT_WIDTH = 1000;
	public static final int DEFAULT_HEIGHT = 600;
	public static final String[] columnNames = { "Category", "Frequency",
			"Amount", "Percentage", "Amount/Frequency" };

	private ReportGenerator rptGen; // Place to store the reference
	private JButton btnGenerateANew;
	private JButton btnThisMonth;

	public ReportFrame(ReportGenerator rptGenRef) { // Passing in the reference
		super("EzXpns - Report");

		rptGen = rptGenRef; // Storing the reference

		this.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		getContentPane().setLayout(new GridLayout(1, 0, 0, 0));

		dateFormat.setCalendar(new GregorianCalendar());

		// Layaered Pane
		layeredPane = new JLayeredPane();
		getContentPane().add(layeredPane);

		// Generate a Report
		generateReport = new JPanel();
		layeredPane.add(generateReport);
		generateReport.setBackground(Color.CYAN.darker());
		generateReport.setBounds(0, 80, 783, 126); // x, y, width, height

		// White Curtain
		curtain = new JPanel();
		curtain.setBackground(new Color(255, 255, 255, 230));
		curtain.setBounds(0, 0, 783, 424);
		layeredPane.add(curtain);

		// Button Panel
		button = new JPanel();
		button.setBackground(new Color(255, 255, 255));
		button.setBounds(0, 0, WIDTH, 400);
		button.setOpaque(false);
		layeredPane.add(button);

		// General & Expense Buttons

		btnGeneral = new JButton("General");
		btnExpense = new JButton("Expense");

		GroupLayout gl_button = new GroupLayout(button);
		gl_button.setHorizontalGroup(gl_button.createParallelGroup(
				Alignment.TRAILING).addGroup(
				gl_button
						.createSequentialGroup()
						.addContainerGap(300, Short.MAX_VALUE)
						.addComponent(btnGeneral, GroupLayout.PREFERRED_SIZE,
								117, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnExpense).addGap(60)));
		gl_button
				.setVerticalGroup(gl_button.createParallelGroup(
						Alignment.LEADING).addGroup(
						gl_button
								.createSequentialGroup()
								.addGap(78)
								.addGroup(
										gl_button
												.createParallelGroup(
														Alignment.BASELINE,
														false)
												.addComponent(btnGeneral)
												.addComponent(btnExpense))
								.addGap(293)));
		button.setLayout(gl_button);

		// Actual Report Panel
		report = new JPanel();
		layeredPane.add(report);
		report.setBackground(Color.WHITE);
		report.setBounds(0, 0, 579, 400);

		// Report Header in Report Panel
		JLabel lblReport = new JLabel("Report");
		lblReport.setFont(new Font("Lucida Grande", Font.PLAIN, 21));

		// cards -> CardLayout
		cards = new JPanel();
		report.add(cards);

		// Setting up the cards
		cards.setLayout(new CardLayout(0, 0));
		cardGeneral = new JPanel(); // General Card
		cardGeneral.setBackground(new Color(255, 255, 255));
		cardGeneral.setForeground(Color.RED);
		cardExpense = new JPanel(); // Expense Card
		cardExpense.setBackground(new Color(255, 255, 255));
		cardExpense.setForeground(Color.MAGENTA);
		cards.add(cardGeneral, "GeneralCard");
		cards.add(cardExpense, "ExpenseCard");
		cardExpense
				.setLayout(new MigLayout("",
						"[262.00,grow,center][260.00,grow,right]",
						"[280.00,grow,fill]"));

		expenseTable = new JPanel();
		expenseTable.setBackground(new Color(255, 255, 255));
		cardExpense.add(expenseTable, "cell 1 0,grow");

		// ActionListener for General Button
		btnGeneral.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, "GeneralCard");
			}
		});
		// ActionListener for Expense Button
		btnExpense.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, "ExpenseCard");
			}
		});

		// Report JLabel
		JLabel fromDisplay = new JLabel("from");
		startDateDisplay = new JLabel("test start");
		JLabel toDisplay = new JLabel("to");
		endDateDisplay = new JLabel("test end");

		btnGenerateANew = new JButton("Generate a new Report");
		
		// Disable Buttons 
		btnGeneral.setEnabled(false);
		btnExpense.setEnabled(false);
		btnGenerateANew.setEnabled(false);

		// ActionListener for Generate new Report Button
		btnGenerateANew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				curtain.setVisible(true);
				generateReport.setVisible(true);
				pieChartIndex = 1;
				btnGeneral.setEnabled(false);
				btnExpense.setEnabled(false);
				btnGenerateANew.setEnabled(false);
				dateChooserStart.setDate(null);
				dateChooserEnd.setDate(null);
			}
		});

		GroupLayout gl_report = new GroupLayout(report);
		gl_report
				.setHorizontalGroup(gl_report
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_report
										.createSequentialGroup()
										.addGap(20)
										.addGroup(
												gl_report
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																cards,
																GroupLayout.DEFAULT_SIZE,
																249,
																Short.MAX_VALUE)
														.addGroup(
																gl_report
																		.createSequentialGroup()
																		.addComponent(
																				lblReport)
																		.addPreferredGap(
																				ComponentPlacement.UNRELATED)
																		.addComponent(
																				fromDisplay)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				startDateDisplay)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				toDisplay)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				endDateDisplay)
																		.addPreferredGap(
																				ComponentPlacement.RELATED,
																				156,
																				Short.MAX_VALUE)
																		.addComponent(
																				btnGenerateANew)
																		.addGap(10)))
										.addGap(27)));
		gl_report.setVerticalGroup(gl_report.createParallelGroup(
				Alignment.LEADING)
				.addGroup(
						gl_report
								.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										gl_report
												.createParallelGroup(
														Alignment.BASELINE)
												.addComponent(lblReport)
												.addComponent(fromDisplay)
												.addComponent(startDateDisplay)
												.addComponent(toDisplay)
												.addComponent(endDateDisplay)
												.addComponent(btnGenerateANew))
								.addGap(24)
								.addComponent(cards, GroupLayout.DEFAULT_SIZE,
										296, Short.MAX_VALUE).addGap(48)));
		report.setLayout(gl_report);

		// Components in Generate a Report
		JLabel lblGenerateAReport = new JLabel("Generate a Report");
		lblGenerateAReport.setFont(new Font("Lucida Grande", Font.PLAIN, 21));
		lblGenerateAReport.setForeground(Color.WHITE);

		JLabel lblStartDate = new JLabel("Start Date");
		lblStartDate.setForeground(Color.WHITE);

		JLabel lblEndDate = new JLabel("End Date");
		lblEndDate.setForeground(Color.WHITE);

		// New Date Chooser
		dateChooserStart = new JDateChooser();
		dateChooserStart.getJCalendar().setTodayButtonVisible(true);
		dateChooserStart.setDateFormatString("dd/MM/yyyy");
		dateChooserStart.setMaxSelectableDate(new Date());
		PropertyChangeListener calendarChangeListener  = new PropertyChangeListener() {
	        @Override
	        public void propertyChange(PropertyChangeEvent evt) {
	            Date selectedDate = ((JCalendar)evt.getSource()).getDate();
	        }
	    };
	    dateChooserStart.getJCalendar().addPropertyChangeListener("calendar",calendarChangeListener);
		dateChooserEnd = new JDateChooser();
		dateChooserEnd.getJCalendar().setTodayButtonVisible(true);
		dateChooserEnd.setDateFormatString("dd/MM/yyyy");
		dateChooserEnd.setMaxSelectableDate(new Date());
	    dateChooserEnd.getJCalendar().addPropertyChangeListener("calendar",calendarChangeListener);

		
		// "Generate" Button
		JButton btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateAction();
			}
		});
		
		

		lblErrorMsg = new JLabel("You can manually type date in this format: dd/mm/yyyy");
		lblErrorMsg.setForeground(Color.WHITE);
		
		btnThisMonth = new JButton("This Month");
		btnThisMonth.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.AM_PM, Calendar.AM);
				dateChooserStart.setDate(calendar.getTime());
				calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.AM_PM, Calendar.AM);
				dateChooserEnd.setDate(calendar.getTime());
			}
		});
		
		JButton btnLastMonth = new JButton("Last Month");
		btnLastMonth.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.MONTH, (Calendar.MONTH)%12);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.AM_PM, Calendar.AM);
				dateChooserStart.setDate(calendar.getTime());
				calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)+1);
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.AM_PM, Calendar.AM);
				dateChooserEnd.setDate(calendar.getTime());
			}
		});
		
		JButton btnThisYear = new JButton("This Year");
		btnThisYear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.MONTH, 0);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.AM_PM, Calendar.AM);
				dateChooserStart.setDate(calendar.getTime());
				calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.AM_PM, Calendar.AM);
				dateChooserEnd.setDate(calendar.getTime());
			}
		});
		
	
		
		
		GroupLayout gl_generateReport = new GroupLayout(generateReport);
		gl_generateReport.setHorizontalGroup(
			gl_generateReport.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_generateReport.createSequentialGroup()
					.addGap(10)
					.addGroup(gl_generateReport.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_generateReport.createSequentialGroup()
							.addGroup(gl_generateReport.createParallelGroup(Alignment.LEADING)
								.addComponent(lblErrorMsg)
								.addComponent(lblGenerateAReport))
							.addPreferredGap(ComponentPlacement.RELATED, 249, Short.MAX_VALUE)
							.addComponent(btnGenerate)
							.addGap(49))
						.addGroup(gl_generateReport.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblStartDate)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(dateChooserStart, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblEndDate)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(dateChooserEnd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnLastMonth)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnThisMonth)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnThisYear)))
					.addGap(23))
		);
		gl_generateReport.setVerticalGroup(
			gl_generateReport.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_generateReport.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_generateReport.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_generateReport.createSequentialGroup()
							.addComponent(lblGenerateAReport)
							.addPreferredGap(ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
							.addGroup(gl_generateReport.createParallelGroup(Alignment.LEADING)
								.addComponent(dateChooserStart, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_generateReport.createSequentialGroup()
									.addGroup(gl_generateReport.createParallelGroup(Alignment.TRAILING)
										.addGroup(gl_generateReport.createSequentialGroup()
											.addComponent(lblStartDate)
											.addPreferredGap(ComponentPlacement.RELATED))
										.addComponent(dateChooserEnd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_generateReport.createParallelGroup(Alignment.BASELINE)
											.addComponent(btnLastMonth)
											.addComponent(btnThisMonth)
											.addComponent(btnThisYear)))
									.addGroup(gl_generateReport.createParallelGroup(Alignment.BASELINE)
										.addComponent(btnGenerate)
										.addComponent(lblErrorMsg))))
							.addGap(22))
						.addGroup(gl_generateReport.createSequentialGroup()
							.addGap(12)
							.addComponent(lblEndDate)
							.addGap(57))))
		);
		generateReport.setLayout(gl_generateReport);

		// Add Component Listener to identified component
		cards.addComponentListener(this);
		curtain.addComponentListener(this);
		report.addComponentListener(this);
		cardGeneral.addComponentListener(this);
		cardExpense.addComponentListener(this);

		cardGeneral
				.setLayout(new MigLayout("",
						"[262.00,grow,center][260.00,grow,right]",
						"[280.00,grow, center]"));

		JPanel generalSummary = new JPanel();
		generalSummary.setBackground(new Color(255, 255, 255));
		cardGeneral.add(generalSummary, "cell 1 0,alignx center,aligny center");
		generalSummary.setLayout(new MigLayout("","[500, center]","[][30][30][30]"));
		
		lblNumRecords = new JLabel();
		generalSummary.add(lblNumRecords, "wrap");
		myBalance = new ColorSquare("Balance");
		myBalance.setBackground(new Color(0,191,255));
		generalSummary.add(myBalance, "wrap");
		myIncome = new ColorSquare("Income");
		myIncome.setBackground(new Color(50,205,50));
		generalSummary.add(myIncome, "wrap");
		myExpense = new ColorSquare("Expense");
		myExpense.setBackground(new Color(255,122,122));
		generalSummary.add(myExpense, "wrap");

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	private void generateAction(){
		try {
			myReportData = rptGen.generateReport(dateChooserStart.getDate(), dateChooserEnd.getDate());
			System.out.println(dateChooserStart.getDate());
			System.out.println(dateChooserEnd.getDate());
			//Check if there is records
			if (myReportData.getNumRecords() == 0){
				JOptionPane.showMessageDialog(this, "No records were found in that time range.", "Alert!", JOptionPane.WARNING_MESSAGE);
				return;
			}
			// Disappear
			initPieChart();
			initTable();
			initSummary();
			generateReport.setVisible(false);
			curtain.setVisible(false);
			
			// enable buttons
			btnGeneral.setEnabled(true);
			btnExpense.setEnabled(true);
			btnGenerateANew.setEnabled(true);
			
			startDateDisplay.setText(dateFormat.format(dateChooserStart.getDate()));
			endDateDisplay.setText(dateFormat.format(dateChooserEnd.getDate()));
		} catch (NullPointerException e1) {
			lblErrorMsg
					.setText("Date Field is blank! Please enter the date in the following format: dd/mm/yyyy");
		} catch (ParseException e1){
			lblErrorMsg
			.setText("Please enter the date in the following format: dd/mm/yyyy");
		} catch (DateOrderException e1) {
			// TODO Auto-generated catch block
			lblErrorMsg
					.setText("Please check the ordering of your start/end date");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
	}

	public void showScreen() {
		this.setVisible(true);
	}

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
		button.setSize(w, h);
		curtain.setSize(w, h);
		cards.setBounds(cards.getX(), cards.getY(), w, h);
		report.setSize(w, h);
		generateReport.setSize(w, 120);
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

	/*private PieDataset createDataset() {
		DefaultPieDataset result = new DefaultPieDataset();
		result.setValue("Balance", myReportData.getBalance());
		result.setValue("Expense", myReportData.getTotalExpense());
		result.setValue("Income", myReportData.getTotalIncome());
		return result;
	}*/
	
	private CategoryDataset createDatasetGeneral() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(myReportData.getIncomePercentage(), "Income", "");
		dataset.addValue(myReportData.getExpensePercentage(), "Expense", "");
		
		return dataset;
	}

	private PieDataset createDatasetExpense() {
		DefaultPieDataset result = new DefaultPieDataset();
		Vector<ReportCategory> list = myReportData.getExpenseCategory();
		for (int i = 0; i < list.size(); i++) {
			result.setValue(list.get(i).getCategory(), list.get(i)
					.getPercentage());
		}
		return result;

	}

	/**
	 * Creates a chart
	 */

	private JFreeChart createChart(PieDataset dataset, String title) {

		JFreeChart chart = ChartFactory.createPieChart(title, // chart title
				dataset, // data
				true, // include legend
				true, false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setExplodePercent("Income", 0.2);
		plot.setStartAngle(0);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setOutlineVisible(false);
		plot.setBackgroundPaint(Color.white);
		plot.setForegroundAlpha(0.5f);
		return chart;

	}
	
private JFreeChart createChart(CategoryDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
            "My Summary",         // chart title
            "Type",               // domain axis label
            "Percentage",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        //renderer.setSeriesPaint(0,new Color(0,191,255));
        renderer.setSeriesPaint(0, new Color(50,205,50));
        renderer.setSeriesPaint(1, new Color(255,122,122));

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;
        
    }

	private void initPieChart() {
		// PIE CHART IN GENERAL
		// This will create the dataset
		if (pieChartIndex == 1){
			cardGeneral.remove(1);
			cardExpense.remove(1);
			}
		CategoryDataset dataset = createDatasetGeneral();
		JFreeChart chart = createChart(dataset);
		JPanel generalPieChart = new JPanel();
		ChartPanel chartPanel = new ChartPanel(chart);
		generalPieChart.add(chartPanel);
		cardGeneral.add(generalPieChart, "cell 0 0,grow");
		generalPieChart.setLayout(new BoxLayout(generalPieChart,
				BoxLayout.X_AXIS));

		// Pie Chart in Expense
		// This will create the dataset
		PieDataset datasetexpense = createDatasetExpense();
		// based on the dataset we create the chart
		JFreeChart chartExpense = createChart(datasetexpense, "MY EXPENSE");
		ChartPanel chartPanelExpense = new ChartPanel(chartExpense);
		JPanel expensePieChart = new JPanel();
		cardExpense.add(expensePieChart, "cell 0 0,grow");
		expensePieChart.setLayout(new BoxLayout(expensePieChart,
				BoxLayout.X_AXIS));
		expensePieChart.add(chartPanelExpense);
	}

	private void initTable() {
		// Table
		if (pieChartIndex == 1){
			expenseTable.removeAll();
			cardExpense.remove(0);
		}
		tableModel = new InteractiveTableModel(columnNames);
		expenseTable.setLayout(new BoxLayout(expenseTable, BoxLayout.Y_AXIS));
		table = new JTable();
		table.setModel(tableModel);
		table.setAutoCreateRowSorter(true);
		table.setPreferredScrollableViewportSize(new Dimension(20, 10));
		JScrollPane scrollPane = new JScrollPane(table);
		// table.setFillsViewportHeight(true);
		expenseTable.add(Box.createVerticalGlue());
		expenseTable.add(scrollPane);
		expenseTable.add(Box.createVerticalGlue());
		cardExpense.add(expenseTable, "cell 1 0,grow");
	}

	private void initSummary() {
		String record = "record";
		if (myReportData.getNumRecords() > 1)
			record = "records";
		lblNumRecords.setText(""+myReportData.getNumRecords()+ " "+record +" were found");
		myIncome.setLabelAmount(myReportData.getTotalIncome());
		myIncome.setLabelPercentage(myReportData.getIncomePercentage());
		myExpense.setLabelAmount(myReportData.getTotalExpense());
		myExpense.setLabelPercentage(myReportData.getExpensePercentage());
		myBalance.setLabelAmount(myReportData.getBalance());
	}

	/**
	 * TableModel for report
	 * 
	 * @author A0087091B
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
		
		DecimalFormat money = new DecimalFormat("#,##0.00");        
		DecimalFormat percent = new DecimalFormat("0.0");

		public InteractiveTableModel(String[] columnNames) {
			this.columnNames = columnNames;
			// dataVector = new Vector<ReportCategory>();
			dataVector = myReportData.getExpenseCategory();
		}

		public String getColumnName(int column) {
			return columnNames[column];
		}

		public Class<?> getColumnClass(int column) {
			switch (column) {
			case CATEGORY_INDEX:
				return String.class;
			case FREQUENCY_INDEX:
				return Integer.class;
			case AMOUNT_INDEX:
				return Double.class;
			case PERCENTAGE_INDEX:
				return Double.class;
			case AMTFEQ_INDEX:
				return Double.class;
			default:
				return String.class;
			}
		}

		public Object getValueAt(int row, int column) {
			ReportCategory report = (ReportCategory) dataVector.get(row);
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
	
	/**
	 * Colored square for report
	 * @author A0087091B
	 *
	 */
	public class ColorSquare extends JPanel{
		
		private JLabel lblName = new JLabel("");
		private JLabel lblAmount = new JLabel("");
		private JLabel lblPercentage = new JLabel("");
		private int WIDTH = 200;
		private int HEIGHT = 20;
		DecimalFormat formatter = new DecimalFormat("#,##0.00");        
		DecimalFormat df = new DecimalFormat("#.#");
		
		public ColorSquare(String name){
			this.setLabelName(name);
			this.setLayout(new MigLayout("","[50][50][15]","[10][10]"));
			this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
			this.add(lblName,"span 2");
			this.add(lblPercentage, "wrap");
			this.add(lblAmount, "span 3");
		}
		
		public void changeBackground(Color myColor){
			this.setBackground(myColor);
		}
		
		public void setLabelName(String name){
			lblName.setText(name);
			lblName.setForeground(Color.WHITE);
			lblName.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		}
		
		public void setLabelAmount(double amount){
			lblAmount.setText("$"+ formatter.format(amount));
			lblAmount.setForeground(Color.WHITE);
		}
		
		public void setLabelPercentage(double percent){
			lblPercentage.setText(""+df.format(percent)+"%");
			lblPercentage.setForeground(Color.WHITE);
		}
		
		
	}
}