package net.simpleframework.mvc.common.element;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public enum ETabMatch {
	/**
	 * 序号匹配
	 */
	tabIndex,

	/**
	 * 匹配参数
	 */
	params,

	/**
	 * url匹配, 不包括参数, 采用endsWith
	 */
	url
}
