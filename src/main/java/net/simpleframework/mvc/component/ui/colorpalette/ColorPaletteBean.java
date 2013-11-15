package net.simpleframework.mvc.component.ui.colorpalette;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ColorPaletteBean extends AbstractContainerBean {

	private EColorMode startMode;

	private String startHex = BeanDefaults.getString(getClass(), "startHex", "CCCCCC");

	private String changeCallback;

	public ColorPaletteBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
	}

	public ColorPaletteBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	public EColorMode getStartMode() {
		return startMode == null ? EColorMode.h : startMode;
	}

	public void setStartMode(final EColorMode startMode) {
		this.startMode = startMode;
	}

	public String getStartHex() {
		return startHex;
	}

	public void setStartHex(final String startHex) {
		this.startHex = startHex;
	}

	public String getChangeCallback() {
		return changeCallback;
	}

	public void setChangeCallback(final String changeCallback) {
		this.changeCallback = changeCallback;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "changeCallback" };
	}
}
