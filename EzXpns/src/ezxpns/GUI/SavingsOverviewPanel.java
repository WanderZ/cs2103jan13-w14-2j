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
import javax.swing.SwingConstants;

/**
 * The Savings Panel for the home screen - to display the Needs-Wants-Savings
 * Ratio
 * 
 * @author A0087091B
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
		lblNWS.setHorizontalAlignment(SwingConstants.CENTER);
		lblNWS.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		this.add(lblNWS, BorderLayout.NORTH);
		
		drawBars();
	}
	
	/**
	 * Draw the bar chart for NWS
	 */
	private void drawBars(){
		if (myBars != null)
			this.remove(myBars);
		myBars = new NWSBarPanel(nwsGen);
		this.add(myBars, BorderLayout.CENTER);
	}

	/**
	 * This is the method to call to update this panel
	 */
	public void update() {
		nwsGen.updateNWSdata(); 
		drawBars();
		this.validate();
	}

	/**
	 * A bar chart displaying Needs, Wants, and Savings information
	 * @author A0087091B
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
			int barWidth = 50;
			Font labelFont = new Font("Book Antiqua", Font.BOLD, 15);
			FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);
			Font ratioFont = new Font("Book Antiqua", Font.PLAIN, 15);
			FontMetrics ratioFontMetrics = g.getFontMetrics(ratioFont);
			int bottom = labelFontMetrics.getHeight() * 2;
			if (maxValue == minValue)
				return;
			if (maxValue > 100)
				maxValue = Math.log(maxValue-100) + 100;
			double scale = (clientHeight - bottom) / (maxValue - minValue);
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
