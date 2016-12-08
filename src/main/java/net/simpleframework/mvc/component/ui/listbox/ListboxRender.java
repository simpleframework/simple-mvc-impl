package net.simpleframework.mvc.component.ui.listbox;

import java.util.Map;

import net.simpleframework.common.JsonUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.common.web.html.HtmlEncoder;
import net.simpleframework.mvc.common.ItemUIBean;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ListboxRender extends ComponentJavascriptRender {
	public static final String EVENT_CLICK = "__click";

	public static final String EVENT_DBLCLICK = "__dblclick";

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		ListItems listItems = null;
		final IListboxHandler lHandle = (IListboxHandler) cp.getComponentHandler();
		if (lHandle != null) {
			listItems = lHandle.getListItems(cp);
		}
		if (listItems == null) {
			listItems = ((ListboxBean) cp.componentBean).getListItems();
		}

		final StringBuilder sb = new StringBuilder();
		sb.append(ComponentRenderUtils.initContainerVar(cp));

		// event
		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		sb.append(actionFunc).append(".").append(
				eventClick(EVENT_CLICK, (String) cp.getBeanProperty("jsClickCallback"), actionFunc));
		sb.append(actionFunc).append(".").append(eventClick(EVENT_DBLCLICK,
				(String) cp.getBeanProperty("jsDblclickCallback"), actionFunc));
		// new
		sb.append(actionFunc).append(".listbox = new $UI.ListBox(")
				.append(ComponentRenderUtils.VAR_CONTAINER).append(", [");
		int i = 0;
		for (final ListItem listItem : listItems) {
			if (i++ > 0) {
				sb.append(",");
			}
			sb.append("{");
			final String text = listItem.getText();
			if (StringUtils.hasText(text)) {
				sb.append("text: \"").append(JavascriptUtils.escape(HtmlEncoder.text(text)))
						.append("\",");
			}
			final String tip = listItem.getTooltip();
			if (StringUtils.hasText(tip)) {
				sb.append("tip: \"").append(JavascriptUtils.escape(HtmlEncoder.text(tip)))
						.append("\",");
			}
			if (lHandle != null) {
				final Map<String, Object> attributes = lHandle.getListItemAttributes(cp, listItem);
				if (attributes != null && attributes.size() > 0) {
					sb.append("\"attributes\": ").append(JsonUtils.toJSON(attributes)).append(",");
				}
			}
			sb.append(getUIEventData(listItem, actionFunc));
			sb.append("id: \"").append(listItem.getId()).append("\"");
			sb.append("}");
		}
		sb.append("], {");
		sb.append("checkbox: ").append(cp.getBeanProperty("checkbox")).append(",");
		sb.append("tooltip: ").append(cp.getBeanProperty("tooltip"));
		sb.append("});");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString());
	}

	private String getUIEventData(final ItemUIBean<?> bean, final String actionFunc) {
		final StringBuilder sb = new StringBuilder();
		sb.append("click: ").append(actionFunc).append(".");
		sb.append(EVENT_CLICK).append(",");
		sb.append("dblclick: ").append(actionFunc).append(".");
		sb.append(EVENT_DBLCLICK).append(",");

		final String click = bean.getJsClickCallback();
		if (StringUtils.hasText(click)) {
			sb.append(EVENT_CLICK).append(": \"");
			sb.append(JavascriptUtils.escape(click)).append("\",");
		}
		final String dblclick = bean.getJsDblclickCallback();
		if (StringUtils.hasText(dblclick)) {
			sb.append(EVENT_DBLCLICK).append(": \"");
			sb.append(JavascriptUtils.escape(click)).append("\",");
		}
		return sb.toString();
	}

	protected String eventClick(final String event, final String defaultBody,
			final String actionFunc) {
		final StringBuilder sb = new StringBuilder();
		sb.append(event).append("=").append("function(item, ev) {");
		sb.append("var cb = function(id, text, item, json, ev) {");
		sb.append("var func = item.data.").append(event).append(";");
		sb.append("if (func) { eval(func); } else {");
		sb.append(StringUtils.blank(defaultBody)).append("}};");
		sb.append("cb(item.getId(), item.getText(), item, ").append(actionFunc)
				.append(".json, ev); };");
		return sb.toString();
	}
}
