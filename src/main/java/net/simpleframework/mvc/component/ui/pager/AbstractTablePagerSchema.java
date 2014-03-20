package net.simpleframework.mvc.component.ui.pager;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.I18n;
import net.simpleframework.common.ID;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.common.xml.XmlAttri;
import net.simpleframework.ctx.common.xml.XmlDocument;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractTablePagerSchema {

	public static LinkElement IMG_DOWNMENU = new LinkElement().setClassName("m down_menu_image");

	private static final TablePagerColumns DEFAULT_COLUMNS = TablePagerColumns.of(
			TablePagerColumn.ICON(), TablePagerColumn.ACTION(), TablePagerColumn.OPE(),
			TablePagerColumn.BLANK());

	/**
	 * 获取列头信息
	 * 
	 * @param compParameter
	 * @return
	 */
	public TablePagerColumns getTablePagerColumns(final ComponentParameter cp) {
		return getTablePagerColumnsInternal(cp);
	}

	private TablePagerColumns _columns;

	protected TablePagerColumns getTablePagerColumnsInternal(final ComponentParameter cp) {
		final TablePagerBean tablePager = (TablePagerBean) cp.componentBean;
		if (_columns == null) {
			_columns = new TablePagerColumns(tablePager.getColumns());
		}
		if (_columns.size() == 0) {
			synchronized (_columns) {
				final InputStream inputStream = ClassUtils.getResourceRecursively(getClass(),
						"columns.xml");
				if (inputStream == null) {
					return _columns;
				}
				final Iterator<?> it = new XmlDocument(inputStream).getRoot().elementIterator();
				while (it.hasNext()) {
					final XmlElement ele = (XmlElement) it.next();
					final String columnName = ele.attributeValue("columnName");
					TablePagerColumn tablePagerColumn = DEFAULT_COLUMNS.get(columnName);
					if (tablePagerColumn != null) {
						_columns.add(tablePagerColumn);
					} else {
						_columns.add(tablePagerColumn = new TablePagerColumn());
						final Iterator<?> attributes = ele.attributeIterator();
						while (attributes.hasNext()) {
							final XmlAttri attri = (XmlAttri) attributes.next();
							BeanUtils.setProperty(tablePagerColumn, attri.getName(),
									I18n.replaceI18n(attri.getValue()));
						}
					}
				}
			}
		}
		return _columns;
	}

	/**
	 * 获取导出的列信息
	 * 
	 * @param compParameter
	 * @return
	 */
	public TablePagerColumns getTablePagerExportColumns(final ComponentParameter cp) {
		final TablePagerColumns columns = TablePagerColumns.of();
		for (final TablePagerColumn oCol : getTablePagerColumns(cp)) {
			if (!oCol.isExport()) {
				continue;
			}
			columns.add(oCol);
		}
		return columns;
	}

	/**
	 * 将真实数据转为显示数据
	 * 
	 * @param compParameter
	 * @param dataObject
	 * @return
	 */
	public Map<String, Object> getRowData(final ComponentParameter cp, final Object dataObject) {
		final KVMap kv = new KVMap();
		for (final TablePagerColumn column : getTablePagerColumns(cp)) {
			final String key = column.getColumnName();
			Object val = null;
			try {
				val = getVal(dataObject, key);
			} catch (final Exception e) {
			}
			if (val != null) {
				kv.add(key, val);
			}
		}
		return kv;
	}

	public Map<String, Object> getExportRowData(final ComponentParameter cp, final Object dataObject) {
		return getExportRowData(cp, null, dataObject);
	}

	/**
	 * 将真实数据转为导出数据
	 * 
	 * @param compParameter
	 * @param columns
	 * @param dataObject
	 * @return
	 */
	public Map<String, Object> getExportRowData(final ComponentParameter cp,
			TablePagerColumns columns, final Object dataObject) {
		if (columns == null) {
			columns = getTablePagerExportColumns(cp);
		}
		final KVMap rowData = new KVMap();
		Object value;
		for (final TablePagerColumn column : columns) {
			final String key = column.getColumnName();
			try {
				value = getVal(dataObject, key);
			} catch (final Exception e) {
				value = "<ERROR>";
			}
			rowData.add(key, value);
		}
		return rowData;
	}

	public static final String ROW_ID = "rowid";

	public static final String MENU_DISABLED = "menu-disabled";

	/**
	 * 获取数据的值
	 * 
	 * @param dataObject
	 * @return
	 */
	public Object getVal(final Object dataObject, final String key) {
		return BeanUtils.getProperty(dataObject, key);
	}

	public ID genId() {
		return ID.uuid();
	}

	/**
	 * 为表格的行添加额外的HTML属性
	 * 
	 * @param compParameter
	 * @param dataObject
	 * @return
	 */
	public Map<String, Object> getRowAttributes(final ComponentParameter cp, final Object dataObject) {
		final KVMap attributes = new KVMap();
		final Object id = getVal(dataObject, "id");
		if (id != null) {
			attributes.add(ROW_ID, id);
		}
		return attributes;
	}
}
