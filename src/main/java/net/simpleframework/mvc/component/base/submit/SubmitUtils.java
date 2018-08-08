package net.simpleframework.mvc.component.base.submit;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.common.FileUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.HttpUtils;
import net.simpleframework.mvc.AbstractBasePage;
import net.simpleframework.mvc.AbstractUrlForward;
import net.simpleframework.mvc.MVCContext;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.UrlForward;
import net.simpleframework.mvc.component.ComponentException;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class SubmitUtils {
	public static final String BEAN_ID = "submit_@bid";

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static void doSubmit(final HttpServletRequest request, final HttpServletResponse response)
			throws IOException {
		final ComponentParameter nCP = get(request, response);
		final ISubmitHandler submitHandle = (ISubmitHandler) nCP.getComponentHandler();
		if (submitHandle != null) {
			final String method = (String) nCP.getBeanProperty("handlerMethod");
			AbstractUrlForward forward;
			if ((Boolean) nCP.getBeanProperty("binary")) {
				nCP.request = MVCContext.get().createMultipartPageRequest(nCP,
						(int) FileUtils.toFileSize((String) nCP.getBeanProperty("fileSizeLimit")));
			}
			try {
				if (StringUtils.hasText(method) && !(submitHandle instanceof AbstractBasePage.Submit)) {
					final Method methodObject = submitHandle.getClass().getMethod(method,
							ComponentParameter.class);
					forward = (UrlForward) methodObject.invoke(submitHandle, nCP);
				} else {
					forward = submitHandle.submit(nCP);
				}
				if (forward != null) {
					nCP.loc(HttpUtils.addParameters(forward.getUrl(), AbstractUrlForward
							.putRequestData(nCP, (String) nCP.getBeanProperty("includeRequestData"))));
				}
			} catch (final Exception e) {
				throw ComponentException.of(e);
			}
		}
	}
}
