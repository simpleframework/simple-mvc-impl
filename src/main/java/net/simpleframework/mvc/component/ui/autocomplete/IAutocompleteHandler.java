package net.simpleframework.mvc.component.ui.autocomplete;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IAutocompleteHandler extends IComponentHandler {

	/**
	 * @param cp
	 * @param val
	 * @return
	 */
	AutocompleteData getData(ComponentParameter cp, String val);
}