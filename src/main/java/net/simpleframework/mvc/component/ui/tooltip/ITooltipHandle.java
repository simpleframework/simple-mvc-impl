package net.simpleframework.mvc.component.ui.tooltip;

import java.util.Collection;

import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface ITooltipHandle {

	Collection<TipBean> getElementTips(ComponentParameter cp);
}
