package net.simpleframework.mvc.component.base.validation;

import net.simpleframework.common.coll.AbstractArrayListEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class Validators extends AbstractArrayListEx<Validators, Validator> {

	public static Validators of(final Validator... items) {
		return new Validators().append(items);
	}

	private static final long serialVersionUID = -357345210542918649L;
}
