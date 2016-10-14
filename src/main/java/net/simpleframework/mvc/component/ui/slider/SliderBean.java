package net.simpleframework.mvc.component.ui.slider;

import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class SliderBean extends AbstractContainerBean {
	private static final long serialVersionUID = 8592447912856637020L;

	private int xminValue = 0, xmaxValue = 100, yminValue = 0, ymaxValue = 0;

	private String arrowImage;

	private String jsChangeCallback;

	public int getXminValue() {
		return xminValue;
	}

	public SliderBean setXminValue(final int xminValue) {
		this.xminValue = xminValue;
		return this;
	}

	public int getXmaxValue() {
		return xmaxValue;
	}

	public SliderBean setXmaxValue(final int xmaxValue) {
		this.xmaxValue = xmaxValue;
		return this;
	}

	public int getYminValue() {
		return yminValue;
	}

	public SliderBean setYminValue(final int yminValue) {
		this.yminValue = yminValue;
		return this;
	}

	public int getYmaxValue() {
		return ymaxValue;
	}

	public SliderBean setYmaxValue(final int ymaxValue) {
		this.ymaxValue = ymaxValue;
		return this;
	}

	public String getArrowImage() {
		return arrowImage;
	}

	public SliderBean setArrowImage(final String arrowImage) {
		this.arrowImage = arrowImage;
		return this;
	}

	public String getJsChangeCallback() {
		return jsChangeCallback;
	}

	public SliderBean setJsChangeCallback(final String jsChangeCallback) {
		this.jsChangeCallback = jsChangeCallback;
		return this;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsChangeCallback" };
	}
}
