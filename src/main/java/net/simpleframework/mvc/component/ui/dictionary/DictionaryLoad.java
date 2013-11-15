package net.simpleframework.mvc.component.ui.dictionary;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.Convert;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.EElementEvent;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean.AbstractDictionaryTypeBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean.DictionaryListBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean.DictionaryTreeBean;
import net.simpleframework.mvc.component.ui.tooltip.ETipElement;
import net.simpleframework.mvc.component.ui.tooltip.ETipPosition;
import net.simpleframework.mvc.component.ui.tooltip.TipBean;
import net.simpleframework.mvc.component.ui.tooltip.TipBean.HideOn;
import net.simpleframework.mvc.component.ui.tooltip.TipBean.Hook;
import net.simpleframework.mvc.component.ui.tooltip.TooltipBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DictionaryLoad extends DefaultPageHandler {

	@Override
	public void onBeforeComponentRender(final PageParameter pp) {
		final ComponentParameter nCP = DictionaryUtils.get(pp);
		final DictionaryBean dictionaryBean = (DictionaryBean) nCP.componentBean;
		final AbstractComponentBean componentRef = (AbstractComponentBean) dictionaryBean
				.getDictionaryTypeBean().getAttr("$$component");
		if (componentRef != null) {
			pp.addComponentBean(componentRef);
		}

		// tooltip
		if (Convert.toBool(nCP.getBeanProperty("showHelpTooltip"))) {
			final String componentName = nCP.getComponentName();
			final TooltipBean tooltip = pp.addComponentBean(componentName + "_tooltip",
					TooltipBean.class);
			final AbstractDictionaryTypeBean dictionaryType = dictionaryBean.getDictionaryTypeBean();
			String key = null;
			final boolean multiple = (Boolean) nCP.getBeanProperty("multiple");
			if (dictionaryType instanceof DictionaryTreeBean) {
				key = multiple ? "getElementTips.0" : "getElementTips.1";
			} else if (dictionaryType instanceof DictionaryListBean) {
				key = multiple ? "getElementTips.2" : "getElementTips.3";
			}
			tooltip.addTip(new TipBean(tooltip).setSelector("#help" + nCP.hashId())
					.setContent($m(key)).setStem(ETipPosition.leftTop)
					.setHook(new Hook(ETipPosition.topRight, ETipPosition.topLeft))
					.setHideOn(new HideOn(ETipElement.tip, EElementEvent.mouseover)).setWidth(240));
		}
		final IDictionaryHandle hdl = (IDictionaryHandle) nCP.getComponentHandler();
		if (hdl != null) {
			hdl.doDictionaryLoad(nCP, componentRef);
		}
	}
}
