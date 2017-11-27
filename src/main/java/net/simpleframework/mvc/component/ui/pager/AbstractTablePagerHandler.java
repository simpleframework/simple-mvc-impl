package net.simpleframework.mvc.component.ui.pager;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.ColumnData;
import net.simpleframework.ado.EFilterOpe;
import net.simpleframework.ado.EFilterRelation;
import net.simpleframework.ado.EOrder;
import net.simpleframework.ado.FilterItem;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ado.query.ListDataQuery;
import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.th.NotImplementedException;
import net.simpleframework.lib.au.com.bytecode.opencsv.CSVWriter;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.BlockElement;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.EInputType;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.ImageElement;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.Option;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentHandlerException;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.component.base.validation.Validators;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItems;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractTablePagerHandler extends AbstractPagerHandler
		implements ITablePagerHandler {

	@Override
	public String toTableHTML(final ComponentParameter cp) {
		return TablePagerHTML.renderTable(cp);
	}

	@Override
	public AbstractTablePagerSchema createTablePagerSchema() {
		return new DefaultTablePagerSchema();
	}

	public class DefaultTablePagerSchema extends AbstractTablePagerSchema {

		private final AbstractTablePagerHandler hdl = AbstractTablePagerHandler.this;

		@Override
		public Map<String, Object> getRowData(final ComponentParameter cp, final Object dataObject) {
			return hdl.getRowData(cp, dataObject);
		}

		@Override
		public Map<String, Object> getRowAttributes(final ComponentParameter cp,
				final Object dataObject) {
			final Map<String, Object> attributes = super.getRowAttributes(cp, dataObject);
			final Map<String, Object> attributes2 = hdl.getRowAttributes(cp, dataObject);
			if (attributes2 != null) {
				attributes.putAll(attributes2);
			}
			return attributes;
		}

		@Override
		public String getRowClass(final ComponentParameter cp, final Object dataObject) {
			return hdl.getRowClass(cp, dataObject);
		}
	}

	protected Map<String, Object> getRowData(final ComponentParameter cp, final Object dataObject) {
		return null;
	}

	protected Map<String, Object> getRowAttributes(final ComponentParameter cp,
			final Object dataObject) {
		return null;
	}

	protected String getRowClass(final ComponentParameter cp, final Object dataObject) {
		return null;
	}

	@Override
	public Object getRowBeanById(final ComponentParameter cp, final Object id) {
		throw ComponentHandlerException.of($m("AbstractTablePagerHandler.0"));
	}

	@Override
	public MenuItems getHeaderMenu(final ComponentParameter cp, final MenuBean menuBean) {
		return null;
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter cp) {
		final Map<String, Object> parameters = super.getFormParameters(cp);

		for (final String k : new String[] { TablePagerUtils.PARAM_SORT_COL,
				TablePagerUtils.PARAM_SORT, "tbl_tbody_height", "tbl_thead_margin_right" }) {
			parameters.put(k, cp.getParameter(k));
		}

		final Map<String, ColumnData> dbColumns = getFilterColumns(cp);
		if (dbColumns != null) {
			final StringBuilder sb = new StringBuilder();
			int i = 0;
			for (final ColumnData dbColumn : dbColumns.values()) {
				final String ovalue = (String) dbColumn.getAttr("ovalue");
				if (StringUtils.hasText(ovalue)) {
					final String col = dbColumn.getName();
					if (i++ > 0) {
						sb.append(";");
					}
					sb.append(col);
					parameters.put(TablePagerHTML.filterColumnKey(col), ovalue);
				}
			}
			if (sb.length() > 0) {
				parameters.put(TablePagerUtils.PARAM_FILTER_COL, sb.toString());
			}
		}
		return parameters;
	}

	// implements IPagerHandle

	protected ColumnData createColumnData(final TablePagerColumn oCol) {
		return new ColumnData(oCol.getColumnName());
	}

	@Override
	public ColumnData getSortColumn(final ComponentParameter cp) {
		final String col = cp.getParameter(TablePagerUtils.PARAM_SORT_COL);
		final TablePagerColumn oCol;
		if (StringUtils.hasText(col)
				&& (oCol = getTablePagerSchema(cp).getTablePagerColumns(cp).get(col)) != null) {
			final String sort = cp.getParameter(TablePagerUtils.PARAM_SORT);
			if ("up".equals(sort) || "down".equals(sort)) {
				final ColumnData dbColumn = createColumnData(oCol);
				dbColumn.setOrder("up".equals(sort) ? EOrder.desc : EOrder.asc);
				return dbColumn;
			}
		}
		return null;
	}

	@Override
	public Map<String, ColumnData> getFilterColumns(final ComponentParameter cp) {
		@SuppressWarnings("unchecked")
		Map<String, ColumnData> dbColumns = (Map<String, ColumnData>) cp
				.getRequestAttr("filter_columns");
		if (dbColumns != null) {
			return dbColumns;
		}

		final String col = cp.getParameter(TablePagerUtils.PARAM_FILTER_COL);
		final String ccol = cp.getParameter(TablePagerUtils.PARAM_FILTER_CUR_COL);
		if (!StringUtils.hasText(ccol) && !StringUtils.hasText(col)) {
			return null;
		}

		final HashSet<String> sets = new LinkedHashSet<>(ArrayUtils.asList(StringUtils.split(col)));
		String filter = null;
		if (StringUtils.hasText(ccol)) {
			filter = cp.getParameter(TablePagerUtils.PARAM_FILTER);
			if (TablePagerUtils.PARAM_FILTER_NONE.equals(filter)) {
				sets.remove(ccol);
			} else {
				filter = StringUtils.text(filter,
						cp.getParameter(TablePagerHTML.filterColumnKey(ccol)));
				if (StringUtils.hasText(filter)) {
					sets.add(ccol);
				} else {
					sets.remove(ccol);
				}
			}
		}
		if (sets.size() == 0) {
			return null;
		}

		final TablePagerColumns tableColumns = getTablePagerSchema(cp).getTablePagerColumns(cp);
		dbColumns = new LinkedHashMap<>();
		for (final String str : sets) {
			final TablePagerColumn oCol = tableColumns.get(str);
			if (oCol == null) {
				continue;
			}
			final String vStr = str.equals(ccol) ? filter
					: cp.getParameter(TablePagerHTML.filterColumnKey(str));
			final String[] vals = StringUtils.split(vStr);
			if (vals.length < 2) {
				continue;
			}
			String val = TablePagerHTML.filterDecode(vals[1]);
			if (TablePagerUtils.PARAM_FILTER_NONE.equals(val)) {
				continue;
			}
			final ColumnData dbColumn = createColumnData(oCol);
			dbColumn.setPropertyClass(oCol.propertyClass());
			dbColumns.put(str, dbColumn);
			dbColumn.setAttr("ovalue", vStr);
			FilterItem item = new FilterItem(null, EFilterRelation.get(vals[0]),
					oCol.stringToObject(val)).setOriginalValue(val);
			dbColumn.getFilterItems().add(item);
			if (vals.length == 5) {
				item.setOpe(EFilterOpe.get(vals[2]));
				val = TablePagerHTML.filterDecode(vals[4]);
				item = new FilterItem(null, EFilterRelation.get(vals[3]), oCol.stringToObject(val))
						.setOriginalValue(val);
				dbColumn.getFilterItems().add(item);
			}
		}
		cp.setRequestAttr("filter_columns", dbColumns);
		return dbColumns;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void doFilterList(final ComponentParameter cp, final ListDataQuery<?> dataQuery) {
		final Map<String, ColumnData> filterColumns = getFilterColumns(cp);
		final String bKey = "backup_filterlist";
		List oData = (List) dataQuery.getAttr(bKey);
		if (filterColumns != null && filterColumns.size() > 0) {
			if (oData == null) {
				dataQuery.setAttr(bKey, oData = new ArrayList(dataQuery.list()));
			}
			final ArrayList result = new ArrayList(oData);
			for (final ColumnData oCol : filterColumns.values()) {
				final Iterator<FilterItem> it = oCol.getFilterItems().iterator();
				final FilterItem item1 = it.hasNext() ? it.next() : null;
				if (item1 == null) {
					continue;
				}
				final String propertyName = oCol.getName();
				final Class<?> propertyType = oCol.getPropertyClass();
				final FilterItem item2 = it.hasNext() ? it.next() : null;
				ArrayList result2 = new ArrayList(result);
				result.clear();
				for (final Object o : result2) {
					final Object v = getVal(cp, o, propertyName);
					boolean delete = item1.isDelete(v, propertyType);
					if (delete && item1.getOpe() == EFilterOpe.or && item2 != null) {
						delete = item2.isDelete(v, propertyType);
					}
					if (!delete) {
						result.add(o);
					}
				}
				if (item1.getOpe() == EFilterOpe.and && item2 != null) {
					result2 = new ArrayList(result);
					result.clear();
					for (final Object o : result2) {
						final Object v = getVal(cp, o, propertyName);
						if (!item2.isDelete(v, propertyType)) {
							result.add(o);
						}
					}
				}
			}
			final List data = dataQuery.list();
			if (data.size() > result.size()) {
				data.clear();
				data.addAll(result);
			}
		} else if (oData != null) {
			final List data = dataQuery.list();
			data.clear();
			data.addAll(oData);
			dataQuery.removeAttr(bKey);
		}
	}

	@Override
	protected void doCount(final ComponentParameter cp, final IDataQuery<?> dataQuery) {
		if (dataQuery instanceof ListDataQuery) {
			doFilterList(cp, (ListDataQuery<?>) dataQuery);
		}
		super.doCount(cp, dataQuery);
	}

	// protected Object get
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected int doSort(final ComponentParameter cp, final ColumnData dbColumn, final Object o1,
			final Object o2) {
		try {
			final String col = dbColumn.getName();
			final Comparable c1 = (Comparable) getVal(cp, o1, col);
			final Comparable c2 = (Comparable) getVal(cp, o2, col);
			if (c1 == null && c2 == null) {
				return 0;
			} else if (c1 == null) {
				return dbColumn.getOrder() == EOrder.desc ? 1 : -1;
			} else if (c2 == null) {
				return dbColumn.getOrder() == EOrder.desc ? -1 : 1;
			} else {
				return dbColumn.getOrder() == EOrder.desc ? c1.compareTo(c2) : c2.compareTo(c1);
			}
		} catch (final Exception e) {
			getLog().warn(e);
		}
		return 0;
	}

	@Override
	protected List<?> getData(final ComponentParameter cp, final int start) {
		final IDataQuery<?> dataQuery = getDataObjectQuery(cp);
		if (dataQuery instanceof ListDataQuery) {
			final ColumnData dbColumn = getSortColumn(cp);
			if (dbColumn != null) {
				Collections.sort(((ListDataQuery<?>) dataQuery).list(), new Comparator<Object>() {
					@Override
					public int compare(final Object o1, final Object o2) {
						return doSort(cp, dbColumn, o1, o2);
					}
				});
			}
		}
		return super.getData(cp, start);
	}

	@Override
	public void export(final ComponentParameter cp, final EExportFileType filetype,
			final TablePagerColumns columns) {
		doExport(cp, createDataObjectQuery(cp), filetype, columns);
	}

	protected void doExport(final ComponentParameter cp, final IDataQuery<?> dQuery,
			final EExportFileType filetype, final TablePagerColumns columns) {
		if (dQuery instanceof ListDataQuery) {
			doFilterList(cp, (ListDataQuery<?>) dQuery);
		}

		dQuery.setFetchSize(0);
		final AbstractTablePagerSchema tablePagerSchema = getTablePagerSchema(cp);
		try {
			if (filetype == EExportFileType.csv) {
				doCSVExport(cp, dQuery, tablePagerSchema, columns);
			} else if (filetype == EExportFileType.excel) {
				doExcelExport(cp, dQuery, tablePagerSchema, columns);
			}
		} catch (final IOException e) {
			throw ComponentHandlerException.of(e);
		}
	}

	@Override
	public AbstractElement<?> getExportSelectElement(final ComponentParameter cp) {
		return InputElement.select("tablepager_export_filetype")
				.addElements(Option.from(EExportFileType.excel, EExportFileType.csv));
	}

	protected void doCSVExport(final ComponentParameter cp, final IDataQuery<?> dQuery,
			final AbstractTablePagerSchema tablePagerData, final TablePagerColumns columns)
			throws IOException {
		CSVWriter csvWriter = null;
		try {
			cp.setSessionAttr(TablePagerExportProgressBar.MAX_PROGRESS, dQuery.getCount());
			String exportCharset = (String) cp.getBeanProperty("exportCharset");
			if (!StringUtils.hasText(exportCharset)) {
				exportCharset = mvcSettings.getCharset();
			}
			csvWriter = new CSVWriter(
					new OutputStreamWriter(cp.getBinaryOutputStream("export.csv"), exportCharset));
			final ArrayList<String> al = new ArrayList<>();
			for (final TablePagerColumn column : columns) {
				al.add(column.getColumnText());
			}
			csvWriter.writeNext(al.toArray(new String[al.size()]));
			Object object;
			int i = 1;
			while ((object = dQuery.next()) != null) {
				al.clear();
				for (final Object o : tablePagerData.getExportRowData(cp, columns, object).values()) {
					al.add(StringUtils.blank(o));
				}
				csvWriter.writeNext(al.toArray(new String[al.size()]));
				cp.setSessionAttr(TablePagerExportProgressBar.STEP, i++);
			}
		} finally {
			try {
				if (csvWriter != null) {
					csvWriter.close();
				}
			} catch (final IOException e) {
			}
		}
	}

	protected void doExcelExport(final ComponentParameter cp, final IDataQuery<?> dQuery,
			final AbstractTablePagerSchema tablePagerData, final TablePagerColumns columns)
			throws IOException {
		throw NotImplementedException.of(getClass(), "doExcelExport");
	}

	/**
	 * 输入过滤条件的验证信息
	 * 
	 * 子类覆盖
	 * 
	 * @param compParameter
	 * @param oCol
	 * @return
	 */
	protected Validators getFilterColumnValidators(final ComponentParameter cp,
			final TablePagerColumn oCol) {
		final Validators al = Validators.of();
		if (Number.class.isAssignableFrom(oCol.propertyClass())) {
			final Validator validator = new Validator();
			validator.setMethod(EValidatorMethod.number);
			validator.setSelector("#tp_filter_v1, #tp_filter_v2");
			al.add(validator);
		}
		return al;
	}

	@Override
	public AbstractElement<?> toRowEditorHTML(final ComponentParameter cp,
			final TablePagerColumn column, final String rowId, final String elementName,
			final Object rowData) {
		AbstractElement<?> element;
		final Class<?> propertyClass = column.propertyClass();
		final String columnName = column.getColumnName();
		final Option[] arr = column.getFilterOptions();
		if (arr != null && arr.length > 0) {
			element = InputElement.select().setName(elementName);
			Object val = null;
			if (rowData != null) {
				val = getVal(cp, rowData, columnName);
			}
			if (val instanceof Enum<?>) {
				val = ((Enum<?>) val).name();
			} else if (val != null) {
				if (BeanUtils.hasProperty(val, "name")) {
					val = getVal(cp, val, "name");
				} else {
					val = val.toString();
				}
			}
			for (final Option opt : arr) {
				if (opt.getName().equals(val)) {
					opt.setSelected(true);
				}
				element.addElements(opt);
			}
		} else if (Boolean.class.isAssignableFrom(propertyClass)) {
			element = new InputElement().setInputType(EInputType.checkbox).setName(elementName)
					.setText(true);
			if (rowData != null) {
				if (Convert.toBool(getVal(cp, rowData, columnName))) {
					((InputElement) element).setChecked(true);
				}
			}
		} else {
			final InputElement input = (InputElement) (element = new InputElement()
					.setName(elementName));
			Object val;
			if (rowData != null && ((val = getVal(cp, rowData, columnName)) != null)) {
				input.setText(column.objectToString(val));
			}
			if (Date.class.isAssignableFrom(propertyClass)) {
				String onclick = "$Actions['rowEditorCalendar'].show(this";
				final String format = column.getFormat();
				if (StringUtils.hasText(format)) {
					onclick += ", '" + format + "');";
				}
				input.setOnclick(onclick);
			}
		}
		return element;
	}

	@Override
	public JavascriptForward doRowSave(final ComponentParameter cp,
			final Map<String, Map<String, Object>> insertRows,
			final Map<String, Map<String, Object>> updateRows) {
		return null;
	}

	@Override
	public ElementList toSaveButtons(final ComponentParameter cp) {
		final String componentName = cp.getComponentName();
		final String act = "$Actions['" + componentName + "']";
		return ElementList.of()
				.append(new ButtonElement($m("TablePagerAction.2"))
						.setHighlight(true).setOnclick(act + ".save_rows(this);"))
				.append(SpanElement.SPACE)
				.append(new ButtonElement($m("TablePagerAction.1")).setOnclick("if (!confirm('"
						+ $m("TablePagerAction.3") + "')) return;" + act + ".uneditAll(this);"))
				.append(SpanElement.SPACE).append(new SpanElement($m("TablePagerAction.4")));
	}

	@Override
	public AbstractElement<?> toFilterHTML(final ComponentParameter cp,
			final TablePagerColumn pagerColumn, final String val) {
		AbstractElement<?> element;
		final String componentName = cp.getComponentName();
		final String columnName = pagerColumn.getColumnName();
		final Class<?> pClass = pagerColumn.propertyClass();
		final Option[] arr = pagerColumn.getFilterOptions();
		boolean bool = false;
		if ((arr != null && arr.length > 0) || (bool = Boolean.class.isAssignableFrom(pClass))) {
			final InputElement input = (InputElement) (element = InputElement.select());
			final String onchange = "$Actions['" + componentName + "']('"
					+ TablePagerUtils.PARAM_FILTER_CUR_COL + "=" + columnName + "&"
					+ TablePagerUtils.PARAM_FILTER + "=%3D;' + $F(this));";
			input.setOnchange(onchange);
			input.addElements(
					new Option(TablePagerUtils.PARAM_FILTER_NONE, $m("AbstractTablePagerHandler.1")));
			if (bool) {
				input.addElements(Option.TRUE().setSelected("true".equals(val)),
						Option.FALSE().setSelected("false".equals(val)));
			} else {
				for (final Option opt : arr) {
					opt.setSelected(opt.getName().equals(val));
					input.addElements(opt);
				}
			}
		} else {
			element = new BlockElement();
			final InputElement input = new InputElement().addAttribute("params",
					TablePagerUtils.PARAM_FILTER_CUR_COL + "=" + columnName);
			final String val2 = pagerColumn.getFilterVal(val);
			if (val2 != null) {
				input.setText(val2);
			}

			if (Date.class.isAssignableFrom(pClass) || !pagerColumn.isEditable()) {
				input.setReadonly(true);
			}
			element.addElements(input, createFilterImage(cp, pagerColumn));
		}
		return element;
	}

	protected AbstractElement<?> createFilterImage(final ComponentParameter cp,
			final TablePagerColumn pagerColumn) {
		final String componentName = cp.getComponentName();
		final String columnName = pagerColumn.getColumnName();
		final ImageElement link = new ImageElement().setClassName("fbtn");
		final Map<String, ColumnData> filterColumns = getFilterColumns(cp);
		final String ipath = cp.getCssResourceHomePath(AbstractTablePagerHandler.class) + "/images/";
		if (filterColumns != null && filterColumns.containsKey(columnName)) {
			link.setSrc(ipath + "delete.png");
			link.setTitle("#(TablePagerUtils.8)")
					.setOnclick("$Actions['" + componentName + "'].filterDelete(this, '"
							+ TablePagerUtils.PARAM_FILTER_CUR_COL + "=" + columnName + "');");
		} else {
			link.setSrc(ipath + "filter.png").setTitle("#(TablePagerUtils.7)");
			final String advClick = pagerColumn.getFilterAdvClick();
			if (StringUtils.hasText(advClick)) {
				link.setOnclick(advClick);
			} else {
				if ((Boolean) cp.getBeanProperty("filterWindow")) {
					link.setOnclick("$Actions['" + componentName + "'].filterWindow(this, '"
							+ TablePagerUtils.PARAM_FILTER_CUR_COL + "=" + columnName + "&"
							+ TablePagerUtils.PARAM_FILTER + "=" + TablePagerUtils.PARAM_FILTER_NONE
							+ "');");
				} else {
					link.setOnclick("$Actions['" + componentName + "'].doReturn(this.previous());");
				}
			}
		}
		return link;
	}

	@Override
	public Map<String, Object> toJSON(final ComponentParameter cp) {
		final KVMap kv = (KVMap) super.toJSON(cp);
		final String jsRowClick = (String) cp.getBeanProperty("jsRowClick");
		if (StringUtils.hasText(jsRowClick)) {
			kv.put("jsRowClick", jsRowClick);
		}
		final String jsRowDblclick = (String) cp.getBeanProperty("jsRowDblclick");
		if (StringUtils.hasText(jsRowDblclick)) {
			kv.put("jsRowDblclick", jsRowDblclick);
		}
		final String detailField = (String) cp.getBeanProperty("detailField");
		kv.put("isDetailField", StringUtils.hasText(detailField));
		kv.put("isExpandDetailField", cp.getBeanProperty("expandDetailField"));
		kv.put("isGroup", TablePagerUtils.isGroup(cp));
		return kv;
	}

	public Collection<Object> doGroups(final ComponentParameter cp,
			final Map<Object, GroupWrapper> groups) {
		return groups.keySet();
	}

	public GroupWrapper getGroupWrapper(final ComponentParameter cp, final Object groupVal) {
		return new GroupWrapper(groupVal);
	}

	public Object getGroupValue(final ComponentParameter cp, final Object bean,
			final String groupColumn) {
		return getVal(cp, bean, groupColumn);
	}

	private Object getVal(final ComponentParameter cp, final Object dataObject, final String key) {
		return getTablePagerSchema(cp).getVal(dataObject, key);
	}

	protected AbstractTablePagerSchema getTablePagerSchema(final ComponentParameter cp) {
		return TablePagerUtils.getTablePagerSchema(cp);
	}
}
