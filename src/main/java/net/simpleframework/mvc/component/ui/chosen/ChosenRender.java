package net.simpleframework.mvc.component.ui.chosen;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.IComponentRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ChosenRender extends ComponentJavascriptRender {

	public ChosenRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final String selector = (String) cp.getBeanProperty("selector");
		if (!StringUtils.hasText(selector)) {
			return null;
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("var selects = $$('").append(selector).append("');");
		sb.append("for (var i = 0; i < selects.length; i++) {");
		sb.append("new Chosen(selects[i], {");
		sb.append("disable_search: !").append(cp.getBeanProperty("enableSearch")).append(",");
		sb.append("allow_single_deselect: true,");
		sb.append("no_results_text: 'no_results_text'");
		sb.append("});");
		sb.append("}");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString());
	}
}
