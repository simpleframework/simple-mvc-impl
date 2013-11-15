package net.simpleframework.mvc.component.ui.tree;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TreeBean extends AbstractContainerBean {

	/* 是否动态装载树数据 */
	private boolean dynamicLoading = BeanDefaults.getBool(getClass(), "dynamicLoading", false);

	/* 定义节点图标的路径 */
	private String imgHome;

	/* 节点上的关联菜单 */
	private String contextMenu;

	private boolean checkboxes = BeanDefaults.getBool(getClass(), "checkboxes", false);

	private boolean checkboxesThreeState = BeanDefaults.getBool(getClass(), "checkboxesThreeState",
			false);

	/* 是否使用cookies */
	private boolean cookies = BeanDefaults.getBool(getClass(), "cookies", true);

	/* check是否使用cookies */
	private boolean checkedCookies = BeanDefaults.getBool(getClass(), "checkedCookies", false);

	private boolean selectCTRL = BeanDefaults.getBool(getClass(), "selectCTRL", true);

	private boolean selectSHIFT = BeanDefaults.getBool(getClass(), "selectSHIFT", true);

	/* 设置线的形状 */
	private ETreeLineStyle lineStyle = (ETreeLineStyle) BeanDefaults.get(getClass(), "lineStyle",
			ETreeLineStyle.line);

	/* 是否显示节点的tip */
	private boolean showTip = BeanDefaults.getBool(getClass(), "showTip", true);

	// event
	private String jsLoadedCallback;

	private String jsCheckCallback;

	private String jsClickCallback, jsDblclickCallback;

	/* 定义拖动的角色 */
	private String roleDrop;

	public TreeBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
	}

	public TreeBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	public boolean isDynamicLoading() {
		return dynamicLoading;
	}

	public TreeBean setDynamicLoading(final boolean dynamicLoading) {
		this.dynamicLoading = dynamicLoading;
		return this;
	}

	public String getImgHome() {
		return imgHome;
	}

	public TreeBean setImgHome(final String imgHome) {
		this.imgHome = imgHome;
		return this;
	}

	public String getContextMenu() {
		return contextMenu;
	}

	public TreeBean setContextMenu(final String contextMenu) {
		this.contextMenu = contextMenu;
		return this;
	}

	public ETreeLineStyle getLineStyle() {
		return lineStyle;
	}

	public TreeBean setLineStyle(final ETreeLineStyle lineStyle) {
		this.lineStyle = lineStyle;
		return this;
	}

	public boolean isShowTip() {
		return showTip;
	}

	public TreeBean setShowTip(final boolean showTip) {
		this.showTip = showTip;
		return this;
	}

	public boolean isCheckboxes() {
		return checkboxes;
	}

	public TreeBean setCheckboxes(final boolean checkboxes) {
		this.checkboxes = checkboxes;
		return this;
	}

	public boolean isCheckboxesThreeState() {
		return checkboxesThreeState;
	}

	public TreeBean setCheckboxesThreeState(final boolean checkboxesThreeState) {
		this.checkboxesThreeState = checkboxesThreeState;
		return this;
	}

	public boolean isCookies() {
		return cookies;
	}

	public TreeBean setCookies(final boolean cookies) {
		this.cookies = cookies;
		return this;
	}

	public boolean isCheckedCookies() {
		return checkedCookies;
	}

	public TreeBean setCheckedCookies(final boolean checkedCookies) {
		this.checkedCookies = checkedCookies;
		return this;
	}

	public boolean isSelectCTRL() {
		return selectCTRL;
	}

	public TreeBean setSelectCTRL(final boolean selectCTRL) {
		this.selectCTRL = selectCTRL;
		return this;
	}

	public boolean isSelectSHIFT() {
		return selectSHIFT;
	}

	public TreeBean setSelectSHIFT(final boolean selectSHIFT) {
		this.selectSHIFT = selectSHIFT;
		return this;
	}

	public String getJsLoadedCallback() {
		return jsLoadedCallback;
	}

	public TreeBean setJsLoadedCallback(final String jsLoadedCallback) {
		this.jsLoadedCallback = jsLoadedCallback;
		return this;
	}

	public String getJsCheckCallback() {
		return jsCheckCallback;
	}

	public TreeBean setJsCheckCallback(final String jsCheckCallback) {
		this.jsCheckCallback = jsCheckCallback;
		return this;
	}

	public String getJsClickCallback() {
		return jsClickCallback;
	}

	public TreeBean setJsClickCallback(final String jsClickCallback) {
		this.jsClickCallback = jsClickCallback;
		return this;
	}

	public String getJsDblclickCallback() {
		return jsDblclickCallback;
	}

	public TreeBean setJsDblclickCallback(final String jsDblclickCallback) {
		this.jsDblclickCallback = jsDblclickCallback;
		return this;
	}

	public String getRoleDrop() {
		return roleDrop;
	}

	public TreeBean setRoleDrop(final String roleDrop) {
		this.roleDrop = roleDrop;
		return this;
	}

	private TreeNodes treeNodes;

	public TreeNodes getTreeNodes() {
		if (treeNodes == null) {
			treeNodes = TreeNodes.of();
		}
		return treeNodes;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsLoadedCallback", "jsClickCallback", "jsDblclickCallback",
				"jsCheckCallback" };
	}
}
