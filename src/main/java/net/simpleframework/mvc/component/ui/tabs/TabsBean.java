package net.simpleframework.mvc.component.ui.tabs;

import net.simpleframework.ctx.common.bean.BeanDefaults;
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
public class TabsBean extends AbstractContainerBean {

	private TabItems tabItems;

	private int activeIndex = BeanDefaults.getInt(getClass(), "activeIndex", 0);

	public TabsBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
	}

	public TabsBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	public TabItems getTabItems() {
		if (tabItems == null) {
			tabItems = TabItems.of();
		}
		return tabItems;
	}

	public TabsBean addTab(final TabItem tab) {
		getTabItems().add(tab);
		return this;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public TabsBean setActiveIndex(final int activeIndex) {
		this.activeIndex = activeIndex;
		return this;
	}
}
