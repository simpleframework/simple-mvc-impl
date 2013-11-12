package net.simpleframework.mvc.component.ui.menu;

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
public class MenuBean extends AbstractContainerBean {

	private MenuItems menuItems;

	private EMenuEvent menuEvent;

	private String minWidth;

	private String jsBeforeShowCallback;

	public MenuBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
		setMinWidth("120");
	}

	public MenuBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	public MenuItems getMenuItems() {
		if (menuItems == null) {
			menuItems = MenuItems.of();
		}
		return menuItems;
	}

	public MenuBean addItem(final MenuItem item) {
		getMenuItems().add(item);
		return this;
	}

	public EMenuEvent getMenuEvent() {
		return menuEvent == null ? EMenuEvent.click : menuEvent;
	}

	public MenuBean setMenuEvent(final EMenuEvent menuEvent) {
		this.menuEvent = menuEvent;
		return this;
	}

	public String getMinWidth() {
		return minWidth;
	}

	public MenuBean setMinWidth(final String minWidth) {
		this.minWidth = minWidth;
		return this;
	}

	public String getJsBeforeShowCallback() {
		return jsBeforeShowCallback;
	}

	public MenuBean setJsBeforeShowCallback(final String jsBeforeShowCallback) {
		this.jsBeforeShowCallback = jsBeforeShowCallback;
		return this;
	}

	@Override
	public String getSelector() {
		return selector;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsBeforeShowCallback" };
	}
}
