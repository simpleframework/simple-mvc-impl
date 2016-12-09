package net.simpleframework.mvc.component.base.ajaxrequest;

import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IAjaxRequestHandler extends IComponentHandler {

	/**
	 * ajax请求的缺省执行方法。当指定handlerMethod属性时，请参考该方法的定义
	 * 
	 * @param compParameter
	 * @return
	 */
	IForward ajaxProcess(ComponentParameter cp) throws Exception;
}
