package net.simpleframework.mvc.component.ui.listbox;

import java.util.Map;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.component.AbstractComponentHandler;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractListboxHandler extends AbstractComponentHandler
		implements IListboxHandler {

	@Override
	public Map<String, Object> getListItemAttributes(final ComponentParameter cp,
			final ListItem listItem) {
		return new KVMap();
	}
}
