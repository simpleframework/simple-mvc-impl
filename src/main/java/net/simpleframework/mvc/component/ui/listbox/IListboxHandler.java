package net.simpleframework.mvc.component.ui.listbox;

import java.util.Map;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IListboxHandler extends IComponentHandler {

	/**
	 * 获取列表数据
	 * 
	 * @param cParameter
	 * @return
	 */
	ListItems getListItems(ComponentParameter cp);

	/**
	 * 获取扩展属性
	 * 
	 * @param cParameter
	 * @param listItem
	 * @return
	 */
	Map<String, Object> getListItemAttributes(ComponentParameter cp, ListItem listItem);
}
