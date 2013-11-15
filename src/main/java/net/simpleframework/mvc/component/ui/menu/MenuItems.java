package net.simpleframework.mvc.component.ui.menu;

import net.simpleframework.common.coll.AbstractArrayListEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MenuItems extends AbstractArrayListEx<MenuItems, MenuItem> {

	public static MenuItems of(final MenuItem... items) {
		return new MenuItems().append(items);
	}

	private static final long serialVersionUID = 5724329853563479717L;
}
