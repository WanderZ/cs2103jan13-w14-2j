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
import javax.swing.JCheckBox;
import javax.swing.JTextField;

/** 
 * Note: This class is now marked for removal, but kept for reference
 * <br />
 */
public class AlertManagerDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField textField_2;
	private JTextField textField_1;

	/**
	 * Create the dialog.
	 */
	public AlertManagerDialog() {
		setBounds(100, 100, 508, 206);
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
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblAlertMeWhen = new JLabel("Alert me when:");
			contentPanel.add(lblAlertMeWhen, "2, 2");
		}
		{
			JCheckBox chckbxAsdf = new JCheckBox("asdf");
			contentPanel.add(chckbxAsdf, "2, 4");
		}
		{
			JCheckBox chckbxMyDebtGoes = new JCheckBox("My debt of the month goes beyong:");
			contentPanel.add(chckbxMyDebtGoes, "4, 4");
		}
		{
			textField = new JTextField();
			contentPanel.add(textField, "6, 4, fill, default");
			textField.setColumns(10);
		}
		{
			JCheckBox chckbxMySpendingGoes = new JCheckBox("My spending of the month goes beyong:");
			contentPanel.add(chckbxMySpendingGoes, "4, 6");
		}
		{
			textField_2 = new JTextField();
			contentPanel.add(textField_2, "6, 6, fill, default");
			textField_2.setColumns(10);
		}
		{
			JCheckBox chckbxMySpeningOf = new JCheckBox("My spening of the day goes beyong:");
			contentPanel.add(chckbxMySpeningOf, "4, 8");
		}
		{
			textField_1 = new JTextField();
			contentPanel.add(textField_1, "6, 8, fill, default");
			textField_1.setColumns(10);
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
