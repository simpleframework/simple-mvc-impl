package net.simpleframework.mvc.component.ui.colorpalette;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ColorPaletteBean extends AbstractContainerBean {
	private static final long serialVersionUID = 1668624711147551635L;

	private EColorMode startMode;

	private String startHex = BeanDefaults.getString(getClass(), "startHex", "CCCCCC");

	private String jsChangeCallback;

	public EColorMode getStartMode() {
		return startMode == null ? EColorMode.h : startMode;
	}

	public ColorPaletteBean setStartMode(final EColorMode startMode) {
		this.startMode = startMode;
		return this;
	}

	public String getStartHex() {
		return startHex;
	}

	public ColorPaletteBean setStartHex(final String startHex) {
		this.startHex = startHex;
		return this;
	}

	public String getJsChangeCallback() {
		return jsChangeCallback;
	}

	public ColorPaletteBean setJsChangeCallback(final String jsChangeCallback) {
		this.jsChangeCallback = jsChangeCallback;
		return this;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsChangeCallback" };
	}
}
