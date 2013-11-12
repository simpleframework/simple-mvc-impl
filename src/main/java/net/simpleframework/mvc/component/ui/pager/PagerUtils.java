package net.simpleframework.mvc.component.ui.pager;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.common.Convert;
import net.simpleframework.common.FileUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.IMVCConst;
import net.simpleframework.mvc.MVCUtils;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class PagerUtils {
	public static final String BEAN_ID_NAME = "__pager_beanId_name";

	public static final String BEAN_ID = "pager_@bid";

	private static String beanId(final HttpServletRequest request) {
		final String beanIdName = request.getParameter(BEAN_ID_NAME);
		return StringUtils.hasText(beanIdName) ? beanIdName : BEAN_ID;
	}

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, beanId(request));
	}

	public static List<?> getPagerList(final PageRequestResponse rRequest) {
		final List<?> l = (List<?>) rRequest.getRequestAttr(IPagerHandler.PAGER_LIST);
		return l != null ? l : Collections.EMPTY_LIST;
	}

	public static void setPageAttributes(final ComponentParameter nCP, final String key,
			final Object value) {
		final String sk = "attributes_" + nCP.hashId();
		@SuppressWarnings("unchecked")
		Map<String, Object> attributes = (Map<String, Object>) nCP.getSessionAttr(sk);
		if (attributes == null) {
			nCP.setSessionAttr(sk, attributes = new HashMap<String, Object>());
		}
		attributes.put(key, value);
	}

	private static Object _getPageAttributes(final ComponentParameter nCP, final String key) {
		@SuppressWarnings("unchecked")
		final Map<String, Object> attributes = (Map<String, Object>) nCP.getSessionAttr("attributes_"
				+ nCP.hashId());
		return attributes != null ? attributes.get(key) : null;
	}

	public static int getPageItems(final ComponentParameter nCP) {
		final String pageItemsParameterName = (String) nCP.getBeanProperty("pageItemsParameterName");
		final String pageItems = nCP.getParameter(pageItemsParameterName);
		final int items;
		if (StringUtils.hasText(pageItems)) {
			items = Convert.toInt(pageItems);
		} else {
			items = Convert.toInt(_getPageAttributes(nCP, pageItemsParameterName));
		}
		return items == 0 ? (Integer) nCP.getBeanProperty("pageItems") : items;
	}

	public static int getPageNumber(final ComponentParameter nCP) {
		final String pageNumberParameterName = (String) nCP
				.getBeanProperty("pageNumberParameterName");
		final String pageNumber = nCP.getParameter(pageNumberParameterName);
		if (StringUtils.hasText(pageNumber)) {
			return Convert.toInt(pageNumber);
		} else {
			return Convert.toInt(_getPageAttributes(nCP, pageNumberParameterName));
		}
	}

	public static void resetPageNumber(final ComponentParameter nCP) {
		final String pageNumberParameterName = (String) nCP
				.getBeanProperty("pageNumberParameterName");
		nCP.putParameter(pageNumberParameterName, 0);
		setPageAttributes(nCP, pageNumberParameterName, 0);
	}

	/***************************** utils for jsp ****************************/

	static String getXmlPathParameter(final ComponentParameter cp) {
		final String dataPath = (String) cp.getBeanProperty("dataPath");
		if (StringUtils.hasText(dataPath)) {
			final String xmlPath = MVCUtils.doPageUrl(cp, FileUtils.stripFilenameExtension(dataPath)
					+ ".xml");
			if (new File(MVCUtils.getRealPath(xmlPath)).exists()) {
				return IMVCConst.PARAM_XMLPATH + "=" + xmlPath;
			}
		}
		return null;
	}
}
