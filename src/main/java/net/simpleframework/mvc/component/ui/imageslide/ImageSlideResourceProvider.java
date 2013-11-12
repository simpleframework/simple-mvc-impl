package net.simpleframework.mvc.component.ui.imageslide;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.IComponentRegistry;
import net.simpleframework.mvc.component.IComponentResourceProvider.AbstractComponentResourceProvider;
import net.simpleframework.mvc.component.ui.menu.MenuRegistry;
import net.simpleframework.mvc.impl.DefaultPageResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ImageSlideResourceProvider extends AbstractComponentResourceProvider {

	public ImageSlideResourceProvider(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String[] getCssPath(final PageParameter pp) {
		return new String[] { getCssResourceHomePath(pp) + "/imageslide.css" };
	}

	@Override
	public String[] getDependentComponents(final PageParameter pp) {
		return new String[] { MenuRegistry.MENU };
	}

	@Override
	public String[] getJavascriptPath(final PageParameter pp) {
		return new String[] {
				getResourceHomePath(DefaultPageResourceProvider.class)
						+ DefaultPageResourceProvider.EFFECTS_FILE,
				getResourceHomePath() + "/js/imageslide.js" };
	}
}
