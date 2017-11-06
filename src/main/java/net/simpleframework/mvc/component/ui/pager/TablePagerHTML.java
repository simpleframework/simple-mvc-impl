package net.simpleframework.mvc.component.ui.pager;

import static net.simpleframework.common.I18n.$m;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.html.HtmlConst;
import net.simpleframework.common.web.html.HtmlUtils;
import net.simpleframework.mvc.MVCUtils;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.ETextAlign;
import net.simpleframework.mvc.common.element.EVerticalAlign;
import net.simpleframework.mvc.common.element.ImageElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class TablePagerHTML implements HtmlConst {

	public static String renderTable(final ComponentParameter cp) {
		if (TablePagerUtils.isGroup(cp)) {
			return renderTable(cp, new RenderGroupTable());
		} else {
			return renderTable(cp, new RenderTable());
		}
	}

	static String renderTable(final ComponentParameter cp, final RenderTable rHandle) {
		final AbstractTablePagerSchema schema = TablePagerUtils.getTablePagerSchema(cp);
		final TablePagerBean tablePager = (TablePagerBean) cp.componentBean;
		final StringBuilder sb = new StringBuilder();

		sb.append("<div class=\"tablepager\" scrollHead=\"");
		sb.append(tablePager.isScrollHead()).append("\">");
		sb.append(rHandle.renderHeader(cp, schema));
		sb.append(rHandle.renderBody(cp, schema));
		sb.append("</div>");
		return sb.toString();
	}

	static int fixWidth(final PageRequestResponse rRequest, final int width) {
		// 当table-layout=fixed时，webkit的解释尽然去掉了padding，怪异
		// return HttpUtils.isWebKit(rRequest.request) ? width + 8 : width;
		return width;
	}

	static int getRownumWidth(final ComponentParameter cp, final int pageNumber,
			final int pageItems) {
		return fixWidth(cp,
				Math.max(String
						.valueOf(
								Math.max(pageNumber, 1) * pageItems == Integer.MAX_VALUE ? 999 : pageItems)
						.length() * 8, 12));
	}

	static String filterColumnKey(final String col) {
		return TablePagerUtils.PARAM_FILTER + "_" + col;
	}

	static String filterEncode(final String filter) {
		if (filter == null) {
			return "";
		}
		return "#" + StringUtils.encodeHex(filter.trim().getBytes());
	}

	static String filterDecode(final String filter) {
		if (filter == null) {
			return "";
		}
		return filter.startsWith("#") ? StringUtils.decodeHexString(filter.substring(1)) : filter;
	}

	static class RenderTable {

		String renderHeader(final ComponentParameter cp,
				final AbstractTablePagerSchema tablePagerData) {
			final ITablePagerHandler pHandle = (ITablePagerHandler) cp.getComponentHandler();
			final StringBuilder sb = new StringBuilder();

			// showHead
			final boolean showHead = (Boolean) cp.getBeanProperty("showHead");
			sb.append("<div class=\"thead\"");
			if (!showHead) {
				sb.append(" style=\"border-top: 0;\"");
			}
			sb.append(">");

			final String componentName = cp.getComponentName();
			if ((Boolean) cp.getBeanProperty("editable")
					&& (Boolean) cp.getBeanProperty("showEditableBtn")) {
				sb.append("<span class='add' onclick=\"$Actions['").append(componentName)
						.append("'].add_row();\"></span>");
			}

			sb.append("<table style=\"width:100%;");
			if (!showHead) {
				sb.append("display: none;");
			}
			final int marginRight = Convert.toInt(cp.getParameter("tbl_thead_margin_right"));
			sb.append("padding-right:").append(marginRight);
			sb.append("px;\" class=\"fixed_table\" cellpadding=\"0\" cellspacing=\"0\"><tr>");

			// showDetail
			final String detailField = (String) cp.getBeanProperty("detailField");
			final boolean showDetail = StringUtils.hasText(detailField);
			if (showDetail) {
				sb.append("<td class='plus'></td>");
			}

			int numWidth = 0;
			if ((Boolean) cp.getBeanProperty("showLineNo")) {
				final int pageNumber = PagerUtils.getPageNumber(cp);
				final int pageItems = PagerUtils.getPageItems(cp);
				sb.append("<td width=\"").append(numWidth = getRownumWidth(cp, pageNumber, pageItems))
						.append("\" class=\"rownum\">").append(NBSP).append("</td>");
			}

			final boolean showCheckbox = (Boolean) cp.getBeanProperty("showCheckbox");
			if (showCheckbox) {
				sb.append("<td class=\"cb\"><input type=\"checkbox\" ");
				sb.append("onclick=\"$Actions['").append(componentName);
				sb.append("'].checkAll(this);\" /></td>");
			}

			final boolean resize = (Boolean) cp.getBeanProperty("resize");
			final int headHeight = (Integer) cp.getBeanProperty("headHeight");
			final boolean showVerticalLine = (Boolean) cp.getBeanProperty("showVerticalLine");

			int i = 0;
			for (final TablePagerColumn pagerColumn : tablePagerData.getTablePagerColumns(cp)) {
				if (!pagerColumn.isVisible()) {
					continue;
				}
				final String columnName = pagerColumn.getColumnName();
				sb.append("<td");
				final String style = pagerColumn.toStyle(cp, null);
				if (StringUtils.hasText(style)) {
					sb.append(" style=\"").append(style).append("\"");
				}
				if (!pagerColumn.isNowrap()) {
					sb.append(" class=\"wrap_text\"");
				}
				final boolean bResize = resize && pagerColumn.isResize();
				sb.append(" columnName=\"").append(pagerColumn.getColumnName()).append("\" resize=\"")
						.append(bResize).append("\"><div class=\"box\"");
				if (headHeight > 0) {
					sb.append(" style=\"height: ").append(headHeight).append("px;\"");
				}
				sb.append(">");

				sb.append("<div class=\"lbl\">");
				String columnText = StringUtils.text(pagerColumn.getColumnText(), NBSP);
				columnText = HtmlUtils.convertHtmlLines(columnText);
				if ((Boolean) cp.getBeanProperty("sort") && pagerColumn.isSort()) {
					final String col2 = cp.getParameter(TablePagerUtils.PARAM_SORT_COL);
					StringBuilder img = null;
					if (columnName.equals(col2)) {
						final String sort = cp.getParameter(TablePagerUtils.PARAM_SORT);
						if ("up".equals(sort) || "down".equals(sort)) {
							img = new StringBuilder();
							img.append("<img style=\"margin-left: 4px;\" src=\"");
							img.append(MVCUtils.getPageResourcePath());
							img.append("/images/").append(sort).append(".png\" />");
						}
					}
					sb.append("<a onclick=\"$Actions['").append(componentName).append("'].sort('")
							.append(columnName).append("');\">").append(columnText).append("</a>");
					if (img != null) {
						sb.append(img);
					}
				} else {
					sb.append(columnText);
				}
				sb.append("</div>");
				if ((showVerticalLine || bResize) && (showCheckbox || i > 0)) {
					sb.append("<div class=\"sep\"></div>");
				}
				sb.append("</div>");
				sb.append("</td>");
				i++;
			}
			sb.append("</tr></table></div>");

			// filter pane
			if ((Boolean) cp.getBeanProperty("filter")) {
				sb.append("<div class=\"tfilter\"><table style=\"width:100%;padding-right:");
				sb.append(marginRight);
				sb.append("px;\" class=\"fixed_table\" cellpadding=\"0\" cellspacing=\"0\"><tr>");
				if (showDetail) {
					sb.append("<td class='plus'></td>");
				}
				if (numWidth > 0) {
					sb.append("<td width=\"").append(numWidth).append("\" class=\"rownum\">")
							.append(NBSP).append("</td>");
				}
				if (showCheckbox) {
					sb.append("<td class=\"cb\">").append(NBSP).append("</td>");
				}

				for (final TablePagerColumn pagerColumn : tablePagerData.getTablePagerColumns(cp)) {
					if (!pagerColumn.isVisible()) {
						continue;
					}
					final String columnName = pagerColumn.getColumnName();
					sb.append("<td");
					final String style = pagerColumn.toStyle(cp, null);
					if (StringUtils.hasText(style)) {
						sb.append(" style=\"").append(style).append("\"");
					}
					sb.append("><div class=\"box\">");
					if (pagerColumn.isFilter()) {
						String val = null;
						final String val2 = columnName
								.equals(cp.getParameter(TablePagerUtils.PARAM_FILTER_CUR_COL))
										? cp.getParameter(TablePagerUtils.PARAM_FILTER)
										: cp.getParameter(filterColumnKey(columnName));
						if (StringUtils.hasText(val2)
								&& !TablePagerUtils.PARAM_FILTER_NONE.equals(val2)) {
							val = filterDecode(StringUtils.split(val2)[1]);
						}
						final AbstractElement<?> filterElement = pHandle.toFilterHTML(cp, pagerColumn,
								val);
						if (filterElement != null) {
							sb.append(filterElement);
						}
					}
					sb.append("</div></td>");
				}
				sb.append("</tr></table></div>");
			}
			return sb.toString();
		}

		void appendTbody(final ComponentParameter cp, final int dataSize, final StringBuilder sb) {
			final List<?> data = PagerUtils.getCurrentPageData(cp);
			sb.append("<div class=\"tbody\"");
			if (data.size() > 0) {
				String height = cp.getParameter("tbl_tbody_height");
				if (!StringUtils.hasText(height)) {
					height = (String) cp.getBeanProperty("height");
				}
				final int h = Convert.toInt(height);
				if (h > 0) {
					height = h + "px";
				}
				if (StringUtils.hasText(height)) {
					sb.append(" style=\"height: ").append(height).append("\"");
				}
			}
			sb.append(">");
		}

		String renderBody(final ComponentParameter cp,
				final AbstractTablePagerSchema tablePagerData) {
			final List<?> data = PagerUtils.getCurrentPageData(cp);
			final StringBuilder sb = new StringBuilder();
			appendTbody(cp, data.size(), sb);
			for (int i = 0; i < data.size(); i++) {
				final String rowHTML = buildRow(cp, data.get(i), tablePagerData, new RowHandler(i));
				if (rowHTML != null) {
					sb.append(rowHTML);
				}
			}
			sb.append("</div>");
			return sb.toString();
		}

		String buildRow(final ComponentParameter cp, final Object oBean,
				final AbstractTablePagerSchema tablePagerData, final RowHandler handler) {
			final Map<String, Object> rowData = (oBean == null) ? null
					: tablePagerData.getRowData(cp, oBean);
			if (rowData == null && !handler.isEdit()) {
				return null;
			}

			final StringBuilder sb = new StringBuilder();
			sb.append("<div class=\"titem");
			if ((handler.getIndex() + 1) % 2 == 0) {
				sb.append(" even");
			}
			String itemClass;
			if (oBean != null
					&& (StringUtils.hasText(itemClass = tablePagerData.getRowClass(cp, oBean)))) {
				sb.append(" ").append(itemClass);
			}
			sb.append("\" style=\"margin-top: ");
			sb.append(-1 + (Integer) cp.getBeanProperty("rowMargin")).append("px;\"");

			final Map<String, Object> rowAttributes = (oBean == null) ? null
					: tablePagerData.getRowAttributes(cp, oBean);
			if (rowAttributes != null) {
				for (final Map.Entry<String, Object> entry : rowAttributes.entrySet()) {
					sb.append(" ").append(entry.getKey()).append("=");
					sb.append("\"").append(entry.getValue()).append("\"");
				}
			}

			final String componentName = cp.getComponentName();
			if (oBean != null && (Boolean) cp.getBeanProperty("editable")
					&& (Boolean) cp.getBeanProperty("dblclickEdit")) {
				sb.append(" ondblclick=\"$Actions['").append(componentName)
						.append("'].edit_row(this);\"");
			}

			sb.append("><table style=\"width: 100%;\" class=\"fixed_table\" ");
			sb.append("cellpadding=\"0\" cellspacing=\"0\"><tr class=\"itr\">");

			// showDetail
			String detailVal = null;
			String detailField;
			if (rowData != null
					&& StringUtils.hasText(detailField = (String) cp.getBeanProperty("detailField"))) {
				sb.append("<td class='plus'>");
				if (StringUtils.hasText(detailVal = Convert.toString(rowData.get(detailField)))) {
					sb.append("<img src=\"").append(ComponentUtils.getCssResourceHomePath(cp));
					sb.append("/images/toggle2.png\" />");
				}
				sb.append("</td>");
			}

			if ((Boolean) cp.getBeanProperty("showLineNo")) {
				final int pageNumber = PagerUtils.getPageNumber(cp);
				final int pageItems = PagerUtils.getPageItems(cp);
				sb.append("<td width=\"").append(getRownumWidth(cp, pageNumber, pageItems))
						.append("\" class=\"rownum\">");
				if (oBean != null) {
					sb.append(Math.max(pageNumber - 1, 0) * pageItems + handler.getIndex() + 1);
				} else {
					sb.append("<span class='del' onclick=\"$Actions['").append(componentName)
							.append("'].unedit(this);\"></span>");
				}
				sb.append("</td>");
			}
			if ((Boolean) cp.getBeanProperty("showCheckbox")) {
				sb.append("<td class=\"cb\">");
				if (rowAttributes != null) {
					sb.append("<input type=\"checkbox\" value=\"");
					sb.append(rowAttributes.get(AbstractTablePagerSchema.ROW_ID)).append("\"");
					if (Convert.toBool(rowData.get("checkVal"))) {
						sb.append(" checked");
					}
					sb.append(" />");
				}
				sb.append("</td>");
			}

			final boolean showVerticalLine = (Boolean) cp.getBeanProperty("showVerticalLine");
			for (final TablePagerColumn pagerColumn : tablePagerData.getTablePagerColumns(cp)) {
				if (!pagerColumn.isVisible()) {
					continue;
				}

				final String key = pagerColumn.getColumnName();
				if (handler.getIndex() == 0 && !StringUtils.hasText(pagerColumn.getPropertyClass())) {
					try {
						final Object oVal = BeanUtils.getProperty(oBean, key);
						if (oVal != null) {
							Class<?> iClass = oVal.getClass();
							if (oVal instanceof Enum) {
								while (iClass != null && iClass.getEnumConstants() == null) {
									iClass = iClass.getSuperclass();
								}
							}
							pagerColumn.setPropertyClass(iClass.getName());
						}
					} catch (final Exception e) {
					}
				}
				sb.append("<td");
				if (handler.isEdit() && pagerColumn.getVerticalAlign() == null) {
					pagerColumn.setVerticalAlign(EVerticalAlign.top);
				}

				final Object val = rowData != null ? rowData.get(key) : null;
				final ETextAlign defaultTextAlign = val instanceof Number || val instanceof Boolean
						|| val instanceof Date || val instanceof Enum ? ETextAlign.center
								: ETextAlign.left;
				final String style = pagerColumn.toStyle(cp, defaultTextAlign);
				if (StringUtils.hasText(style)) {
					sb.append(" style=\"").append(style).append("\"");
				}
				final String colClass = pagerColumn.toClassName(showVerticalLine);
				if (StringUtils.hasText(colClass)) {
					sb.append(" class=\"").append(colClass).append("\"");
				}
				sb.append("><div class=\"lbl\"");
				String lblStyle = pagerColumn.getLblStyle();
				if (pagerColumn.isNowrap()) {
					lblStyle = "overflow: hidden;" + (lblStyle != null ? lblStyle : "");
				}
				if (StringUtils.hasText(lblStyle)) {
					sb.append(" style=\"").append(lblStyle).append("\"");
				}
				sb.append(">").append(handler.getValue(val, pagerColumn)).append("</div></td>");
			}
			sb.append("</tr></table>");

			if (StringUtils.hasText(detailVal)) {
				sb.append("<div class=\"tdetail\" style=\"display: none;\">");
				sb.append(HtmlUtils.convertHtmlLines(detailVal));
				sb.append("</div>");
			}
			sb.append("</div>");
			return sb.toString();
		}
	}

	static class RowHandler {

		private final int index;

		private final boolean edit;

		RowHandler(final int index, final boolean edit) {
			this.index = index;
			this.edit = edit;
		}

		private RowHandler(final int index) {
			this(index, false);
		}

		int getIndex() {
			return index;
		}

		boolean isEdit() {
			return edit;
		}

		protected Object getValue(final Object value, final TablePagerColumn pagerColumn) {
			String ret = null;
			if (value != null) {
				ret = pagerColumn.objectToString(value);
			}
			// 不能含有脚本
			return StringUtils.text(HtmlUtils.escapeScript(ret), HtmlConst.NBSP);
		}
	}

	static class RenderGroupTable extends RenderTable {

		@Override
		String renderBody(final ComponentParameter cp,
				final AbstractTablePagerSchema tablePagerData) {
			final List<?> data = PagerUtils.getCurrentPageData(cp);
			if (data == null) {
				return "";
			}

			final IGroupTablePagerHandler gHandle = (IGroupTablePagerHandler) cp.getComponentHandler();
			final String groupColumn = (String) cp.getBeanProperty("groupColumn");
			final StringBuilder sb = new StringBuilder();
			final Map<Object, GroupWrapper> wrappers = new LinkedHashMap<>();
			for (final Object bean : data) {
				Object groupVal = gHandle.getGroupValue(cp, bean, groupColumn);
				if (!StringUtils.hasObject(groupVal)) {
					groupVal = $m("TablePagerHTML.0");
				}
				GroupWrapper gw = wrappers.get(groupVal);
				if (gw == null) {
					wrappers.put(groupVal, gw = gHandle.getGroupWrapper(cp, groupVal));
				}
				gw.getData().add(bean);
			}

			appendTbody(cp, data.size(), sb);

			final String imgPath = ComponentUtils.getCssResourceHomePath(cp);

			final Collection<Object> groupVals = gHandle.doGroups(cp, wrappers);
			for (final Object groupVal : groupVals) {
				final GroupWrapper gw = wrappers.get(groupVal);
				sb.append("<div class=\"group_t\">");
				if (gw.isToggle()) {
					sb.append(" <div class=\"toggle\">");
					sb.append(new ImageElement(imgPath + "/images/p_toggle.png"));
					sb.append(" </div>");
				}
				sb.append(" <div class=\"t\">").append(gw).append("</div>");
				sb.append("</div>");
				final List<Object> gData = gw.getData();
				if (gData != null) {
					sb.append("<div class=\"group_c\">");
					int i = 0;
					for (final Object row : gData) {
						final String rowHTML = buildRow(cp, row, tablePagerData, new RowHandler(i++));
						if (rowHTML != null) {
							sb.append(rowHTML);
						}
					}
					sb.append("</div>");
				}
			}
			sb.append("</div>");
			return sb.toString();
		}
	}
}
