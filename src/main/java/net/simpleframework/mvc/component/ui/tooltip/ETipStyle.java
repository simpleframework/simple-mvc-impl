package net.simpleframework.mvc.component.ui.tooltip;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public enum ETipStyle {

	tipDefault {
		@Override
		public String toString() {
			return "default";
		}
	},

	tipBlue {
		@Override
		public String toString() {
			return "protoblue";
		}
	},

	tipDarkgrey {
		@Override
		public String toString() {
			return "darkgrey";
		}
	}
}
