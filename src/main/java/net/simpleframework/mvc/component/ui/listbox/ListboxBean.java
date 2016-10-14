package net.simpleframework.mvc.component.ui.listbox;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ListboxBean extends AbstractContainerBean {
	private static final long serialVersionUID = 470805567610184101L;

	private ListItems listItems;

	private boolean checkbox = BeanDefaults.getBool(getClass(), "checkbox", false);

	private boolean tooltip = BeanDefaults.getBool(getClass(), "tooltip", false);

	private String jsClickCallback, jsDblclickCallback;

	public boolean isCheckbox() {
		return checkbox;
	}

	public ListboxBean setCheckbox(final boolean checkbox) {
		this.checkbox = checkbox;
		return this;
	}

	public boolean isTooltip() {
		return tooltip;
	}

	public ListboxBean setTooltip(final boolean tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	public ListItems getListItems() {
		if (listItems == null) {
			listItems = ListItems.of();
		}
		return listItems;
	}

	public ListboxBean addListItem(final ListItem listItem) {
		getListItems().add(listItem);
		return this;
	}

	public String getJsClickCallback() {
		return jsClickCallback;
	}

	public ListboxBean setJsClickCallback(final String jsClickCallback) {
		this.jsClickCallback = jsClickCallback;
		return this;
	}

	public String getJsDblclickCallback() {
		return jsDblclickCallback;
	}

	public ListboxBean setJsDblclickCallback(final String jsDblclickCallback) {
		this.jsDblclickCallback = jsDblclickCallback;
		return this;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsClickCallback", "jsDblclickCallback" };
	}

	{
		setWidth("200");
		setHeight("180");
	}
}
