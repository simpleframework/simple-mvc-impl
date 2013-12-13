package net.simpleframework.mvc.component.base.ajaxrequest;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.logger.Log;
import net.simpleframework.common.logger.LogFactory;
import net.simpleframework.common.th.RuntimeExceptionEx;
import net.simpleframework.mvc.AbstractBasePage;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.IMVCContextVar;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.JsonForward;
import net.simpleframework.mvc.MVCContext;
import net.simpleframework.mvc.MVCUtils;
import net.simpleframework.mvc.SessionCache;
import net.simpleframework.mvc.component.ComponentException;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AjaxRequestUtils implements IMVCContextVar {
	public static final String BEAN_ID = "ajax_@bid";

	static Log log = LogFactory.getLogger(AjaxRequestUtils.class);

	public static void doAjaxRequest(final HttpServletRequest request,
			final HttpServletResponse response) {
		final ComponentParameter cp = ComponentParameter.get(request, response, BEAN_ID);
		final KVMap json = new KVMap();
		IForward forward = null;
		boolean bPermission = false;
		if (cp.componentBean == null) {
			// session过期
			if (request.getSession().isNew()) {
				forward = new JavascriptForward("if (confirm('").append($m("AjaxRequestUtils.1"))
						.append("')) { $Actions.reloc(); }");
			} else {
				forward = new JsonForward("exception", MVCUtils.createException(cp,
						ComponentException.of($m("AjaxRequestUtils.0"))));
			}
		} else {
			try {
				forward = MVCContext.permission().accessForward(cp, cp.getBeanProperty("role"));
				if (forward != null) {
					bPermission = true;
				}
				IAjaxRequestHandler ajaxRequestHandle;
				if (forward == null
						&& (ajaxRequestHandle = (IAjaxRequestHandler) cp.getComponentHandler()) != null) {
					boolean doing = false;
					String dKey = null;
					if (!(Boolean) cp.getBeanProperty("parallel")) {
						dKey = cp.getComponentName();
						// dKey = ajaxRequestHandle.getClass().getName();
						// if (StringUtils.hasText(method)) {
						// dKey += "#" + method;
						// }
						final Boolean bObj = (Boolean) SessionCache.lget(dKey);
						if (bObj != null && bObj.booleanValue()) {
							doing = true;
						} else {
							SessionCache.lput(dKey, Boolean.TRUE);
						}
					}
					if (!doing) {
						try {
							final String method = (String) cp.getBeanProperty("handleMethod");
							if (StringUtils.hasText(method)
									&& !(ajaxRequestHandle instanceof AbstractBasePage.AjaxRequest)) {
								final Method methodObject = ajaxRequestHandle.getClass().getMethod(method,
										ComponentParameter.class);
								forward = (IForward) methodObject.invoke(ajaxRequestHandle, cp);
							} else {
								forward = ajaxRequestHandle.ajaxProcess(cp);
							}
						} catch (Throwable th) {
							th = MVCUtils.convertThrowable(th);
							if (th instanceof RuntimeExceptionEx && th.getCause() == null) {
								log.error(th.getClass().getName() + ": " + th.getMessage());
							} else {
								log.error(th);
							}
							forward = new JsonForward("exception", MVCUtils.createException(cp, th));
						} finally {
							if (dKey != null) {
								SessionCache.lremove(dKey);
							}
						}
					}
				}

				json.add("id", cp.getBeanProperty("ajaxRequestId"));
			} catch (final Throwable th) {
				forward = new JsonForward("exception", MVCUtils.createException(cp, th));
			}
		}

		final String responseText = forward != null ? forward.getResponseText(cp) : "";
		json.add("rt", responseText.replace("\t", ""));
		json.add("isJavascript", forward instanceof JavascriptForward);
		json.add("isJSON", forward instanceof JsonForward);
		json.add("hasPermission", bPermission);
		PrintWriter out;
		try {
			out = response.getWriter();
		} catch (final IOException e) {
			throw ComponentException.of(e);
		}
		out.write(json.toJSON());
		out.flush();
	}
}
