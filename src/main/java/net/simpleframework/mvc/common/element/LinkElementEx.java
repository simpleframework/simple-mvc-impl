package net.simpleframework.mvc.common.element;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class LinkElementEx extends LinkElement {

	/* 是否显示下拉菜单 */
	private boolean menuIcon;

	private boolean selected;

	public LinkElementEx(final Object text) {
		super(text);
	}

	public LinkElementEx() {
	}

	public boolean isSelected() {
		return selected;
	}

	public LinkElementEx setSelected(final boolean selected) {
		this.selected = selected;
		setStrong(selected);
		return this;
	}

	public boolean isMenuIcon() {
		return menuIcon;
	}

	public LinkElementEx setMenuIcon(final boolean menuIcon) {
		this.menuIcon = menuIcon;
		return this;
	}

	@Override
	public String getText() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.getText());
		if (isMenuIcon()) {
			sb.append("<span class='micon'></span>");
		}
		return sb.toString();
	}

	@Override
	protected void doAttri(final StringBuilder sb) {
		addClassName("link_ex");
		if (isSelected()) {
			addClassName("link_selected");
		}
		if (isMenuIcon()) {
			addClassName("link_menuicon");
		}
		super.doAttri(sb);
	}
}
