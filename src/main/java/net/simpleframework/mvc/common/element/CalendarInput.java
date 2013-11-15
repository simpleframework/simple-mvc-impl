package net.simpleframework.mvc.common.element;

import net.simpleframework.common.StringUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class CalendarInput extends TextButton {
	private String calendarComponent;

	private String dateFormat;

	public CalendarInput() {
	}

	public CalendarInput(final String id) {
		super(id);
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public CalendarInput setDateFormat(final String dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}

	public String getCalendarComponent() {
		return calendarComponent;
	}

	public CalendarInput setCalendarComponent(final String calendarComponent) {
		this.calendarComponent = calendarComponent;
		return this;
	}

	@Override
	public String toString() {
		final String calendarComponent = getCalendarComponent();
		if (StringUtils.hasText(calendarComponent)) {
			final StringBuilder sb = new StringBuilder();
			sb.append("$Actions['").append(calendarComponent).append("'].show('");
			sb.append(getId()).append("'");
			final String fm = getDateFormat();
			if (StringUtils.hasText(fm)) {
				sb.append(", '").append(fm).append("'");
			}
			sb.append(");");
			setOnclick(sb.toString());
		}
		return super.toString();
	}
}
