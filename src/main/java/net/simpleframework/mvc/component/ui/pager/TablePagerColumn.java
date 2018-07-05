package net.simpleframework.mvc.component.ui.pager;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.Convert;
import net.simpleframework.common.NumberUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.html.HtmlConst;
import net.simpleframework.ctx.common.xml.AbstractElementBean;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.common.element.ETextAlign;
import net.simpleframework.mvc.common.element.EVerticalAlign;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.common.element.Option;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TablePagerColumn extends AbstractElementBean {
	/* 表格列名称，数据的key值 */
	private String columnName, columnAlias;

	/* 表格列的显示名称 */
	private String columnText;

	/* 表格列的数据类型 */
	private String propertyClass;

	/* 表格列的宽度 */
	private int width;

	/* 表格列文本的对齐方式 */
	private ETextAlign textAlign;

	private EVerticalAlign verticalAlign;

	/* 表格列是否可以排序 */
	private boolean sort = true;

	/* 表格列的宽度是否可以改变 */
	private boolean resize = true;

	/* 表格列是否可以过滤 */
	private boolean filter = true;

	/* 过滤条件是否可以自定义action */
	private String filterAdvClick;

	/* 表格列是否可以导出、编辑 */
	private boolean export = true, editable = true;

	/* 是否可见 */
	private boolean visible = true;

	private String format;

	/* 表格列是否wrap超出列大小的数据内容 */
	private boolean nowrap = true;

	private boolean relative;

	private String lblStyle;

	public TablePagerColumn(final String columnName, final String columnText, final int width) {
		setColumnName(columnName).setColumnText(columnText).setWidth(width);
	}

	public TablePagerColumn(final String columnName, final String columnText) {
		this(columnName, columnText, 0);
	}

	public TablePagerColumn(final String columnName) {
		this(columnName, null);
	}

	public TablePagerColumn() {
		this((String) null);
	}

	public String getColumnName() {
		return columnName;
	}

	public TablePagerColumn setColumnName(final String columnName) {
		this.columnName = columnName;
		return this;
	}

	public String getColumnText() {
		return StringUtils.hasText(columnText) ? columnText : getColumnName();
	}

	public TablePagerColumn setColumnText(final String columnText) {
		this.columnText = columnText;
		return this;
	}

	public String getColumnAlias() {
		return StringUtils.hasText(columnAlias) ? columnAlias : getColumnName();
	}

	public TablePagerColumn setColumnAlias(final String columnAlias) {
		this.columnAlias = columnAlias;
		return this;
	}

	public String getPropertyClass() {
		return propertyClass;
	}

	public TablePagerColumn setPropertyClass(final String propertyClass) {
		this.propertyClass = propertyClass;
		return this;
	}

	public TablePagerColumn setPropertyClass(final Class<?> propertyClass) {
		this.propertyClass = propertyClass.getName();
		return this;
	}

	public int getWidth() {
		return width;
	}

	public TablePagerColumn setWidth(final int width) {
		this.width = width;
		return this;
	}

	public ETextAlign getTextAlign() {
		return textAlign;
	}

	public TablePagerColumn setTextAlign(final ETextAlign textAlign) {
		this.textAlign = textAlign;
		return this;
	}

	public TablePagerColumn center() {
		return setTextAlign(ETextAlign.center);
	}

	public EVerticalAlign getVerticalAlign() {
		return verticalAlign;
	}

	public TablePagerColumn setVerticalAlign(final EVerticalAlign verticalAlign) {
		this.verticalAlign = verticalAlign;
		return this;
	}

	public String getLblStyle() {
		return lblStyle;
	}

	public TablePagerColumn setLblStyle(final String lblStyle) {
		this.lblStyle = lblStyle;
		return this;
	}

	public boolean isResize() {
		return resize;
	}

	public TablePagerColumn setResize(final boolean resize) {
		this.resize = resize;
		return this;
	}

	public boolean isExport() {
		return export;
	}

	public TablePagerColumn setExport(final boolean export) {
		this.export = export;
		return this;
	}

	public boolean isEditable() {
		return editable;
	}

	public TablePagerColumn setEditable(final boolean editable) {
		this.editable = editable;
		return this;
	}

	public boolean isSort() {
		return sort;
	}

	public TablePagerColumn setSort(final boolean sort) {
		this.sort = sort;
		return this;
	}

	public boolean isFilter() {
		return filter;
	}

	public TablePagerColumn setFilter(final boolean filter) {
		this.filter = filter;
		return this;
	}

	public TablePagerColumn setFilterSort(final boolean filterSort) {
		return setFilter(filterSort).setSort(filterSort);
	}

	public String getFilterAdvClick() {
		return filterAdvClick;
	}

	public String getFilterVal(final String val) {
		return val;
	}

	public TablePagerColumn setFilterAdvClick(final String filterAdvClick) {
		this.filterAdvClick = filterAdvClick;
		setEditable(false);
		return this;
	}

	public boolean isVisible() {
		return visible;
	}

	public TablePagerColumn setVisible(final boolean visible) {
		this.visible = visible;
		return this;
	}

	public boolean isNowrap() {
		return nowrap;
	}

	public TablePagerColumn setNowrap(final boolean nowrap) {
		this.nowrap = nowrap;
		return this;
	}

	public boolean isRelative() {
		return relative;
	}

	public TablePagerColumn setRelative(final boolean relative) {
		this.relative = relative;
		return this;
	}

	String toStyle(final PageRequestResponse rRequest, final ETextAlign defaultTextAlign) {
		final HashSet<String> set = new HashSet<>();
		if (defaultTextAlign != null) {
			ETextAlign textAlign = getTextAlign();
			if (textAlign == null) {
				textAlign = defaultTextAlign;
			}
			set.add("text-align:" + textAlign);
		}
		final EVerticalAlign verticalAlign = getVerticalAlign();
		if (verticalAlign != null) {
			set.add("vertical-align:" + verticalAlign);
		}

		final int w = getWidth();
		if (w > 0) {
			set.add("width:" + TablePagerHTML.fixWidth(rRequest, w) + "px");
		}
		if (isNowrap()) {
			set.add("white-space: nowrap");
		}
		if (isRelative()) {
			set.add("position: relative");
		}
		return set.size() > 0 ? StringUtils.join(set, ";") : null;
	}

	String toClassName(final boolean showVerticalLine) {
		String className = "";
		if (!isNowrap()) {
			className = "wrap_text";
		}
		if (showVerticalLine) {
			className += " vline";
		}
		return className;
	}

	public Class<?> propertyClass() {
		final String clazz = getPropertyClass();
		if (StringUtils.hasText(clazz)) {
			try {
				return ClassUtils.forName(clazz);
			} catch (final ClassNotFoundException e) {
			}
		}
		return String.class;
	}

	public String getFormat() {
		return StringUtils.hasText(format) ? format : defaultFormats.get(propertyClass());
	}

	public TablePagerColumn setFormat(final String format) {
		this.format = format;
		return this;
	}

	private Option[] _filterOptions;

	protected Option[] getFilterOptions() {
		if (_filterOptions != null) {
			return _filterOptions;
		}

		final Class<?> pClass = propertyClass();
		if (Enum.class.isAssignableFrom(pClass)) {
			return Option.from((Enum<?>[]) pClass.getEnumConstants());
		}
		return null;
	}

	public TablePagerColumn setFilterOptions(final Option[] filterOptions) {
		this._filterOptions = filterOptions;
		return this;
	}

	public Object stringToObject(final String val) {
		final Class<?> propertyClass = propertyClass();
		final String format = getFormat();
		if (StringUtils.hasText(format)) {
			if (Date.class.isAssignableFrom(propertyClass)) {
				return Convert.toDate(val, format);
			}
		}
		return Convert.convert(val, propertyClass);
	}

	public String objectToString(final Object val) {
		final Class<?> propertyClass = propertyClass();
		final String format = getFormat();
		if (StringUtils.hasText(format)) {
			if (val instanceof Date && Date.class.isAssignableFrom(propertyClass)) {
				return Convert.toDateString((Date) val, format);
			} else if (val instanceof Number && Number.class.isAssignableFrom(propertyClass)) {
				return NumberUtils.format((Number) val, format);
			}
		}
		return Convert.toString(val);
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "title" };
	}

	public static TablePagerColumn DATE(final String columnName, final String columnText) {
		return new TablePagerColumn(columnName, columnText).setWidth(115).center()
				.setPropertyClass(Date.class);
	}

	public static final TablePagerColumn OPE(final int width) {
		return new TablePagerColumn(OPE, $m("OPE")).setWidth(width).center().setNowrap(false)
				.setFilterSort(false).setExport(false).setEditable(false);
	}

	public static final TablePagerColumn ACTION() {
		return new TablePagerColumn(ACTION,
				new LinkElement().setClassName("m2 down_menu_image").toString()).setWidth(22)
						.setFilterSort(false).setExport(false).setEditable(false);
	}

	public static final TablePagerColumn ICON() {
		return new TablePagerColumn(ICON, HtmlConst.NBSP).setWidth(20).setFilterSort(false)
				.setResize(false).center().setExport(false).setEditable(false);
	}

	public static final TablePagerColumn BLANK() {
		return new TablePagerColumn(BLANK, HtmlConst.NBSP).setExport(false).setFilterSort(false)
				.setEditable(false);
	}

	public static final TablePagerColumn DESCRIPTION() {
		return new TablePagerColumn(DESCRIPTION, $m("Description")).setFilterSort(false)
				.setNowrap(false).setTextAlign(ETextAlign.justify);
	}

	public static final TablePagerColumn BOOL(final String columnName, final String columnText) {
		return new TablePagerColumn(columnName, columnText, 40).setPropertyClass(Boolean.class)
				.center().setFilterSort(false);
	}

	public static final TablePagerColumn STATUS(final int width) {
		return STATUS(width, null);
	}

	public static final TablePagerColumn PHOTO(final int width) {
		return new TablePagerColumn("photo", "&nbsp;", width).center().setFilterSort(false)
				.setNowrap(false).setLblStyle("line-height: 1.1;");
	}

	public static final TablePagerColumn STATUS(final int width, final Class<?> propertyClass) {
		final TablePagerColumn col = new TablePagerColumn(STATUS, $m("Status")).setWidth(width)
				.setSort(false).setFilter(false).center();
		if (propertyClass != null) {
			col.setPropertyClass(propertyClass);
		}
		return col;
	}

	public static final TablePagerColumn TOPIC() {
		return new TablePagerColumn(TOPIC, $m("TablePagerColumn.topic")).setNowrap(false)
				.setTextAlign(ETextAlign.justify);
	}

	public static final String OPE = "ope";
	public static final String ACTION = "action";
	public static final String ICON = "icon";
	public static final String BLANK = "blank";
	public static final String DESCRIPTION = "description";
	public static final String STATUS = "status";
	public static final String TOPIC = "topic";

	private static Map<Class<?>, String> defaultFormats;
	static {
		defaultFormats = new HashMap<>();
		defaultFormats.put(Date.class, Convert.defaultDatePattern);
	}

	private static final long serialVersionUID = 1602297615549580124L;
}
