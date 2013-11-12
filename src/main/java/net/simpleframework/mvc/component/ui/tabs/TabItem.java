package net.simpleframework.mvc.component.ui.tabs;

import net.simpleframework.ctx.common.xml.AbstractElementBean;
import net.simpleframework.ctx.common.xml.XmlElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TabItem extends AbstractElementBean {
	private String title;

	private String content;

	private String contentStyle;

	private String contentRef;

	private boolean cache;

	private String jsActiveCallback;

	private String jsContentLoadedCallback;

	public TabItem(final XmlElement xmlElement) {
		super(xmlElement);
	}

	public TabItem() {
		super(null);
	}

	public TabItem(final String title) {
		super(null);
		setTitle(title);
	}

	public String getTitle() {
		return title;
	}

	public TabItem setTitle(final String title) {
		this.title = title;
		return this;
	}

	public String getContent() {
		return content;
	}

	public TabItem setContent(final String content) {
		this.content = content;
		return this;
	}

	public String getContentRef() {
		return contentRef;
	}

	public TabItem setContentRef(final String contentRef) {
		this.contentRef = contentRef;
		return this;
	}

	public String getContentStyle() {
		return contentStyle;
	}

	public TabItem setContentStyle(final String contentStyle) {
		this.contentStyle = contentStyle;
		return this;
	}

	public boolean isCache() {
		return cache;
	}

	public TabItem setCache(final boolean cache) {
		this.cache = cache;
		return this;
	}

	public String getJsActiveCallback() {
		return jsActiveCallback;
	}

	public TabItem setJsActiveCallback(final String jsActiveCallback) {
		this.jsActiveCallback = jsActiveCallback;
		return this;
	}

	public String getJsContentLoadedCallback() {
		return jsContentLoadedCallback;
	}

	public TabItem setJsContentLoadedCallback(final String jsContentLoadedCallback) {
		this.jsContentLoadedCallback = jsContentLoadedCallback;
		return this;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsActiveCallback", "jsContentLoadedCallback" };
	}
}
