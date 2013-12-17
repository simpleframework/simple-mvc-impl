package net.simpleframework.mvc.component.ui.pager;

import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.mvc.PageParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TablePagerResourceProvider extends PagerResourceProvider {

	@Override
	public String[] getCssPath(final PageParameter pp) {
		return ArrayUtils.add(super.getCssPath(pp),
				getCssResourceHomePath(pp, TablePagerResourceProvider.class) + "/tablepager.css");
	}

	@Override
	public String[] getJavascriptPath(final PageParameter pp) {
		return ArrayUtils.add(super.getJavascriptPath(pp),
				getResourceHomePath(TablePagerResourceProvider.class) + "/js/tablepager.js");
	}
}
