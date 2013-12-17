package net.simpleframework.mvc.component.ui.propeditor;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.IComponentResourceProvider.AbstractComponentResourceProvider;
import net.simpleframework.mvc.component.ui.tooltip.TooltipRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PropEditorResourceProvider extends AbstractComponentResourceProvider {

	@Override
	public String[] getDependentComponents(final PageParameter pp) {
		return new String[] { TooltipRegistry.TOOLTIP };
	}

	@Override
	public String[] getCssPath(final PageParameter pp) {
		return new String[] { getCssResourceHomePath(pp) + "/propeditor.css" };
	}

	@Override
	public String[] getJavascriptPath(final PageParameter pp) {
		return new String[] { getResourceHomePath() + "/js/propeditor.js" };
	}
}
