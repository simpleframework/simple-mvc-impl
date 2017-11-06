package net.simpleframework.mvc.common.element;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.common.web.html.HtmlConst;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DictMultiSelectInput extends DictInput {
	public DictMultiSelectInput() {
	}

	public DictMultiSelectInput(final String id) {
		super(id);
	}

	private Map<String, String> values;

	public DictMultiSelectInput addValue(final String id, final String text) {
		if (values == null) {
			values = new HashMap<>();
		}
		values.put(id, text);
		return this;
	}

	@Override
	public InputElement getHiddenField() {
		return null;
	}

	@Override
	public String toString() {
		final String dictComponent = getDictComponent();
		final boolean hasComp = StringUtils.hasText(dictComponent);
		if (hasComp) {
			final StringBuilder sb = new StringBuilder();
			sb.append("var act = $Actions['").append(dictComponent).append("'];");
			sb.append("act(");
			final String parameters = getParameters();
			if (StringUtils.hasText(parameters)) {
				sb.append("$UI.evalParam(\"").append(JavascriptUtils.escape(parameters)).append("\")");
			}
			sb.append(");");
			setOnclick(sb.toString());
		}
		final StringBuilder sb = new StringBuilder();
		final boolean readonly = isReadonly();
		sb.append("<div class='multi_dselect'");
		final String style = getStyle();
		if (StringUtils.hasText(style)) {
			sb.append(" style='").append(style).append("'");
		}
		sb.append(">");
		final InputElement val = InputElement.hidden(getId());
		final boolean hasVal = values != null && values.size() > 0;
		if (hasVal) {
			val.setText(StringUtils.join(values.keySet(), ";"));
		}
		sb.append(val);
		if (!readonly) {
			sb.append(toSbtnHTML(getOnclick()));
			sb.append(toSbtnHTML(getOnclick2()));
			sb.append(toSbtnHTML(getOnclick3()));
		}
		sb.append("</div>");
		sb.append(HtmlConst.TAG_SCRIPT_START);
		sb.append("(function() {");
		sb.append(" var r = $('").append(getId()).append("');");
		sb.append(" if (!r) return;");
		sb.append(" r = r.up('.multi_dselect');");
		sb.append(" $UI.multiSelectInputField_init(r, ").append(readonly).append(");");
		if (hasVal) {
			for (final Map.Entry<String, String> entry : values.entrySet()) {
				sb.append("r.insertItem('").append(entry.getKey());
				sb.append("', '").append(entry.getValue()).append("');");
			}
		}
		if (hasComp) {
			sb.append("var act = $Actions['").append(dictComponent).append("'];");
			sb.append("act.jsSelectCallback = function(selects) {");
			sb.append(" for ( var i = 0; i < selects.length; i++)");
			sb.append("  r.insertItem(selects[i].id, selects[i].text);");
			sb.append(" return true;");
			sb.append("};");
		}
		sb.append("})();");
		sb.append(HtmlConst.TAG_SCRIPT_END);
		return sb.toString();
	}
}
