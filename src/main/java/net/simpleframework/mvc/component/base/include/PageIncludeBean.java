package net.simpleframework.mvc.component.base.include;

import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PageIncludeBean extends AbstractContainerBean {
	private String pageUrl;

	public PageIncludeBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
	}

	public PageIncludeBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public PageIncludeBean setPageUrl(final String pageUrl) {
		this.pageUrl = pageUrl;
		return this;
	}
}
