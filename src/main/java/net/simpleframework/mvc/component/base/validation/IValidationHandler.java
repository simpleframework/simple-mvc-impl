package net.simpleframework.mvc.component.base.validation;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IValidationHandler extends IComponentHandler {

	/**
	 * 动态方式获取验证项
	 * 
	 * @param cParameter
	 * @return
	 */
	Validators getValidators(ComponentParameter cp);
}
