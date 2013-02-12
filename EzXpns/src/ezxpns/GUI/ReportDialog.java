package ezxpns.GUI;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JRadioButton;
import java.awt.Insets;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class ReportDialog extends JDialog {
	ReportDialog() {
		setBounds(100, 100, 450, 300);
		
		JTextArea txtrYouHaveSpent = new JTextArea();
		txtrYouHaveSpent.setText("You have spent in total $100 this month, well within you alert limit.\n\nFor this month:\nDaily expense: $1\nDaily income: $1\nTotal expense: $1\nTotal Income: $1\nTotal Debt: $1\nMaximum expnse: $10000 on Feb 30\n\u2026..\n\nReport for this year:\n\u2026\u2026.");
		txtrYouHaveSpent.setEditable(false);
		txtrYouHaveSpent.setLineWrap(true);
		txtrYouHaveSpent.setWrapStyleWord(true);
		getContentPane().add(txtrYouHaveSpent, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblSelectAReport = new JLabel("Select a report range:");
		panel.add(lblSelectAReport);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Feb 2013", "Jan 2013", "Dec 2012", "Nov 2012"}));
		panel.add(comboBox);

	}

}
