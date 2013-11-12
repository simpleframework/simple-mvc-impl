package net.simpleframework.mvc.component.base.include;

import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IPageIncludeHandler extends IComponentHandler {

	/**
	 * 指向的包含页面
	 * 
	 * @param cParameter
	 * @return
	 */
	IForward include(ComponentParameter cp);
}
