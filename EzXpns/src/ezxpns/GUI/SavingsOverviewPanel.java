package ezxpns.GUI;

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

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import ezxpns.data.NWSGenerator;
import ezxpns.data.records.ExpenseType;
/**
 * The Savings Panel for the home screen - to display the Needs-Wants-Savings Ratio
 * @author tingzhe A0087091B
 */
@SuppressWarnings("serial")
public class SavingsOverviewPanel extends JPanel {
	
	private NWSGenerator nwsGen;
	private int DONUT_DIM = 250;
	//NWSGenerator nwsGenRef // parameter
	public SavingsOverviewPanel() {
		super();
		//this.nwsGen = nwsGenRef; // reference
		
		setLayout(new BorderLayout(0, 0));
		JPanel panelDonut = new JPanel();
		add(panelDonut, BorderLayout.CENTER);
		panelDonut.setLayout(new BoxLayout(panelDonut, BoxLayout.X_AXIS));
		
		// NEEDS
		//DonutGraphics myNeeds = new DonutGraphics(nwsGen.getNeeds(), nwsGen.getMonthlyNeeds(), ExpenseType.NEED);
		DonutGraphics myNeeds = new DonutGraphics(70, 50, ExpenseType.NEED);
		myNeeds.setPreferredSize(new Dimension(DONUT_DIM,DONUT_DIM));
		panelDonut.add(myNeeds);
		
		// WANTS
		//DonutGraphics myWants = new DonutGraphics(nwsGen.getWants(), nwsGen.getMonthlyWants(), ExpenseType.WANT);
		DonutGraphics myWants = new DonutGraphics(50, 40, ExpenseType.WANT);
		myWants.setPreferredSize(new Dimension(DONUT_DIM,DONUT_DIM));
		panelDonut.add(myWants);
		
		// SAVES
		//DonutGraphics mySaves = new DonutGraphics(nwsGen.getSavings(), nwsGen.getMonthlySavings(), ExpenseType.SAVE);
		DonutGraphics mySaves = new DonutGraphics(10, 20, ExpenseType.SAVE);
		mySaves.setPreferredSize(new Dimension(DONUT_DIM, DONUT_DIM));
		panelDonut.add(mySaves);
		
	}
	
	/**
	 * This is the method to call to update this panel
	 */
	public void update() {
		//TODO: Update This Panel
		this.validate();
	}
	
	// INNER CLASS
	/**
	 * Graphics for Needs, Wants, Savings
	 * @author tingzhe A0087091B
	 *
	 */
	public class DonutGraphics extends JPanel{
		
		private int OUTER_RADIUS = 130;
		private int INNER_RADIUS = 60;
		private double current;
		private double target;
		private ExpenseType type;
		private int status = 0; // 0 - normal, 1 - exceed
		private int VERTICAL_OFFSET = 60; // If this is 0, donut will be at the top
		
		/**
		 * DonutGraphics constructor with relevant information on ExpenseTarget
		 * @param current
		 * @param target
		 * @param type
		 */
		public DonutGraphics(double current, double target, ExpenseType type){
			this.current = current;
			this.target = target;
			this.type = type; 
		}
		
		/**
		 * Paint the donut graphics
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
	        Graphics2D g2d = (Graphics2D)g;
			status = checkStatus();
			
			// Base Color
			if (status == 0)
				g2d.setColor(type.getBaseColor()); // color will be based on enum ExpenseType (edit later)
			else
				g2d.setColor(type.getNormalColor());
			g2d.fill(generateDonut(0,0+VERTICAL_OFFSET,INNER_RADIUS,OUTER_RADIUS));
			
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
			g.drawString(type.name, OUTER_RADIUS/2-fm.stringWidth(type.name)/2, OUTER_RADIUS/2+fm.getHeight()/4+VERTICAL_OFFSET);
			
			// Ratio
			String ratio = ""+current+"/"+target;
			g.drawString(ratio, OUTER_RADIUS/2-fm.stringWidth(ratio)/2, OUTER_RADIUS+OUTER_RADIUS/8+VERTICAL_OFFSET);

		}
		
		

		/**
	     * Generates a donut shape from the given location and radii by subtracting
	     * an inner circular Area from an outer one.
	     * @return a Shape object that will be filled by Graphics2D later
	     */
	    private Shape generateDonut(double x, double y,
	            double innerRadius, double outerRadius) {
	        Area a1 = new Area(new Ellipse2D.Double(x, y, outerRadius, outerRadius));
	        double innerOffset = (outerRadius - innerRadius)/2;
	        Area a2 = new Area(new Ellipse2D.Double(x + innerOffset, y + innerOffset, 
	                innerRadius, innerRadius));
	        a1.subtract(a2);
	        return a1;
	    }
	    
	    /**
	     * Generate a shape similar to donut, with a particular section missing
	     * @return a Shape object that will be filled by Graphics2D later
	     */
	    private Shape generateArc(){
	    	Area a1 = new Area(new Arc2D.Double(0, 0+VERTICAL_OFFSET, OUTER_RADIUS, OUTER_RADIUS, 90, getCurrentAngle(), Arc2D.PIE)); // change 180 to something else
	        double innerOffset = (OUTER_RADIUS - INNER_RADIUS)/2;
	        Area a2 = new Area(new Ellipse2D.Double(0 + innerOffset, 0+VERTICAL_OFFSET + innerOffset, 
	        		INNER_RADIUS, INNER_RADIUS));
	        a1.subtract(a2);
	        return a1;
	    }
	    
	    /**
	     * Check if current ExpenseType is over the NWS target or not
	     * @return 1 if exceed, 0 if not
	     */
	    private int checkStatus() {
			if (current > target)
				return 1;
			return 0;
		}
	    
	    /**
	     * Returns the angle for the current sector
	     * @return
	     */
	    private double getCurrentAngle(){
	    	if (status == 0)
	    		return current/target * 360;
	    	else{
	    		if (current == target*2)
	    			return 360;
	    		return (current - target)/target * 360;
	    	}
	    }
		
	}
}
