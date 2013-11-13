package net.simpleframework.mvc.component.ui.menu;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.I18n;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.html.HtmlConst;
import net.simpleframework.ctx.common.xml.AbstractElementBean;
import net.simpleframework.ctx.common.xml.XmlElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MenuItem extends AbstractElementBean {

	private final MenuItem parent;

	private MenuItems children;

	private String ref;

	private String title;

	private String url;

	private String iconClass;

	private boolean disabled;

	private String onclick, oncheck;

	private boolean checkbox, checked;

	private String description;

	public MenuItem(final XmlElement xmlElement, final MenuItem parent) {
		super(xmlElement);
		this.parent = parent;
	}

	public MenuItem() {
		this(null);
	}

	public MenuItem(final MenuItem parent) {
		this(null, parent);
	}

	public MenuItems children() {
		if (children == null) {
			children = MenuItems.of();
		}
		return children;
	}

	public MenuItem addChild(final MenuItem child) {
		children().add(child);
		return this;
	}

	public MenuItem getParent() {
		return parent;
	}

	public String getRef() {
		return ref;
	}

	public MenuItem setRef(final String ref) {
		this.ref = ref;
		return this;
	}

	public String getTitle() {
		if (!StringUtils.hasText(title)) {
			return HtmlConst.NBSP;
		}
		return I18n.replaceI18n(title);
	}

	public MenuItem setTitle(final String title) {
		this.title = title;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public MenuItem setUrl(final String url) {
		this.url = url;
		return this;
	}

	public String getIconClass() {
		return iconClass;
	}

	public MenuItem setIconClass(final String iconClass) {
		this.iconClass = iconClass;
		return this;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public MenuItem setDisabled(final boolean disabled) {
		this.disabled = disabled;
		return this;
	}

	public String getOnclick() {
		return onclick;
	}

	public MenuItem setOnclick(final String onclick) {
		this.onclick = onclick;
		return this;
	}

	public String getOncheck() {
		return oncheck;
	}

	public MenuItem setOncheck(final String oncheck) {
		this.oncheck = oncheck;
		return this;
	}

	public boolean isCheckbox() {
		return checkbox;
	}

	public MenuItem setCheckbox(final boolean checkbox) {
		this.checkbox = checkbox;
		return this;
	}

	public boolean isChecked() {
		return checked;
	}

	public MenuItem setChecked(final boolean checked) {
		this.checked = checked;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public MenuItem setDescription(final String description) {
		this.description = description;
		return this;
	}

	public MenuItem setOnclick_act(final String act, final String idKey) {
		return setOnclick_act(act, idKey, null);
	}

	public MenuItem setOnclick_act(final String act, final String rowId, final String params) {
		final StringBuilder sb = new StringBuilder();
		sb.append("$Actions['").append(act).append("']('");
		if (StringUtils.hasText(params)) {
			sb.append(params).append("&");
		}
		sb.append(rowId).append("=' + $pager_action(item).rowId());");
		return setOnclick(sb.toString());
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "onclick", "oncheck", "description" };
	}

	public static MenuItem of(final String title) {
		return of(title, null);
	}

	public static MenuItem of(final String title, final String iconClass) {
		return of(title, iconClass, null);
	}

	public static MenuItem of(final String title, final String iconClass,
			final String jsSelectCallback) {
		return new MenuItem().setTitle(title).setIconClass(iconClass).setOnclick(jsSelectCallback);
	}

	public static MenuItem sep() {
		return new MenuItem().setTitle("-");
	}

	public static MenuItem itemAdd() {
		return new MenuItem().setTitle($m("Add")).setIconClass(ICON_ADD);
	}

	public static MenuItem itemEdit() {
		return new MenuItem().setTitle($m("Edit")).setIconClass(ICON_EDIT);
	}

	public static MenuItem itemDelete() {
		return new MenuItem().setTitle($m("Delete")).setIconClass(ICON_DELETE);
	}

	public static MenuItem itemLog() {
		return new MenuItem().setTitle($m("Button.Log")).setIconClass(ICON_LOG);
	}

	public static final String ICON_ADD = "menu_icon_add";
	public static final String ICON_EDIT = "menu_icon_edit";
	public static final String ICON_DELETE = "menu_icon_delete";
	public static final String ICON_REFRESH = "menu_icon_refresh";
	public static final String ICON_EXPAND = "menu_icon_expand";
	public static final String ICON_COLLAPSE = "menu_icon_collapse";

	public static final String ICON_UP = "menu_icon_up";
	public static final String ICON_UP2 = "menu_icon_up2";
	public static final String ICON_DOWN = "menu_icon_down";
	public static final String ICON_DOWN2 = "menu_icon_down2";

	public static final String ICON_LOG = "menu_icon_log";
}
