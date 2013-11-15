package net.simpleframework.mvc.component.ui.colorpalette;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentHtmlRenderEx;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(ColorPaletteRegistry.COLORPALETTE)
@ComponentBean(ColorPaletteBean.class)
@ComponentRender(ColorPaletteRender.class)
@ComponentResourceProvider(ColorPaletteResourceProvider.class)
public class ColorPaletteRegistry extends AbstractComponentRegistry {
	public static final String COLORPALETTE = "colorpalette";

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pp, final Object attriData) {
		final ColorPaletteBean colorPalette = (ColorPaletteBean) super.createComponentBean(pp,
				attriData);
		ComponentHtmlRenderEx.createAjaxRequest(ComponentParameter.get(pp, colorPalette));
		return colorPalette;
	}
}
