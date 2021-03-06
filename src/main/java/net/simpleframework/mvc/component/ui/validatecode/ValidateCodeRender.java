package net.simpleframework.mvc.component.ui.validatecode;

import static net.simpleframework.common.I18n.$m;

import java.util.UUID;

import net.simpleframework.common.object.ObjectUtils;
import net.simpleframework.common.web.html.HtmlConst;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.TextForward;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.component.ComponentHtmlRenderEx;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ValidateCodeRender extends ComponentHtmlRenderEx {

	private final String inputId = "input_" + ObjectUtils.hashStr(this);

	@Override
	public String getHtml(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='simple_validatecode clearfix'>");
		sb.append("	<div class='b1'>");
		sb.append(new InputElement().setClassName("ifocus").setId(inputId)
				.setName((String) cp.getBeanProperty("inputName"))
				.setPlaceholder($m("ValidateCodeRender.1")));
		sb.append("	</div>");
		sb.append("	<div class='b2'>");
		sb.append("  <img class='photo_icon' src='")
				.append(ComponentUtils.getResourceHomePath(ValidateCodeBean.class))
				.append("/jsp/validatecode.jsp?").append(ValidateCodeUtils.BEAN_ID).append("=")
				.append(cp.hashId()).append("&r=").append(UUID.randomUUID()).append("' />");
		sb.append("  <a onclick=\"$Actions['").append(cp.getComponentName()).append("']();\">")
				.append($m("ValidateCodeRender.0")).append("</a>");
		sb.append("	</div>");
		sb.append("</div>");
		return sb.toString();
	}

	@Override
	public IForward getResponseForward(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder(getHtml(cp));
		sb.append(HtmlConst.TAG_SCRIPT_START);
		sb.append("$('").append(inputId).append("').focus();");
		sb.append(HtmlConst.TAG_SCRIPT_END);
		return new TextForward(sb.toString());
	}
}
