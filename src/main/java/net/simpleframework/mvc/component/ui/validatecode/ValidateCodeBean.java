package net.simpleframework.mvc.component.ui.validatecode;

import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ValidateCodeBean extends AbstractContainerBean {

	private String inputName;

	public String getInputName() {
		return inputName;
	}

	public ValidateCodeBean setInputName(final String inputName) {
		this.inputName = inputName;
		return this;
	}

	{
		setHandlerClass(DefaultValidateCodeHandler.class);
	}
}
