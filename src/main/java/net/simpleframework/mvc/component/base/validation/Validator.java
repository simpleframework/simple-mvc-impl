package net.simpleframework.mvc.component.base.validation;

import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.common.xml.AbstractElementBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class Validator extends AbstractElementBean {
	private static final long serialVersionUID = -8236208817832770846L;

	private String selector;

	private EWarnType warnType;

	private EValidatorMethod method;

	private String message;

	private String args;

	public Validator() {
	}

	public Validator(final EValidatorMethod method, final String selector, final Object... args) {
		setSelector(selector);
		setMethod(method);
		if (args != null && args.length > 0) {
			setArgs(StringUtils.join(args, ","));
		}
	}

	public EValidatorMethod getMethod() {
		return method;
	}

	public Validator setMethod(final EValidatorMethod method) {
		this.method = method;
		return this;
	}

	public String getSelector() {
		return selector;
	}

	public Validator setSelector(final String selector) {
		this.selector = selector;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Validator setMessage(final String message) {
		this.message = message;
		return this;
	}

	public String getArgs() {
		return args;
	}

	public Validator setArgs(final String args) {
		this.args = args;
		return this;
	}

	public EWarnType getWarnType() {
		return warnType;
	}

	public Validator setWarnType(final EWarnType warnType) {
		this.warnType = warnType;
		return this;
	}
}
