package net.simpleframework.mvc.component.ui.calendar;

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
public class CalendarBean extends AbstractContainerBean {

	/* 选择目标 */
	private String inputField;

	/* 日期格式 */
	private String dateFormat = BeanDefaults.getString(getClass(), "dateFormat", "yyyy-MM-dd");

	/* 是否显示时间 */
	private boolean showTime = BeanDefaults.getBool(getClass(), "showTime", false);;

	private String closeCallback;

	public CalendarBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
	}

	public CalendarBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

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

	public String getCloseCallback() {
		return closeCallback;
	}

	public CalendarBean setCloseCallback(final String closeCallback) {
		this.closeCallback = closeCallback;
		return this;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "closeCallback" };
	}
}
