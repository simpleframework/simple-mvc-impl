package net.simpleframework.mvc.common.element;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.html.HtmlEncoder;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TextButton extends AbstractInputElement<TextButton> {

	/**
	 * 自动生成一个名字和id一样的隐藏域
	 */
	private InputElement hiddenField;

	private boolean editable;

	public TextButton() {
	}

	public TextButton(final String id) {
		setId(id).setName(id);
	}

	public InputElement getHiddenField() {
		return hiddenField;
	}

	public TextButton setHiddenField(final InputElement hiddenField) {
		this.hiddenField = hiddenField;
		return this;
	}

	public TextButton setHiddenField(final String hiddenField) {
		this.hiddenField = InputElement.hidden(hiddenField);
		return this;
	}

	public boolean isEditable() {
		return editable;
	}

	public TextButton setEditable(final boolean editable) {
		this.editable = editable;
		return this;
	}

	public boolean _isReadonly() {
		return super.isReadonly();
	}

	@Override
	public boolean isReadonly() {
		return _isReadonly() || !isEditable();
	}

	@Override
	public TextButton addAttribute(final String key, final Object val) {
		if ("onclick".equals(key)) {
			return this;
		}
		return super.addAttribute(key, val);
	}

	protected String toInputHTML() {
		final StringBuilder sb = new StringBuilder();
		final InputElement hiddenField = getHiddenField();
		if (hiddenField != null && !isReadonly()) {
			addAttribute("onkeyup", "this.next().clear();");
		}
		sb.append(super.toString());
		if (hiddenField != null) {
			sb.append(hiddenField);
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='text text_button'>");
		sb.append(" <div class='d1'>");
		sb.append("  <div class='d2'>").append(toInputHTML()).append("</div>");
		sb.append(" </div>");
		if (!super.isReadonly()) {
			sb.append("<div class='sbtn'");
			final String onclick = getOnclick();
			if (StringUtils.hasText(onclick)) {
				sb.append(" onclick=\"").append(HtmlEncoder.text(onclick)).append("\"");
			}
			sb.append("></div>");
		}
		sb.append("</div>");
		return sb.toString();
	}
}
