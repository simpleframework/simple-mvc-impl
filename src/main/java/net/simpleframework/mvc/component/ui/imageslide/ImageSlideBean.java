package net.simpleframework.mvc.component.ui.imageslide;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ImageSlideBean extends AbstractContainerBean {

	private ImageItems imageItems;

	private int titleHeight = BeanDefaults.getInt(getClass(), "titleHeight", 48);

	private float titleOpacity = (float) BeanDefaults.getDouble(getClass(), "titleOpacity", 0.6);

	private float frequency = (float) BeanDefaults.getDouble(getClass(), "frequency", 4);

	private boolean autoStart = BeanDefaults.getBool(getClass(), "autoStart", true);

	private int start = BeanDefaults.getInt(getClass(), "start", 0);

	private boolean showPreAction = BeanDefaults.getBool(getClass(), "showPreAction", true);

	private boolean showNextAction = BeanDefaults.getBool(getClass(), "showNextAction", true);

	public ImageItems getImageItems() {
		if (imageItems == null) {
			imageItems = ImageItems.of();
		}
		return imageItems;
	}

	public int getTitleHeight() {
		return titleHeight;
	}

	public ImageSlideBean setTitleHeight(final int titleHeight) {
		this.titleHeight = titleHeight;
		return this;
	}

	public float getTitleOpacity() {
		return titleOpacity;
	}

	public ImageSlideBean setTitleOpacity(final float titleOpacity) {
		this.titleOpacity = titleOpacity;
		return this;
	}

	public float getFrequency() {
		return frequency;
	}

	public ImageSlideBean setFrequency(final float frequency) {
		this.frequency = frequency;
		return this;
	}

	public boolean isAutoStart() {
		return autoStart;
	}

	public ImageSlideBean setAutoStart(final boolean autoStart) {
		this.autoStart = autoStart;
		return this;
	}

	public int getStart() {
		return start;
	}

	public ImageSlideBean setStart(final int start) {
		this.start = start;
		return this;
	}

	public boolean isShowPreAction() {
		return showPreAction;
	}

	public ImageSlideBean setShowPreAction(final boolean showPreAction) {
		this.showPreAction = showPreAction;
		return this;
	}

	public boolean isShowNextAction() {
		return showNextAction;
	}

	public ImageSlideBean setShowNextAction(final boolean showNextAction) {
		this.showNextAction = showNextAction;
		return this;
	}
}
