package net.simpleframework.mvc.component.ui.window;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentException;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(WindowRegistry.WINDOW)
@ComponentBean(WindowBean.class)
@ComponentRender(WindowRender.class)
@ComponentResourceProvider(WindowResourceProvider.class)
public class WindowRegistry extends AbstractComponentRegistry {
	public static final String WINDOW = "window";

	@Override
	public WindowBean createComponentBean(final PageParameter pp, final Object attriData) {
		final WindowBean windowBean = (WindowBean) super.createComponentBean(pp, attriData);
		final String contentRef = windowBean.getContentRef();
		if (StringUtils.hasText(contentRef)) {
			final AbstractComponentBean ref = pp.getComponentBeanByName(contentRef);
			if (ref == null) {
				if (pp.getComponentBeanByName(contentRef) == null) {
					throw ComponentException.wrapException_ComponentRef(contentRef);
				}
			} else {
				ref.setRunImmediately(false);
				windowBean.setContent(getLoadingContent());
				if (ref instanceof AjaxRequestBean) {
					((AjaxRequestBean) ref).setShowLoading(false);
				}
			}
		}
		return windowBean;
	}
}
