package net.simpleframework.mvc.component.ui.window;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.AbstractComponentBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class WindowBean extends AbstractComponentBean {
	/* 窗口的基本尺寸属性 */
	private int top = BeanDefaults.getInt(getClass(), "top", 0);
	private int left = BeanDefaults.getInt(getClass(), "left", 0);
	private int width = BeanDefaults.getInt(getClass(), "width", 400);
	private int height = BeanDefaults.getInt(getClass(), "height", 300);
	private int minWidth = BeanDefaults.getInt(getClass(), "minWidth", 200);
	private int minHeight = BeanDefaults.getInt(getClass(), "minHeight", 100);
	private int maxWidth = BeanDefaults.getInt(getClass(), "maxWidth", 0);
	private int maxHeight = BeanDefaults.getInt(getClass(), "maxHeight", 0);
	private int xdelta = BeanDefaults.getInt(getClass(), "xdelta", 0);
	private int ydelta = BeanDefaults.getInt(getClass(), "ydelta", 0);

	private boolean resizable = BeanDefaults.getBool(getClass(), "resizable", true);
	private boolean draggable = BeanDefaults.getBool(getClass(), "draggable", true);
	private boolean modal = BeanDefaults.getBool(getClass(), "modal", true);
	private boolean minimize = BeanDefaults.getBool(getClass(), "minimize", false);
	private boolean maximize = BeanDefaults.getBool(getClass(), "maximize", false);

	/* 关闭窗口时,是否从DOM中移出,否则为隐藏 */
	private boolean destroyOnClose = BeanDefaults.getBool(getClass(), "destroyOnClose", true);

	/* 是否显示在屏幕中央 */
	private boolean showCenter = BeanDefaults.getBool(getClass(), "showCenter", true);

	/* 是否弹出式窗口 */
	private boolean popup = BeanDefaults.getBool(getClass(), "popup", false);

	/* 窗口内容 */
	private String content;

	/* 窗口内容邦定某一组件(ajax, tab等),该属性为引用组件的名称 */
	private String contentRef;

	/* 窗口内容的url, frame内展示. 优先级依次: url->content->contentRef */
	private String url;

	/* 窗口的标题 */
	private String title;

	/* 是否单个窗口实例 */
	private boolean singleWindow = BeanDefaults.getBool(getClass(), "singleWindow", true);

	/* 客户端窗口显示、隐藏后的js事件 */
	private String jsShownCallback, jsHiddenCallback;

	public WindowBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
		setRunImmediately(false);
	}

	public WindowBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	public String getContentRef() {
		return contentRef;
	}

	public WindowBean setContentRef(final String contentRef) {
		this.contentRef = contentRef;
		return this;
	}

	public boolean isPopup() {
		return popup;
	}

	public WindowBean setPopup(final boolean popup) {
		this.popup = popup;
		return this;
	}

	public int getTop() {
		return top;
	}

	public WindowBean setTop(final int top) {
		this.top = top;
		return this;
	}

	public int getLeft() {
		return left;
	}

	public WindowBean setLeft(final int left) {
		this.left = left;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public WindowBean setWidth(final int width) {
		this.width = width;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public WindowBean setHeight(final int height) {
		this.height = height;
		return this;
	}

	public int getMinWidth() {
		return minWidth;
	}

	public WindowBean setMinWidth(final int minWidth) {
		this.minWidth = minWidth;
		return this;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public WindowBean setMinHeight(final int minHeight) {
		this.minHeight = minHeight;
		return this;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public WindowBean setMaxWidth(final int maxWidth) {
		this.maxWidth = maxWidth;
		return this;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public WindowBean setMaxHeight(final int maxHeight) {
		this.maxHeight = maxHeight;
		return this;
	}

	public int getXdelta() {
		return xdelta;
	}

	public WindowBean setXdelta(final int xdelta) {
		this.xdelta = xdelta;
		return this;
	}

	public int getYdelta() {
		return ydelta;
	}

	public WindowBean setYdelta(final int ydelta) {
		this.ydelta = ydelta;
		return this;
	}

	public boolean isResizable() {
		return resizable;
	}

	public WindowBean setResizable(final boolean resizable) {
		this.resizable = resizable;
		return this;
	}

	public boolean isDraggable() {
		return draggable;
	}

	public WindowBean setDraggable(final boolean draggable) {
		this.draggable = draggable;
		return this;
	}

	public boolean isDestroyOnClose() {
		return destroyOnClose;
	}

	public WindowBean setDestroyOnClose(final boolean destroyOnClose) {
		this.destroyOnClose = destroyOnClose;
		return this;
	}

	public boolean isMinimize() {
		return minimize;
	}

	public WindowBean setMinimize(final boolean minimize) {
		this.minimize = minimize;
		return this;
	}

	public boolean isMaximize() {
		return maximize;
	}

	public WindowBean setMaximize(final boolean maximize) {
		this.maximize = maximize;
		return this;
	}

	public boolean isModal() {
		return modal;
	}

	public WindowBean setModal(final boolean modal) {
		this.modal = modal;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public WindowBean setUrl(final String url) {
		this.url = url;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public WindowBean setTitle(final String title) {
		this.title = title;
		return this;
	}

	public String getContent() {
		return content;
	}

	public WindowBean setContent(final String content) {
		this.content = content;
		return this;
	}

	public boolean isShowCenter() {
		return showCenter;
	}

	public WindowBean setShowCenter(final boolean showCenter) {
		this.showCenter = showCenter;
		return this;
	}

	public boolean isSingleWindow() {
		return singleWindow;
	}

	public WindowBean setSingleWindow(final boolean singleWindow) {
		this.singleWindow = singleWindow;
		return this;
	}

	public String getJsShownCallback() {
		return jsShownCallback;
	}

	public WindowBean setJsShownCallback(final String jsShownCallback) {
		this.jsShownCallback = jsShownCallback;
		return this;
	}

	public String getJsHiddenCallback() {
		return jsHiddenCallback;
	}

	public WindowBean setJsHiddenCallback(final String jsHiddenCallback) {
		this.jsHiddenCallback = jsHiddenCallback;
		return this;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsShownCallback", "jsHiddenCallback", "content" };
	}
}
