package ezxpns.GUI;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
	
	private JButton btnUndo;
	
	private EzNavigator() {
		super();
		this.setBounds(new Rectangle (0, 0, 134, 600));
		// TODO: Incorporate Simple Search Field [?]
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		// gbc.weighty = 1;
		gbc.weightx = 1;
		JButton btn = createMenuBtn(MenuOption.SEARCH);
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(btn, gbc);

		btn = createMenuBtn(MenuOption.REVERT);
		gbc.gridx = 0;
		gbc.gridy = 2;
		btnUndo = btn; // Stored for cosmetic updates
		this.add(btn, gbc);
		
		btn = createMenuBtn(MenuOption.NEWEXP);
		gbc.gridx = 0;
		gbc.gridy = 3;
		this.add(btn, gbc);
		
		btn = createMenuBtn(MenuOption.NEWINC);
		gbc.gridx = 0;
		gbc.gridy = 4;
		this.add(btn, gbc);
		
		btn = createMenuBtn(MenuOption.DASHBD);
		gbc.gridx = 0;
		gbc.gridy = 5;
		this.add(btn, gbc);

		btn = createMenuBtn(MenuOption.CATMGR);
		gbc.gridx = 0;
		gbc.gridy = 6;
		this.add(btn, gbc);
		
		btn = createMenuBtn(MenuOption.PAYMGR);
		gbc.gridx = 0;
		gbc.gridy = 7;
		this.add(btn, gbc);
		
		btn = createMenuBtn(MenuOption.REPORT);
		gbc.gridx = 0;
		gbc.gridy = 8;
		this.add(btn, gbc);

		// TODO: Manipulate the main gui panels
		
		// TODO: Setup the layout properly for the menu buttons
		
		// TODO: SUBMENU Hiding - Categorizing Menu options, etc

	}
	
	/**
	 * Constructs a Navigator
	 * @param contentMgrRef CardLayout object that will manage the content switching
	 * @param contentPane JLayeredPane object that will be hosting all the content (content Placeholder)
	 */
	public EzNavigator(CardLayout contentMgrRef, JLayeredPane contentPane) {
		this();
		this.contentMgr = contentMgrRef;
		this.content = contentPane;
	}
	
	/**
	 * Navigates the content panel
	 * @param card MenuCard to be called
	 */
	public void navigate(MenuOption card) {
		contentMgr.show(content, card.toString());
	}
	
	/**
	 * Creates a Menu button for the navigator
	 * @param card MenuCard Object to tag button to
	 * @return a JButton object tagged to the given MenuCard.
	 */
	private JButton createMenuBtn(final MenuOption card) {
		JButton btn = new JButton(card.toString());
		btn.setContentAreaFilled(false);
		btn.setBorder(BorderFactory.createRaisedBevelBorder());
		
		btn.setFont(Config.MENU_FONT);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				navigate(card);
			}
		});
		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent mEvent) { // Hover start
				JButton btn = (JButton) mEvent.getSource();
				btn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLineBorder(Color.cyan)));
			}
			
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
			
			public void mouseExited(MouseEvent mEvent) { // Hover end
				JButton btn = (JButton) mEvent.getSource();
				btn.setBorder(BorderFactory.createRaisedBevelBorder());
			}
		});
		return btn;
	}
}

/**
 * Menu Names for labeling and operating cards
 */
enum MenuOption {
	
	NEWRCD 	("New Record"), 			// Maybe this isn't an option
	NEWINC 	("New Income Record"),		
	NEWEXP 	("New Expense Record"),
	SEARCH 	("Search"),					// TODO: Advanced Search ?
	CATMGR 	("Manage Category"),		
	PAYMGR 	("Manage Payments"),		// TODO: Remove or keep?
	TARGET 	("Manage Targets"),			// TODO: Is that the one to keep?
	DASHBD 	("Dashboard"),
	CONFIG	("Configurations"),
	REVERT	("Undo"),
	REPORT 	("Generate Report");
	
	public final String name;
	private MenuOption(String name) {this.name = name;}
	
	@Override
	public String toString() {
		return this.name;
	}
}