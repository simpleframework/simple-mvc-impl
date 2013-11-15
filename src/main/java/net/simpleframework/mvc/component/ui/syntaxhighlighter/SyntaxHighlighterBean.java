package net.simpleframework.mvc.component.ui.syntaxhighlighter;

import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.AbstractComponentBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class SyntaxHighlighterBean extends AbstractComponentBean {
	private ESyntaxHighlighterTheme shTheme;

	private String jsSelectedCallback; // editor

	public SyntaxHighlighterBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
		setHandleClass(DefaultSyntaxHighlighterHandler.class);
	}

	public SyntaxHighlighterBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	public ESyntaxHighlighterTheme getShTheme() {
		return shTheme == null ? ESyntaxHighlighterTheme.shThemeEclipse : shTheme;
	}

	public void setShTheme(final ESyntaxHighlighterTheme shTheme) {
		this.shTheme = shTheme;
	}

	public String getJsSelectedCallback() {
		return jsSelectedCallback;
	}

	public void setJsSelectedCallback(final String jsSelectedCallback) {
		this.jsSelectedCallback = jsSelectedCallback;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsSelectedCallback" };
	}
}
