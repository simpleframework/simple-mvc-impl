package net.simpleframework.mvc.component.base.ajaxrequest;

import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.MVCUtils;
import net.simpleframework.mvc.UrlForward;
import net.simpleframework.mvc.component.AbstractComponentHandler;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DefaultAjaxRequestHandler extends AbstractComponentHandler
		implements IAjaxRequestHandler {
	public static final String JK_ERROR = "error";

	@Override
	public IForward ajaxProcess(final ComponentParameter cp) throws Exception {
		return null;
	}

	public IForward doUrlForward(final ComponentParameter cp) {
		return new UrlForward(MVCUtils.doPageUrl(cp, (String) cp.getBeanProperty("urlForward")));
	}
}
