package net.simpleframework.mvc.component.ui.window;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.HttpUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.mvc.MVCUtils;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class WindowRender extends ComponentJavascriptRender {

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		final StringBuilder sb = new StringBuilder();
		final boolean destroyOnClose = (Boolean) cp.getBeanProperty("destroyOnClose");
		if (!destroyOnClose) {
			sb.append("if (!").append(actionFunc).append(".window) {");
		}

		sb.append(actionFunc).append(".createWindow(");
		final String url = (String) cp.getBeanProperty("url");
		if (StringUtils.hasText(url)) {
			sb.append("\"");
			sb.append(cp.wrapContextPath(
					MVCUtils.doPageUrl(cp, HttpUtils.addParameters(url, "iframe=true")))).append("\", ");
		} else {
			sb.append("null, ");
		}

		final String content = (String) cp.getBeanProperty("content");
		if (StringUtils.hasText(content)) {
			sb.append("\"").append(JavascriptUtils.escape(content)).append("\", ");
		} else {
			sb.append("null, ");
		}

		final String contentRef = (String) cp.getBeanProperty("contentRef");
		if (StringUtils.hasText(contentRef)) {
			sb.append("\"").append(contentRef).append("\"");
		} else {
			sb.append("null");
		}
		sb.append(");");
		final String jsHiddenCallback = (String) cp.getBeanProperty("jsHiddenCallback");
		if (StringUtils.hasText(jsHiddenCallback)) {
			sb.append(actionFunc).append(".observe('hidden', function() {");
			sb.append(jsHiddenCallback).append("});");
		}
		final String jsShownCallback = (String) cp.getBeanProperty("jsShownCallback");
		if (StringUtils.hasText(jsShownCallback)) {
			sb.append(actionFunc).append(".observe('shown', function() {");
			sb.append(jsShownCallback).append("});");
		}
		if (!destroyOnClose) {
			sb.append("}");
		}

		sb.append(actionFunc).append(".showWindow(");
		final String title = (String) cp.getBeanProperty("title");
		if (StringUtils.hasText(title)) {
			sb.append("\"").append(JavascriptUtils.escape(title)).append("\", ");
		} else {
			sb.append("null, ");
		}
		final boolean popup = (Boolean) cp.getBeanProperty("popup");
		final boolean showCenter = (Boolean) cp.getBeanProperty("showCenter");
		final boolean modal = (Boolean) cp.getBeanProperty("modal");
		sb.append(popup).append(", ");
		sb.append(showCenter).append(", ");
		sb.append(modal).append(", ");
		sb.append(destroyOnClose).append(", arguments[0]);");

		final StringBuilder sb2 = new StringBuilder();
		sb2.append("__window_actions_init(").append(actionFunc).append(", '")
				.append(cp.getComponentName()).append("', ")
				.append((!modal || popup) && (Boolean) cp.getBeanProperty("singleWindow")).append(");");
		sb2.append(jsonOptions(cp, actionFunc, popup, destroyOnClose));
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString(), sb2.toString());
	}

	private static String jsonOptions(final ComponentParameter cp, final String actionFunc,
			final boolean popup, final boolean destroyOnClose) {
		final StringBuilder sb = new StringBuilder();
		sb.append(actionFunc).append(".options = {");
		if (popup) {
			sb.append("theme: \"popup\",");
		}

		sb.append("top: ").append(cp.getBeanProperty("top")).append(",");
		sb.append("left: ").append(cp.getBeanProperty("left")).append(",");
		sb.append("width: ").append(cp.getBeanProperty("width")).append(",");
		sb.append("height: ").append(cp.getBeanProperty("height")).append(",");
		sb.append("minWidth: ").append(cp.getBeanProperty("minWidth")).append(",");
		sb.append("minHeight: ").append(cp.getBeanProperty("minHeight")).append(",");
		sb.append("maxWidth: ").append(cp.getBeanProperty("maxWidth")).append(",");
		sb.append("maxHeight: ").append(cp.getBeanProperty("maxHeight")).append(",");
		sb.append("xdelta: ").append(cp.getBeanProperty("xdelta")).append(",");
		sb.append("ydelta: ").append(cp.getBeanProperty("ydelta")).append(",");
		if (!(Boolean) cp.getBeanProperty("minimize")) {
			sb.append("minimize: false,");
		}
		if (!(Boolean) cp.getBeanProperty("maximize")) {
			sb.append("maximize: false,");
		}
		if (!(Boolean) cp.getBeanProperty("resizable")) {
			sb.append("resizable: false,");
		}
		if (!(Boolean) cp.getBeanProperty("draggable")) {
			sb.append("draggable: false,");
		}
		if (!destroyOnClose) {
			sb.append("close: \"hide\",");
		}
		sb.append("effects : Browser.effects && ").append(cp.getBeanProperty("effects"));
		sb.append("};");
		return sb.toString();
	}
}
