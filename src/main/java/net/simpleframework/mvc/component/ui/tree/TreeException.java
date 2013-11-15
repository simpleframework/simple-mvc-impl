package net.simpleframework.mvc.component.ui.tree;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.th.RuntimeExceptionEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TreeException extends RuntimeExceptionEx {
	private static final long serialVersionUID = 220404294448529138L;

	public TreeException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static RuntimeException of_delete() {
		return of($m("TreeException.0"));
	}

	public static RuntimeException of(final String msg) {
		return _of(TreeException.class, msg);
	}
}
