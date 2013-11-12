package net.simpleframework.mvc.component.ui.listbox;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ListboxBean extends AbstractContainerBean {

	private ListItems listItems;

	private boolean checkbox = BeanDefaults.getBool(getClass(), "checkbox", false);

	private boolean tooltip = BeanDefaults.getBool(getClass(), "tooltip", false);

	private String jsClickCallback, jsDblclickCallback;

	public ListboxBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
		setWidth("200");
		setHeight("180");
	}

	public ListboxBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

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
}
