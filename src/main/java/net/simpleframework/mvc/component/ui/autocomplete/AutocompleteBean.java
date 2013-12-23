package net.simpleframework.mvc.component.ui.autocomplete;

import net.simpleframework.mvc.component.AbstractComponentBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class AutocompleteBean extends AbstractComponentBean {

	/* 需要自动完成的目标控件 */
	private String inputField;

	public String getInputField() {
		return inputField;
	}

	public AutocompleteBean setInputField(final String inputField) {
		this.inputField = inputField;
		return this;
	}
}