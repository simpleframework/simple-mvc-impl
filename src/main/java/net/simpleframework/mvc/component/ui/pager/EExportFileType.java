package net.simpleframework.mvc.component.ui.pager;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public enum EExportFileType {
	csv {

		@Override
		public String toString() {
			return "CSV";
		}
	},

	excel {

		@Override
		public String toString() {
			return "Excel";
		}
	}
}
