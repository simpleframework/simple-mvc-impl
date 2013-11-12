package net.simpleframework.mvc.component.ui.pager;

import java.util.Collection;
import java.util.Set;

import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IGroupTablePagerHandler extends ITablePagerHandler {

	public static final String G = "g";

	/**
	 * 返回分组列
	 * 
	 * @param cp
	 * @param groups
	 * @return
	 */
	Collection<Object> doGroups(ComponentParameter cp, Set<Object> groups);

	/**
	 * 获取列分组描述信息
	 * 
	 * @param cp
	 * @param groupVal
	 * @return
	 */
	GroupWrapper getGroupWrapper(ComponentParameter cp, Object groupVal);

	/**
	 * 
	 * @param cp
	 * @param bean
	 * @param groupColumn
	 * @return
	 */
	Object getGroupValue(ComponentParameter cp, Object bean, String groupColumn);
}
