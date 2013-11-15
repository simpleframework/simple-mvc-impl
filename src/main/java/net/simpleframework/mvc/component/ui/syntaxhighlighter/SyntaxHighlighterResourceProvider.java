package net.simpleframework.mvc.component.ui.syntaxhighlighter;

import java.util.LinkedHashSet;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.IComponentRegistry;
import net.simpleframework.mvc.component.IComponentResourceProvider.AbstractComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class SyntaxHighlighterResourceProvider extends AbstractComponentResourceProvider {

	public SyntaxHighlighterResourceProvider(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String[] getCssPath(final PageParameter pp) {
		final LinkedHashSet<String> l = new LinkedHashSet<String>();
		final String rPath = getCssResourceHomePath(pp);
		l.add(rPath + "/shCore.css");
		for (final AbstractComponentBean componentBean : pp.getComponentBeans().values()) {
			if (componentBean instanceof SyntaxHighlighterBean) {
				l.add(rPath + "/" + ((SyntaxHighlighterBean) componentBean).getShTheme() + ".css");
				break;
			}
		}
		return l.toArray(new String[l.size()]);
	}

	@Override
	public String[] getJavascriptPath(final PageParameter pp) {
		final String rPath = getResourceHomePath();
		return new String[] { rPath + "/js/xregexp.js", rPath + "/js/shCore.js",
				rPath + "/js/shAutoloader.js" };
	}
}
