package net.simpleframework.mvc.component.ui.pager;

import java.util.ArrayList;
import java.util.List;

import net.simpleframework.common.object.DescriptionObject;
import net.simpleframework.mvc.common.element.LabelElement;
import net.simpleframework.mvc.common.element.SupElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class GroupWrapper extends DescriptionObject<GroupWrapper> {

	private Object groupVal;

	private boolean toggle = true;

	private boolean showNums = true;

	private List<Object> data;

	public GroupWrapper(final Object groupVal) {
		this.groupVal = groupVal;
	}

	public GroupWrapper() {
	}

	public Object getGroupVal() {
		return groupVal;
	}

	public GroupWrapper setGroupVal(final Object groupVal) {
		this.groupVal = groupVal;
		return this;
	}

	public boolean isToggle() {
		return toggle;
	}

	public GroupWrapper setToggle(final boolean toggle) {
		this.toggle = toggle;
		return this;
	}

	public boolean isShowNums() {
		return showNums;
	}

	public GroupWrapper setShowNums(final boolean showNums) {
		this.showNums = showNums;
		return this;
	}

	public List<Object> getData() {
		if (data == null) {
			data = new ArrayList<Object>();
		}
		return data;
	}

	public GroupWrapper setData(final List<Object> data) {
		this.data = data;
		return this;
	}

	protected String toCountHTML() {
		return new SupElement("(" + getData().size() + ")").addStyle("margin-left: 6px; color: #440")
				.toString();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(new LabelElement(getGroupVal()).setTitle(getDescription()));
		if (isShowNums()) {
			sb.append(toCountHTML());
		}
		return sb.toString();
	}
}
