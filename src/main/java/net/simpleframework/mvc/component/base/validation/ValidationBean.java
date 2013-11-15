package net.simpleframework.mvc.component.base.validation;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.AbstractComponentBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ValidationBean extends AbstractComponentBean {

	private EWarnType warnType = (EWarnType) BeanDefaults.get(getClass(), "warnType",
			EWarnType.alert);

	private String triggerSelector;

	private Validators validators;

	public ValidationBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
	}

	public ValidationBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	public Validators getValidators() {
		if (validators == null) {
			validators = Validators.of();
		}
		return validators;
	}

	public ValidationBean addValidators(final Validator... validators) {
		getValidators().append(validators);
		return this;
	}

	public String getTriggerSelector() {
		return triggerSelector;
	}

	public ValidationBean setTriggerSelector(final String triggerSelector) {
		this.triggerSelector = triggerSelector;
		return this;
	}

	public EWarnType getWarnType() {
		return warnType;
	}

	public ValidationBean setWarnType(final EWarnType warnType) {
		this.warnType = warnType;
		return this;
	}
}
