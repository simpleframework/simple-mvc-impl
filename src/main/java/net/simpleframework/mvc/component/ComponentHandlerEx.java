package net.simpleframework.mvc.component;

import java.io.InputStream;
import java.util.Iterator;

import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.I18n;
import net.simpleframework.ctx.common.xml.XmlAttri;
import net.simpleframework.ctx.common.xml.XmlDocument;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.menu.MenuItems;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ComponentHandlerEx extends AbstractComponentHandler {

	public MenuItems getContextMenu(final ComponentParameter cp, final MenuBean menuBean,
			final MenuItem menuItem) {
		return createXmlMenu(ClassUtils.getResourceRecursively(getClass(), "menu.xml"), menuBean);
	}

	protected MenuItems createXmlMenu(final InputStream inputStream, final MenuBean menuBean) {
		if (inputStream == null) {
			return null;
		}
		final MenuItems menuItems = MenuItems.of();

		final XmlDocument doc = new XmlDocument(inputStream);
		final Iterator<?> it = doc.getRoot().elementIterator("item");
		while (it.hasNext()) {
			_create(menuItems, (XmlElement) it.next());
		}

		return menuItems;
	}

	private void _create(final MenuItems children, final XmlElement xmlElement) {
		final MenuItem item = new MenuItem();
		Iterator<?> it = xmlElement.attributeIterator();
		while (it.hasNext()) {
			final XmlAttri attri = (XmlAttri) it.next();
			BeanUtils.setProperty(item, attri.getName(), I18n.replaceI18n(attri.getValue()));
			item.setOnclick(xmlElement.elementText("onclick"));
		}
		children.add(item);

		it = xmlElement.elementIterator("item");
		while (it.hasNext()) {
			_create(item.children(), (XmlElement) it.next());
		}
	}
}
