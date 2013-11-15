package net.simpleframework.mvc.component.ui.pager;

import java.util.Collection;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.AbstractArrayListEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TablePagerColumns extends AbstractArrayListEx<TablePagerColumns, TablePagerColumn> {

	public static TablePagerColumns of(final TablePagerColumn... columns) {
		return new TablePagerColumns().append(columns);
	}

	public TablePagerColumns() {
	}

	public TablePagerColumns(final Collection<TablePagerColumn> columns) {
		addAll(columns);
	}

	public TablePagerColumn get(final String columnName) {
		if (!StringUtils.hasText(columnName)) {
			return null;
		}
		for (final TablePagerColumn col : this) {
			if (columnName.equals(col.getColumnName())) {
				return col;
			}
		}
		return null;
	}

	private static final long serialVersionUID = -6328187213776030850L;
}
