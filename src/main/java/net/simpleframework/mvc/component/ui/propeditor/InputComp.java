package net.simpleframework.mvc.component.ui.propeditor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.simpleframework.common.Convert;
import net.simpleframework.ctx.common.xml.AbstractElementBean;
import net.simpleframework.mvc.common.element.EElementEvent;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class InputComp extends AbstractElementBean {

	public static InputComp select(final String name) {
		return new InputComp(name).setType(EInputCompType.select);
	}

	public static InputComp select(final String name, final Class<? extends Enum<?>> eClass) {
		return select(name).setDefaultValue(eClass.getEnumConstants());
	}

	public static InputComp checkbox(final String name) {
		return new InputComp(name).setType(EInputCompType.checkbox).setDefaultValue("true");
	}

	public static InputComp label(final Object lbl) {
		return new InputComp().setType(EInputCompType.div).setDefaultValue(Convert.toString(lbl));
	}

	private String name;

	private EInputCompType type;

	private String style;

	/**
	 * 扩展属性，格式：readonly;rows:6
	 */
	private String attributes;

	private String defaultValue;

	private Map<EElementEvent, String> eventCallback;

	public InputComp() {
	}

	public InputComp(final String name) {
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
