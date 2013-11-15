package net.simpleframework.mvc.component.base.validation;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public enum EValidatorMethod {
	/**
	 * 必须的
	 */
	required,

	equals,

	less_than,

	great_than,

	number,

	digits,

	alpha,

	alphanum,

	/**
	 * 日期类型
	 */
	date,

	date_less_than,

	date_great_than,

	email,

	url,

	min_value,

	max_value,

	min_length,

	max_length,

	int_range,

	float_range,

	length_range,

	file,

	chinese,

	ip,

	phone,

	mobile_phone
}
