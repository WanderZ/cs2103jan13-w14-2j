package ezxpns.GUI;

import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.ScrollPaneConstants;
import javax.swing.JProgressBar;

/**
 * This is the panel in which will be slotted into the MainGUI 
 * (the main interface) as the display for the visual breakdown of the targets 
 * on the user defined categories (Food, Transport, etc...)
 */
@SuppressWarnings("serial")
public class TargetOverviewPanel extends JPanel {
	public TargetOverviewPanel() {
		super();
		this.setOpaque(false);
		
		JPanel targetPanel = new JPanel();
		
		JPanel panel = new JPanel();
		
		JLabel lblCategory = new JLabel("Category");
		
		JLabel lblProgress = new JLabel("Progress");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(targetPanel, GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
							.addGap(10))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblCategory)
							.addGap(47)
							.addComponent(lblProgress)
							.addGap(483))
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(targetPanel, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCategory)
						.addComponent(lblProgress))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 252, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setToolTipText("k");
		panel.add(progressBar);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scrollPane);
		targetPanel.setLayout(new BoxLayout(targetPanel, BoxLayout.X_AXIS));
		
		JLabel lblTarget = new JLabel("Target");
		targetPanel.add(lblTarget);
		
		JLabel alertNumber = new JLabel("(3)");
		targetPanel.add(alertNumber);
		setLayout(groupLayout);
	}
}
