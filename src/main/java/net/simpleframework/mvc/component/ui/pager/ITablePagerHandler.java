package net.simpleframework.mvc.component.ui.pager;

import java.util.Map;

import net.simpleframework.ado.ColumnData;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItems;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface ITablePagerHandler extends IPagerHandler {

	String toTableHTML(ComponentParameter cp);

	/**
	 * 创建表格的元信息
	 * 
	 * @return
	 */
	AbstractTablePagerSchema createTablePagerSchema();

	/**
	 * 根据id查找行数据
	 * 
	 * @param compParameter
	 * @param id
	 * @return
	 */
	Object getRowBeanById(ComponentParameter cp, Object id);

	/**
	 * 获取当前的排序列
	 * 
	 * @param compParameter
	 * @return
	 */
	ColumnData getSortColumn(ComponentParameter cp);

	/**
	 * 获取当前的过滤列
	 * 
	 * 和getSortColumn一起提供排序和过滤的列信息
	 * 
	 * @param compParameter
	 * @return
	 */
	Map<String, ColumnData> getFilterColumns(ComponentParameter cp);

	// menu
	MenuItems getHeaderMenu(ComponentParameter cp, MenuBean menuBean);

	/**
	 * 获取列的过滤html
	 * 
	 * @param cp
	 * @param column
	 * @param val
	 * @return
	 */
	AbstractElement<?> toFilterHTML(ComponentParameter cp, TablePagerColumn column, String val);

	/**
	 * 在编辑时，获取编辑器的HTML代码
	 * 
	 * @param compParameter
	 * @param column
	 * @param rowId
	 * @param elementName
	 * @param rowData
	 *        行的真实数据
	 * @return
	 */
	AbstractElement<?> toRowEditorHTML(ComponentParameter cp, TablePagerColumn column, String rowId,
			String elementName, Object rowData);

	/**
	 * 保存行编辑数据
	 * 
	 * @param cp
	 * @param insertRows
	 * @param updateRows
	 * @return
	 */
	JavascriptForward doRowSave(ComponentParameter cp, Map<String, Map<String, Object>> insertRows,
			Map<String, Map<String, Object>> updateRows);

	ElementList toSaveButtons(ComponentParameter cp);

	/**
	 * 数据导出
	 * 
	 * @param compParameter
	 * @param filetype
	 * @param columns
	 */
	void export(ComponentParameter cp, EExportFileType filetype, TablePagerColumns columns);

	/**
	 * 获取数据导出的选择框
	 * 
	 * @param cp
	 * @return
	 */
	AbstractElement<?> getExportSelectElement(ComponentParameter cp);
}
