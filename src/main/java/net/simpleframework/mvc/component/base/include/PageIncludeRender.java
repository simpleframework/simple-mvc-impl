package net.simpleframework.mvc.component.base.include;

import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.UrlForward;
import net.simpleframework.mvc.component.ComponentHtmlRenderEx;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PageIncludeRender extends ComponentHtmlRenderEx {

	public PageIncludeRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public IForward getResponseForward(final ComponentParameter cp) {
		final IPageIncludeHandler jHandle = (IPageIncludeHandler) cp.getComponentHandler();
		IForward forward;
		if (jHandle != null && (forward = jHandle.include(cp)) != null) {
			return forward;
		}
		return new UrlForward((String) cp.getBeanProperty("pageUrl"));
	}
}
