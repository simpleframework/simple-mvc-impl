package net.simpleframework.mvc.common.element;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.common.element.ElementList.IElementFilter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("unchecked")
public abstract class AbstractButtonEx<T extends AbstractLinkElement<T>>
		extends AbstractLinkElement<T> {
	/* 是否选中 */
	private boolean checked;

	private boolean highlight;

	/* 按钮的图标 */
	private String iconClass;

	/* 是否分割类型的按钮 */
	private boolean separator;
	/* 是否显示下拉菜单 */
	private boolean menuIcon;

	private String onmenuclick;

	private boolean leftCorner, rightCorner;

	public AbstractButtonEx() {
	}

	public AbstractButtonEx(final Object text) {
		setText(text);
	}

	public boolean isChecked() {
		return checked;
	}

	public T setChecked(final boolean checked) {
		this.checked = checked;
		return (T) this;
	}

	public boolean isHighlight() {
		return highlight;
	}

	public T setHighlight(final boolean highlight) {
		this.highlight = highlight;
		return (T) this;
	}

	public boolean isSeparator() {
		return separator;
	}

	public T setSeparator(final boolean separator) {
		this.separator = separator;
		return (T) this;
	}

	public String getIconClass() {
		return iconClass;
	}

	public T setIconClass(final String iconClass) {
		this.iconClass = iconClass;
		return (T) this;
	}

	public boolean isMenuIcon() {
		return menuIcon;
	}

	public T setMenuIcon(final boolean menuIcon) {
		this.menuIcon = menuIcon;
		return (T) this;
	}

	public String getOnmenuclick() {
		return onmenuclick;
	}

	public T setOnmenuclick(final String onmenuclick) {
		this.onmenuclick = onmenuclick;
		return (T) this;
	}

	public boolean isLeftCorner() {
		return leftCorner;
	}

	public T setLeftCorner(final boolean leftCorner) {
		this.leftCorner = leftCorner;
		return (T) this;
	}

	public boolean isRightCorner() {
		return rightCorner;
	}

	public T setRightCorner(final boolean rightCorner) {
		this.rightCorner = rightCorner;
		return (T) this;
	}

	public T corner() {
		setLeftCorner(true);
		return setRightCorner(true);
	}

	@Override
	public String getText() {
		final StringBuilder sb = new StringBuilder();
		final String iconClass = getIconClass();
		if (StringUtils.hasText(iconClass)) {
			sb.append(getIcon(iconClass));
		}
		sb.append(super.getText());
		if (isMenuIcon()) {
			sb.append(getDownMenu());
		}
		return sb.toString();
	}

	protected AbstractElement<?> getIcon(final String iconClass) {
		return new SpanElement().setClassName("left_icon " + iconClass);
	}

	protected AbstractElement<?> getDownMenu() {
		final SpanElement span = new SpanElement().setClassName("right_down_menu");
		final String menuclick = getOnmenuclick();
		if (StringUtils.hasText(menuclick)) {
			String onclick = menuclick;
			if (!menuclick.endsWith(";")) {
				onclick += ";";
			}
			onclick += "Event.stop(document.getEvent());";
			span.setOnclick(onclick);
		}
		return span;
	}

	@Override
	public String toString() {
		if (isSeparator()) {
			return SpanElement.SPACE.toString();
		}
		if (isHighlight()) {
			addClassName("highlight");
		}
		doDisabled();
		doChecked();
		doMenuIcon();
		return super.toString();
	}

	protected void doDisabled() {
		if (isDisabled()) {
			addClassName("disabled_color");
		}
	}

	protected void doChecked() {
		if (isChecked()) {
			addClassName("simple_btn_checked");
		}
	}

	protected void doMenuIcon() {
		if (isMenuIcon()) {
			addStyle("padding-right: 7px;");
		}
	}

	static {
		ElementList.addFilter(new IElementFilter() {
			@Override
			public void doFilter(final AbstractElement<?> ele, final ElementList el, final int index) {
				if (ele instanceof AbstractButtonEx<?>) {
					final AbstractButtonEx<?> btn = (AbstractButtonEx<?>) ele;
					btn.setLeftCorner(true);
					btn.setRightCorner(true);
					AbstractElement<?> pre;
					if (index > 0 && (pre = el.get(index - 1)) instanceof AbstractButtonEx<?>) {
						final AbstractButtonEx<?> btn2 = (AbstractButtonEx<?>) pre;
						if (!btn2.isSeparator()) {
							btn2.setRightCorner(false);
							btn.setLeftCorner(false);
						}
					}
				}
			}
		});
	}
}
