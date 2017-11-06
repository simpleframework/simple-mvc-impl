package net.simpleframework.mvc.component.base.validation;

import static net.simpleframework.common.I18n.$m;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ValidationRender extends ComponentJavascriptRender {

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final ValidationBean validationBean = (ValidationBean) cp.componentBean;

		Validators validators;
		final IValidationHandler validationHandle = (IValidationHandler) cp.getComponentHandler();
		if (validationHandle != null) {
			validators = validationHandle.getValidators(cp);
		} else {
			validators = validationBean.getValidators();
		}
		if (validators == null) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		sb.append(ComponentRenderUtils.actionFunc(cp)).append(".validation = new Validation(\"");
		sb.append(validationBean.getTriggerSelector()).append("\", [");
		int i = 0;
		for (final Validator validator : validators) {
			if (i++ > 0) {
				sb.append(", ");
			}
			sb.append("{");
			sb.append("selector: \"").append(validator.getSelector()).append("\", ");
			final String args = validator.getArgs();
			if (StringUtils.hasText(args)) {
				sb.append("args: \"").append(args).append("\", ");
			}
			final String method = validator.getMethod().name();
			String message = validator.getMessage();
			if (!StringUtils.hasText(message)) {
				message = defaultMessages.get(method);
			}
			sb.append("message: \"");
			sb.append(JavascriptUtils.escape(message)).append("\", ");
			final EWarnType warnType = validator.getWarnType();
			if (warnType != null) {
				sb.append("warnType: \"").append(warnType).append("\",");
			}
			sb.append("method: \"").append(method).append("\"");
			sb.append("}");
		}
		sb.append("] ,{");
		sb.append("warnType: \"").append(validationBean.getWarnType()).append("\"");
		sb.append("});");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString());
	}

	static Map<String, String> defaultMessages = new HashMap<>();
	static {
		for (final EValidatorMethod method : EValidatorMethod.values()) {
			final String key = method.name();
			defaultMessages.put(key, $m("VM." + key));
		}
	}
}
