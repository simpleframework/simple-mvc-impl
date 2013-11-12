package net.simpleframework.mvc.component.ui.listbox;

import net.simpleframework.common.coll.AbstractArrayListEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ListItems extends AbstractArrayListEx<ListItems, ListItem> {

	public static ListItems of(final ListItem... items) {
		return new ListItems().append(items);
	}

	private static final long serialVersionUID = 6215022552238105810L;
}
