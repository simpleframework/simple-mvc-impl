package net.simpleframework.mvc.component.ui.dictionary;

import net.simpleframework.mvc.common.element.BlockElement;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.window.AbstractWindowHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractDictionaryHandler extends AbstractWindowHandler
		implements IDictionaryHandle {

	@Override
	public void doDictionaryLoad(final ComponentParameter cp,
			final AbstractComponentBean componentRef) {
	}

	protected ElementList getLeftElements(final ComponentParameter cp) {
		return null;
	}

	protected ElementList getRightElements(final ComponentParameter cp) {
		return null;
	}

	@Override
	public String toToolbarHTML(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		final ElementList ll = getLeftElements(cp);
		final ElementList lr = getRightElements(cp);
		if (ll != null || lr != null) {
			if (ll != null) {
				sb.append("<div style='float: left'>").append(ll).append("</div>");
			}
			if (lr != null) {
				sb.append("<div style='float: right'>").append(lr).append("</div>");
			}
			sb.append(BlockElement.CLEAR);
		}
		return sb.toString();
	}
}
