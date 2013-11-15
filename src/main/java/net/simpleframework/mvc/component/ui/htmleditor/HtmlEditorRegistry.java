package net.simpleframework.mvc.component.ui.htmleditor;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.ui.syntaxhighlighter.SyntaxHighlighterBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(HtmlEditorRegistry.HTMLEDITOR)
@ComponentBean(HtmlEditorBean.class)
@ComponentRender(HtmlEditorRender.class)
@ComponentResourceProvider(HtmlEditorResourceProvider.class)
public class HtmlEditorRegistry extends AbstractComponentRegistry {
	public static final String HTMLEDITOR = "htmlEditor";

	@Override
	public HtmlEditorBean createComponentBean(final PageParameter pp, final Object attriData) {
		final HtmlEditorBean htmlEditor = (HtmlEditorBean) super.createComponentBean(pp, attriData);
		final ComponentParameter nCP = ComponentParameter.get(pp, htmlEditor);

		// 添加语法高亮组件
		if ((Boolean) nCP.getBeanProperty("codeEnabled")) {
			pp.addComponentBean("sh_" + nCP.hashId(), SyntaxHighlighterBean.class);
		}
		return htmlEditor;
	}
}
