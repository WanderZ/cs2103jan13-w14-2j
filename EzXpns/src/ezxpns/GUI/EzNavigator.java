package ezxpns.GUI;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JToggleButton;

/**
 * Navigator for the MainGUI
 */
@SuppressWarnings("serial")
public class EzNavigator extends JLayeredPane {

	/**
	 * CardLayout to manage the different content panels
	 */
	private CardLayout contentMgr;
	
	/**
	 * The parent content panel that will do the switching of panels
	 */
	private JLayeredPane content;
	
	/**
	 * UIControl - to manipulate the Panels 
	 */
	private UIControl uiCtrl;
	
	/**
	 * JToggleButton reference to the undo button
	 */
	private JToggleButton btnUndo;
	
	/**
	 * JToggleButton reference to the selected button 
	 */
	private JToggleButton selected;
	
	private EzNavigator(UIControl uiCtrl) {
		super();
		this.uiCtrl = uiCtrl;
		this.setDoubleBuffered(true);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		AbstractButton btn;
		ButtonGroup btnGrp = new ButtonGroup();
		
		// Adding the LOGO
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.anchor = GridBagConstraints.NORTH;
		LogoIcon ezxpnsLogo = new LogoIcon();
		this.add(ezxpnsLogo, gbc);
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		// End of LOGO
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		/* Insert First Button here */
		btn = createMenuBtn(NormalMenuOpt.REVERT);
		btnUndo = (JToggleButton) btn; // Stored for cosmetic updates
		this.add(btn, gbc);
		btnGrp.add(btn);
		btnUndo.setAction(uiCtrl.getUndoMgr().getAction());
		btnUndo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnUndo.setSelected(false);
			}
		});
//		btnUndo.setBorderPainted(btnUndo.isEnabled());
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		/* Insert Second Button here */
		btn = createMenuBtn(new NewExpenseDialog(this.uiCtrl));
		this.add(btn, gbc);
		btnGrp.add(btn);
		/* Button ends*/
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		/* Insert Third Button here */
		btn = createMenuBtn(new NewIncomeDialog(this.uiCtrl));
		this.add(btn, gbc);
		btnGrp.add(btn);
		/* Button ends*/
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		/* Insert Fourth Button here */
		btn = createMenuBtn(NormalMenuOpt.DASHBD);
		this.add(btn, gbc);
		btnGrp.add(btn);
		btn.setSelected(true);
//		btn.setBorder(BorderFactory.createLoweredBevelBorder());
		selected = (JToggleButton) btn;
		/* Button ends*/
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		/* Insert Fifth Button here */
		btn = createMenuBtn(NormalMenuOpt.SEARCH);
		this.add(btn, gbc);
		btnGrp.add(btn);
		/* Button ends*/
		
		gbc.gridx = 0;
		gbc.gridy = 6;
		/* Insert Sixth Button here */
		btn = createMenuBtn(NormalMenuOpt.CATMGR);
		this.add(btn, gbc);
		btnGrp.add(btn);
		/* Button ends*/
		
		gbc.gridx = 0;
		gbc.gridy = 7;
		/* Insert Seventh Button here */
		
		// To create the space between the last button and the bottom
		gbc.insets = new Insets(5, 5, 250, 5); 
		btn = createMenuBtn(new ReportDialog(this.uiCtrl));
		this.add(btn, gbc);
		btnGrp.add(btn);
		/* Button ends*/
		
//		btn = createMenuBtn(NormalMenuOpt.PAYMGR);
//		gbc.gridx = 0;
//		gbc.gridy = 7;
//		this.add(btn, gbc);
	}
	
	/**
	 * Updates the undo button cosmetics
	 */
	public void updateUndoBtn() {
		btnUndo.setBorderPainted(btnUndo.isEnabled());
		btnUndo.setSelected(false);
	}
	
	/**
	 * Constructs a Navigator
	 * @param contentMgrRef CardLayout object that will manage the content switching
	 * @param contentPane JLayeredPane object that will be hosting all the content (content Placeholder)
	 */
	public EzNavigator(UIControl uiCtrl, CardLayout contentMgrRef, JLayeredPane contentPane) {
		this(uiCtrl);
		this.contentMgr = contentMgrRef;
		this.content = contentPane;
	}
	
	/**
	 * Navigates the content panel
	 * @param card MenuCard to be called
	 */
	public void navigate(NormalMenuOpt option) {
		contentMgr.show(content, option.toString());
	}
	
	/**
	 * Opens a new dialog
	 * @param option the DialogMenuOpt reference to open the dialog
	 */
	public void navigate(DialogMenuOpt option) {
		option.openDialog();
	}
	
	/**
	 * Links the button to the card
	 * @param btn JButton to be linked
	 * @param card MenuOption card to be linked to
	 */
	public void linkNavi(AbstractButton btn, MenuOption card) {
		if(card instanceof NormalMenuOpt) {
			linkNormalMenuOpt((JToggleButton) btn, (NormalMenuOpt) card);
		}
		if(card instanceof DialogMenuOpt) {
			linkDiagMenuOpt((JButton) btn, (DialogMenuOpt) card);
		}
	}
	
	
	/**
	 * Links the provided button to a dialog
	 * @param btn JButton Object to be linked
	 * @param option DialogMenuOpt to be linked to
	 */
	public void linkDiagMenuOpt(JButton btn, final DialogMenuOpt option) {
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				navigate(option);
			}
		});
	}
	
	/**
	 * Links the provided button to a panel
	 * @param btn JButton Object to be linked
	 * @param option NormalMenuOpt to be linked to
	 */
	public void linkNormalMenuOpt(JToggleButton btn, final NormalMenuOpt option) {
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				navigate(option);
			}
		});
	}
	
	/**
	 * Creates a Menu button for the navigator
	 * @param card MenuCard Object to tag button to
	 * @return a JButton object tagged to the given MenuCard.
	 */
	private JToggleButton createMenuBtn(NormalMenuOpt option) {
		JToggleButton btn = new JToggleButton(option.toString());
//		btn.setContentAreaFilled(false);
//		btn.setBorder(BorderFactory.createRaisedBevelBorder());
//		btn.setFocusPainted(false);
		btn.setFont(Config.MENU_FONT);
		linkNavi(btn, option);
		btn.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mousePressed(MouseEvent mEvent) {
//				JToggleButton btn = (JToggleButton) mEvent.getSource();
//				if(btn != selected)
//					btn.setBorder(BorderFactory.createLoweredBevelBorder());
//			}
			
			@Override
			public void mouseClicked(MouseEvent mEvent) {
				JToggleButton btn = (JToggleButton) mEvent.getSource();
				if(btn == btnUndo) {
//					btn.setBorder(BorderFactory.createRaisedBevelBorder());
					btnUndo.setSelected(false);
					selected.setSelected(true);
					return;
				}
				if(btn != selected) {
//					selected.setBorder(BorderFactory.createRaisedBevelBorder());
					selected = btn;
				}
			}
//			
//			@Override
//			public void mouseExited(MouseEvent mEvent) { // Hover end
//				JToggleButton btn = (JToggleButton) mEvent.getSource();
//				if(btn != selected)
//					btn.setBorder(BorderFactory.createRaisedBevelBorder());
//			}
		});
		return btn;
	}
	
	/**
	 * Creates a JButton for the navigator that will open dialogs
	 * @param option
	 * @return
	 */
	private JButton createMenuBtn(DialogMenuOpt option) {
		JButton btn = new JButton(option.toString());
//		btn.setContentAreaFilled(false);
//		btn.setBorder(BorderFactory.createRaisedBevelBorder());
//		btn.setFocusPainted(false);
		btn.setFont(Config.MENU_FONT);
		linkNavi(btn, option);
//		btn.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mousePressed(MouseEvent mEvent) {
//				JToggleButton btn = (JToggleButton) mEvent.getSource();
//				btn.setBorder(BorderFactory.createLoweredBevelBorder());
//			}
//			
//			@Override
//			public void mouseClicked(MouseEvent mEvent) {
//				JButton btn = (JButton) mEvent.getSource();
//				btn.setBorder(BorderFactory.createRaisedBevelBorder());
//			}
//			
//			@Override
//			public void mouseReleased(MouseEvent mEvent) {
//				JButton btn = (JButton) mEvent.getSource();
//				btn.setBorder(BorderFactory.createRaisedBevelBorder());
//			}
//		});
		return btn;
	}
}

/**
 * Generic Menu Option
 */
interface MenuOption {}

/**
 *	Defines the normal menu buttons for the EzNavigator
 */
enum NormalMenuOpt implements MenuOption {
	
	NEWRCD 	("New Record"), 			// Maybe this isn't an option
	SEARCH 	("Search"),					// inclusive of Advanced Search
	CATMGR 	("Manage Category"),		
	PAYMGR 	("Manage Payments"),		// TODO: Remove @Yujian
	TARGET 	("Manage Targets"),			// TODO: Is that the one to keep?
	DASHBD 	("Dashboard"),
	CONFIG	("Configurations"),			// Future implementation
	REVERT	("Undo");
	
	
	public final String name;
	private NormalMenuOpt(String name) {this.name = name;}
	
	@Override
	public String toString() {
		return this.name;
	}
}

/**
 * Defines a Dialog Menu Option for EzNavigator
 */
abstract class DialogMenuOpt implements MenuOption {
	
	protected String name;
	protected UIControl uiCtrl;
	
	public DialogMenuOpt(String name, UIControl uiCtrlRef) {
		this.name = name;
		this.uiCtrl = uiCtrlRef;
	}
	
	public String toString() {
		return this.name;
	}
	public abstract void openDialog();
}


/**
 * Defines a Dialog Menu Option for EzNavigator to link to new income dialog
 */
class NewIncomeDialog extends DialogMenuOpt {
	
	public NewIncomeDialog(UIControl uiCtrl) {
		super("New Income", uiCtrl);
	}

	@Override
	public void openDialog() {
		// TODO Auto-generated method stub
		uiCtrl.showRecWin(RecordDialog.TAB_INCOME);
	}	
}

/**
 * Defines a Dialog Menu Option for EzNavigator to link to new expense dialog
 */
class NewExpenseDialog extends DialogMenuOpt {
	
	public NewExpenseDialog(UIControl uiCtrl) {
		super("New Expense", uiCtrl);
	}

	@Override
	public void openDialog() {
		uiCtrl.showRecWin(RecordDialog.TAB_EXPENSE);
	}
}

/**
 * Defines a Dialog Menu Option for EzNavigator to link to the report dialog
 */
class ReportDialog extends DialogMenuOpt {
	
	public ReportDialog(UIControl uiCtrl) {
		super("Generate Report", uiCtrl);
	}

	@Override
	public void openDialog() {
		uiCtrl.showReportWin();
	}
}