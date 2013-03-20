package ezxpns.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import ezxpns.data.Bar;
import ezxpns.data.TargetManager;

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
	private JPanel columnpanel;
	private JLabel lblTargets;
	private JLabel lblalertnumber;

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
		largeBorderLayoutPanel.add(tagsPane, BorderLayout.NORTH);
		tagsPane.setLayout(new MigLayout("", "[37px][14px][]", "[14px]"));

		lblTargets = new JLabel("Targets");
		lblTargets.setHorizontalAlignment(SwingConstants.CENTER);
		tagsPane.add(lblTargets, "cell 0 0,alignx left,aligny top");

		targetScrollPane = new JScrollPane();

		targetScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		largeBorderLayoutPanel.add(targetScrollPane, BorderLayout.CENTER);

		smallBorderLayoutpanel = new JPanel();
		targetScrollPane.setViewportView(smallBorderLayoutpanel);
		smallBorderLayoutpanel.setLayout(new BorderLayout(0, 0));

		columnpanel = new JPanel();
		smallBorderLayoutpanel.add(columnpanel, BorderLayout.NORTH);
		columnpanel.setLayout(new GridLayout(0, 1, 0, 1));

		targetScrollPane.setPreferredSize(new Dimension(50, 50));

		updateTargets();
		updateAlerts();

	}// end constructor

	/**
	 * displays a list of targets in targetScrollPane
	 */
	public void updateTargets() {
		for (int i = targetMgr.getOrderedBar().size() - 1; i >= 0; i--) {
			Bar bar = targetMgr.getOrderedBar().get(i);

			JPanel rowPanel = new JPanel();
			rowPanel.setPreferredSize(new Dimension(200, 50)); // width (just
																// make it big
																// enough for
																// scroll to
																// appear),
																// height
			columnpanel.add(rowPanel);
			rowPanel.setLayout(new BorderLayout());

			// CATEGORY LABEL
			JLabel lblBar = new JLabel(bar.getTarget().getCategory().getName());
			rowPanel.add(lblBar, BorderLayout.WEST);
			lblBar.setPreferredSize(new Dimension(100, 20));
			lblBar.setBackground(new Color(154, 205, 50));

			// BAR GRAPHICS
			BarGraphic myBarGraphics = new BarGraphic(bar);
			rowPanel.add(myBarGraphics, BorderLayout.CENTER);

			// CURRENT AMOUNT/TARGET AMOUNT
			JLabel lblCurrentAmt = new JLabel(MONEY_FORMAT.format(bar
					.getCurrentAmt())
					+ "/"
					+ MONEY_FORMAT.format(bar.getTargetAmt()));
			rowPanel.add(lblCurrentAmt, BorderLayout.EAST);

		}

	}

	/**
	 * Display number of Alerts in the tagsPane
	 */
	public void updateAlerts() {
		lblalertnumber = new JLabel();
		lblalertnumber.setText("(" + targetMgr.getAlerts().size() + ")");
		tagsPane.add(lblalertnumber, "cell 2 0,alignx left,aligny top");

	}
	public void update() {
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
		private int HEIGHT = 20;
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
			g.drawString(stringPercentage, WIDTH-percentageFontMetrics.stringWidth(stringPercentage),HEIGHT+10);
		}
	}
}
