package net.simpleframework.mvc.component.ui.pager;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.menu.AbstractMenuHandler;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.menu.MenuItems;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TablePagerMenu2 extends AbstractMenuHandler {

	@Override
	public MenuItems getMenuItems(final ComponentParameter cp, final MenuItem menuItem) {
		if (menuItem != null) {
			return null;
		}
		return getHeaderMenu(PagerUtils.get(cp), (MenuBean) cp.componentBean);
	}

	static MenuItems getHeaderMenu(final ComponentParameter cp, final MenuBean menuBean) {
		return cp.getRequestCache("@getHeaderMenu", new IVal<MenuItems>() {
			@Override
			public MenuItems get() {
				return ((AbstractTablePagerHandler) cp.getComponentHandler()).getHeaderMenu(cp,
						menuBean);
			}
		});
	}
}
