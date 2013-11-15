package net.simpleframework.mvc.component.ui.syntaxhighlighter;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.window.WindowBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(SyntaxHighlighterRegistry.SYNTAXHIGHLIGHTER)
@ComponentBean(SyntaxHighlighterBean.class)
@ComponentRender(SyntaxHighlighterRender.class)
@ComponentResourceProvider(SyntaxHighlighterResourceProvider.class)
public class SyntaxHighlighterRegistry extends AbstractComponentRegistry {
	public static final String SYNTAXHIGHLIGHTER = "syntaxHighlighter";

	@Override
	public SyntaxHighlighterBean createComponentBean(final PageParameter pp, final Object attriData) {
		final SyntaxHighlighterBean syntaxHighlighter = (SyntaxHighlighterBean) super
				.createComponentBean(pp, attriData);

		final String beanId = syntaxHighlighter.hashId();
		final String ajaxRequest = "ajaxRequest_" + beanId;
		pp.addComponentBean(ajaxRequest, AjaxRequestBean.class).setUrlForward(
				getComponentResourceProvider().getResourceHomePath() + "/jsp/sh_window.jsp?"
						+ SyntaxHighlighterUtils.BEAN_ID + "=" + beanId);
		pp.addComponentBean("window_" + beanId, WindowBean.class).setContentRef(ajaxRequest)
				.setTitle($m("SyntaxHighlighterRegistry.0")).setPopup(true).setHeight(380)
				.setWidth(500);
		return syntaxHighlighter;
	}
}
