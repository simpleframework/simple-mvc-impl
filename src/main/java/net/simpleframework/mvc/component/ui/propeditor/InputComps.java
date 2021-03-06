package net.simpleframework.mvc.component.ui.propeditor;

import net.simpleframework.common.coll.AbstractArrayListEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class InputComps extends AbstractArrayListEx<InputComps, InputComp> {

	public static InputComps of(final InputComp... items) {
		return new InputComps().append(items);
	}

	private static final long serialVersionUID = -214630607114161778L;
}
