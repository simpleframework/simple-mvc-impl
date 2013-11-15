package net.simpleframework.mvc.component.ui.listbox;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.IComponentRegistry;
import net.simpleframework.mvc.component.IComponentResourceProvider.AbstractComponentResourceProvider;
import net.simpleframework.mvc.component.ui.tooltip.TooltipRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ListboxResourceProvider extends AbstractComponentResourceProvider {

	public ListboxResourceProvider(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String[] getDependentComponents(final PageParameter pp) {
		boolean tooltip = false;
		for (final AbstractComponentBean componentBean : pp.getComponentBeans().values()) {
			if (componentBean instanceof ListboxBean) {
				tooltip = ((ListboxBean) componentBean).isTooltip();
				if (tooltip) {
					break;
				}
			}
		}
		if (tooltip) {
			return new String[] { TooltipRegistry.TOOLTIP };
		} else {
			return null;
		}
	}

	@Override
	public String[] getCssPath(final PageParameter pp) {
		return new String[] { getCssResourceHomePath(pp) + "/listbox.css" };
	}

	@Override
	public String[] getJavascriptPath(final PageParameter pp) {
		return new String[] { getResourceHomePath() + "/js/listbox.js" };
	}
}
