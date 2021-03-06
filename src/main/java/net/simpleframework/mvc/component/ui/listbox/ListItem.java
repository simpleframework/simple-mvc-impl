package net.simpleframework.mvc.component.ui.listbox;

import net.simpleframework.mvc.common.ItemUIBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ListItem extends ItemUIBean<ListItem> {
	private static final long serialVersionUID = -8682685245721251889L;

	private final ListboxBean listboxBean;

	public ListItem(final ListboxBean listboxBean, final Object data) {
		super(data);
		this.listboxBean = listboxBean;
	}

	public ListboxBean getListboxBean() {
		return listboxBean;
	}
}
