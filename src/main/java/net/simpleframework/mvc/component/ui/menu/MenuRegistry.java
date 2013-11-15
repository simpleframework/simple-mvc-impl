package net.simpleframework.mvc.component.ui.menu;

import java.util.Iterator;

import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentException;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(MenuRegistry.MENU)
@ComponentBean(MenuBean.class)
@ComponentRender(MenuRender.class)
@ComponentResourceProvider(MenuResourceProvider.class)
public class MenuRegistry extends AbstractComponentRegistry {
	public static final String MENU = "menu";

	@Override
	protected void initComponentFromXml(final PageParameter pp,
			final AbstractComponentBean componentBean, final XmlElement xmlElement) {
		super.initComponentFromXml(pp, componentBean, xmlElement);

		final MenuBean menuBean = (MenuBean) componentBean;

		final Iterator<?> it = xmlElement.elementIterator("menuitem");
		while (it.hasNext()) {
			final XmlElement ele = (XmlElement) it.next();
			initMenuItem(pp, menuBean, null, menuBean.getMenuItems(), ele);
		}
	}

	void initMenuItem(final PageParameter pp, final MenuBean menuBean, final MenuItem parent,
			final MenuItems children, final XmlElement xmlElement) {
		final MenuItem menuItem = new MenuItem(xmlElement, parent);
		menuItem.parseElement(pp.getScriptEval());
		final String ref = menuItem.getRef();
		if (StringUtils.hasText(ref)) {
			final MenuBean menuRef = (MenuBean) pp.getComponentBeanByName(ref);
			if (menuRef == null) {
				throw ComponentException.wrapException_ComponentRef(ref);
			} else {
				children.addAll(menuRef.getMenuItems());
			}
		} else {
			children.add(menuItem);
		}
		final Iterator<?> it = xmlElement.elementIterator("menuitem");
		while (it.hasNext()) {
			final XmlElement ele = (XmlElement) it.next();
			initMenuItem(pp, menuBean, menuItem, menuItem.children(), ele);
		}
	}
}
