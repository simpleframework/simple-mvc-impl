package net.simpleframework.mvc.component.ui.imageslide;

import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ImageSlideRender extends ComponentJavascriptRender {

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final ImageSlideBean imageSlide = (ImageSlideBean) cp.componentBean;
		ImageItems items;
		final IImageSlideHandler handle = (IImageSlideHandler) cp.getComponentHandler();
		if (handle != null) {
			items = handle.getImageItems(cp);
		} else {
			items = imageSlide.getImageItems();
		}
		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		final StringBuilder sb = new StringBuilder();
		sb.append(ComponentRenderUtils.initContainerVar(cp));
		sb.append(actionFunc).append(".imageSlide = new $UI.ImageSlide(")
				.append(ComponentRenderUtils.VAR_CONTAINER).append(", [");
		if (items != null) {
			int i = 0;
			for (final ImageItem item : items) {
				if (i++ > 0) {
					sb.append(",");
				}
				sb.append("{");
				final String link = item.getLink();
				if (StringUtils.hasText(link)) {
					sb.append("link: \"").append(cp.wrapContextPath(link)).append("\",");
				}
				sb.append("imageUrl: \"").append(cp.wrapContextPath(item.getImageUrl())).append("\",");
				sb.append("title: \"").append(JavascriptUtils.escape(item.getTitle())).append("\"");
				sb.append("}");
			}
		}
		sb.append("], {");

		final int titleHeight = Convert.toInt(cp.getBeanProperty("titleHeight"));
		if (titleHeight > 0) {
			sb.append("titleHeight: ").append(titleHeight).append(",");
		}
		sb.append("titleOpacity: ").append(cp.getBeanProperty("titleOpacity")).append(",");
		sb.append("frequency: ").append(cp.getBeanProperty("frequency")).append(",");
		sb.append("showNextAction: ").append(cp.getBeanProperty("showNextAction")).append(",");
		sb.append("showPreAction: ").append(cp.getBeanProperty("showPreAction")).append(",");
		sb.append("autoStart: ").append(cp.getBeanProperty("autoStart")).append(",");
		sb.append("start: ").append(cp.getBeanProperty("start")).append(",");
		sb.append("effects: Browser.effects && ").append(cp.getBeanProperty("effects"));
		sb.append("});");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString());
	}
}
