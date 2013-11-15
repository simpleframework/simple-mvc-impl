package net.simpleframework.mvc.component.ui.menu;

import net.simpleframework.common.Convert;
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
public class MenuRender extends ComponentJavascriptRender {
	public MenuRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final MenuBean menuBean = (MenuBean) cp.componentBean;
		MenuItems menuItems = null;
		final IMenuHandler mHandle = (IMenuHandler) cp.getComponentHandler();
		if (mHandle != null) {
			menuItems = mHandle.getMenuItems(cp, null);
		}
		if (menuItems == null) {
			menuItems = menuBean.getMenuItems();
		}
		final StringBuilder sb = new StringBuilder();
		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		final String containerId = (String) cp.getBeanProperty("containerId");
		final boolean container = StringUtils.hasText(containerId);
		if (container) {
			sb.append(ComponentRenderUtils.initContainerVar(cp));
			sb.append(actionFunc).append(".menu = new $UI.Menu(")
					.append(ComponentRenderUtils.VAR_CONTAINER).append(", {");
		} else {
			sb.append(actionFunc).append(".menu = new $UI.Menu(null, {");
			final String selector = (String) cp.getBeanProperty("selector");
			if (StringUtils.hasText(selector)) {
				sb.append("\"selector\": \"").append(selector).append("\",");
			}
			final EMenuEvent menuEvent = (EMenuEvent) cp.getBeanProperty("menuEvent");
			if (menuEvent != null) {
				sb.append("\"menuEvent\": \"").append(menuEvent).append("\",");
			}
		}

		final int minWidth = Convert.toInt(cp.getBeanProperty("minWidth"));
		if (minWidth > 0) {
			sb.append("\"minWidth\": \"").append(minWidth).append("px\",");
		}
		sb.append("\"effects\": Browser.effects && ").append(cp.getBeanProperty("effects"))
				.append(",");

		final String beforeShowCallback = (String) cp.getBeanProperty("jsBeforeShowCallback");
		if (StringUtils.hasText(beforeShowCallback)) {
			sb.append("\"onBeforeShow\": function(menu, e) {");
			sb.append(beforeShowCallback);
			sb.append("},");
		}
		sb.append("\"menuItems\": ").append(jsonData(cp, menuItems));
		sb.append("});");

		final StringBuilder sb2 = new StringBuilder();
		sb2.append("__menu_actions_init(").append(actionFunc).append(", '")
				.append(cp.getComponentName()).append("');");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString(), sb2.toString());
	}

	String jsonData(final ComponentParameter cp, final MenuItems menuItems) {
		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		int i = 0;
		for (final MenuItem menu : menuItems) {
			if (i++ > 0) {
				sb.append(",");
			}
			sb.append("{");
			final String title = menu.getTitle();
			if (title.equals("-")) {
				sb.append("\"separator\": true");
			} else {
				sb.append("\"name\": \"").append(JavascriptUtils.escape(title)).append("\",");
				final boolean checkbox = menu.isCheckbox();
				if (checkbox) {
					sb.append("\"checkbox\": true,");
					sb.append("\"checked\": ").append(menu.isChecked()).append(",");
					final String checkCallback = menu.getOncheck();
					if (StringUtils.hasText(checkCallback)) {
						sb.append("\"onCheck\": function(item, e) {");
						sb.append(checkCallback);
						sb.append("},");
					}
				} else {
					final String icon = menu.getIconClass();
					if (StringUtils.hasText(icon)) {
						sb.append("\"icon\": \"").append(icon).append("\",");
					}
				}
				final String url = menu.getUrl();
				if (StringUtils.hasText(url)) {
					sb.append("\"url\": \"").append(cp.wrapContextPath(url)).append("\",");
				} else {
					final String selectCallback = menu.getOnclick();
					if (StringUtils.hasText(selectCallback)) {
						sb.append("\"onSelect\": function(item, e) {");
						sb.append(selectCallback.trim());
						sb.append("},");
					}
				}

				final String desc = menu.getDescription();
				if (StringUtils.hasText(desc)) {
					sb.append("\"desc\": \"").append(JavascriptUtils.escape(desc)).append("\",");
				}

				MenuItems children = null;
				final IMenuHandler handle = (IMenuHandler) cp.getComponentHandler();
				if (handle != null) {
					children = handle.getMenuItems(cp, menu);
				}
				if (children == null) {
					children = menu.children();
				}
				if (children.size() > 0) {
					sb.append("\"submenu\": ").append(jsonData(cp, children)).append(",");
				}
				sb.append("\"disabled\": ").append(menu.isDisabled());
			}
			sb.append("}");
		}
		sb.append("]");
		return sb.toString();
	}
}
