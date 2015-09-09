package net.simpleframework.mvc.component.ui.propeditor;

import net.simpleframework.ctx.common.xml.AbstractElementBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PropField extends AbstractElementBean {

	private String label;

	private String labelStyle = "width: 90px;";

	private String description;

	private InputComps inputComps;

	public PropField() {
	}

	public PropField(final String label) {
		setLabel(label);
	}

	public InputComps getInputComponents() {
		if (inputComps == null) {
			inputComps = InputComps.of();
		}
		return inputComps;
	}

	public PropField addComponents(final InputComp... components) {
		getInputComponents().append(components);
		return this;
	}

	public String getLabel() {
		return label;
	}

	public PropField setLabel(final String label) {
		this.label = label;
		return this;
	}

	public String getLabelStyle() {
		return labelStyle;
	}

	public PropField setLabelStyle(final String labelStyle) {
		this.labelStyle = labelStyle;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public PropField setDescription(final String description) {
		this.description = description;
		return this;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "description" };
	}
}
