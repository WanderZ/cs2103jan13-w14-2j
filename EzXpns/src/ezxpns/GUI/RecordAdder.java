package ezxpns.GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class RecordAdder extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Create the dialog.
	 */
	public RecordAdder() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		{
			JLabel lblRecordType = new JLabel("Record type:");
			contentPanel.add(lblRecordType, "2, 2");
		}
		{
			JComboBox comboBox = new JComboBox();
			comboBox.setModel(new DefaultComboBoxModel(new String[] {"Expense", "Income"}));
			contentPanel.add(comboBox, "6, 2, fill, default");
		}
		{
			JLabel lblCategory = new JLabel("Category");
			contentPanel.add(lblCategory, "2, 4");
		}
		{
			JComboBox comboBox = new JComboBox();
			comboBox.setModel(new DefaultComboBoxModel(new String[] {"Coding", "Hacking", "Developing"}));
			contentPanel.add(comboBox, "6, 4, fill, default");
		}
		{
			JLabel lblName = new JLabel("Name:");
			contentPanel.add(lblName, "2, 6");
		}
		{
			textField = new JTextField();
			contentPanel.add(textField, "6, 6, fill, default");
			textField.setColumns(10);
		}
		{
			JLabel lblAmount = new JLabel("Amount:");
			contentPanel.add(lblAmount, "2, 8");
		}
		{
			textField_1 = new JTextField();
			contentPanel.add(textField_1, "6, 8, fill, default");
			textField_1.setColumns(10);
		}
		{
			JLabel lblTime = new JLabel("Time:");
			contentPanel.add(lblTime, "2, 10");
		}
		{
			textField_2 = new JTextField();
			contentPanel.add(textField_2, "6, 10, fill, default");
			textField_2.setColumns(10);
		}
		{
			JLabel lblRemark = new JLabel("Remark:");
			contentPanel.add(lblRemark, "2, 12");
		}
		{
			JTextArea textArea = new JTextArea();
			contentPanel.add(textArea, "6, 12, fill, fill");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
