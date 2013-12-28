package net.simpleframework.mvc.component.ui.autocomplete;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class AutocompleteData {
	private String val;

	private Object[] list;

	public String getVal() {
		return val;
	}

	public AutocompleteData setVal(final String val) {
		this.val = val;
		return this;
	}

	public Object[] getList() {
		return list;
	}

	public AutocompleteData setList(final Object[] list) {
		this.list = list;
		return this;
	}
}
