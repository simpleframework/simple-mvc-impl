package net.simpleframework.mvc.common.element;

import net.simpleframework.common.coll.AbstractArrayListEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TableRows extends AbstractArrayListEx<TableRows, TableRow> {
	public static TableRows of(final TableRow... rows) {
		return new TableRows().append(rows);
	}

	public TableRows setReadonly(final boolean readonly) {
		for (final TableRow row : this) {
			row.setReadonly(readonly);
		}
		return this;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (final TableRow row : this) {
			sb.append(row);
		}
		return sb.toString();
	}

	private static final long serialVersionUID = 7977394637833523185L;
}
