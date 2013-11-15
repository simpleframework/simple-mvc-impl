package net.simpleframework.mvc.component.ui.pager;

import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PagerLoaded extends DefaultPageHandler {

	@Override
	public Object getBeanProperty(final PageParameter pp, final String beanProperty) {
		if ("role".equals(beanProperty)) {
			final ComponentParameter nCP = PagerUtils.get(pp);
			if (nCP.componentBean != null) {
				return nCP.getBeanProperty("role");
			}
		}
		return super.getBeanProperty(pp, beanProperty);
	}
}
