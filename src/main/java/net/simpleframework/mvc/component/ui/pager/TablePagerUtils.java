package net.simpleframework.mvc.component.ui.pager;

import static net.simpleframework.ado.EFilterRelation.equal;
import static net.simpleframework.ado.EFilterRelation.gt;
import static net.simpleframework.ado.EFilterRelation.gt_equal;
import static net.simpleframework.ado.EFilterRelation.like;
import static net.simpleframework.ado.EFilterRelation.lt;
import static net.simpleframework.ado.EFilterRelation.lt_equal;
import static net.simpleframework.ado.EFilterRelation.not_equal;
import static net.simpleframework.common.I18n.$m;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.common.element.CalendarInput;
import net.simpleframework.mvc.common.element.Checkbox;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.Radio;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class TablePagerUtils {

	/**
	 * 当前排序列名
	 */
	public static final String PARAM_SORT_COL = "sort_col";

	/**
	 * 排序状态，值为up、down或空
	 */
	public static final String PARAM_SORT = "sort";

	public static final String PARAM_FILTER_COL = "filter_col";

	public static final String PARAM_FILTER_CUR_COL = "filter_cur_col";

	public static final String PARAM_FILTER = "filter";

	public static final String PARAM_FILTER_NONE = "none";

	/**
	 * 是否上移数据，参考tablepager.js中move函数
	 */
	public static final String PARAM_MOVE_UP = "up";

	public static final String PARAM_MOVE_ROWID = "rowId", PARAM_MOVE_ROWID2 = "rowId2";

	public static AbstractTablePagerSchema getTablePagerData(final ComponentParameter cp) {
		AbstractTablePagerSchema tablePagerData = (AbstractTablePagerSchema) cp
				.getRequestAttr("table_pager_data");
		if (tablePagerData == null) {
			final ITablePagerHandler pHandle = (ITablePagerHandler) cp.getComponentHandler();
			cp.setRequestAttr("table_pager_data", tablePagerData = pHandle.createTablePagerSchema());
		}
		return tablePagerData;
	}

	public static TablePagerColumn getSelectedColumn(final ComponentParameter nCP) {
		return getTablePagerData(nCP).getTablePagerColumns(nCP).get(
				nCP.getParameter(PARAM_FILTER_CUR_COL));
	}

	public static void doExport(final HttpServletRequest request, final HttpServletResponse response) {
		final ComponentParameter nCP = PagerUtils.get(request, response);
		final ITablePagerHandler tHandle = (ITablePagerHandler) nCP.getComponentHandler();
		final String[] arr = StringUtils.split(request.getParameter("v"), ";");
		TablePagerColumns columns = null;
		if (arr != null && arr.length > 0) {
			columns = TablePagerColumns.of();
			final TablePagerColumns all = getTablePagerData(nCP).getTablePagerColumns(nCP);
			for (final String v : arr) {
				final TablePagerColumn oCol = all.get(v);
				if (oCol != null) {
					columns.add(oCol);
				}
			}
		}
		tHandle.export(nCP, Convert.toEnum(EExportFileType.class, request.getParameter("filetype")),
				columns);
	}

	static boolean isGroup(final ComponentParameter cp) {
		return cp.getComponentHandler() instanceof IGroupTablePagerHandler
				&& StringUtils.hasText((String) cp.getBeanProperty("groupColumn"));
	}

	public static String toFilterSelectHTML(final TablePagerColumn col, final String id) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<select name='").append(id).append("' id='").append(id).append("'>");
		final Class<?> clazz = col.propertyClass();
		if (String.class.isAssignableFrom(clazz)) {
			sb.append("<option value=\"").append(like).append("\">#(TablePagerUtils.6)</option>");
		}
		sb.append("<option value=\"").append(equal).append("\">#(TablePagerUtils.0)</option>");
		sb.append("<option value=\"").append(not_equal).append("\">#(TablePagerUtils.1)</option>");
		if (Number.class.isAssignableFrom(clazz) || Date.class.isAssignableFrom(clazz)) {
			sb.append("<option value=\"").append(gt).append("\">#(TablePagerUtils.2)</option>");
			sb.append("<option value=\"").append(gt_equal).append("\">#(TablePagerUtils.3)</option>");
			sb.append("<option value=\"").append(lt).append("\">#(TablePagerUtils.4)</option>");
			sb.append("<option value=\"").append(lt_equal).append("\">#(TablePagerUtils.5)</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}

	public static String toFilterInputHTML(final TablePagerColumn col, final String id) {
		final StringBuilder sb = new StringBuilder();
		if (Date.class.isAssignableFrom(col.propertyClass())) {
			sb.append(new CalendarInput(id).setCalendarComponent("calendarTablePagerFilter")
					.setDateFormat(col.getFormat()));
		} else {
			sb.append(new InputElement(id));
		}
		return sb.toString();
	}

	public static String toFilterRelationHTML(final TablePagerColumn col) {
		final StringBuilder sb = new StringBuilder();
		for (final Checkbox r : new Checkbox[] {
				new Radio("tp_filter_op0", $m("tablepager_filter.0")).setText("none").setChecked(true),
				new Radio("tp_filter_op1", $m("tablepager_filter.2")).setText("and"),
				new Radio("tp_filter_op2", $m("tablepager_filter.3")).setText("or") }) {
			r.setName("tp_filter_op").setOnclick("tp_filter_click();");
			sb.append(r).append(SpanElement.SPACE15);
		}
		return sb.toString();
	}
}
