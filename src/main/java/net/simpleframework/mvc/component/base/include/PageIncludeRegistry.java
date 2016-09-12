package net.simpleframework.mvc.component.base.include;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentHtmlRenderEx;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRender;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(PageIncludeRegistry.INCLUDE)
@ComponentBean(PageIncludeBean.class)
@ComponentRender(PageIncludeRender.class)
public class PageIncludeRegistry extends AbstractComponentRegistry {
	public static final String INCLUDE = "include";

	@Override
	public PageIncludeBean createComponentBean(final PageParameter pp, final Object attriData) {
		final PageIncludeBean pageInclude = (PageIncludeBean) super.createComponentBean(pp,
				attriData);
		ComponentHtmlRenderEx.createAjaxRequest(ComponentParameter.get(pp, pageInclude));
		return pageInclude;
	}
}
