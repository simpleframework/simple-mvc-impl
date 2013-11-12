package net.simpleframework.mvc.component.ui.progressbar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.simpleframework.common.Convert;
import net.simpleframework.common.JsonUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class ProgressBarUtils {
	public static final String BEAN_ID = "progressbar_@bid";

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static String doProgressBarHandler(final HttpServletRequest request,
			final HttpServletResponse response) {
		final ComponentParameter nCP = get(request, response);
		if (nCP.componentBean != null) {
			final IProgressBarHandler handle = (IProgressBarHandler) nCP.getComponentHandler();
			if (handle != null) {
				final HttpSession httpSession = request.getSession();
				ProgressState state;
				final String beanId = nCP.hashId();
				if (Convert.toBool(request.getParameter("starting"))) {
					httpSession.setAttribute(beanId, state = new ProgressState());
				} else {
					state = (ProgressState) httpSession.getAttribute(beanId);
				}
				if (state != null) {
					if (Convert.toBool(request.getParameter("messages"))) {
						return JsonUtils.toJSON(state.messages);
					}
					state.abort = Convert.toBool(request.getParameter("abort"));
					handle.doProgressState(nCP, state);
					final StringBuilder sb = new StringBuilder();
					sb.append("{");
					sb.append("\"step\" : ").append(state.step).append(",");
					sb.append("\"maxProgressValue\" : ").append(state.maxProgressValue).append(",");
					final int size = state.messages.size();
					if (size > 0) {
						sb.append("\"message\" : \"");
						sb.append(JavascriptUtils.escape(String.valueOf(state.messages.get(size - 1))));
						sb.append("\",");
					}
					sb.append("\"abort\" : ").append(state.abort);
					sb.append("}");
					return sb.toString();
				}
			}
		}
		return "{}";
	}
}
