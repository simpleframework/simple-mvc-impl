package net.simpleframework.mvc.component.base.include;

import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PageIncludeBean extends AbstractContainerBean {
	private static final long serialVersionUID = -5694139352368220286L;

	private String pageUrl;

	public String getPageUrl() {
		return pageUrl;
	}

	public PageIncludeBean setPageUrl(final String pageUrl) {
		this.pageUrl = pageUrl;
		return this;
	}
}
