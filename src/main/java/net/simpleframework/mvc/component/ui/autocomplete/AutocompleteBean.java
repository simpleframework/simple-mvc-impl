package net.simpleframework.mvc.component.ui.autocomplete;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.component.AbstractComponentBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class AutocompleteBean extends AbstractComponentBean {

	/* 需要自动完成的目标控件 */
	private String inputField;

	private int width = BeanDefaults.getInt(getClass(), "width", 300);
	private int height = BeanDefaults.getInt(getClass(), "height", 200);

	/* 返回最大数量 */
	private int maxResults = BeanDefaults.getInt(getClass(), "height", 100);

	public String getInputField() {
		return inputField;
	}

	public AutocompleteBean setInputField(final String inputField) {
		this.inputField = inputField;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public AutocompleteBean setWidth(final int width) {
		this.width = width;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public AutocompleteBean setHeight(final int height) {
		this.height = height;
		return this;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public AutocompleteBean setMaxResults(final int maxResults) {
		this.maxResults = maxResults;
		return this;
	}
}
