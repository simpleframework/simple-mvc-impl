package net.simpleframework.mvc.component.ui.tooltip;

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
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(TooltipRegistry.TOOLTIP)
@ComponentBean(TooltipBean.class)
@ComponentRender(TooltipRender.class)
@ComponentResourceProvider(TooltipResourceProvider.class)
public class TooltipRegistry extends AbstractComponentRegistry {
	public static final String TOOLTIP = "tooltip";

	@Override
	protected void initComponentFromXml(final PageParameter pp,
			final AbstractComponentBean componentBean, final XmlElement xmlElement) {
		super.initComponentFromXml(pp, componentBean, xmlElement);

		final TooltipBean tooltipBean = (TooltipBean) componentBean;
		final IScriptEval scriptEval = pp.getScriptEval();

		final Iterator<?> it = xmlElement.elementIterator("tip");
		while (it.hasNext()) {
			final XmlElement ele = (XmlElement) it.next();
			final TipBean tip = new TipBean(ele, tooltipBean);
			tip.parseElement(scriptEval);
			final String contentRef = tip.getContentRef();
			if (StringUtils.hasText(contentRef)) {
				final AbstractComponentBean componentBean2 = pp.getComponentBeanByName(contentRef);
				if (componentBean2 == null) {
					throw ComponentException.wrapException_ComponentRef(contentRef);
				} else {
					if (componentBean2 instanceof AjaxRequestBean) {
						((AjaxRequestBean) componentBean2).setShowLoading(false);
					}
				}
			}
			final XmlElement hideOnElement = ele.element("hideOn");
			if (hideOnElement != null) {
				final TipBean.HideOn hideOn = new TipBean.HideOn(hideOnElement);
				hideOn.parseElement(scriptEval);
				tip.setHideOn(hideOn);
			}

			final XmlElement hookElement = ele.element("hook");
			if (hookElement != null) {
				final TipBean.Hook hook = new TipBean.Hook(hookElement);
				hook.parseElement(scriptEval);
				tip.setHook(hook);
			}

			tooltipBean.getTips().add(tip);
		}
	}
}
