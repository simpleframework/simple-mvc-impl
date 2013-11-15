package net.simpleframework.mvc.component.ui.tabs;

import net.simpleframework.common.I18n;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.IComponentRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TabsRender extends ComponentJavascriptRender {
	public TabsRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final TabsBean tabsBean = (TabsBean) cp.componentBean;

		TabItems tabs = null;
		final ITabsHandler tabsHandle = (ITabsHandler) cp.getComponentHandler();
		if (tabsHandle != null) {
			tabs = tabsHandle.tabItems(cp);
		}
		if (tabs == null) {
			tabs = tabsBean.getTabItems();
		}

		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		final StringBuilder sb = new StringBuilder();
		sb.append(ComponentRenderUtils.initContainerVar(cp));
		sb.append(actionFunc).append(".options = {");
		sb.append("effects: Browser.effects && ").append(cp.getBeanProperty("effects")).append(",");
		final String parameters = (String) cp.getBeanProperty("parameters");
		if (StringUtils.hasText(parameters)) {
			sb.append("parameters: \"").append(parameters).append("\".addParameter(arguments[0]),");
		} else {
			sb.append("parameters: arguments[0],");
		}
		sb.append("activeIndex: ").append(cp.getBeanProperty("activeIndex"));
		sb.append("};");

		sb.append(actionFunc).append(".tabs = new $UI.Tabs(");
		sb.append(ComponentRenderUtils.VAR_CONTAINER).append(", [");
		if (tabs != null) {
			int i = 0;
			for (final TabItem tab : tabs) {
				if (i++ > 0) {
					sb.append(",");
				}
				sb.append("{");
				final String content = tab.getContent();
				if (StringUtils.hasText(content)) {
					sb.append("content: \"");
					sb.append(JavascriptUtils.escape(I18n.replaceI18n(content))).append("\",");
				}
				final String contentStyle = tab.getContentStyle();
				if (StringUtils.hasText(contentStyle)) {
					sb.append("contentStyle: \"").append(contentStyle).append("\",");
				}
				final String contentRef = tab.getContentRef();
				if (StringUtils.hasText(contentRef)) {
					sb.append("contentRef: \"").append(contentRef).append("\",");
				}
				sb.append("cache: ").append(tab.isCache()).append(",");
				final String jsActiveCallback = tab.getJsActiveCallback();
				if (StringUtils.hasText(jsActiveCallback)) {
					sb.append("onActive: \"");
					sb.append(JavascriptUtils.escape(jsActiveCallback)).append("\",");
				}
				final String jsContentLoadedCallback = tab.getJsContentLoadedCallback();
				if (StringUtils.hasText(jsContentLoadedCallback)) {
					sb.append("onContentLoaded: \"");
					sb.append(JavascriptUtils.escape(jsContentLoadedCallback)).append("\",");
				}
				sb.append("title: \"");
				sb.append(JavascriptUtils.escape(tab.getTitle())).append("\"");
				sb.append("}");
			}
		}
		sb.append("], ").append(actionFunc).append(".options);");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString());
	}
}