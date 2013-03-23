package ezxpns.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.toedter.calendar.JDateChooser;

import ezxpns.data.ReportGenerator;

public class ReportFrameNew extends JFrame {

	private ReportGenerator rptGen; // Place to store the reference

	public static final int DEFAULT_WIDTH = 1440;
	public static final int DEFAULT_HEIGHT = 800;
	
	public ReportFrameNew(ReportGenerator rptGenRef){ // Passing in the reference
		super("EzXpns - Report");
		rptGen = rptGenRef; // Storing the reference
		
		this.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		initBanner();
		
		

	}

	/**
	 * Initialise cyan banner at the top
	 */
	private void initBanner() {
		JPanel panelBanner = new JPanel();
		panelBanner.setBackground(new Color(0, 191, 255));
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 230, 140));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(233, 150, 122));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panelBanner, GroupLayout.PREFERRED_SIZE, 1292, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(14)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 227, GroupLayout.PREFERRED_SIZE)
							.addGap(35)
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 982, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(148, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panelBanner, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 629, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 621, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(36, Short.MAX_VALUE))
		);
		
		JLabel lblEzxpnsreport = new JLabel("EzXpns.Report");
		lblEzxpnsreport.setForeground(new Color(255, 255, 255));
		lblEzxpnsreport.setFont(new Font("Lucida Grande", Font.BOLD, 30));
		lblEzxpnsreport.setBackground(new Color(255, 255, 255));
		
		/*
		.setForeground(new Color(255, 255, 255));
		.setFocusPainted(false);
        .setMargin(new Insets(0, 0, 0, 0));
        .setContentAreaFilled(false);
        .setBorderPainted(false);
        .setOpaque(false);
        */
		
		JButton btnAllTime = new JButton("All Time");
		makeTextButton(btnAllTime);
		
		JButton btnThisYear = new JButton("This Year");
		makeTextButton(btnThisYear);
		
		JButton btnThisMonth = new JButton("This Month");
		makeTextButton(btnThisMonth);
		
		JButton btnThisWeek = new JButton("This Week");
		makeTextButton(btnThisWeek);
		
		JButton btnToday = new JButton("Today");
		makeTextButton(btnToday);
		
		JButton btnCustom = new JButton("Custom");
		makeTextButton(btnCustom);

		
		JLabel lblStart = new JLabel("Start");
		
		JDateChooser dateChooserStart = new JDateChooser();
		dateChooserStart.getJCalendar().setTodayButtonVisible(true);
		dateChooserStart.setDateFormatString("dd/MM/yyyy");
		
		JLabel lblEnd = new JLabel("End");
		
		JDateChooser dateChooserEnd = new JDateChooser();
		dateChooserEnd.getJCalendar().setTodayButtonVisible(true);
		dateChooserEnd.setDateFormatString("dd/MM/yyyy");
		
		JButton btnGenerate = new JButton("Generate");
		GroupLayout gl_panelBanner = new GroupLayout(panelBanner);
		gl_panelBanner.setHorizontalGroup(
			gl_panelBanner.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelBanner.createSequentialGroup()
					.addGap(14)
					.addGroup(gl_panelBanner.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelBanner.createSequentialGroup()
							.addComponent(btnAllTime)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnThisYear)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnThisMonth)
							.addGap(6)
							.addComponent(btnThisWeek)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnToday)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCustom)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblStart)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(dateChooserStart, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblEnd)
							.addGap(6)
							.addComponent(dateChooserEnd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnGenerate))
						.addComponent(lblEzxpnsreport))
					.addContainerGap(195, Short.MAX_VALUE))
		);
		gl_panelBanner.setVerticalGroup(
			gl_panelBanner.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelBanner.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblEzxpnsreport)
					.addGap(18)
					.addGroup(gl_panelBanner.createParallelGroup(Alignment.LEADING)
						.addComponent(dateChooserStart, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelBanner.createParallelGroup(Alignment.TRAILING)
							.addGroup(gl_panelBanner.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnAllTime)
								.addComponent(btnThisYear)
								.addComponent(btnThisMonth)
								.addComponent(btnThisWeek)
								.addComponent(btnToday)
								.addComponent(btnCustom)
								.addComponent(lblStart)
								.addComponent(lblEnd))
							.addGroup(gl_panelBanner.createParallelGroup(Alignment.LEADING)
								.addComponent(btnGenerate)
								.addComponent(dateChooserEnd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(18, Short.MAX_VALUE))
		);
		panelBanner.setLayout(gl_panelBanner);
		getContentPane().setLayout(groupLayout);
		
	}
	
	/**
	 * Make JButton looks like JLabel
	 * @param button
	 */
	private void makeTextButton(JButton button){
		button.setForeground(new Color(255, 255, 255));
		button.setFocusPainted(false);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setOpaque(false);
	}
}
