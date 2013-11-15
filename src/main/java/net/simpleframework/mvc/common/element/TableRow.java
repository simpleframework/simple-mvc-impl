package net.simpleframework.mvc.common.element;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.AbstractArrayListEx;
import net.simpleframework.common.web.html.HtmlConst;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TableRow extends AbstractArrayListEx<TableRow, RowField> implements HtmlConst {

	private BlockElement blankElement;

	private String id;

	private String style;

	public TableRow(final RowField... rows) {
		if (rows != null) {
			for (final RowField r : rows) {
				add(r);
			}
		}
	}

	public BlockElement getBlankElement() {
		return blankElement;
	}

	public TableRow setBlankElement(final BlockElement blankElement) {
		this.blankElement = blankElement;
		return this;
	}

	public TableRow setReadonly(final boolean readonly) {
		for (final RowField li : this) {
			li.setReadonly(readonly);
		}
		return this;
	}

	public String getId() {
		return id;
	}

	public TableRow setId(final String id) {
		this.id = id;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public TableRow setStyle(final String style) {
		this.style = style;
		return this;
	}

	@Override
	public String toString() {
		final BlockElement blankElement = getBlankElement();
		if (blankElement != null) {
			return blankElement.addClassName("form_block").toString();
		}
		final int size = size();
		if (size == 0) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("<table class='form_tbl' cellspacing='1'");
		final String id = getId();
		if (StringUtils.hasText(id)) {
			sb.append(" id='").append(id).append("'");
		}
		final String style = getStyle();
		if (StringUtils.hasText(style)) {
			sb.append(" style='").append(style).append("'");
		}
		sb.append(">");
		sb.append("  <tr>");
		for (final RowField li : this) {
			final String label = li.getLabel();
			if (StringUtils.hasText(label)) {
				sb.append(" <td class='l'");
				final String labelStyle = li.getLabelStyle();
				if (StringUtils.hasText(labelStyle)) {
					sb.append(" style='").append(labelStyle).append("'");
				}
				sb.append(">");
				if (li.isStarMark()) {
					sb.append("<span style='color:red'>*</span>").append(NBSP);
				}
				sb.append(label);
				sb.append("</td>");
			}
			sb.append(" <td class='v'");
			final String elementsStyle = li.getElementsStyle();
			if (StringUtils.hasText(elementsStyle)) {
				sb.append(" style='").append(elementsStyle).append("'");
			}
			sb.append(">");
			final AbstractElement<?>[] elements = li.getInputElements();
			if (elements != null) {
				for (final AbstractElement<?> ele : elements) {
					if (ele != null) {
						sb.append(ele);
					}
				}
			}
			sb.append(" </td>");
		}
		sb.append("  </tr>");
		sb.append("</table>");
		return sb.toString();
	}

	private static final long serialVersionUID = -5881344205644047999L;
}
