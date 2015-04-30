package net.simpleframework.mvc.component.ui.pager;

import static net.simpleframework.common.I18n.$m;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.web.html.HtmlConst;
import net.simpleframework.mvc.AbstractUrlForward;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.JsonForward;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;
import net.simpleframework.mvc.component.ui.pager.TablePagerHTML.RenderTable;
import net.simpleframework.mvc.component.ui.pager.TablePagerHTML.RowHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TablePagerAction extends DefaultAjaxRequestHandler {

	public IForward doExport(final ComponentParameter cp) {
		final JavascriptForward js = new JavascriptForward();
		js.append("$Actions.loc('");
		js.append(ComponentUtils.getResourceHomePath(PagerBean.class)).append(
				"/jsp/tablepager_export_dl.jsp");
		js.append(";jsessionid=").append(cp.getSession().getId()).append("?");
		js.append(AbstractUrlForward.putRequestData(cp, "p", true));
		js.append("&filetype=").append(cp.getParameter("filetype"));
		js.append("&v=").append(cp.getParameter("v")).append("');");
		return js;
	}

	public IForward doFilter(final ComponentParameter cp) {
		final ComponentParameter nCP = PagerUtils.get(cp);
		final JavascriptForward js = new JavascriptForward("$Actions['tpFilterWindow_").append(
				nCP.hashId()).append("'].close();");
		js.append("$Actions['").append(nCP.getComponentName()).append("']('")
				.append(TablePagerUtils.PARAM_FILTER_CUR_COL).append("=")
				.append(cp.getParameter(TablePagerUtils.PARAM_FILTER_CUR_COL));
		js.append("&").append(TablePagerUtils.PARAM_FILTER).append("=")
				.append(cp.getParameter("tp_filter_r1", true)).append(";")
				.append(TablePagerHTML.filterEncode(cp.getParameter("tp_filter_v1")));
		final String op = cp.getParameter("tp_filter_op");
		if ("and".equals(op) || "or".equals(op)) {
			js.append(";").append(op).append(";").append(cp.getParameter("tp_filter_r2", true))
					.append(";").append(TablePagerHTML.filterEncode(cp.getParameter("tp_filter_v2")));
		}
		js.append("');");
		return js;
	}

	public IForward doFilter2(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append(TablePagerUtils.PARAM_FILTER_CUR_COL).append("=");
		sb.append(cp.getParameter(TablePagerUtils.PARAM_FILTER_CUR_COL)).append("&")
				.append(TablePagerUtils.PARAM_FILTER).append("=");
		sb.append("like;");
		sb.append(TablePagerHTML.filterEncode(cp.getParameter("v")));
		return new JsonForward("filter", sb.toString());
	}

	public IForward doFilterDelete(final ComponentParameter cp) {
		final JavascriptForward jf = new JavascriptForward();
		final ComponentParameter nCP = PagerUtils.get(cp);
		jf.append("$Actions['").append(nCP.getComponentName()).append("']('")
				.append(TablePagerUtils.PARAM_FILTER_CUR_COL).append("=")
				.append(cp.getParameter(TablePagerUtils.PARAM_FILTER_CUR_COL)).append("&")
				.append(TablePagerUtils.PARAM_FILTER).append("=")
				.append(TablePagerUtils.PARAM_FILTER_NONE).append("');");
		return jf;
	}

	public IForward doRowAdd(final ComponentParameter cp) {
		final ComponentParameter nCP = PagerUtils.get(cp);
		final ITablePagerHandler hdl = (ITablePagerHandler) nCP.getComponentHandler();

		final JsonForward json = new JsonForward();
		final AbstractTablePagerSchema tablePagerData = TablePagerUtils.getTablePagerSchema(nCP);
		final String rowId = "#" + tablePagerData.genId();
		json.put("row",
				new RenderTable().buildRow(nCP, new KVMap(), tablePagerData, new RowHandler(0, true) {
					@Override
					protected Object getValue(final Object val, final TablePagerColumn pagerColumn) {
						if (!pagerColumn.isEditable()) {
							return HtmlConst.NBSP;
						}
						final AbstractElement<?> element = hdl.toRowEditorHTML(nCP, pagerColumn, rowId,
								getElementName(pagerColumn, rowId), null);
						return element != null ? element : HtmlConst.NBSP;
					}
				}));

		final ElementList el = hdl.toSaveButtons(nCP);
		if (el != null) {
			json.put("tb", el.toString());
		}
		return json;
	}

	private String getElementName(final TablePagerColumn pagerColumn, final String rowId) {
		return rowId + ";" + pagerColumn.getColumnName();
	}

	public IForward doRowEdit(final ComponentParameter cp) {
		final ComponentParameter nCP = PagerUtils.get(cp);
		final ITablePagerHandler hdl = (ITablePagerHandler) nCP.getComponentHandler();

		final String rowId = cp.getParameter(AbstractTablePagerSchema.ROW_ID);
		Object oBean;
		if (!StringUtils.hasText(rowId) || (oBean = hdl.getRowBeanById(nCP, rowId)) == null) {
			final JavascriptForward js = new JavascriptForward();
			return js.append("alert('").append($m("TablePagerAction.0")).append("');");
		} else {
			final JsonForward json = new JsonForward();
			final TablePagerColumns columns = TablePagerUtils.getTablePagerSchema(nCP)
					.getTablePagerColumns(nCP);
			for (final TablePagerColumn column : columns) {
				if (!column.isEditable()) {
					continue;
				}
				final AbstractElement<?> element = hdl.toRowEditorHTML(nCP, column, rowId,
						getElementName(column, rowId), oBean);
				if (element != null) {
					json.put(column.getColumnName(), element.toString());
				}
			}
			final ElementList el = hdl.toSaveButtons(nCP);
			if (el != null) {
				json.put("tb", el.toString());
			}
			return json;
		}
	}

	public IForward doRowSave(final ComponentParameter cp) {
		final ComponentParameter nCP = PagerUtils.get(cp);
		final ITablePagerHandler hdl = (ITablePagerHandler) nCP.getComponentHandler();
		final TablePagerColumns columns = TablePagerUtils.getTablePagerSchema(nCP)
				.getTablePagerColumns(nCP);

		final Map<String, Map<String, Object>> insertRows = new LinkedHashMap<String, Map<String, Object>>();
		final Map<String, Map<String, Object>> updateRows = new LinkedHashMap<String, Map<String, Object>>();
		final Enumeration<String> names = cp.getParameterNames();
		while (names.hasMoreElements()) {
			final String pkey = names.nextElement();
			final String[] arr = StringUtils.split(pkey, ";");
			if (arr.length == 2) {
				final TablePagerColumn col = columns.get(arr[1]);
				if (col == null) {
					continue;
				}
				final String k2 = arr[0];
				final boolean insert = k2.startsWith("#");
				final Map<String, Map<String, Object>> rows = insert ? insertRows : updateRows;
				Map<String, Object> row = rows.get(k2);
				if (row == null) {
					rows.put(k2, row = new KVMap().setCaseInsensitive(true));
					if (insert) {
						row.put("ID", k2);
					}
				}
				row.put(col.getColumnName(), col.stringToObject(cp.getParameter(pkey)));
			}
		}
		final JavascriptForward js = hdl.doRowSave(nCP, insertRows, updateRows);
		return js != null ? js : new JavascriptForward("$Actions['").append(nCP.getComponentName())
				.append("']();");
	}
}
