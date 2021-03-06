package net.simpleframework.mvc.component.ui.tabs;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TabsBean extends AbstractContainerBean {
	private static final long serialVersionUID = -7715507246215731431L;

	private TabItems tabItems;

	private int activeIndex = BeanDefaults.getInt(getClass(), "activeIndex", 0);

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
