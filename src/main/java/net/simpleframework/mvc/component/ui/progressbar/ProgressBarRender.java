package net.simpleframework.mvc.component.ui.progressbar;

import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.IComponentRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ProgressBarRender extends ComponentJavascriptRender {
	public ProgressBarRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append(ComponentRenderUtils.initContainerVar(cp));
		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		sb.append(actionFunc).append(".progressbar = new $UI.ProgressBar(")
				.append(ComponentRenderUtils.VAR_CONTAINER).append(", {");
		sb.append("\"url\": \"").append(ComponentUtils.getResourceHomePath(ProgressBarBean.class));
		sb.append("/jsp/progressbar.jsp?").append(ProgressBarUtils.BEAN_ID).append("=");
		sb.append(cp.hashId()).append("\",");
		sb.append("\"maxProgressValue\": ");
		sb.append(cp.getBeanProperty("maxProgressValue")).append(",");
		sb.append("\"step\": ");
		sb.append(cp.getBeanProperty("step")).append(",");
		final int detailHeight = (Integer) cp.getBeanProperty("detailHeight");
		if (detailHeight > 0) {
			sb.append("\"detailHeight\": \"").append(detailHeight).append("px\",");
		}
		sb.append("\"interval\": ");
		sb.append(cp.getBeanProperty("interval")).append(",");
		sb.append("\"startAfterCreate\": ").append(cp.getBeanProperty("startAfterCreate"))
				.append(",");
		sb.append("\"showAbortAction\": ").append(cp.getBeanProperty("showAbortAction")).append(",");
		sb.append("\"showDetailAction\": ").append(cp.getBeanProperty("showDetailAction"))
				.append(",");
		sb.append("\"effects\": Browser.effects && ").append(cp.getBeanProperty("effects"));
		sb.append("});");

		final StringBuilder sb2 = new StringBuilder();
		sb2.append("__progressbar_actions_init(").append(actionFunc).append(");");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString(), sb2.toString());
	}
}
