package net.simpleframework.mvc.component.ui.dictionary;

import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.window.IWindowHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IDictionaryHandle extends IWindowHandler {

	/**
	 * 在字典装载时触发，参考DictionaryLoad
	 * 
	 * @param cp
	 * @param componentRef
	 */
	void doDictionaryLoad(ComponentParameter cp, AbstractComponentBean componentRef);

	/**
	 * 定义工具栏
	 * 
	 * @param cp
	 * @return
	 */
	String toToolbarHTML(ComponentParameter cp);
}