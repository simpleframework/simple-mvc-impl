package net.simpleframework.mvc.component.ui.propeditor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.simpleframework.ctx.common.xml.AbstractElementBean;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.common.element.EElementEvent;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class InputComp extends AbstractElementBean {

	private String name;

	private EInputCompType type;

	private String style;

	/**
	 * 扩展属性，格式：readonly;rows:6
	 */
	private String attributes;

	private String defaultValue;

	private Map<EElementEvent, String> eventCallback;

	public InputComp(final XmlElement xmlElement) {
		super(xmlElement);
	}

	public InputComp() {
		super(null);
	}

	public InputComp(final String name) {
		super(null);
		setName(name);
	}

	public String getName() {
		return name;
	}

	public InputComp setName(final String name) {
		this.name = name;
		return this;
	}

	public EInputCompType getType() {
		return type == null ? EInputCompType.text : type;
	}

	public InputComp setType(final EInputCompType type) {
		this.type = type;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public InputComp setStyle(final String style) {
		this.style = style;
		return this;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public InputComp setDefaultValue(final Enum<?>... vals) {
		final StringBuilder sb = new StringBuilder();
		int i = 0;
		for (final Enum<?> v : vals) {
			if (i++ > 0) {
				sb.append(";");
			}
			sb.append(v.name()).append("=").append(v);
		}
		return setDefaultValue(sb.toString());
	}

	public InputComp setDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	public String getAttributes() {
		return attributes;
	}

	public InputComp setAttributes(final String attributes) {
		this.attributes = attributes;
		return this;
	}

	public Map<EElementEvent, String> getEventCallback() {
		if (eventCallback == null) {
			eventCallback = new ConcurrentHashMap<EElementEvent, String>();
		}
		return eventCallback;
	}

	public InputComp addEvent(final EElementEvent event, final String javascript) {
		getEventCallback().put(event, javascript);
		return this;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "defaultValue" };
	}
}
