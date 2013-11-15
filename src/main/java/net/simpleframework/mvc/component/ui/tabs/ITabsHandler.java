package net.simpleframework.mvc.component.ui.tabs;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface ITabsHandler extends IComponentHandler {

	/**
	 * 获取标签列表
	 * 
	 * @param cParameter
	 * @return
	 */
	TabItems tabItems(ComponentParameter cp);
}
