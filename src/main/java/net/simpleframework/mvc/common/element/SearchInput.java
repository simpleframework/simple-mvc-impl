package net.simpleframework.mvc.common.element;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SearchInput extends AbstractInputElement<SearchInput> {

	/* 查找 */
	private String onSearchClick;

	/* 高级选项 */
	private String onAdvClick;

	/* 搜索显示文本 */
	private String searchText;

	public SearchInput() {
	}

	public SearchInput(final Object id) {
		setId(Convert.toString(id));
	}

	@Override
	public EInputType getInputType() {
		return EInputType.text;
	}

	public String getOnSearchClick() {
		return onSearchClick;
	}

	public SearchInput setOnSearchClick(final String onSearchClick) {
		this.onSearchClick = onSearchClick;
		return this;
	}

	public String getOnAdvClick() {
		return onAdvClick;
	}

	public SearchInput setOnAdvClick(final String onAdvClick) {
		this.onAdvClick = onAdvClick;
		return this;
	}

	@Override
	public String getPlaceholder() {
		return StringUtils.text(super.getPlaceholder(), $m("SearchInput.0"));
	}

	public String getSearchText() {
		return StringUtils.text(searchText, $m("Button.Search"));
	}

	public SearchInput setSearchText(final String searchText) {
		this.searchText = searchText;
		return this;
	}

	@Override
	public String toString() {
		final SpanElement br = new SpanElement(getSearchText()).setClassName("br");
		final SpanElement ar = new SpanElement().setClassName("ar");
		if (!isDisabled()) {
			final String onSearchClick = getOnSearchClick();
			br.setOnclick(onSearchClick);
			ar.setOnclick(getOnAdvClick());
			if (StringUtils.hasText(onSearchClick)) {
				addAttribute("onkeypress",
						"if (((event.which) ? event.which : event.keyCode) != Event.KEY_RETURN) {return;}"
								+ onSearchClick);
			}
		}

		final SpanElement btn = new SpanElement().setClassName("btn").addElements(br);
		if (StringUtils.hasText(ar.getOnclick())) {
			btn.addElements(ar);
		}

		final StringBuilder sb = new StringBuilder();
		sb.append("<span class='sech_pane'>");
		sb.append(super.toString());
		sb.append(btn);
		sb.append("</span>");
		return sb.toString();
	}
}
