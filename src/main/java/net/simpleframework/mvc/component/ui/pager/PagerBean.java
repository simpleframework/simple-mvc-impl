package net.simpleframework.mvc.component.ui.pager;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.ctx.permission.IPermissionConst;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PagerBean extends AbstractContainerBean {

	/* 定义按钮栏(分页按钮,导出按钮等)所在的位置, 具体含义见EPagerBarLayout */
	private EPagerBarLayout pagerBarLayout = (EPagerBarLayout) BeanDefaults.get(getClass(),
			"pagerBarLayout", EPagerBarLayout.both);

	/* 定义分页组件的标题, 一般显示在左上区域 */
	private String title;

	/* 统计信息, 一般显示在左下区域 */
	private String stat;

	/* 定义一页显示的数据,默认为15 */
	private int pageItems = BeanDefaults.getInt(getClass(), "pageItems", 15);

	/* 定义分页按钮的个数 */
	private int indexPages = BeanDefaults.getInt(getClass(), "indexPages", 9);

	/* 分页组件数据内容的路径 */
	private String dataPath;

	/* 定义没有数据时, 内容区域显示的提示信息, null不提示 */
	private String noResultDesc;

	/* 在按钮栏显示当前页数据显示数的编辑框 */
	private boolean showEditPageItems = BeanDefaults.getBool(getClass(), "showEditPageItems", true);

	/* 定义导出的js，null不显示导出按钮 */
	private String exportAction = BeanDefaults.getString(getClass(), "showEditPageItems", "false");

	/* 定义导出采用的编码 */
	private String exportCharset;

	/* 定义是否显示装载进度提示，一般会在浏览器的右上角加上进度提示条 */
	private boolean showLoading = BeanDefaults.getBool(getClass(), "showLoading", true);

	/* 装载时，使页面模态，即不能点击任何页面内容 */
	private boolean loadingModal = BeanDefaults.getBool(getClass(), "loadingModal", false);

	/* 定义当前分页号、当前页数据大小的参数名称，一般不需要关心这两个属性 */
	private String pageNumberParameterName, pageItemsParameterName;

	/* 定义分页组件的角色 */
	private String role;

	/* 当分页组件被装载完后，触发的前端js事件 */
	private String jsLoadedCallback;

	public PagerBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
		setRole(IPermissionConst.ROLE_ANONYMOUS).setNoResultDesc($m("pager.0"));
	}

	public PagerBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	public String getTitle() {
		return title;
	}

	public PagerBean setTitle(final String title) {
		this.title = title;
		return this;
	}

	public String getStat() {
		return stat;
	}

	public PagerBean setStat(final String stat) {
		this.stat = stat;
		return this;
	}

	public int getPageItems() {
		return getPagerBarLayout() == EPagerBarLayout.none ? Integer.MAX_VALUE : pageItems;
	}

	public PagerBean setPageItems(final int pageItems) {
		this.pageItems = pageItems;
		return this;
	}

	public boolean isShowEditPageItems() {
		return showEditPageItems;
	}

	public PagerBean setShowEditPageItems(final boolean showEditPageItems) {
		this.showEditPageItems = showEditPageItems;
		return this;
	}

	public int getIndexPages() {
		return indexPages;
	}

	public PagerBean setIndexPages(final int indexPages) {
		this.indexPages = indexPages;
		return this;
	}

	public EPagerBarLayout getPagerBarLayout() {
		return pagerBarLayout;
	}

	public PagerBean setPagerBarLayout(final EPagerBarLayout pagerBarLayout) {
		this.pagerBarLayout = pagerBarLayout;
		return this;
	}

	public String getDataPath() {
		return dataPath;
	}

	public PagerBean setDataPath(final String dataPath) {
		this.dataPath = dataPath;
		return this;
	}

	public String getNoResultDesc() {
		return noResultDesc;
	}

	public PagerBean setNoResultDesc(final String noResultDesc) {
		this.noResultDesc = noResultDesc;
		return this;
	}

	public String getJsLoadedCallback() {
		return jsLoadedCallback;
	}

	public PagerBean setJsLoadedCallback(final String jsLoadedCallback) {
		this.jsLoadedCallback = jsLoadedCallback;
		return this;
	}

	public String getRole() {
		return StringUtils.text(role, settings.getDefaultRole());
	}

	public PagerBean setRole(final String role) {
		this.role = role;
		return this;
	}

	public String getExportAction() {
		return exportAction;
	}

	public PagerBean setExportAction(final String exportAction) {
		this.exportAction = exportAction;
		return this;
	}

	public String getExportCharset() {
		return exportCharset;
	}

	public PagerBean setExportCharset(final String exportCharset) {
		this.exportCharset = exportCharset;
		return this;
	}

	static final String PAGE_NUMBER = "pageNumber";

	static final String PAGE_ITEMs = "pageItems";

	public String getPageNumberParameterName() {
		return StringUtils.text(pageNumberParameterName, PAGE_NUMBER);
	}

	public PagerBean setPageNumberParameterName(final String pageNumberParameterName) {
		this.pageNumberParameterName = pageNumberParameterName;
		return this;
	}

	public String getPageItemsParameterName() {
		return StringUtils.text(pageItemsParameterName, PAGE_ITEMs);
	}

	public PagerBean setPageItemsParameterName(final String pageItemsParameterName) {
		this.pageItemsParameterName = pageItemsParameterName;
		return this;
	}

	public boolean isShowLoading() {
		return showLoading;
	}

	public PagerBean setShowLoading(final boolean showLoading) {
		this.showLoading = showLoading;
		return this;
	}

	public boolean isLoadingModal() {
		return loadingModal;
	}

	public PagerBean setLoadingModal(final boolean loadingModal) {
		this.loadingModal = loadingModal;
		return this;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsLoadedCallback" };
	}
}
