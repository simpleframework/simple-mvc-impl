package net.simpleframework.mvc.component.ui.chosen;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.AbstractComponentBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ChosenBean extends AbstractComponentBean {
	/* 可查找 */
	private boolean enableSearch = BeanDefaults.getBool(getClass(), "enableSearch", false);

	public ChosenBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
	}

	public ChosenBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	public boolean isEnableSearch() {
		return enableSearch;
	}

	public ChosenBean setEnableSearch(final boolean enableSearch) {
		this.enableSearch = enableSearch;
		return this;
	}
}
