package ezxpns.GUI;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JTextArea;
import java.awt.BorderLayout;

public class ReportDialog extends JDialog {
	ReportDialog() {
		setBounds(100, 100, 450, 300);
		
		JTextArea txtrYouHaveSpent = new JTextArea();
		txtrYouHaveSpent.setText("You have spent in total $100 this month, well within you alert limit.\n\nFor this month:\nDaily expense: $1\nDaily income: $1\nTotal expense: $1\nTotal Income: $1\nTotal Debt: $1\nMaximum expnse: $10000 on Feb 30\n\u2026..\n\nReport for this year:\n\u2026\u2026.");
		txtrYouHaveSpent.setEditable(false);
		txtrYouHaveSpent.setLineWrap(true);
		txtrYouHaveSpent.setWrapStyleWord(true);
		getContentPane().add(txtrYouHaveSpent, BorderLayout.CENTER);

	}

}
