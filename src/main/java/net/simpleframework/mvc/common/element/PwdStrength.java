package net.simpleframework.mvc.common.element;

import net.simpleframework.common.object.ObjectUtils;
import net.simpleframework.common.web.html.HtmlConst;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PwdStrength extends AbstractTagElement<PwdStrength> {
	private String textInput;

	private int height = 17;

	public int getHeight() {
		return height;
	}

	public PwdStrength setHeight(final int height) {
		this.height = height;
		return this;
	}

	public String getTextInput() {
		return textInput;
	}

	public PwdStrength setTextInput(final String textInput) {
		this.textInput = textInput;
		return this;
	}

	@Override
	protected String tag() {
		return "span";
	}

	@Override
	public String getText() {
		final StringBuilder sb = new StringBuilder();
		sb.append("<span class='pwdc'>");
		sb.append(" <span class='pwds'></span>");
		sb.append("</span>");
		sb.append("<span class='lbl'></span>");
		sb.append(HtmlConst.TAG_SCRIPT_START);
		sb.append("(function() { $UI.pwdStrength_update('").append(getId()).append("', '")
				.append(textInput).append("', true); }");
		sb.append(")();");
		sb.append(HtmlConst.TAG_SCRIPT_END);
		return sb.toString();
	}

	@Override
	public String toString() {
		setId("pwdstrength_" + ObjectUtils.hashStr(this));
		addClassName("pwdstrength");
		addStyle("width: 100px; height: " + getHeight() + "px");
		return super.toString();
	}

	private static final long serialVersionUID = 7731024051680408901L;
}
