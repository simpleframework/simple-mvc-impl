package net.simpleframework.mvc.component.base.ajaxrequest;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.IComponentRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AjaxRequestRender extends ComponentJavascriptRender {
	public AjaxRequestRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		final StringBuilder sb = new StringBuilder();
		final String containerId = (String) cp.getBeanProperty("updateContainerId");
		sb.append("if (").append(actionFunc).append(".doInit(");
		if (StringUtils.hasText(containerId)) {
			sb.append("\"").append(containerId).append("\", ");
		} else {
			sb.append("null, ");
		}
		final String confirmMessage = (String) cp.getBeanProperty("confirmMessage");
		if (StringUtils.hasText(confirmMessage)) {
			sb.append("\"").append(JavascriptUtils.escape(confirmMessage)).append("\", ");
		} else {
			sb.append("null, ");
		}
		sb.append(cp.getBeanProperty("parallel")).append(", ")
				.append(cp.getBeanProperty("showLoading")).append(", ")
				.append(cp.getBeanProperty("loadingModal")).append(", ")
				.append(cp.getBeanProperty("disabledTriggerAction"));
		sb.append(")) return;");

		// doComplete
		sb.append("function dc(req) { ").append(actionFunc).append(".doLoaded(req); };");

		// __callback
		sb.append(actionFunc).append(".__callback = function(req, responseText, json, trigger) {");
		sb.append("var act = ").append(actionFunc).append(".jsCompleteCallback;");
		sb.append("if (act) { act(req, responseText, json, trigger); }");
		final String callback = (String) cp.getBeanProperty("jsCompleteCallback");
		if (StringUtils.hasText(callback)) {
			sb.append("else {").append(callback).append("}");
		}
		sb.append("};");

		sb.append("var p=\"ajax_request=true&").append(AjaxRequestUtils.BEAN_ID);
		sb.append("=").append(cp.hashId()).append("\";");
		ComponentRenderUtils.appendParameters(sb, cp, "p");
		sb.append("p = p.addParameter(arguments[0]);");
		sb.append("new Ajax.Request(\"");
		sb.append(ComponentUtils.getResourceHomePath(AjaxRequestBean.class)).append(
				"/jsp/ajax_request.jsp\", {");
		sb.append("postBody: p,");

		final String encoding = (String) cp.getBeanProperty("encoding");
		if (StringUtils.hasText(encoding)) {
			sb.append("encoding: \"").append(encoding).append("\",");
		}
		sb.append("onComplete: function(req) {");
		sb.append(actionFunc).append(".doComplete(req, ");
		final String containerRef = (String) cp.getBeanProperty("containerRef");
		if (StringUtils.hasText(containerRef)) {
			sb.append("\"").append(containerRef).append("\", ");
		} else {
			sb.append("null, ");
		}
		sb.append(cp.getBeanProperty("updateContainerCache")).append(", ");
		final EThrowException throwException = (EThrowException) cp.getBeanProperty("throwException");
		sb.append(throwException == EThrowException.alert);
		sb.append(");");
		sb.append("}, onException: dc, onFailure: dc, on404: dc });");
		final StringBuilder sb2 = new StringBuilder();
		sb2.append("__ajax_actions_init(").append(actionFunc).append(", '")
				.append(cp.getComponentName()).append("');");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString(), sb2.toString());
	}
}
