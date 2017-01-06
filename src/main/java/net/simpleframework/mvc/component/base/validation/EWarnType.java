package net.simpleframework.mvc.component.base.validation;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public enum EWarnType {

	/**
	 * alert方式验证
	 */
	alert,

	/**
	 * 插入到验证元素之后
	 */
	insertAfter,

	/**
	 * 插入到验证元素所在层的最后
	 */
	insertLast,

	placeholder
}
