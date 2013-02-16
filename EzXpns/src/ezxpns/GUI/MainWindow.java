/*
 * Main window, also master GUI class to bind all other GUI classes
 */

package ezxpns.GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JList;
import java.awt.BorderLayout;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import java.awt.Color;
import javax.swing.UIManager;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import java.awt.Component;

public class MainWindow {

	private JFrame frame;
	
	private RecordAdder adderDialog;
	private AlertManagerDialog alertmngDialog;
	private ReportDialog reportDialog;
	private SearchDialog searchDialog;

	/**
	 * Create the application.
	 */
	/**
	 * @wbp.parser.entryPoint
	 */
	public MainWindow() {
		adderDialog = new RecordAdder();
		alertmngDialog = new AlertManagerDialog();
		reportDialog = new ReportDialog();
		searchDialog = new SearchDialog();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 456, 359);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnRecord = new JMenu("Record");
		menuBar.add(mnRecord);
		
		JMenuItem mntmNewExpenseRecord = new JMenuItem("New Expense Record");
		mnRecord.add(mntmNewExpenseRecord);
		
		JMenuItem mntmNewIncomeRecord = new JMenuItem("New Income Record");
		mnRecord.add(mntmNewIncomeRecord);
		
		JSeparator separator = new JSeparator();
		mnRecord.add(separator);
		
		JMenuItem mntmTypeManager = new JMenuItem("Category Manager");
		mnRecord.add(mntmTypeManager);
		
		JMenuItem mntmSearch = new JMenuItem("Search");
		mnRecord.add(mntmSearch);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmUndo = new JMenuItem("Undo..");
		mnEdit.add(mntmUndo);
		
		JMenuItem mntmSettings = new JMenuItem("Settings");
		mnEdit.add(mntmSettings);
		
		JMenu mnReport = new JMenu("Report");
		menuBar.add(mnReport);
		
		JMenuItem mntmViewReport = new JMenuItem("View report");
		mnReport.add(mntmViewReport);
		
		JMenu mnAlerts = new JMenu("Alerts");
		menuBar.add(mnAlerts);
		
		JMenuItem mntmSetAlert = new JMenuItem("Set alert");
		mntmSetAlert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				alertmngDialog.setVisible(true);
			}
		});
		mnAlerts.add(mntmSetAlert);
		
		JList list = new JList();
		list.setLayoutOrientation(JList.VERTICAL_WRAP);
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"$100 for Dinner at 21:00 (food)", "$100 for Dinner at 21:00 yesterday (food)", "$100 for Lulz at 1:00 Dec 10 (lulz)", "this goes on and will show 20 records"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		
		JPanel panel_2 = new JPanel();
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnSearchRecords = new JButton("Search Records");
		panel.add(btnSearchRecords);
		btnSearchRecords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchDialog.setVisible(true);
			}
		});
		
		JButton btnViewDetailedReport = new JButton("View Detailed Report");
		btnViewDetailedReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reportDialog.setVisible(true);
			}
		});
		panel.add(btnViewDetailedReport);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.getContentPane().add(panel_2);
		
		JPanel panel_1 = new JPanel();
		panel_2.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnNewExpense = new JButton("New Expense");
		btnNewExpense.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				adderDialog.setVisible(true);
			}
		});
		panel_1.add(btnNewExpense);
		
		JButton btnNewIncome = new JButton("New Income");
		panel_1.add(btnNewIncome);
		frame.getContentPane().add(list, BorderLayout.NORTH);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(list, popupMenu);
		
		JMenuItem mntmEdit = new JMenuItem("Edit");
		popupMenu.add(mntmEdit);
		
		JMenuItem mntmRemove = new JMenuItem("Remove");
		popupMenu.add(mntmRemove);
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnSettings = new JButton("Settings");
		panel.add(btnSettings);
		
		JTextArea txtrA = new JTextArea();
		txtrA.setBackground(UIManager.getColor("ToolBar.background"));
		txtrA.setEditable(false);
		txtrA.setText("Monthly Report:\nSpent: $10\nEarned: $1\nNet Debt: $9");
		frame.getContentPane().add(txtrA, BorderLayout.WEST);
		
		JPanel panel_3 = new JPanel();
		frame.getContentPane().add(panel_3, BorderLayout.EAST);
		
		JTextArea txtrWarningYouHave = new JTextArea();
		txtrWarningYouHave.setWrapStyleWord(true);
		txtrWarningYouHave.setEditable(false);
		txtrWarningYouHave.setForeground(new Color(255, 0, 0));
		txtrWarningYouHave.setLineWrap(true);
		txtrWarningYouHave.setText("Warning: you have exceeded your monthly target and daily target");
		panel_3.add(txtrWarningYouHave);
		
		frame.setVisible(true);
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
