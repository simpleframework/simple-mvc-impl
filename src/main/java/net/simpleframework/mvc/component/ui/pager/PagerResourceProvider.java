package net.simpleframework.mvc.component.ui.pager;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.IComponentResourceProvider.AbstractComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PagerResourceProvider extends AbstractComponentResourceProvider {

	@Override
	public String[] getCssPath(final PageParameter pp) {
		return new String[] {
				getCssResourceHomePath(pp, PagerResourceProvider.class) + "/pager.css" };
	}

	@Override
	public String[] getJavascriptPath(final PageParameter pp) {
		return new String[] {
				getResourceHomePath(TablePagerResourceProvider.class) + "/js/pager.js" };
	}
}
