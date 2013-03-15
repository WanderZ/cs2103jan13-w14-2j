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
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ScrollPaneConstants;
import net.miginfocom.swing.MigLayout;

/**
 * This is the panel in which will be slotted into the MainGUI 
 * (the main interface) as the display for the visual breakdown of the targets 
 * on the user defined categories (Food, Transport, etc...)
 */
@SuppressWarnings("serial")
public class TargetOverviewPanel extends JPanel {
	
	private TargetManager targetMgr;
	
	public TargetOverviewPanel(TargetManager targetMgrRef) {
		super(); // new Panel();
		
		this.targetMgr = targetMgrRef;
		this.setOpaque(false);
		setLayout(new BorderLayout(0, 0));
		
		JPanel bigBorderPane = new JPanel();
		add(bigBorderPane);
		bigBorderPane.setLayout(new BorderLayout(0, 0));
		
		JPanel tagsPane = new JPanel();
		bigBorderPane.add(tagsPane, BorderLayout.NORTH);
		tagsPane.setLayout(new MigLayout("", "[37px][14px][]", "[14px]"));
		
		JLabel lblTargets = new JLabel("Targets");
		lblTargets.setHorizontalAlignment(SwingConstants.CENTER);
		tagsPane.add(lblTargets, "cell 0 0,alignx left,aligny top");
		
		JLabel lblalertnumber = new JLabel("("+"0"+")");
		tagsPane.add(lblalertnumber, "cell 2 0,alignx left,aligny top");
		
		JScrollPane targetScrollPane = new JScrollPane();
		
		targetScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		bigBorderPane.add(targetScrollPane, BorderLayout.CENTER);
		
		JPanel borderlayoutpanel = new JPanel();
		 targetScrollPane.setViewportView(borderlayoutpanel);
		 borderlayoutpanel.setLayout(new BorderLayout(0, 0));
		
		 JPanel columnpanel = new JPanel();
		 borderlayoutpanel.add(columnpanel, BorderLayout.NORTH);
		 columnpanel.setLayout(new GridLayout(0, 1, 0, 1));
		 columnpanel.setBackground(Color.white);
		 
		 targetScrollPane.setPreferredSize(new Dimension(50,50));
		 
		 for(int i=0;i<15;i++) //i < ordered.size()
		 {
		 JPanel rowPanel = new JPanel();
		 rowPanel.setPreferredSize(new Dimension(200,20));
		 columnpanel.add(rowPanel);
		 rowPanel.setLayout(new FlowLayout());
		 
		 JLabel lblBar = new JLabel("Food" + i);
		 rowPanel.add(lblBar);
		 //lblBar.setLocation()
		 lblBar.setBackground(new Color(154, 205, 50));
		 lblBar.setHorizontalAlignment(SwingConstants.CENTER);
		 JLabel lblCurrentAmt = new JLabel("$"+ i*100);
		 rowPanel.add(lblCurrentAmt);
		 lblCurrentAmt.setHorizontalAlignment(SwingConstants.CENTER);
		 JLabel lblTargetAmt = new JLabel("/ $"+200*i);
		 rowPanel.add(lblTargetAmt);
		 lblTargetAmt.setHorizontalAlignment(SwingConstants.CENTER);
		 
		 
		 switch(i){
		 	case 1:
				lblCurrentAmt.setForeground(new Color(255, 20, 30)); //red
				break;
			 case 2:
				 lblCurrentAmt.setForeground(new Color(255, 150, 20)); // orange
				 break;
			 case 3:
				 lblCurrentAmt.setForeground(new Color(255, 245, 40)); // yellow
				 break;
			 case 4:
				 lblCurrentAmt.setForeground(new Color(71, 255, 102)); // orange
				 break;
			default:
				lblCurrentAmt.setForeground(new Color(122, 122, 122)); //dark gray
		 }
		 

		 }
		
	}
}
