package net.simpleframework.mvc.component.ui.pager;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.menu.AbstractMenuHandler;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.menu.MenuItems;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TablePagerMenu2 extends AbstractMenuHandler {

	@Override
	public MenuItems getMenuItems(final ComponentParameter cp, final MenuItem menuItem) {
		if (menuItem != null) {
			return null;
		}
		final ComponentParameter nCP = PagerUtils.get(cp);
		return ((ITablePagerHandler) nCP.getComponentHandler()).getHeaderMenu(nCP,
				(MenuBean) cp.componentBean);
	}
}
