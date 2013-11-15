package net.simpleframework.mvc.component.ui.syntaxhighlighter;

import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.IComponentRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class SyntaxHighlighterRender extends ComponentJavascriptRender {

	public SyntaxHighlighterRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		final StringBuilder sb = new StringBuilder();
		final String hp = ComponentUtils.getResourceHomePath(SyntaxHighlighterBean.class) + "/js/";
		sb.append("SyntaxHighlighter.autoloader(");
		sb.append("['cpp', 'c++', 'c', '").append(hp).append("shBrushCpp.js'],");
		sb.append("['c#', 'c-sharp', 'csharp', '").append(hp).append("shBrushCSharp.js'],");
		sb.append("['css', '").append(hp).append("shBrushCss.js'],");
		sb.append("['groovy', '").append(hp).append("shBrushGroovy.js'],");
		sb.append("['java', '").append(hp).append("shBrushJava.js'],");
		sb.append("['js', 'javascript', '").append(hp).append("shBrushJScript.js'],");
		sb.append("['php', '").append(hp).append("shBrushPhp.js'],");
		sb.append("['py', 'python', '").append(hp).append("shBrushPython.js'],");
		sb.append("['ruby', 'rails', 'ror', 'rb', '").append(hp).append("shBrushRuby.js'],");
		sb.append("['sql', '").append(hp).append("shBrushSql.js'],");
		sb.append("['xml', 'xhtml', 'xslt', 'html', '").append(hp).append("shBrushXml.js']");
		sb.append(");");
		sb.append("SyntaxHighlighter.all({");
		sb.append("'toolbar' : false,");
		sb.append("'tab-size' : 2");
		sb.append("});");
		// loadScript
		final StringBuilder sb2 = new StringBuilder();
		sb.append(actionFunc).append(".showEditor = function(data) { ");
		sb.append("var win = $Actions['window_").append(cp.hashId()).append("'];");
		sb.append(" win._sh_data = data; win();");
		sb.append("};");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString(), sb2.toString());
	}
}
