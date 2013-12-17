package net.simpleframework.mvc.component.ui.tabs;

import java.util.Collection;
import java.util.Iterator;

import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.ctx.script.IScriptEval;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentException;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(TabsRegistry.TABS)
@ComponentBean(TabsBean.class)
@ComponentRender(TabsRender.class)
@ComponentResourceProvider(TabsResourceProvider.class)
public class TabsRegistry extends AbstractComponentRegistry {
	public static final String TABS = "tabs";

	@Override
	protected void initComponentFromXml(final PageParameter pp,
			final AbstractComponentBean componentBean, final XmlElement xmlElement) {
		super.initComponentFromXml(pp, componentBean, xmlElement);

		final TabsBean tabsBean = (TabsBean) componentBean;
		final IScriptEval scriptEval = pp.getScriptEval();

		final Iterator<?> it = xmlElement.elementIterator("tab");
		final Collection<TabItem> coll = tabsBean.getTabItems();
		while (it.hasNext()) {
			final XmlElement ele = (XmlElement) it.next();
			final TabItem tab = new TabItem(ele);
			tab.parseElement(scriptEval);

			final String contentRef = tab.getContentRef();
			if (StringUtils.hasText(contentRef)) {
				final AbstractComponentBean componentBean2 = pp.getComponentBeanByName(contentRef);
				if (componentBean2 == null) {
					throw ComponentException.wrapException_ComponentRef(contentRef);
				} else {
					componentBean2.setRunImmediately(false);
					if (componentBean2 instanceof AjaxRequestBean) {
						((AjaxRequestBean) componentBean2).setShowLoading(false);
					}
					tab.setContent(ComponentUtils.getLoadingContent());
				}
			} else {
				tab.setBeanFromElementAttributes(scriptEval, new String[] { "content" });
			}
			coll.add(tab);
		}
	}
}
