package net.simpleframework.mvc.component.ui.autocomplete;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.IComponentResourceProvider.AbstractComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class AutocompleteResourceProvider extends AbstractComponentResourceProvider {

	@Override
	public String[] getCssPath(final PageParameter pp) {
		return new String[] { getCssResourceHomePath(pp) + "/autocomplete.css" };
	}

	@Override
	public String[] getJavascriptPath(final PageParameter pp) {
		final String rPath = getResourceHomePath();
		return new String[] { rPath + "/js/autocomplete.js" };
	}
}