package net.simpleframework.mvc.component.ui.pager;

import static net.simpleframework.common.I18n.$m;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.HttpUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.UrlForward;
import net.simpleframework.mvc.common.element.ImageElement;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentHtmlRenderEx;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PagerRender extends ComponentHtmlRenderEx {

	@Override
	protected String getRelativePath(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/jsp/pager.jsp?").append(PagerUtils.BEAN_ID);
		sb.append("=").append(cp.hashId());
		return sb.toString();
	}

	@Override
	public IForward getResponseForward(final ComponentParameter cp) {
		String url = ComponentUtils.getResourceHomePath(PagerBean.class) + getRelativePath(cp);
		final String xpParameter = PagerUtils.getXmlPathParameter(cp);
		if (StringUtils.hasText(xpParameter)) {
			url = HttpUtils.addParameters(url, xpParameter);
		}
		final String pageItemsParameterName = (String) cp.getBeanProperty("pageItemsParameterName");
		final String pageItems = cp.getParameter(pageItemsParameterName);
		if (StringUtils.hasText(pageItems)) {
			url = HttpUtils.addParameters(url, pageItemsParameterName + "=" + pageItems);
		}
		final String parameters = (String) cp.getBeanProperty("parameters");
		if (StringUtils.hasText(parameters)) {
			url = HttpUtils.addParameters(url, parameters);
		}
		return new UrlForward(url);
	}

	private String getLoadingText(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append(new ImageElement(cp.getCssResourceHomePath(PagerUtils.class) + "/images/load.gif"));
		sb.append(new SpanElement($m("pager.4")));
		return sb.toString();
	}

	@Override
	public String getHtmlJavascriptCode(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.getHtmlJavascriptCode(cp));
		sb.append(ComponentRenderUtils.actionFunc(cp)).append(".doPager = function(to, params) {");
		sb.append("var af = $Actions[\"__doPager\"];");
		if (PagerUtils.isMoreLoad(cp)) {
			sb.append("to.innerHTML = '").append(JavascriptUtils.escape(getLoadingText(cp)))
					.append("';");
			sb.append("af.jsCompleteCallback = function(req, responseText, json) {");
			sb.append(" to.insert({");
			sb.append("  'before' : responseText");
			sb.append(" });");
			sb.append(" if (responseText == '') {");
			sb.append("  to.innerHTML = '").append($m("pager.3")).append("';");
			sb.append("  to.removeAttribute('onclick');");
			sb.append("  to._top = null;");
			sb.append(" } else {");
			sb.append("  to.innerHTML = '").append($m("pager.2")).append("';");
			sb.append("  to.pn++;");
			sb.append("  to._top = to.cumulativeOffset().top;");
			sb.append(" }");
			sb.append("};");
		} else {
			sb.append("var p = to.up(\".pager\");");
			sb.append("af.container = p.up();");
		}
		sb.append("af.selector = \"").append(cp.getBeanProperty("selector")).append("\";");
		sb.append("af(\"").append(PagerUtils.BEAN_ID).append("=");
		sb.append(cp.hashId()).append("\".addParameter(params));");
		sb.append("};");
		return sb.toString();
	}

	public static void createDoPager(final ComponentParameter cp) {
		final PagerBean pagerBean = (PagerBean) cp.componentBean;
		final AjaxRequestBean ajaxRequest = (AjaxRequestBean) cp
				.addComponentBean("__doPager", AjaxRequestBean.class)
				.setAjaxRequestId("ajaxRequest_" + pagerBean.hashId())
				.setHandlerClass(PagerAction.class);
		ajaxRequest.setAttr("container", pagerBean);
	}
}
