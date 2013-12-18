package net.simpleframework.mvc.component.ui.chosen;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.component.AbstractComponentBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ChosenBean extends AbstractComponentBean {
	/* 可查找 */
	private boolean enableSearch = BeanDefaults.getBool(getClass(), "enableSearch", false);

	public boolean isEnableSearch() {
		return enableSearch;
	}

	public ChosenBean setEnableSearch(final boolean enableSearch) {
		this.enableSearch = enableSearch;
		return this;
	}
}
