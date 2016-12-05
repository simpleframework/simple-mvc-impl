package net.simpleframework.mvc.component.ui.pager;

import java.util.Iterator;
import java.util.Map;

import net.simpleframework.ado.ColumnData;
import net.simpleframework.ado.EFilterOpe;
import net.simpleframework.ado.FilterItem;
import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.EWarnType;
import net.simpleframework.mvc.component.base.validation.ValidationBean;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.component.ui.calendar.CalendarBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TablePagerFilterPageLoad extends DefaultPageHandler {

	@Override
	public void onPageLoad(final PageParameter pp, final Map<String, Object> dataBinding,
			final PageSelector selector) {
		final String col = pp.getParameter(TablePagerUtils.PARAM_FILTER_CUR_COL);
		if (!StringUtils.hasText(col)) {
			return;
		}
		final ComponentParameter nCP = PagerUtils.get(pp);
		boolean disabled = true;
		ColumnData dbColumn;
		final Map<String, ColumnData> dbColumns = ((ITablePagerHandler) nCP.getComponentHandler())
				.getFilterColumns(nCP);
		if (dbColumns != null && (dbColumn = dbColumns.get(col)) != null) {
			final Iterator<FilterItem> it = dbColumn.getFilterItems().iterator();
			if (it.hasNext()) {
				FilterItem item = it.next();
				dataBinding.put("tp_filter_r1", item.getRelation().toString());
				dataBinding.put("tp_filter_v1", item.getOriginalValue());
				final EFilterOpe ope = item.getOpe();
				if (ope == EFilterOpe.and) {
					dataBinding.put("tp_filter_op1", true);
					disabled = false;
				} else if (ope == EFilterOpe.or) {
					dataBinding.put("tp_filter_op2", true);
					disabled = false;
				}
				if (it.hasNext()) {
					item = it.next();
					dataBinding.put("tp_filter_r2", item.getRelation().toString());
					dataBinding.put("tp_filter_v2", item.getOriginalValue());
				}
			}
		}
		if (disabled) {
			selector.disabledSelector = "#tp_filter_r2, #tp_filter_v2";
		}
	}

	@Override
	public void onBeforeComponentRender(final PageParameter pp) {
		super.onBeforeComponentRender(pp);

		pp.addComponentBean("ajaxTablePagerFilterSave", AjaxRequestBean.class)
				.setHandlerMethod("doFilter").setHandlerClass(TablePagerAction.class)
				.setSelector(".filter_window");

		// 加入验证
		pp.addComponentBean(ValidationBean.class, TablePagerFilterValidation.class)
				.setTriggerSelector("#idTablePagerFilterSave").setWarnType(EWarnType.insertAfter)
				.addValidators(
						new Validator(EValidatorMethod.required, "#tp_filter_v1, #tp_filter_v2"));

		// 加入日期
		pp.addComponentBean("calendarTablePagerFilter", CalendarBean.class).setShowTime(true);
	}
}
