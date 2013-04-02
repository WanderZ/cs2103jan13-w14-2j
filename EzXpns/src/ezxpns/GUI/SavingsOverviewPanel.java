package ezxpns.GUI;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ezxpns.data.NWSGenerator;
import ezxpns.data.NWSdata;
import ezxpns.data.records.ExpenseType;

/**
 * The Savings Panel for the home screen - to display the Needs-Wants-Savings
 * Ratio
 * 
 * @author tingzhe A0087091B
 */
@SuppressWarnings("serial")
public class SavingsOverviewPanel extends JPanel {

	private NWSGenerator nwsGen;
	private NWSBarPanel myBars;
	private int DONUT_DIM = 250;

	public SavingsOverviewPanel(NWSGenerator dataGenerator) {
		super();
		nwsGen = dataGenerator;
		setLayout(new BorderLayout());
		JLabel lblNWS = new JLabel("Needs, Wants, Savings");
		lblNWS.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		this.add(lblNWS, BorderLayout.NORTH);
		setBackground(new Color(255, 255, 255));
		
		drawBars();

		/*
		 * JPanel panelDonut = new JPanel(); panelDonut.setBackground(new
		 * Color(255, 255, 255)); add(panelDonut, BorderLayout.CENTER);
		 * panelDonut.setLayout(new BoxLayout(panelDonut, BoxLayout.X_AXIS));
		 * 
		 * NWSdata data = nwsGen.getNWSdataCopy();
		 * 
		 * // NEEDS //DonutGraphics myNeeds = new
		 * DonutGraphics(nwsGen.getNeeds(), nwsGen.getMonthlyNeeds(),
		 * ExpenseType.NEED); DonutGraphics myNeeds = new DonutGraphics(70,
		 * data.getNeeds()* 100, ExpenseType.NEED); myNeeds.setPreferredSize(new
		 * Dimension(DONUT_DIM,DONUT_DIM)); myNeeds.setBackground(new Color(255,
		 * 255, 255)); panelDonut.add(myNeeds);
		 * 
		 * // WANTS //DonutGraphics myWants = new
		 * DonutGraphics(nwsGen.getWants(), nwsGen.getMonthlyWants(),
		 * ExpenseType.WANT); DonutGraphics myWants = new DonutGraphics(50,
		 * data.getWants() * 100, ExpenseType.WANT);
		 * myWants.setPreferredSize(new Dimension(DONUT_DIM,DONUT_DIM));
		 * myWants.setBackground(new Color(255, 255, 255));
		 * panelDonut.add(myWants);
		 * 
		 * // SAVES //DonutGraphics mySaves = new
		 * DonutGraphics(nwsGen.getSavings(), nwsGen.getMonthlySavings(),
		 * ExpenseType.SAVE); DonutGraphics mySaves = new DonutGraphics(10,
		 * data.getSavings() * 100, ExpenseType.SAVE);
		 * mySaves.setPreferredSize(new Dimension(DONUT_DIM, DONUT_DIM));
		 * mySaves.setBackground(new Color(255, 255, 255));
		 * panelDonut.add(mySaves);
		 */

	}
	
	/**
	 * Draw the bar chart for NWS
	 */
	private void drawBars(){
		if (myBars != null)
			this.remove(myBars);
		myBars = new NWSBarPanel(nwsGen);
		myBars.setBackground(new Color(255, 255, 255));
		this.add(myBars, BorderLayout.CENTER);
	}

	/**
	 * This is the method to call to update this panel
	 */
	public void update() {
		nwsGen.updateNWSdata(); // should I remove this?
		drawBars();
		this.validate();
	}

	// INNER CLASS
	/**
	 * Graphics for Needs, Wants, Savings
	 * 
	 * @author tingzhe A0087091B
	 * 
	 */
	public class DonutGraphics extends JPanel {

		private int OUTER_RADIUS = 130;
		private int INNER_RADIUS = 60;
		private double current;
		private double target;
		private ExpenseType type;
		private int status = 0; // 0 - normal, 1 - exceed
		private int VERTICAL_OFFSET = 60; // If this is 0, donut will be at the
											// top

		/**
		 * DonutGraphics constructor with relevant information on ExpenseTarget
		 * 
		 * @param current
		 * @param target
		 * @param type
		 */
		public DonutGraphics(double current, double target, ExpenseType type) {
			this.current = current;
			this.target = target;
			this.type = type;
		}

		/**
		 * Paint the donut graphics
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			status = checkStatus();

			// Base Color
			if (status == 0)
				g2d.setColor(type.getBaseColor()); // color will be based on
													// enum ExpenseType (edit
													// later)
			else
				g2d.setColor(type.getNormalColor());
			g2d.fill(generateDonut(0, 0 + VERTICAL_OFFSET, INNER_RADIUS,
					OUTER_RADIUS));

			// Second Color
			if (status == 0)
				g2d.setColor(type.getNormalColor()); // next color
			else
				g2d.setColor(type.getExceedColor());
			g2d.fill(generateArc());

			// Name
			g.setColor(Color.BLACK);
			Font myFont = new Font("fontname", Font.PLAIN, 20);
			FontMetrics fm = g.getFontMetrics();
			g.drawString(type.name,
					OUTER_RADIUS / 2 - fm.stringWidth(type.name) / 2,
					OUTER_RADIUS / 2 + fm.getHeight() / 4 + VERTICAL_OFFSET);

			// Ratio
			String ratio = "" + current + "/" + target;
			g.drawString(ratio, OUTER_RADIUS / 2 - fm.stringWidth(ratio) / 2,
					OUTER_RADIUS + OUTER_RADIUS / 8 + VERTICAL_OFFSET);

		}

		/**
		 * Generates a donut shape from the given location and radii by
		 * subtracting an inner circular Area from an outer one.
		 * 
		 * @return a Shape object that will be filled by Graphics2D later
		 */
		private Shape generateDonut(double x, double y, double innerRadius,
				double outerRadius) {
			Area a1 = new Area(new Ellipse2D.Double(x, y, outerRadius,
					outerRadius));
			double innerOffset = (outerRadius - innerRadius) / 2;
			Area a2 = new Area(new Ellipse2D.Double(x + innerOffset, y
					+ innerOffset, innerRadius, innerRadius));
			a1.subtract(a2);
			return a1;
		}

		/**
		 * Generate a shape similar to donut, with a particular section missing
		 * 
		 * @return a Shape object that will be filled by Graphics2D later
		 */
		private Shape generateArc() {
			Area a1 = new Area(new Arc2D.Double(0, 0 + VERTICAL_OFFSET,
					OUTER_RADIUS, OUTER_RADIUS, 90, getCurrentAngle(),
					Arc2D.PIE)); // change 180 to something else
			double innerOffset = (OUTER_RADIUS - INNER_RADIUS) / 2;
			Area a2 = new Area(
					new Ellipse2D.Double(0 + innerOffset, 0 + VERTICAL_OFFSET
							+ innerOffset, INNER_RADIUS, INNER_RADIUS));
			a1.subtract(a2);
			return a1;
		}

		/**
		 * Check if current ExpenseType is over the NWS target or not
		 * 
		 * @return 1 if exceed, 0 if not
		 */
		private int checkStatus() {
			if (current > target)
				return 1;
			return 0;
		}

		/**
		 * Returns the angle for the current sector
		 * 
		 * @return
		 */
		private double getCurrentAngle() {
			if (status == 0)
				return current / target * 360;
			else {
				if (current == target * 2)
					return 360;
				return (current - target) / target * 360;
			}
		}
	}

	/**
	 * A bar chart displaying Needs, Wants, and Savings information
	 * @author tingzhe A0087091B
	 *
	 */
	public class NWSBarPanel extends JPanel {

		double[] value;
		double[] target;
		String[] type = { "Needs", "Wants", "Savings" };
		ExpenseType[] exType = { ExpenseType.NEED,ExpenseType.WANT, ExpenseType.SAVE};
        DecimalFormat df = new DecimalFormat("#.##");

		
		NWSdata nwsData;

		public NWSBarPanel(NWSGenerator nwsGen) {
			this.nwsData = nwsGen.getNWSdataCopy();
			value = new double[]{nwsData.getCurrNeedsRatio()*100, nwsData.getCurrWantsRatio()*100, nwsData.getCurrSavingsRatio()*100}; 
			target = new double[]{nwsData.getTargetNeedsRatio()*100, nwsData.getTargetWantsRatio()*100, nwsData.getTargetSavingsRatio()*100};
			System.out.println(nwsData.getTargetNeedsRatio()*100);
		}

		

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;

			if (value == null || value.length == 0)
				return;
			double minValue = 0;
			double maxValue = 0;
			for (int i = 0; i < value.length; i++) {
				if (minValue > value[i])
					minValue = value[i];
				if (maxValue < value[i])
					maxValue = value[i];
			}
			for (int i = 0; i < target.length; i++) {
				if (minValue > target[i])
					minValue = target[i];
				if (maxValue < target[i])
					maxValue = target[i];
			}
			Dimension dim = getSize();
			int clientWidth = dim.width;
			int clientHeight = dim.height;
			// int barWidth = clientWidth / value.length;
			int barWidth = 50;
			// Font titleFont = new Font("Book Antiqua", Font.BOLD, 15);
			// FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
			Font labelFont = new Font("Book Antiqua", Font.BOLD, 15);
			FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);
			Font ratioFont = new Font("Book Antiqua", Font.PLAIN, 15);
			FontMetrics ratioFontMetrics = g.getFontMetrics(ratioFont);
			// int titleWidth = titleFontMetrics.stringWidth(title);
			// int q = titleFontMetrics.getAscent();
			// int p = (clientWidth - titleWidth) / 2;
			// graphics.setFont(titleFont);
			// graphics.drawString(title, p, q);
			// int top = titleFontMetrics.getHeight();
			int bottom = labelFontMetrics.getHeight() * 2;
			if (maxValue == minValue)
				return;
			System.out.println(maxValue);
			if (maxValue > 100)
				maxValue = Math.log(maxValue-100) + 100;
			System.out.println(maxValue);
			double scale = (clientHeight - bottom) / (maxValue - minValue);
			System.out.println(scale);
			int q = clientHeight - labelFontMetrics.getDescent();
			g.setFont(labelFont);

			for (int j = 0; j < value.length; j++) {
				int valueP = j * 2 * barWidth + (clientWidth/5);
				int valueQ = 0;
				int valueQT = 0;
				int height = (int) (value[j] * scale);
				int targetHeight = (int) (target[j] * scale);
				if (value[j] >= 0)
					valueQ += (int) ((maxValue - value[j]) * scale);
				else {
					valueQ += (int) (maxValue * scale);
					height = -height;
				}
				if (target[j] >= 0)
					valueQT += (int) ((maxValue - target[j]) * scale);
				else{
					valueQT += (int) (maxValue * scale);
					targetHeight = -targetHeight;
				}
				
				g.setColor(exType[j].getNormalColor());
				g.fillRect(valueP, valueQ, barWidth - 2, height);
				g2d.setColor(exType[j].getExceedColor());
				g2d.setStroke(new BasicStroke(4));
				g2d.drawRect(valueP, valueQT, barWidth - 2, targetHeight);
				g.setColor(Color.black);
				int labelWidth = labelFontMetrics.stringWidth(type[j]);
				int ratioWidth = ratioFontMetrics.stringWidth(""+df.format(value[j])+"%/"+df.format(target[j])+"%");
				int p = j * 2 * barWidth + (clientWidth/5) + (barWidth - labelWidth) / 2;
				int pRatio = j * 2 * barWidth + (clientWidth/5) + (barWidth - ratioWidth) / 2;
				g.setFont(labelFont);
				g.drawString(type[j], p, q - ratioFontMetrics.getAscent());
				g.setFont(ratioFont);
				g.drawString(""+df.format(value[j])+"%/"+df.format(target[j])+"%", pRatio, clientHeight - ratioFontMetrics.getDescent());
			}
		}
	}
}
