package ezxpns.GUI;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLayeredPane;

/**
 * Navigator for the MainGUI
 */
/**
 * @author Andrew
 *
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
	
	private UIControl uiCtrl;
	
	private JButton btnUndo;
	private JButton selected;
	
	private EzNavigator(UIControl uiCtrl) {
		super();
		this.uiCtrl = uiCtrl;
		this.setBounds(new Rectangle (0, 0, 80, 600));
		this.setDoubleBuffered(true);
		// TODO: Incorporate Simple Search Field [?]
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 15, 5, 15);
		// gbc.weighty = 1;
		gbc.weightx = 0.5;
		JButton btn = createMenuBtn(NormalMenuOpt.SEARCH);
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(btn, gbc);

		btn = createMenuBtn(NormalMenuOpt.REVERT);
		gbc.gridx = 0;
		gbc.gridy = 2;
		btnUndo = btn; // Stored for cosmetic updates
		this.add(btn, gbc);
		btnUndo.setAction(uiCtrl.getUndoMgr().getAction());
		(new Thread() {
			@Override
			public void run() {
				while(true) {
					btnUndo.setBorderPainted(btnUndo.isEnabled());
				}
			}
		}).start();
		
		btn = createMenuBtn(new NewExpenseDialog(this.uiCtrl));
		gbc.gridx = 0;
		gbc.gridy = 3;
		this.add(btn, gbc);
		
		btn = createMenuBtn(new NewIncomeDialog(this.uiCtrl));
		gbc.gridx = 0;
		gbc.gridy = 4;
		this.add(btn, gbc);
		
		btn = createMenuBtn(NormalMenuOpt.DASHBD);
		gbc.gridx = 0;
		gbc.gridy = 5;
		this.add(btn, gbc);
		selected = btn;

		btn = createMenuBtn(NormalMenuOpt.CATMGR);
		gbc.gridx = 0;
		gbc.gridy = 6;
		this.add(btn, gbc);
		
		btn = createMenuBtn(new ReportDialog(this.uiCtrl));
		gbc.gridx = 0;
		gbc.gridy = 7;
		this.add(btn, gbc);
		
//		btn = createMenuBtn(NormalMenuOpt.PAYMGR);
//		gbc.gridx = 0;
//		gbc.gridy = 7;
//		this.add(btn, gbc);

		// TODO: Manipulate the main gui panels
		
		// TODO: Setup the layout properly for the menu buttons
		
		// TODO: SUBMENU Hiding - Categorizing Menu options, etc
		

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
	 * @param option
	 */
	public void navigate(DialogMenuOpt option) {
		option.openDialog();
	}
	
	/**
	 * 
	 * @param btn
	 * @param card
	 */
	public void linkNavi(JButton btn, MenuOption card) {
		if(card instanceof NormalMenuOpt) {
			linkNormalMenuOpt(btn, (NormalMenuOpt) card);
		}
		if(card instanceof DialogMenuOpt) {
			linkDiagMenuOpt(btn, (DialogMenuOpt) card);
		}
	}
	
	
	/**
	 * @param btn
	 * @param option
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
	 * 
	 * @param btn
	 * @param option
	 */
	public void linkNormalMenuOpt(JButton btn, final NormalMenuOpt option) {
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
	private JButton createMenuBtn(NormalMenuOpt option) {
		JButton btn = new JButton(option.toString());
		btn.setContentAreaFilled(false);
		btn.setBorder(BorderFactory.createRaisedBevelBorder());
		btn.setFocusPainted(false);
		btn.setFont(Config.MENU_FONT);
		linkNavi(btn, option);
		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mEvent) {
				JButton btn = (JButton) mEvent.getSource();
				if(btn != selected)
					btn.setBorder(BorderFactory.createLoweredBevelBorder());
			}
			
			@Override
			public void mouseReleased(MouseEvent mEvent) {
				JButton btn = (JButton) mEvent.getSource();
				if(btn != selected) {
					selected.setBorder(BorderFactory.createRaisedBevelBorder());
					selected = btn;
				}
			}
			
			public void mouseExited(MouseEvent mEvent) { // Hover end
				JButton btn = (JButton) mEvent.getSource();
				if(btn != selected)
					btn.setBorder(BorderFactory.createRaisedBevelBorder());
			}
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
		btn.setContentAreaFilled(false);
		btn.setBorder(BorderFactory.createRaisedBevelBorder());
		btn.setFocusPainted(false);
		btn.setFont(Config.MENU_FONT);
		linkNavi(btn, option);
		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mEvent) {
				JButton btn = (JButton) mEvent.getSource();
				btn.setBorder(BorderFactory.createLoweredBevelBorder());
			}
			
			@Override
			public void mouseReleased(MouseEvent mEvent) {
				JButton btn = (JButton) mEvent.getSource();
				btn.setBorder(BorderFactory.createRaisedBevelBorder());
			}
		});
		return btn;
	}
}

/**
 * Generic Menu Options
 */
interface MenuOption {}

/**
 *
 */
enum NormalMenuOpt implements MenuOption {
	
	NEWRCD 	("New Record"), 			// Maybe this isn't an option
	SEARCH 	("Search"),					// TODO: Advanced Search ?
	CATMGR 	("Manage Category"),		
	PAYMGR 	("Manage Payments"),		// TODO: Remove @Yujian
	TARGET 	("Manage Targets"),			// TODO: Is that the one to keep?
	DASHBD 	("Dashboard"),
	CONFIG	("Configurations"),			// Further implementation
	REVERT	("Undo");
	
	
	public final String name;
	private NormalMenuOpt(String name) {this.name = name;}
	
	@Override
	public String toString() {
		return this.name;
	}
}

/**
 * 
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
 *
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
 *
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
 *
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