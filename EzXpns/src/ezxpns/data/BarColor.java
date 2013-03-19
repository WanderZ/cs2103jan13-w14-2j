package ezxpns.data;

import java.awt.Color;

public enum BarColor {
	HIGH {
		public Color getColor() {
			return Color.RED;
		}
	},
	MEDIUM {
		public Color getColor() {
			return Color.ORANGE;
		}
	},
	LOW {
		public Color getColor() {
			return Color.YELLOW;
		}
	},
	SAFE {
		public Color getColor() {
			return Color.GREEN;
		}
	};
	
	/**
	 * Returns a Color object based on the BarColor
	 * @return
	 */
	public abstract Color getColor();
}
