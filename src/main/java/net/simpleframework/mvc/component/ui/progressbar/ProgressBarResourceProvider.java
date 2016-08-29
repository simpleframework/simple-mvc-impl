package net.simpleframework.mvc.component.ui.progressbar;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ui.AbstractComponentUIResourceProvider;
import net.simpleframework.mvc.component.ui.menu.MenuRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ProgressBarResourceProvider extends AbstractComponentUIResourceProvider {

	@Override
	public String[] getDependentComponents(final PageParameter pp) {
		return new String[] { MenuRegistry.MENU };
	}

	@Override
	public String[] getJavascriptPath(final PageParameter pp) {
		return new String[] { getResourceHomePath() + "/js/progressbar.js" };
	}
}
