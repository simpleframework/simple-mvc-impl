package net.simpleframework.mvc.component.ui.calendar;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class CalendarBean extends AbstractContainerBean {
	private static final long serialVersionUID = -7050227142993520451L;

	/* 选择目标 */
	private String inputField;

	/* 日期格式 */
	private String dateFormat = BeanDefaults.getString(getClass(), "dateFormat", "yyyy-MM-dd");

	/* 是否显示时间 */
	private boolean showTime = BeanDefaults.getBool(getClass(), "showTime", false);

	private boolean clearButton = BeanDefaults.getBool(getClass(), "clearButton", true);

	/* onchange */
	private String jsChangeCallback;
	/* 选择事件 */
	private String jsCloseCallback;

	public String getInputField() {
		return inputField;
	}

	public CalendarBean setInputField(final String inputField) {
		this.inputField = inputField;
		return this;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public CalendarBean setDateFormat(final String dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}

	public boolean isShowTime() {
		return showTime;
	}

	public CalendarBean setShowTime(final boolean showTime) {
		this.showTime = showTime;
		return this;
	}

	public boolean isClearButton() {
		return clearButton;
	}

	public CalendarBean setClearButton(final boolean clearButton) {
		this.clearButton = clearButton;
		return this;
	}

	public String getJsChangeCallback() {
		return jsChangeCallback;
	}

	public CalendarBean setJsChangeCallback(final String jsChangeCallback) {
		this.jsChangeCallback = jsChangeCallback;
		return this;
	}

	public String getJsCloseCallback() {
		return jsCloseCallback;
	}

	public CalendarBean setJsCloseCallback(final String jsCloseCallback) {
		this.jsCloseCallback = jsCloseCallback;
		return this;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsChangeCallback", "jsCloseCallback" };
	}
}
