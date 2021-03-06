package net.simpleframework.mvc.component.base.submit;

import net.simpleframework.mvc.AbstractUrlForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface ISubmitHandler extends IComponentHandler {

	AbstractUrlForward submit(ComponentParameter cp);
}
