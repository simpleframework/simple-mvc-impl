package net.simpleframework.mvc.component.ui.colorpalette;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.IComponentResourceProvider.AbstractComponentResourceProvider;
import net.simpleframework.mvc.component.ui.slider.SliderRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ColorPaletteResourceProvider extends AbstractComponentResourceProvider {

	@Override
	public String[] getDependentComponents(final PageParameter pp) {
		return new String[] { SliderRegistry.SLIDER };
	}

	@Override
	public String[] getCssPath(final PageParameter pp) {
		return new String[] { getCssResourceHomePath(pp) + "/colorpalette.css" };
	}

	@Override
	public String[] getJavascriptPath(final PageParameter pp) {
		final String rPath = getResourceHomePath();
		return new String[] { rPath + "/js/colorutils.js", rPath + "/js/colorpalette.js" };
	}
}
