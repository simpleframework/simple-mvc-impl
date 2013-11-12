package net.simpleframework.mvc.component.ui.menu;

import java.util.ArrayList;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class MenuUtils {

	public static void removeDblSeparator(final ArrayList<MenuItem> menuItems) {
		final ArrayList<MenuItem> removes = new ArrayList<MenuItem>();
		final int size = menuItems.size();
		for (int i = 0; i < size; i++) {
			final MenuItem menuItem = menuItems.get(i);
			if ("-".equals(menuItem.getTitle())) {
				if (i < size - 1) {
					if ("-".equals(menuItems.get(i + 1).getTitle())) {
						removes.add(menuItem);
						i++;
					}
				}
			}
		}
		if (removes.size() > 0) {
			menuItems.removeAll(removes);
		}
	}
}
