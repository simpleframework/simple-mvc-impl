package net.simpleframework.mvc.component.ui.dictionary;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean.AbstractDictionaryTypeBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean.DictionaryListBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean.DictionaryTreeBean;
import net.simpleframework.mvc.component.ui.listbox.ListboxBean;
import net.simpleframework.mvc.component.ui.tree.TreeBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class DictionaryUtils {
	public static final String BEAN_ID = "dictionary_@bid";

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static boolean isMultiple(final ComponentParameter cp) {
		final AbstractDictionaryTypeBean dictionaryType = ((DictionaryBean) cp.componentBean)
				.getDictionaryTypeBean();
		if (dictionaryType instanceof DictionaryTreeBean) {
			return ((TreeBean) dictionaryType.getAttr("$$component")).isCheckboxes();
		} else if (dictionaryType instanceof DictionaryListBean) {
			return ((ListboxBean) dictionaryType.getAttr("$$component")).isCheckbox();
		}
		return (Boolean) cp.getBeanProperty("multiple");
	}
}
