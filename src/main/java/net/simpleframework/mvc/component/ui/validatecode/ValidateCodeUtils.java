package net.simpleframework.mvc.component.ui.validatecode;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.simpleframework.common.ImageUtils;
import net.simpleframework.common.logger.Log;
import net.simpleframework.common.logger.LogFactory;
import net.simpleframework.common.web.HttpUtils;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class ValidateCodeUtils {
	static Log log = LogFactory.getLogger(ValidateCodeUtils.class);

	public static final String BEAN_ID = "validateCode_@bid";

	public static void doRender(final HttpServletRequest request, final HttpServletResponse response) {
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			final ComponentParameter nCP = ComponentParameter.get(request, response, BEAN_ID);
			final IValidateCodeHandler vHandle = (IValidateCodeHandler) nCP.getComponentHandler();
			if (vHandle == null) {
				return;
			}
			HttpUtils.setNoCache(response);
			response.setContentType("image/png");
			response.reset();
			vHandle.doValidateCode(nCP, ImageUtils.genCode(outputStream));
		} catch (final Exception ex) {
			log.warn(ex);
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (final IOException e) {
			}
		}
	}

	public static String getValidateCode(final HttpSession session) {
		return (String) session.getAttribute(IValidateCodeHandler.GEN_CODE);
	}

	public static boolean isValidateCode(final HttpServletRequest request, final String inputName) {
		final String validateCode = request.getParameter(inputName);
		return validateCode != null ? validateCode.equalsIgnoreCase(getValidateCode(request
				.getSession())) : true;
	}

	public static String getErrorString() {
		return $m("ValidateCodeUtils.0");
	}
}
