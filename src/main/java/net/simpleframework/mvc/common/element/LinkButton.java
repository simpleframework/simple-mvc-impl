package net.simpleframework.mvc.common.element;

import static net.simpleframework.common.I18n.$m;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class LinkButton extends AbstractButtonEx<LinkButton> {

	public static final LinkButton okBtn() {
		return new LinkButton().setText($m("Button.Ok"));
	}

	public static final LinkButton saveBtn() {
		return new LinkButton().setText($m("Button.Save")).setIconClass(Icon.hdd);
	}

	public static final LinkButton addBtn() {
		return new LinkButton().setText($m("Add")).setIconClass(Icon.file);
	}

	public static final LinkButton editBtn() {
		return new LinkButton().setText($m("Edit")).setIconClass(Icon.edit);
	}

	public static final LinkButton deleteBtn() {
		return new LinkButton().setText($m("Delete")).setIconClass(Icon.trash);
	}

	public static final LinkButton closeBtn() {
		return new LinkButton("#(Button.Close)").setOnclick("$win(this).close();").setIconClass(
				Icon.off);
	}

	public static LinkButton of(final Object text) {
		return new LinkButton(text);
	}

	public static LinkButton corner(final Object text) {
		return of(text).setLeftCorner(true).setRightCorner(true);
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
