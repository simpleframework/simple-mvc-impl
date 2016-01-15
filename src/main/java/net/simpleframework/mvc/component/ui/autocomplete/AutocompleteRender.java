package net.simpleframework.mvc.component.ui.autocomplete;

import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class AutocompleteRender extends ComponentJavascriptRender {

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		sb.append(actionFunc).append(".autocomplete = new $UI.Autocomplete('")
				.append(cp.getBeanProperty("inputField")).append("', {");
		sb.append("ajax: 'ajax_").append(cp.getComponentName()).append("',");
		sb.append("params: '").append(cp.getBeanProperty("parameters")).append("',");
		sb.append("sepChar: '").append(cp.getBeanProperty("sepChar")).append("',");
		sb.append("width: ").append(cp.getBeanProperty("width")).append(",");
		sb.append("height: ").append(cp.getBeanProperty("height"));
		sb.append("});");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString());
	}
}
