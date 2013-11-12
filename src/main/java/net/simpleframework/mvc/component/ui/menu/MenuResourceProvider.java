package net.simpleframework.mvc.component.ui.menu;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.IComponentRegistry;
import net.simpleframework.mvc.component.IComponentResourceProvider.AbstractComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MenuResourceProvider extends AbstractComponentResourceProvider {

	public MenuResourceProvider(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String[] getCssPath(final PageParameter pp) {
		return new String[] { getCssResourceHomePath(pp) + "/menu.css" };
	}

	@Override
	public String[] getJavascriptPath(final PageParameter pp) {
		return new String[] { getResourceHomePath() + "/js/menu.js" };
	}
}
