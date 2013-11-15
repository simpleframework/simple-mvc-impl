package net.simpleframework.mvc.component.ui.pager;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.ComponentUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TablePagerBean extends PagerBean {

	/* 是否显示多选框，选择多行 */
	private boolean showCheckbox = BeanDefaults.getBool(getClass(), "showCheckbox", true);

	/* 是否显示竖线及边框 */
	private boolean showVerticalLine = BeanDefaults.getBool(getClass(), "showVerticalLine", false);

	/* 是否显示行号 */
	private boolean showLineNo = BeanDefaults.getBool(getClass(), "showLineNo", false);

	private int rowMargin = BeanDefaults.getInt(getClass(), "rowMargin", 0);

	/* 是否可编辑 */
	private boolean editable = BeanDefaults.getBool(getClass(), "editable", false);

	/* 是否显示可编辑的按钮 */
	private boolean showEditableBtn = BeanDefaults.getBool(getClass(), "showEditableBtn", true);

	/* 是否双击可编辑，当editable=true */
	private boolean dblclickEdit = BeanDefaults.getBool(getClass(), "dblclickEdit", true);

	/* 保存编辑数据时的确认信息 */
	private String rowSaveConfirmMessage;

	/* 列头是否自动滚动，使其一直在可见范围 */
	private boolean scrollHead = BeanDefaults.getBool(getClass(), "scrollHead", true);

	/* 是否显示列头 */
	private boolean showHead = BeanDefaults.getBool(getClass(), "showHead", true);

	private int headHeight = BeanDefaults.getInt(getClass(), "headHeight", 0);

	/* 是否显示快捷过滤面板，表格列下面 */
	private boolean showFilterBar = BeanDefaults.getBool(getClass(), "showFilterBar", true);

	/* 是否显示详细信息 */
	private String detailField;

	/* 分组字段 */
	private String groupColumn;

	private String jsRowClick, jsRowDblclick;

	private TablePagerColumns columns;

	public TablePagerBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
		setExportAction(null);
	}

	public TablePagerBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	public TablePagerColumns getColumns() {
		if (columns == null) {
			columns = TablePagerColumns.of();
		}
		return columns;
	}

	public TablePagerBean addColumn(final TablePagerColumn column) {
		getColumns().add(column);
		return this;
	}

	@Override
	public String getDataPath() {
		return ComponentUtils.getResourceHomePath(TablePagerBean.class) + "/jsp/tablepager.jsp";
	}

	public boolean isShowCheckbox() {
		return showCheckbox;
	}

	public TablePagerBean setShowCheckbox(final boolean showCheckbox) {
		this.showCheckbox = showCheckbox;
		return this;
	}

	public boolean isShowVerticalLine() {
		return showVerticalLine;
	}

	public TablePagerBean setShowVerticalLine(final boolean showVerticalLine) {
		this.showVerticalLine = showVerticalLine;
		return this;
	}

	public int getRowMargin() {
		return rowMargin;
	}

	public TablePagerBean setRowMargin(final int rowMargin) {
		this.rowMargin = rowMargin;
		return this;
	}

	public boolean isEditable() {
		return editable;
	}

	public TablePagerBean setEditable(final boolean editable) {
		this.editable = editable;
		return this;
	}

	public boolean isShowEditableBtn() {
		return showEditableBtn;
	}

	public TablePagerBean setShowEditableBtn(final boolean showEditableBtn) {
		this.showEditableBtn = showEditableBtn;
		return this;
	}

	public boolean isDblclickEdit() {
		return dblclickEdit;
	}

	public TablePagerBean setDblclickEdit(final boolean dblclickEdit) {
		this.dblclickEdit = dblclickEdit;
		return this;
	}

	public String getRowSaveConfirmMessage() {
		return rowSaveConfirmMessage;
	}

	public TablePagerBean setRowSaveConfirmMessage(final String rowSaveConfirmMessage) {
		this.rowSaveConfirmMessage = rowSaveConfirmMessage;
		return this;
	}

	public boolean isShowLineNo() {
		return showLineNo;
	}

	public TablePagerBean setShowLineNo(final boolean showLineNo) {
		this.showLineNo = showLineNo;
		return this;
	}

	public boolean isScrollHead() {
		return scrollHead;
	}

	public TablePagerBean setScrollHead(final boolean scrollHead) {
		this.scrollHead = scrollHead;
		return this;
	}

	public boolean isShowHead() {
		return showHead;
	}

	public TablePagerBean setShowHead(final boolean showHead) {
		this.showHead = showHead;
		return this;
	}

	public int getHeadHeight() {
		return headHeight;
	}

	public TablePagerBean setHeadHeight(final int headHeight) {
		this.headHeight = headHeight;
		return this;
	}

	public boolean isShowFilterBar() {
		return showFilterBar;
	}

	public TablePagerBean setShowFilterBar(final boolean showFilterBar) {
		this.showFilterBar = showFilterBar;
		return this;
	}

	public String getDetailField() {
		return detailField;
	}

	public TablePagerBean setDetailField(final String detailField) {
		this.detailField = detailField;
		return this;
	}

	public String getGroupColumn() {
		return groupColumn;
	}

	public TablePagerBean setGroupColumn(final String groupColumn) {
		this.groupColumn = groupColumn;
		return this;
	}

	public String getJsRowClick() {
		return jsRowClick;
	}

	public TablePagerBean setJsRowClick(final String jsRowClick) {
		this.jsRowClick = jsRowClick;
		return this;
	}

	public String getJsRowDblclick() {
		return jsRowDblclick;
	}

	public TablePagerBean setJsRowDblclick(final String jsRowDblclick) {
		this.jsRowDblclick = jsRowDblclick;
		return this;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsRowClick", "jsRowDblclick", "jsLoadedCallback" };
	}
}