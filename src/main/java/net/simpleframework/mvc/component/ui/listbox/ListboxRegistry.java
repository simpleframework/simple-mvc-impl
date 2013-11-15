package net.simpleframework.mvc.component.ui.listbox;

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
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(ListboxRegistry.LISTBOX)
@ComponentBean(ListboxBean.class)
@ComponentRender(ListboxRender.class)
@ComponentResourceProvider(ListboxResourceProvider.class)
public class ListboxRegistry extends AbstractComponentRegistry {
	public static final String LISTBOX = "listbox";

	@Override
	protected void initComponentFromXml(final PageParameter pp,
			final AbstractComponentBean componentBean, final XmlElement xmlElement) {
		super.initComponentFromXml(pp, componentBean, xmlElement);

		final ListboxBean listboxBean = (ListboxBean) componentBean;
		final IScriptEval scriptEval = pp.getScriptEval();

		final Iterator<?> it = xmlElement.elementIterator("item");
		while (it.hasNext()) {
			final XmlElement ele = (XmlElement) it.next();
			final ListItem item = new ListItem(ele, listboxBean, null);
			item.parseElement(scriptEval);
			listboxBean.getListItems().add(item);
		}
	}
}
