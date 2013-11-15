package net.simpleframework.mvc.component.ui.slider;

import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class SliderBean extends AbstractContainerBean {

	private int xminValue = 0, xmaxValue = 100, yminValue = 0, ymaxValue = 0;

	private String arrowImage;

	private String jsChangeCallback;

	public SliderBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
	}

	public SliderBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	public int getXminValue() {
		return xminValue;
	}

	public void setXminValue(final int xminValue) {
		this.xminValue = xminValue;
	}

	public int getXmaxValue() {
		return xmaxValue;
	}

	public void setXmaxValue(final int xmaxValue) {
		this.xmaxValue = xmaxValue;
	}

	public int getYminValue() {
		return yminValue;
	}

	public void setYminValue(final int yminValue) {
		this.yminValue = yminValue;
	}

	public int getYmaxValue() {
		return ymaxValue;
	}

	public void setYmaxValue(final int ymaxValue) {
		this.ymaxValue = ymaxValue;
	}

	public String getArrowImage() {
		return arrowImage;
	}

	public void setArrowImage(final String arrowImage) {
		this.arrowImage = arrowImage;
	}

	public String getJsChangeCallback() {
		return jsChangeCallback;
	}

	public void setJsChangeCallback(final String jsChangeCallback) {
		this.jsChangeCallback = jsChangeCallback;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsChangeCallback" };
	}
}
