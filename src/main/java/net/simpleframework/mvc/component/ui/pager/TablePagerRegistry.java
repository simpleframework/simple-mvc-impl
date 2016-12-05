package net.simpleframework.mvc.component.ui.pager;

import static net.simpleframework.common.I18n.$m;

import java.util.Iterator;

import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.ctx.script.IScriptEval;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.window.WindowBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(TablePagerRegistry.TABLEPAGER)
@ComponentBean(TablePagerBean.class)
@ComponentResourceProvider(TablePagerResourceProvider.class)
public class TablePagerRegistry extends PagerRegistry {
	public static final String TABLEPAGER = "tablePager";

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pp,
			final Object attriData) {
		final TablePagerBean tablePager = (TablePagerBean) super.createComponentBean(pp, attriData);

		// 添加表格内部用到的组件
		final ComponentParameter cp = ComponentParameter.get(pp, tablePager);
		final String hashId = cp.hashId();

		// 导出
		pp.addComponentBean("tpExportPage_" + hashId, AjaxRequestBean.class)
				.setUrlForward(ComponentUtils.getResourceHomePath(TablePagerBean.class)
						+ "/jsp/tablepager_export.jsp");
		pp.addComponentBean("tpExportWindow_" + hashId, WindowBean.class)
				.setContentRef("tpExportPage_" + hashId).setResizable(false)
				.setTitle($m("tablepager_export.0")).setWidth(480).setHeight(360);

		return tablePager;
	}

	@Override
	protected void initComponentFromXml(final PageParameter pp,
			final AbstractComponentBean componentBean, final XmlElement element) {
		super.initComponentFromXml(pp, componentBean, element);

		final TablePagerBean tablePager = (TablePagerBean) componentBean;
		final IScriptEval scriptEval = pp.getScriptEval();

		final XmlElement elements = element.element("columns");
		if (elements != null) {
			final Iterator<?> it = elements.elementIterator("column");
			final TablePagerColumns columns = tablePager.getColumns();
			while (it.hasNext()) {
				final TablePagerColumn column = (TablePagerColumn) new TablePagerColumn()
						.setElement((XmlElement) it.next());
				column.parseElement(scriptEval);
				columns.add(column);
			}
		}
	}
}
