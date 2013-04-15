package ezxpns.data.records;

import java.awt.Color;

/**
 * Enum for Needs, Wants and Savings expense type
 * @author A0087091B
 *
 */
public enum ExpenseType {
	NEED("Need") {
		@Override
		public Color getBaseColor() {
			return new Color(195,195,195);
		}

		@Override
		public Color getNormalColor() {
			return new Color(255,122,122);
		}

		@Override
		public Color getExceedColor() {
			return new Color(245,0,0);
		}
	}, 
	WANT("Want") {
		@Override
		public Color getBaseColor() {
			// TODO Auto-generated method stub
			return new Color(195,195,195);
		}

		@Override
		public Color getNormalColor() {
			// TODO Auto-generated method stub
			return new Color(50,205,50);
		}

		@Override
		public Color getExceedColor() {
			// TODO Auto-generated method stub
			return new Color(0,100,0);
		}
	},
	SAVE("Save"){

		@Override
		public Color getBaseColor() {
			// TODO Auto-generated method stub
			return new Color(195,195,195);
		}

		@Override
		public Color getNormalColor() {
			// TODO Auto-generated method stub
			return new Color(0,191,255);
		}

		@Override
		public Color getExceedColor() {
			// TODO Auto-generated method stub
			return new Color(0,0,205);
		}
		
	};
	
	public final String name;
	
	ExpenseType(String name) {
		this.name = name;
	}
	
	/**
	 * Get base color for NWS chart
	 * @return
	 * @author A0087091B
	 */
	public abstract Color getBaseColor();
	
	/**
	 * Get normal color for NWS chart
	 * @return
	 * @author A0087091B
	 */
	public abstract Color getNormalColor();
	
	/**
	 * Get exceed color for NWS chart
	 * @return
	 * @author A0087091B
	 */
	public abstract Color getExceedColor();
}
