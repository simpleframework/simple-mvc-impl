package net.simpleframework.mvc.component.ui.tooltip;

import java.util.Collection;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.common.web.html.HtmlUtils;
import net.simpleframework.mvc.common.element.EElementEvent;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.IComponentRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TooltipRender extends ComponentJavascriptRender {
	public TooltipRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final TooltipBean tooltipBean = (TooltipBean) cp.componentBean;
		Collection<TipBean> tips = null;
		final ITooltipHandle tooltipHandle = (ITooltipHandle) cp.getComponentHandler();
		if (tooltipHandle != null) {
			tips = tooltipHandle.getElementTips(cp);
		}
		if (tips == null) {
			tips = tooltipBean.getTips();
		}

		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		final StringBuilder sb = new StringBuilder();
		final StringBuilder sb2 = new StringBuilder();
		sb2.append(actionFunc).append(".options = [];");
		int i = 0;
		for (final TipBean tip : tips) {
			final String selector = tip.getSelector();
			// options
			sb2.append(actionFunc).append(".options.push({");
			sb2.append("style: '").append(tip.getTipStyle()).append("',");
			sb2.append("viewport: true,");
			final String title = tip.getTitle();
			if (StringUtils.hasText(title)) {
				sb2.append("title: \"").append(JavascriptUtils.escape(title)).append("\",");
			}
			sb2.append("radius: ").append(tip.getRadius()).append(",");
			final int width = tip.getWidth();
			sb2.append("width: ").append(width <= 0 ? "\"auto\"" : width).append(",");
			sb2.append("delay: ").append(tip.getDelay()).append(",");
			sb2.append("fixed: ").append(tip.isFixed()).append(",");
			final double hideAfter = tip.getHideAfter();
			if (hideAfter > 0) {
				sb2.append("hideAfter: ").append(hideAfter).append(",");
			}
			sb2.append("hideOthers: ").append(tip.isHideOthers()).append(",");
			final TipBean.HideOn hideOn = tip.getHideOn();
			if (hideOn != null) {
				sb2.append("hideOn: { element: \"").append(hideOn.getTipElement());
				sb2.append("\", event: ");
				final EElementEvent event = hideOn.getEvent();
				if (event == EElementEvent.none) {
					sb2.append("false");
				} else {
					sb2.append("\"").append(event).append("\"");
				}
				sb2.append(" },");
			}
			final EElementEvent showOn = tip.getShowOn();
			if (showOn != null) {
				sb2.append("showOn: \"").append(showOn).append("\",");
			}
			final TipBean.Hook hook = tip.getHook();
			if (hook != null) {
				sb2.append("hook: { target: \"").append(hook.getTarget()).append("\", ");
				sb2.append("tip: \"").append(hook.getTip()).append("\", ");
				sb2.append("mouse: ").append(hook.isMouse()).append(" },");
			}
			final ETipPosition stem = tip.getStem();
			if (stem != null) {
				sb2.append("stem: \"").append(stem).append("\",");
			}
			final String target = tip.getTarget();
			if (StringUtils.hasText(target)) {
				sb2.append("target: \"").append(target).append("\",");
			}
			sb2.append("offset:  { x: ").append(tip.getOffsetX());
			sb2.append(", y: ").append(tip.getOffsetY()).append(" },");

			sb2.append("effects: Browser.effects && ").append(cp.getBeanProperty("effects"));
			sb2.append("});");

			// createTip
			if (StringUtils.hasText(selector)) {
				sb.append(actionFunc).append(".createTip(\"").append(selector).append("\", ");
				final String contentRef = tip.getContentRef();
				if (StringUtils.hasText(contentRef)) {
					sb.append("\"").append(contentRef).append("\", ");
				} else {
					sb.append("null, ");
				}
				sb.append(tip.isCache()).append(", ");
				final String content = tip.getContent();
				if (StringUtils.hasText(content)) {
					sb.append("\"").append(JavascriptUtils.escape(HtmlUtils.convertHtmlLines(content)))
							.append("\", ");
				} else {
					sb.append("null, ");
				}
				final String jsTipCreate = tip.getJsTipCreate();
				if (StringUtils.hasText(jsTipCreate)) {
					sb.append("function(element, c) { ").append(jsTipCreate).append(" }, ");
				} else {
					sb.append("null, ");
				}
				sb.append(i).append(");");
			}
			i++;
		}

		sb2.append("__prototip_actions_init(").append(actionFunc).append(");");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString(), sb2.toString());
	}
}
