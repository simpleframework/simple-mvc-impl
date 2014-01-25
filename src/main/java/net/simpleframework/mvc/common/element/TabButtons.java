package net.simpleframework.mvc.common.element;

import java.util.Map;
import java.util.UUID;

import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.AbstractArrayListEx;
import net.simpleframework.common.object.ObjectUtils;
import net.simpleframework.common.web.HttpUtils;
import net.simpleframework.common.web.html.HtmlConst;
import net.simpleframework.mvc.IMVCConst;
import net.simpleframework.mvc.PageRequestResponse;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TabButtons extends AbstractArrayListEx<TabButtons, TabButton> {
	public static TabButtons of(final TabButton... btns) {
		return of(0, btns);
	}

	public static TabButtons of(final int defaultIndex, final TabButton... btns) {
		return new TabButtons(defaultIndex).append(btns);
	}

	private int selectedIndex;

	private String tabsClass;

	private ETextAlign textAlign;

	private ElementList topElements;

	private boolean vertical;

	private TabButtons(final int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public TabButtons setSelectedIndex(final int selectedIndex) {
		this.selectedIndex = selectedIndex;
		return this;
	}

	public String getTabsClass() {
		return StringUtils.text(tabsClass, "simple_tabs");
	}

	public TabButtons setTabsClass(final String tabsClass) {
		this.tabsClass = tabsClass;
		return this;
	}

	public ETextAlign getTextAlign() {
		return textAlign;
	}

	public TabButtons setTextAlign(final ETextAlign textAlign) {
		this.textAlign = textAlign;
		return this;
	}

	public boolean isVertical() {
		return vertical;
	}

	public TabButtons setVertical(final boolean vertical) {
		this.vertical = vertical;
		return this;
	}

	public ElementList getTopElements() {
		return topElements;
	}

	public TabButtons setTopElements(final ElementList topElements) {
		this.topElements = topElements;
		return this;
	}

	public String toString(final PageRequestResponse rRequest) {
		final StringBuilder sb = new StringBuilder();
		final String id = UUID.randomUUID().toString();
		sb.append("<ul class=\"").append(getTabsClass());
		if (isVertical()) {
			sb.append(" vertical");
		}
		sb.append("\" id=\"").append(id).append("\">");

		final String requestURI = rRequest.stripContextPath(rRequest.stripJSessionId(rRequest
				.getRequestURI()));
		final int size = size();
		if (selectedIndex == 0) {
			TabButton _tab = null;
			for (int i = 0; i < size; i++) {
				final TabButton tab = get(i);
				if (match(rRequest, tab, requestURI)) {
					// ordinal作为匹配的优先级
					if (_tab == null || _tab.getTabMatch().ordinal() > tab.getTabMatch().ordinal()) {
						selectedIndex = i;
						_tab = tab;
					}
				}
			}
		}

		boolean isShowTip = false;
		for (int i = 0; i < size; i++) {
			final TabButton tab = get(i);
			tab.setClassName(i == selectedIndex ? "active" : null);
			sb.append("<li>").append(tab);
			final String tooltip = tab.getTooltip();
			if (StringUtils.hasText(tooltip)) {
				isShowTip = true;
				sb.append(BlockElement.tip(tooltip).setClassName("tt"));
			}
			final Object stat = tab.getStat();
			if (stat != null) {
				sb.append(new SupElement(stat).setHighlight(true));
			}
			sb.append("</li>");
		}
		sb.append("</ul>");
		if (isShowTip) {
			sb.append(HtmlConst.TAG_SCRIPT_START);
			sb.append("$UI.doTabButtonsTooltip('#").append(id).append(" li');");
			sb.append(HtmlConst.TAG_SCRIPT_END);
		}
		return sb.toString();
	}

	private boolean match(final PageRequestResponse rRequest, final TabButton tab,
			final String requestURI) {
		String href = rRequest.stripContextPath(tab.getHref());
		Map<String, Object> params = null;
		int pos;
		if ((pos = href.indexOf("?")) > -1) {
			params = HttpUtils.toQueryParams(href.substring(pos + 1));
			href = href.substring(0, pos);
		}

		final ETabMatch tabMatch = tab.getTabMatch();
		if (tabMatch == ETabMatch.tabIndex) {
			return tab.getTabIndex() == rRequest.getIntParameter("tab");
		} else if (tabMatch == ETabMatch.params) {
			if (params != null) {
				// 如果有不相等的参数，则认为是不能匹配
				for (final String key : params.keySet()) {
					if (!ObjectUtils.objectEquals(Convert.toString(params.get(key)),
							rRequest.getParameter(key))) {
						return false;
					}
				}
			}
		} else if (tabMatch == ETabMatch.url) {
			boolean match = requestURI.endsWith(href);
			if (!match) {
				String referer = rRequest.getParameter(IMVCConst.PARAM_REFERER);
				if (StringUtils.hasText(referer)) {
					referer = rRequest.stripContextPath(rRequest.stripJSessionId(referer));
					final int p = referer.indexOf('?');
					if (p > 0) {
						referer = referer.substring(0, p);
					}
					match = referer.endsWith(href);
				}
			}
			return match;
		}
		return true;
	}

	private static final long serialVersionUID = -8125512004759930526L;
}
