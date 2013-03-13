package ezxpns.GUI;

import javax.swing.JFrame;

import ezxpns.data.TargetManager;

/**
 * Window for users to manage their targets and / or categories [?]
 */
@SuppressWarnings("serial")
public class TargetFrame extends JFrame {
	
	private TargetManager targetMgr;
	
	public TargetFrame(TargetManager targetMgrRef) {
		super(); // new JFrame();
		targetMgr = targetMgrRef;
		
	}

}

/**
 * This bit may assist in filling up the "scrolling bit of the various categories 
 * the user would be able to set targets for, one for each category...
 */
// class <SomeNameHere> extends JPanel {}