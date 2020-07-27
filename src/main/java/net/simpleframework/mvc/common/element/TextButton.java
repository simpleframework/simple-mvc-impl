package net.simpleframework.mvc.common.element;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.html.HtmlConst;
import net.simpleframework.common.web.html.HtmlEncoder;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TextButton extends AbstractInputElement<TextButton> {
	/**
	 * 自动生成一个名字和id一样的隐藏域
	 */
	private InputElement hiddenField;

	/* 第二个按钮 */
	private String onclick2;

	/* 第三个按钮 */
	private String onclick3;

	private boolean editable;

	private int bwidth;

	/* 显示清除按钮 */
	private boolean showClearButton;

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

	public String getOnclick2() {
		return onclick2;
	}

	public TextButton setOnclick2(final String onclick2) {
		this.onclick2 = onclick2;
		return this;
	}

	public String getOnclick3() {
		return onclick3;
	}

	public TextButton setOnclick3(final String onclick3) {
		this.onclick3 = onclick3;
		return this;
	}

	public boolean isEditable() {
		return editable;
	}

	public TextButton setEditable(final boolean editable) {
		this.editable = editable;
		return this;
	}

	public int getBwidth() {
		return bwidth;
	}

	public TextButton setBwidth(final int bwidth) {
		this.bwidth = bwidth;
		return this;
	}

	public boolean isShowClearButton() {
		return showClearButton && !isEditable();
	}

	public TextButton setShowClearButton(final boolean showClearButton) {
		this.showClearButton = showClearButton;
		return this;
	}

	@Override
	protected boolean isTextReadonly() {
		return isReadonly() || !isEditable();
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
		if (hiddenField != null && isEditable()) {
			addAttribute("onkeyup", "this.next().clear();");
		}
		sb.append(super.toString());
		if (hiddenField != null) {
			sb.append(hiddenField);
		}
		return sb.toString();
	}

	protected String toSbtnHTML(final String onclick, final int i) {
		final StringBuilder sb = new StringBuilder();
		if (StringUtils.hasText(onclick)) {
			sb.append("<div class='sbtn' onclick=\"");
			sb.append(HtmlEncoder.text(onclick)).append("\"");
			final int w = getBwidth();
			if (w > 0) {
				sb.append(" style=\"width: ").append(w).append("px;\"");
			}
			sb.append("></div>");
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='text text_button'");
		if (isAutoRows()) {
			addStyle("display: block;");
		}
		final String style = getStyle();
		if (StringUtils.hasText(style)) {
			sb.append(" style='").append(style).append("'");
		}
		sb.append(">");
		int w = getBwidth();
		if (StringUtils.hasText(getOnclick2())) {
			w = w * 2;
			if (StringUtils.hasText(getOnclick3())) {
				w = w * 3;
			}
		}
		sb.append(" <div class='d1' style='margin-right: -").append(w).append("px;'>");
		sb.append("  <div class='d2' style='margin-right: ").append(w).append("px;'>")
				.append(toInputHTML()).append("</div>");
		sb.append(" </div>");
		boolean clearButton = false;
		if (!isReadonly()) {
			if (isShowClearButton()) {
				sb.append("<div class='sbtn del'></div>");
				clearButton = true;
			}
			sb.append(toSbtnHTML(getOnclick(), 0));
			sb.append(toSbtnHTML(getOnclick2(), 1));
			sb.append(toSbtnHTML(getOnclick3(), 2));
		}
		sb.append("</div>");
		if (clearButton) {
			sb.append(HtmlConst.TAG_SCRIPT_START);
			sb.append("(function() {");
			sb.append(" var r = $('").append(getId()).append("');");
			sb.append(" if (!r) return;");
			sb.append(" var del = r.up('.text_button').down('.del');");
			sb.append(" if (!del) return;");
			sb.append(" del.observe('click', function(evn) { ")
					.append("r.clear(); var n=r.next(); if (n) n.clear(); del.hide();").append(" });");
			sb.append(" var f = function(evn) { ")
					.append("if (r.value.length > 0) del.show(); else del.hide();").append(" }; f();");
			sb.append(" r.observe('input', f).observe('focus', f);");
			sb.append("})();");
			sb.append(HtmlConst.TAG_SCRIPT_END);
		}
		return sb.toString();
	}

	private static final long serialVersionUID = 8089943714058746956L;
}
