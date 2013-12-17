package net.simpleframework.mvc.component.ui.propeditor;

import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.mvc.common.element.EElementEvent;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PropEditorRender extends ComponentJavascriptRender {

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		PropFields propFields = null;
		final IPropEditorHandler peHandle = (IPropEditorHandler) cp.getComponentHandler();
		if (peHandle != null) {
			propFields = peHandle.getFormFields(cp);
		}
		if (propFields == null) {
			propFields = ((PropEditorBean) cp.componentBean).getFormFields();
		}
		final StringBuilder sb = new StringBuilder();
		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		sb.append(ComponentRenderUtils.initContainerVar(cp));
		sb.append(actionFunc).append(".formEditor = new $UI.FormEditor(")
				.append(ComponentRenderUtils.VAR_CONTAINER).append(", [");
		int i = 0;
		for (final PropField field : propFields) {
			if (i++ > 0) {
				sb.append(",");
			}
			sb.append("{");
			final String label = field.getLabel();
			if (StringUtils.hasText(label)) {
				sb.append("label: \"").append(label).append("\",");
			}
			final String labelStyle = field.getLabelStyle();
			if (StringUtils.hasText(labelStyle)) {
				sb.append("labelStyle: \"").append(labelStyle).append("\",");
			}
			final String description = field.getDescription();
			if (StringUtils.hasText(description)) {
				sb.append("desc: \"").append(JavascriptUtils.escape(description)).append("\",");
			}
			sb.append("components: ").append(getComponents(field));
			sb.append("}");
		}
		sb.append("], {");
		final String title = (String) cp.getBeanProperty("title");
		if (StringUtils.hasText(title)) {
			sb.append("title: \"").append(JavascriptUtils.escape(title)).append("\",");
		}
		sb.append("titleToggle: ").append(cp.getBeanProperty("titleToggle")).append(",");
		sb.append("onLoaded: function() {");
		final String callback = (String) cp.getBeanProperty("jsLoadedCallback");
		if (StringUtils.hasText(callback)) {
			sb.append(callback);
		}
		sb.append("}");
		sb.append("});");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString());
	}

	private String getComponents(final PropField field) {
		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		int i = 0;
		for (final InputComp comp : field.getInputComponents()) {
			if (i++ > 0) {
				sb.append(",");
			}
			sb.append("{");
			final String name = comp.getName();
			if (StringUtils.hasText(name)) {
				sb.append("name: \"").append(name).append("\",");
			}
			final String style = comp.getStyle();
			if (StringUtils.hasText(style)) {
				sb.append("style: \"").append(style).append("\",");
			}
			final String[] attributes = StringUtils.split(comp.getAttributes(), ";");
			if (attributes != null && attributes.length > 0) {
				sb.append("attributes: {");
				int j = 0;
				for (final String attribute : attributes) {
					final String[] arr = StringUtils.split(attribute, ":");
					if (arr != null && arr.length > 0) {
						if (j++ > 0) {
							sb.append(",");
						}
						sb.append(arr[0]).append(": \"");
						sb.append(arr.length == 1 ? arr[0] : arr[1]);
						sb.append("\"");
					}
				}
				sb.append("},");
			}
			final String defaultValue = comp.getDefaultValue();
			if (StringUtils.hasText(defaultValue)) {
				sb.append("defaultValue: \"");
				sb.append(JavascriptUtils.escape(defaultValue)).append("\",");
			}
			sb.append("type: \"").append(comp.getType()).append("\",");
			sb.append("events: {");
			int j = 0;
			for (final Map.Entry<EElementEvent, String> entry : comp.getEventCallback().entrySet()) {
				if (j++ > 0) {
					sb.append(",");
				}
				sb.append(entry.getKey()).append(": \"");
				sb.append(JavascriptUtils.escape(entry.getValue()));
				sb.append("\"");
			}
			sb.append("}");
			sb.append("}");
		}
		sb.append("]");
		return sb.toString();
	}
}
