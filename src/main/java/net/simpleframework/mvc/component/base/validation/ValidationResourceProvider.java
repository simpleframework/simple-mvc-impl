package net.simpleframework.mvc.component.base.validation;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.IComponentResourceProvider.AbstractComponentResourceProvider;
import net.simpleframework.mvc.component.ui.calendar.CalendarResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ValidationResourceProvider extends AbstractComponentResourceProvider {

	@Override
	public String[] getJavascriptPath(final PageParameter pp) {
		return new String[] { getResourceHomePath(CalendarResourceProvider.class) + "/js/date.js",
				getResourceHomePath() + "/js/validation.js" };
	}
}
