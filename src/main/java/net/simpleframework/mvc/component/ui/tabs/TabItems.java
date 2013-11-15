package net.simpleframework.mvc.component.ui.tabs;

import net.simpleframework.common.coll.AbstractArrayListEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TabItems extends AbstractArrayListEx<TabItems, TabItem> {

	public static TabItems of(final TabItem... items) {
		return new TabItems().append(items);
	}

	private static final long serialVersionUID = 1620383138690227387L;
}
