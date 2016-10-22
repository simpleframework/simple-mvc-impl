package net.simpleframework.mvc.component.ui.pager;

import java.util.List;

import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.TextForward;
import net.simpleframework.mvc.UrlForward;
import net.simpleframework.mvc.component.ComponentHtmlRenderEx;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PagerAction extends ComponentHtmlRenderEx.RefreshAction {

	@Override
	public IForward ajaxProcess(final ComponentParameter cp) {
		final ComponentParameter nCP = PagerUtils.get(cp);
		final String pageNumberParameterName = (String) nCP
				.getBeanProperty("pageNumberParameterName");
		final int pageNumber = Math.max(Convert.toInt(nCP.getParameter(pageNumberParameterName)), 1);
		PagerUtils.setPageAttributes(nCP, pageNumberParameterName, pageNumber);
		final String pageItemsParameterName = (String) nCP.getBeanProperty("pageItemsParameterName");
		final int pageItems = Convert.toInt(nCP.getParameter(pageItemsParameterName));
		if (pageItems > 0) {
			PagerUtils.setPageAttributes(nCP, pageItemsParameterName, pageItems);
		}

		if (cp.isMobile()) {
			final AbstractPagerHandler pHdl = (AbstractPagerHandler) nCP.getComponentHandler();
			final List<?> data = pHdl.getData(nCP, pageNumber * pageItems);
			if (data.size() > 0) {
				return new TextForward(pHdl.toPagerHTML(nCP, data));
			}
			return null;
		} else {
			final StringBuilder sb = new StringBuilder();
			sb.append(ComponentUtils.getResourceHomePath(PagerBean.class)).append("/jsp/pager.jsp");
			final String xpParameter = PagerUtils.getXmlPathParameter(nCP);
			if (StringUtils.hasText(xpParameter)) {
				sb.append("?").append(xpParameter);
			}
			return new UrlForward(sb.toString());
		}
	}
}
