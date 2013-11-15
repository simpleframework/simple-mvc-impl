package net.simpleframework.mvc.component.ui.pager;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentHtmlRenderEx;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(PagerRegistry.PAGER)
@ComponentBean(PagerBean.class)
@ComponentRender(PagerRender.class)
@ComponentResourceProvider(PagerResourceProvider.class)
public class PagerRegistry extends AbstractComponentRegistry {
	public static final String PAGER = "pager";

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pp, final Object attriData) {
		final PagerBean pagerBean = (PagerBean) super.createComponentBean(pp, attriData);

		final ComponentParameter nCP = ComponentParameter.get(pp, pagerBean);
		ComponentHtmlRenderEx.createAjaxRequest(nCP);
		PagerRender.createDoPager(nCP);

		return pagerBean;
	}
}
