package net.simpleframework.mvc.component.ui.validatecode;

import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ValidateCodeBean extends AbstractContainerBean {

	private String inputName;

	public ValidateCodeBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
		setHandleClass(DefaultValidateCodeHandler.class);
	}

	public ValidateCodeBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	public String getInputName() {
		return inputName;
	}

	public ValidateCodeBean setInputName(final String inputName) {
		this.inputName = inputName;
		return this;
	}
}
