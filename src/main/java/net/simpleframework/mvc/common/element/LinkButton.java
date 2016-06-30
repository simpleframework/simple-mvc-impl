package net.simpleframework.mvc.common.element;

import static net.simpleframework.common.I18n.$m;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class LinkButton extends AbstractButtonEx<LinkButton> {
	public static LinkButton of(final Object text) {
		return new LinkButton(text);
	}

	public static LinkButton BLANK(final Object text) {
		return new LinkButton(text).blank();
	}

	public static final LinkButton okBtn() {
		return of($m("Button.Ok"));
	}

	public static final LinkButton saveBtn() {
		return of($m("Button.Save")).setIconClass(Icon.hdd);
	}

	public static final LinkButton addBtn() {
		return of($m("Add")).setIconClass(Icon.file);
	}

	public static final LinkButton editBtn() {
		return of($m("Edit")).setIconClass(Icon.edit);
	}

	public static final LinkButton deleteBtn() {
		return of($m("Delete")).setIconClass(Icon.trash);
	}

	public static final LinkButton closeBtn() {
		return of("#(Button.Close)").setOnclick("$win(this).close();").setIconClass(Icon.off);
	}

	public static final LinkButton logBtn() {
		return of($m("Button.Log"));
	}

	public static final LinkButton backBtn() {
		return new LinkButton($m("LinkButton.0")).setIconClass(Icon.share_alt);
	}

	public static LinkButton corner(final Object text) {
		return of(text).corner();
	}

	public static LinkButton menu(final Object text) {
		return of(text).setMenuIcon(true);
	}

	public LinkButton() {
	}

	public LinkButton(final Object text) {
		super(text);
	}

	@Override
	protected void doAttri(final StringBuilder sb) {
		addClassName("simple_btn");
		final boolean lcorner = isLeftCorner();
		final boolean rcorner = isRightCorner();
		if (lcorner && rcorner) {
			addClassName("simple_btn_all");
		} else if (lcorner) {
			addClassName("simple_btn_left");
		} else if (rcorner) {
			addClassName("simple_btn_right");
		}
		super.doAttri(sb);
	}
}
