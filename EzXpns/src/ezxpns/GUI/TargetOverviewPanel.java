package ezxpns.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import ezxpns.data.Bar;
import ezxpns.data.TargetManager;
import javax.swing.JSeparator;

/**
 * This is the panel in which will be slotted into the MainGUI (the main
 * interface) as the display for the visual breakdown of the targets on the user
 * defined categories (Food, Transport, etc...)
 */
@SuppressWarnings("serial")
public class TargetOverviewPanel extends JPanel {

	private TargetManager targetMgr;
	private final DecimalFormat MONEY_FORMAT = new DecimalFormat(
			"$###,###,##0.00");
	private final DecimalFormat TWO_DP = new DecimalFormat("#.##");
	private JPanel largeBorderLayoutPanel;
	private JPanel tagsPane;
	private JScrollPane targetScrollPane;
	private JPanel smallBorderLayoutpanel;
	private JPanel columnPanel;
	private JPanel separatorPanel;
	private JLabel lblDaysTill;
	private JLabel lblMonth;
	String[] monthName = {"January", "February",
            "March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"};
	private JLabel lblCountDown;
	private JSeparator separator;

	/**
	 * 
	 * @param targetMgrRef
	 */
	public TargetOverviewPanel(TargetManager targetMgrRef) {
		this.targetMgr = targetMgrRef;
		setLayout(new BorderLayout(0, 0));

		largeBorderLayoutPanel = new JPanel();
		add(largeBorderLayoutPanel);
		largeBorderLayoutPanel.setLayout(new BorderLayout(0, 0));

		tagsPane = new JPanel();
		tagsPane.setBackground(new Color(255, 255, 255));
		largeBorderLayoutPanel.add(tagsPane, BorderLayout.NORTH);

		targetScrollPane = new JScrollPane();
		targetScrollPane.setBorder(BorderFactory.createEmptyBorder());
		tagsPane.setLayout(new MigLayout("", "[222.00]50[30][100]", "[25][16px]0[][1]0"));


		targetScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		largeBorderLayoutPanel.add(targetScrollPane, BorderLayout.CENTER);

		smallBorderLayoutpanel = new JPanel();
		smallBorderLayoutpanel.setBackground(new Color(255, 255, 255));
		smallBorderLayoutpanel.setForeground(new Color(255, 102, 204));
		targetScrollPane.setViewportView(smallBorderLayoutpanel);
		smallBorderLayoutpanel.setLayout(new BorderLayout(0, 0));

		columnPanel = new JPanel();
		columnPanel.setBackground(new Color(255, 255, 255));
		smallBorderLayoutpanel.add(columnPanel, BorderLayout.NORTH);
		columnPanel.setLayout(new GridLayout(0, 1));

		targetScrollPane.setPreferredSize(new Dimension(50, 50));

		updateTargets();
		updateAlerts();

	}// end constructor

	/**
	 * displays a list of targets in targetScrollPane
	 */
	public void updateTargets() {
		columnPanel.removeAll();
		smallBorderLayoutpanel.add(columnPanel, BorderLayout.NORTH);
		columnPanel.setLayout(new GridLayout(0, 1, 0, 1));
		
		if (targetMgr.getOrderedBar().size() == 0)
			columnPanel.add(new JLabel("You don't have a target this month. Why not add one today?"));
		
		for (int i = targetMgr.getOrderedBar().size() - 1; i >= 0; i--) {
			Bar bar = targetMgr.getOrderedBar().get(i);

			JPanel rowPanel = new JPanel();
			rowPanel.setPreferredSize(new Dimension(200, 50)); // width (just
																// make it big
																// enough for
																// scroll to
																// appear),
																// height
			rowPanel.setBackground(new Color(255, 255, 255));
			columnPanel.add(rowPanel);
			//rowPanel.setLayout(new BorderLayout());
			rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

			// CATEGORY LABEL
			JLabel lblBar = new JLabel(bar.getTarget().getCategory().getName());
			//rowPanel.add(lblBar, BorderLayout.WEST);
			rowPanel.add(lblBar);
			lblBar.setPreferredSize(new Dimension(100, 50));

			// BAR GRAPHICS
			BarGraphic myBarGraphics = new BarGraphic(bar);
			myBarGraphics.setPreferredSize(new Dimension(150,50));
			myBarGraphics.setBackground(new Color(255,255,255));
			//rowPanel.add(myBarGraphics, BorderLayout.CENTER);
			rowPanel.add(myBarGraphics);
			
			// SUB LAYOUT FOR AMOUNT
			JPanel subPanel = new JPanel();
			//subPanel.setPreferredSize(new Dimension (200,60));
			subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));
			subPanel.setBackground(new Color(255, 255, 255));

			//rowPanel.add(subPanel, BorderLayout.EAST);
			rowPanel.add(subPanel);
			
			// CURRENT AMOUNT/TARGET AMOUNT
			JLabel lblCurrentAmt = new JLabel(MONEY_FORMAT.format(bar.getCurrentAmt()) + " spent");
			lblCurrentAmt.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
			lblCurrentAmt.setHorizontalAlignment(SwingConstants.LEFT);
			subPanel.add(lblCurrentAmt);
			JLabel lblRemainingAmt = new JLabel(MONEY_FORMAT.format(bar.getRemainingAmt())+ " left");
			lblRemainingAmt.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
			lblRemainingAmt.setHorizontalAlignment(SwingConstants.LEFT);
			subPanel.add(lblRemainingAmt);
		}

	}

	/**
	 * Display number of Alerts in the tagsPane
	 */
	public void updateAlerts() {
		
		tagsPane.removeAll();
		
		// lblTargets
		JLabel lblTargets = new JLabel("targets");
		lblTargets.setForeground(new Color(255, 153, 0));
		lblTargets.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        Calendar cal = Calendar.getInstance();
        String target;
        if (targetMgr.getOrderedBar().size() == 1)
        	target = "Budget";
        else 
        	target = "Budgets";
		lblTargets.setText("<html><b><font size=\"26\">"+targetMgr.getOrderedBar().size()+"</font></b>"+" "+target+" in "+monthName[cal.get(Calendar.MONTH)]+"</html>");
		tagsPane.add(lblTargets, "cell 0 0,alignx left,aligny top");
		
		// lblCountdown
		int remainingDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH);
		String day;
		if (remainingDay == 1)
			day = "day";
		else
			day = "days";
		lblCountDown = new JLabel(""+remainingDay+" more "+day+" to "+ monthName[(cal.get(Calendar.MONTH)+1)%12]);
		tagsPane.add(lblCountDown, "cell 0 1");
		
		separator = new JSeparator();
		tagsPane.add(separator, "cell 0 2,span,growx, wrap");
	
		// ALERTS
		int numAlert = targetMgr.getAlerts().size();
		if (numAlert > 0){
			String alert;
			if (numAlert == 1)
				alert = "Alert";
			else
				alert = "Alerts";
			JLabel lblAlert = new JLabel("<html><b><font size=\"26\">"+numAlert+"</font></b>"+" "+alert+"</html>");
			lblAlert.setForeground(Color.RED);
			lblAlert.setFont(new Font("Lucida Grande", Font.BOLD, 20));
			tagsPane.add(lblAlert, "cell 2 0");

		}
		
		/*// lblCountdown
		JLabel lblCountdown = new JLabel("Countdown");
		lblCountdown.setForeground(new Color(255, 153, 0));
		lblCountdown.setFont(new Font("Lucida Grande", Font.BOLD, 48));
		tagsPane.add(lblCountdown, "cell 1 0 1 2");
		int remainingDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH);
		lblCountdown.setText(""+remainingDay);
		
		// lblDaysTill
		lblDaysTill = new JLabel("days till " + monthName[(cal.get(Calendar.MONTH)+1)%12]);
		lblDaysTill.setForeground(new Color(255, 153, 0));
		tagsPane.add(lblDaysTill, "cell 2 0,aligny bottom");
		
		// lblStatus
		int numAlert = targetMgr.getAlerts().size();
		String status;
		if (numAlert == 0)
			status = "You're ontrack! Good job!";
		else
			status = ""+ numAlert + " targets in danger!";
		JLabel lblStatus = new JLabel("status");
		lblStatus.setText(status);
		tagsPane.add(lblStatus, "cell 0 1,alignx left,aligny top");
		
		// lblMonth
		lblMonth = new JLabel("month");
		lblMonth.setForeground(new Color(255, 153, 0));
		tagsPane.add(lblMonth, "cell 2 1");
		lblMonth.setText(monthName[(cal.get(Calendar.MONTH)+1)%12]);*/

	}
	public void update() {
		//TODO: update the stuff and such in this panel
		this.updateAlerts();
		this.updateTargets();
		this.validate();
	}
	
	
	/**
	 * Bar Graphics (JPanel) displayed in TargetOverviewPanel
	 * 
	 * @author tingzhe
	 * 
	 */
	public class BarGraphic extends JPanel {

		private Bar bar;
		private int WIDTH = 150;
		private int HEIGHT = 40;
		private int X = 15;

		// Constructor: Add values for target/ratio
		public BarGraphic(Bar bar) {
			this.bar = bar;
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.GRAY); // base color
			g.fillRect(0, X, WIDTH, HEIGHT); // x, y, width, height
			g.setColor(bar.getBarColor().getColor());
			double remaining = bar.getRemainingAmt();
			int barWidth = (int) ((bar.getCurrentAmt() / bar.getTargetAmt()) * WIDTH);
			if (barWidth > WIDTH)
				barWidth = 150;
			g.fillRect(0, X, barWidth, HEIGHT);
			
			//font
			Font percentage = new Font("Book Antiqua", Font.BOLD, 15);
			FontMetrics percentageFontMetrics = g.getFontMetrics(percentage);
			String stringPercentage = "" + TWO_DP.format(bar.getCurrentPercentage()) + "%";
			g.setColor(Color.WHITE);
			g.drawString(stringPercentage, WIDTH-percentageFontMetrics.stringWidth(stringPercentage),HEIGHT);
		}
	}
}
