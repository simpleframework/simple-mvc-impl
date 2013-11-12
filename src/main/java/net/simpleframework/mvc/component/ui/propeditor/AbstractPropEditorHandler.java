package net.simpleframework.mvc.component.ui.propeditor;

import net.simpleframework.mvc.component.AbstractComponentHandler;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractPropEditorHandler extends AbstractComponentHandler implements
		IPropEditorHandler {

	@Override
	public PropFields getFormFields(final ComponentParameter cp) {
		return null;
	}
}
