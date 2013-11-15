package net.simpleframework.mvc.common.element;

import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.web.HttpUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DictInput extends TextButton {
	private String dictComponent;

	private String parameters;

	private Map<String, Object> _parameters;

	public DictInput() {
	}

	public DictInput(final String id) {
		super(id);
	}

	public String getDictComponent() {
		return dictComponent;
	}

	public DictInput setDictComponent(final String dictComponent) {
		this.dictComponent = dictComponent;
		return this;
	}

	public String getParameters() {
		if (!StringUtils.hasText(parameters)) {
			parameters = HttpUtils.toQueryString(_parameters);
		}
		return parameters;
	}

	public DictInput setParameters(final String parameters) {
		this.parameters = parameters;
		return this;
	}

	public DictInput addParameter(final String key, final Object val) {
		if (_parameters == null) {
			_parameters = new KVMap();
		}
		_parameters.put(key, val);
		return this;
	}

	/**
	 * 业务字典模块
	 * 
	 * @param val
	 * @return
	 */
	public DictInput addDictNameParameter(final Object val) {
		return addParameter("dictName", val);
	}

	@Override
	public String toString() {
		final String dictComponent = getDictComponent();
		if (StringUtils.hasText(dictComponent)) {
			final StringBuilder sb = new StringBuilder();
			sb.append("var act = $Actions['").append(dictComponent).append("'];");
			final InputElement hiddenField = getHiddenField();
			if (hiddenField != null) {
				sb.append("act.bindingId = '").append(hiddenField.getId()).append("';");
			} else {
				sb.append("act.bindingId = undefined;");
			}
			sb.append("act.bindingText = '").append(getId()).append("';");
			sb.append("act(");
			final String parameters = getParameters();
			if (StringUtils.hasText(parameters)) {
				sb.append("$UI.evalParam('").append(parameters).append("')");
			}
			sb.append(");");
			setOnclick(sb.toString());
		}
		return super.toString();
	}
}
