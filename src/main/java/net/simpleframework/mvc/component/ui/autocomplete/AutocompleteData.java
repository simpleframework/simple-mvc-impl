package net.simpleframework.mvc.component.ui.autocomplete;

import java.io.Serializable;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class AutocompleteData implements Serializable {
	private String data;
	private String txt;

	private String txt2;

	public AutocompleteData(final String data) {
		this(data, data);
	}

	public AutocompleteData(final String data, final String txt) {
		this.data = data;
		this.txt = txt;
	}

	public String getData() {
		return data;
	}

	public AutocompleteData setData(final String data) {
		this.data = data;
		return this;
	}

	public String getTxt() {
		return txt;
	}

	public AutocompleteData setTxt(final String txt) {
		this.txt = txt;
		return this;
	}

	public String getTxt2() {
		return txt2;
	}

	public AutocompleteData setTxt2(final String txt2) {
		this.txt2 = txt2;
		return this;
	}

	private static final long serialVersionUID = -5363326546679712963L;
}
