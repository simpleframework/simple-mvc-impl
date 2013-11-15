package net.simpleframework.mvc.component.ui.imageslide;

import java.util.Collection;
import java.util.Iterator;

import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.ctx.script.IScriptEval;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(ImageSlideRegistry.IMAGESLIDE)
@ComponentBean(ImageSlideBean.class)
@ComponentRender(ImageSlideRender.class)
@ComponentResourceProvider(ImageSlideResourceProvider.class)
public class ImageSlideRegistry extends AbstractComponentRegistry {
	public static final String IMAGESLIDE = "imageSlide";

	@Override
	protected void initComponentFromXml(final PageParameter pp,
			final AbstractComponentBean componentBean, final XmlElement xmlElement) {
		super.initComponentFromXml(pp, componentBean, xmlElement);

		final IScriptEval scriptEval = pp.getScriptEval();

		final Iterator<?> it = xmlElement.elementIterator("imageitem");
		final Collection<ImageItem> coll = ((ImageSlideBean) componentBean).getImageItems();
		while (it.hasNext()) {
			final XmlElement ele = (XmlElement) it.next();
			final ImageItem imageItem = new ImageItem(ele);
			imageItem.parseElement(scriptEval);
			coll.add(imageItem);
		}
	}
}
