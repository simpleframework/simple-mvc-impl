package net.simpleframework.mvc.component.ui.propeditor;

import net.simpleframework.common.coll.AbstractArrayListEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PropFields extends AbstractArrayListEx<PropFields, PropField> {

	public static PropFields of(final PropField... items) {
		return new PropFields().append(items);
	}

	private static final long serialVersionUID = -7301366056172203145L;
}
