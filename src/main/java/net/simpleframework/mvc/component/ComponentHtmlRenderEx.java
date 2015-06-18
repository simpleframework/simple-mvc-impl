package net.simpleframework.mvc.component;

import net.simpleframework.common.BeanUtils;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentHtmlRender;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class ComponentHtmlRenderEx extends ComponentHtmlRender {

	@Override
	public String getHtmlJavascriptCode(final ComponentParameter cp) {
		final AbstractContainerBean containerBean = (AbstractContainerBean) cp.componentBean;
		// 给container组件设置refresh函数
		final AjaxRequestBean ajaxRequestBean = (AjaxRequestBean) containerBean
				.getAttr("ajaxRequest");
		if (ajaxRequestBean == null) {
			return "";
		}
		final StringBuilder sb2 = new StringBuilder();
		sb2.append("var cf = $Actions[\"");
		sb2.append(cp.getComponentName()).append("\"];");
		sb2.append("var af = $Actions[\"").append(ajaxRequestBean.getName()).append("\"];");
		sb2.append("af.container = cf.container || '").append(cp.getBeanProperty("containerId"))
				.append("';");
		sb2.append("af.selector = cf.selector || '").append(cp.getBeanProperty("selector"))
				.append("';");
		sb2.append("af.jsCompleteCallback = cf.jsCompleteCallback;");
		sb2.append("af(arguments[0]);");

		final StringBuilder sb = new StringBuilder();
		sb.append(ComponentRenderUtils.genActionWrapper(cp, sb2.toString(), null, false, false));

		// refresh函数
		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		sb.append(actionFunc).append(".refresh = function(params) {");
		sb.append(actionFunc).append("(params);");
		sb.append("};");
		return sb.toString();
	}

	public static AjaxRequestBean createAjaxRequest(final ComponentParameter cp) {
		final AbstractContainerBean containerBean = (AbstractContainerBean) cp.componentBean;
		final AjaxRequestBean ajaxRequest = (AjaxRequestBean) cp.addComponentBean(
				"ajaxRequest_" + containerBean.hashId(), AjaxRequestBean.class).setHandlerClass(
				RefreshAction.class);
		ajaxRequest.setAttr("container", containerBean);
		containerBean.setAttr("ajaxRequest", ajaxRequest);
		return ajaxRequest;
	}

	public static class RefreshAction extends DefaultAjaxRequestHandler {
		@Override
		public Object getBeanProperty(final ComponentParameter cp, final String beanProperty) {
			if ("showLoading".equals(beanProperty) || "loadingModal".equals(beanProperty)) {
				final AbstractContainerBean containerBean = (AbstractContainerBean) cp.componentBean
						.getAttr("container");
				if (BeanUtils.hasProperty(containerBean, beanProperty)) {
					return ComponentParameter.get(cp, containerBean).getBeanProperty(beanProperty);
				}
			}
			return super.getBeanProperty(cp, beanProperty);
		}

		@Override
		public IForward ajaxProcess(final ComponentParameter cp) {
			final AbstractContainerBean containerBean = (AbstractContainerBean) cp.componentBean
					.getAttr("container");
			return ((ComponentHtmlRender) containerBean.getComponentRegistry().getComponentRender())
					.getResponseForward(ComponentParameter.get(cp, containerBean));
		}
	}
}
