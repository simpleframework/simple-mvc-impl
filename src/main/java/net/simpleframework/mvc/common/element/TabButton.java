package net.simpleframework.mvc.common.element;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TabButton extends AbstractButtonEx<TabButton> {

	/* 一般放入统计信息 */
	private Object stat;

	/* 匹配的方法 */
	private ETabMatch tabMatch;

	/* 匹配的方法 */
	private int tabIndex;

	/* 当ETabMatch.url时，实际匹配的url */
	private String matchUrl;

	private String tooltip;

	public TabButton() {
	}

	public TabButton(final Object text, final String href) {
		super(text);
		setHref(href);
	}

	public TabButton(final Object text) {
		super(text);
	}

	public ETabMatch getTabMatch() {
		return tabMatch == null ? ETabMatch.url : tabMatch;
	}

	public TabButton setTabMatch(final ETabMatch tabMatch) {
		this.tabMatch = tabMatch;
		return this;
	}

	public Object getStat() {
		return stat;
	}

	public TabButton setStat(final Object stat) {
		this.stat = stat;
		return this;
	}

	public String getTooltip() {
		return tooltip;
	}

	public TabButton setTooltip(final String tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public TabButton setTabIndex(final int tabIndex) {
		this.tabIndex = tabIndex;
		setTabMatch(ETabMatch.tabIndex);
		return this;
	}

	public String getMatchUrl() {
		return matchUrl;
	}

	public TabButton setMatchUrl(final String matchUrl) {
		this.matchUrl = matchUrl;
		return this;
	}

	@Override
	protected void doMenuIcon() {
	}

	@Override
	protected AbstractElement<?> getDownMenu() {
		return new SpanElement().setClassName("tmenu").setId("m" + getId());
	}
}
