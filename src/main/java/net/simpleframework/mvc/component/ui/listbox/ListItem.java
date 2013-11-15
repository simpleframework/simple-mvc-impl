package net.simpleframework.mvc.component.ui.listbox;

import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.common.ItemUIBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ListItem extends ItemUIBean<ListItem> {

	private final ListboxBean listboxBean;

	public ListItem(final XmlElement xmlElement, final ListboxBean listboxBean, final Object data) {
		super(xmlElement, data);
		this.listboxBean = listboxBean;
	}

	public ListItem(final ListboxBean listboxBean, final Object data) {
		this(null, listboxBean, data);
	}

	public ListboxBean getListboxBean() {
		return listboxBean;
	}
}
