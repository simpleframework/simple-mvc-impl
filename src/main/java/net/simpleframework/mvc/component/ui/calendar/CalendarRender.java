package net.simpleframework.mvc.component.ui.calendar;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.IComponentRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class CalendarRender extends ComponentJavascriptRender {
	public CalendarRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		sb.append(actionFunc).append(".calendar = new CalendarDateSelect({");

		final String dateFormat = (String) cp.getBeanProperty("dateFormat");
		if (StringUtils.hasText(dateFormat)) {
			sb.append("dateFormat: \"").append(dateFormat).append("\",");
		}
		final String closeCallback = (String) cp.getBeanProperty("closeCallback");
		if (StringUtils.hasText(closeCallback)) {
			sb.append("onclose: function(cal) {");
			sb.append(closeCallback);
			sb.append("},");
		}
		sb.append("showTime: ").append(cp.getBeanProperty("showTime")).append(",");
		sb.append("effects: Browser.effects && ").append(cp.getBeanProperty("effects"));
		sb.append("});");

		// show函数
		sb.append(actionFunc).append(".show = function(inputField, dateFormat) { ");
		sb.append(actionFunc).append(".calendar.show(inputField || \"")
				.append(cp.getBeanProperty("inputField")).append("\", dateFormat); };");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString());
	}
}
