package net.simpleframework.mvc.component.ui.progressbar;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ProgressBarBean extends AbstractContainerBean {
	private float interval = (float) BeanDefaults.getDouble(getClass(), "interval", 0.5);

	private int maxProgressValue = BeanDefaults.getInt(getClass(), "maxProgressValue", 100);

	private int step = BeanDefaults.getInt(getClass(), "step", 0);

	private boolean startAfterCreate = BeanDefaults.getBool(getClass(), "startAfterCreate", true);

	private boolean showAbortAction = BeanDefaults.getBool(getClass(), "showAbortAction", true);

	private boolean showDetailAction = BeanDefaults.getBool(getClass(), "showDetailAction", true);

	private int detailHeight = BeanDefaults.getInt(getClass(), "detailHeights", 120);

	private String changeCallback;

	public float getInterval() {
		return interval;
	}

	public ProgressBarBean setInterval(final float interval) {
		this.interval = interval;
		return this;
	}

	public int getMaxProgressValue() {
		return maxProgressValue;
	}

	public ProgressBarBean setMaxProgressValue(final int maxProgressValue) {
		this.maxProgressValue = maxProgressValue;
		return this;
	}

	public int getStep() {
		return step;
	}

	public ProgressBarBean setStep(final int step) {
		this.step = step;
		return this;
	}

	public boolean isStartAfterCreate() {
		return startAfterCreate;
	}

	public ProgressBarBean setStartAfterCreate(final boolean startAfterCreate) {
		this.startAfterCreate = startAfterCreate;
		return this;
	}

	public boolean isShowAbortAction() {
		return showAbortAction;
	}

	public ProgressBarBean setShowAbortAction(final boolean showAbortAction) {
		this.showAbortAction = showAbortAction;
		return this;
	}

	public boolean isShowDetailAction() {
		return showDetailAction;
	}

	public ProgressBarBean setShowDetailAction(final boolean showDetailAction) {
		this.showDetailAction = showDetailAction;
		return this;
	}

	public int getDetailHeight() {
		return detailHeight;
	}

	public ProgressBarBean setDetailHeight(final int detailHeight) {
		this.detailHeight = detailHeight;
		return this;
	}

	public String getChangeCallback() {
		return changeCallback;
	}

	public ProgressBarBean setChangeCallback(final String changeCallback) {
		this.changeCallback = changeCallback;
		return this;
	}

	{
		setHeight("17");
	}
}
