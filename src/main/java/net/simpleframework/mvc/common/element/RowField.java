package net.simpleframework.mvc.common.element;

import java.util.Map;

import net.simpleframework.common.web.html.HtmlUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class RowField {
	private String label;

	private AbstractElement<?>[] inputElements;

	private String labelStyle, elementsStyle;

	private boolean starMark;

	public RowField(final String label, final AbstractElement<?>... inputElements) {
		setLabel(label);
		setInputElements(inputElements);
	}

	public String getLabel() {
		return label;
	}

	public RowField setLabel(final String label) {
		this.label = label;
		return this;
	}

	public boolean isStarMark() {
		return starMark;
	}

	public RowField setStarMark(final boolean starMark) {
		this.starMark = starMark;
		return this;
	}

	public AbstractElement<?>[] getInputElements() {
		return inputElements;
	}

	public RowField setInputElements(final AbstractElement<?>[] inputElements) {
		this.inputElements = inputElements;
		return this;
	}

	public String getElementsStyle() {
		return elementsStyle;
	}

	public RowField setElementsStyle(final String elementsStyle) {
		this.elementsStyle = elementsStyle;
		return this;
	}

	public RowField addElementsStyle(final String elementsStyle) {
		final Map<String, String> s1 = HtmlUtils.toStyle(getElementsStyle());
		s1.putAll(HtmlUtils.toStyle(elementsStyle));
		this.elementsStyle = HtmlUtils.joinStyle(s1);
		return this;
	}

	public String getLabelStyle() {
		return labelStyle;
	}

	public RowField setLabelStyle(final String labelStyle) {
		this.labelStyle = labelStyle;
		return this;
	}

	public RowField setReadonly(final boolean readonly) {
		if (inputElements != null) {
			for (int i = 0; i < inputElements.length; i++) {
				if (inputElements[i] instanceof AbstractInputElement) {
					((AbstractInputElement<?>) inputElements[i]).setReadonly(readonly);
				}
			}
		}
		return this;
	}

	public RowField addLabelStyle(final String labelStyle) {
		final Map<String, String> s1 = HtmlUtils.toStyle(getLabelStyle());
		s1.putAll(HtmlUtils.toStyle(labelStyle));
		this.labelStyle = HtmlUtils.joinStyle(s1);
		return this;
	}
}
