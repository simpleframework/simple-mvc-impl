package net.simpleframework.mvc.component.ui.tooltip;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.mvc.component.AbstractComponentBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TooltipBean extends AbstractComponentBean {

	private Collection<TipBean> tips;

	public Collection<TipBean> getTips() {
		if (tips == null) {
			tips = new ArrayList<TipBean>();
		}
		return tips;
	}

	public TooltipBean addTip(final TipBean tip) {
		getTips().add(tip);
		return this;
	}
}
