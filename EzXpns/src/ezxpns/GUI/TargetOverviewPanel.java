package ezxpns.GUI;

import javax.swing.JPanel;

import ezxpns.data.TargetManager;
import ezxpns.data.Bar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.SystemColor;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ScrollPaneConstants;
import net.miginfocom.swing.MigLayout;

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
		columnpanel.setBackground(Color.white);

		targetScrollPane.setPreferredSize(new Dimension(50, 50));
		
		
		updateTargets();
		updateAlerts();

	}// end constructor
	
	/**
	 * displays a list of targets in targetScrollPane
	 */
	public void updateTargets(){
		for (int i = targetMgr.getOrderedBar().size() - 1; i >= 0; i--) {
			Bar bar = targetMgr.getOrderedBar().get(i);

			JPanel rowPanel = new JPanel();
			rowPanel.setPreferredSize(new Dimension(200, 20));
			columnpanel.add(rowPanel);
			rowPanel.setLayout(new FlowLayout());

			JLabel lblBar = new JLabel(bar.getTarget().getCategory().getName());
			rowPanel.add(lblBar);
			lblBar.setBackground(new Color(154, 205, 50));
			lblBar.setHorizontalAlignment(SwingConstants.CENTER);

			String cAmtName = "$" + MONEY_FORMAT.format(bar.getCurrentAmt());
			JLabel lblCurrentAmt = new JLabel(cAmtName);
			rowPanel.add(lblCurrentAmt);
			lblCurrentAmt.setHorizontalAlignment(SwingConstants.CENTER);

			String tAmtName = "$"
					+ MONEY_FORMAT.format(bar.getTargetAmt()); // to be
			// changed
			JLabel lblTargetAmt = new JLabel("/" + tAmtName);
			rowPanel.add(lblTargetAmt);
			lblTargetAmt.setHorizontalAlignment(SwingConstants.CENTER);

			switch (bar.getColor()) {
			case HIGH:
				lblCurrentAmt.setForeground(new Color(255, 20, 30)); // red
				break;
			case MEDIUM:
				lblCurrentAmt.setForeground(new Color(255, 150, 20)); // orange
				break;
			case LOW:
				lblCurrentAmt.setForeground(new Color(255, 245, 40)); // yellow
				break;
			case SAFE:
				lblCurrentAmt.setForeground(new Color(71, 255, 102)); // orange
				break;
			default:
				lblCurrentAmt.setForeground(new Color(122, 122, 122)); // dark
				// gray
			}
		}
		
		
	}
	
	/**
	 * Display number of Alerts in the tagsPane
	 */
	public void updateAlerts(){
		lblalertnumber = new JLabel();
		lblalertnumber.setText("(" + targetMgr.getAlerts().size() + ")");
		tagsPane.add(lblalertnumber, "cell 2 0,alignx left,aligny top");

	}
	
	/** 
	 * The method to call to update this panel 
	 */
	public void update() {
		//TODO: update the stuff and such in this panel
		this.validate();
	}
}
