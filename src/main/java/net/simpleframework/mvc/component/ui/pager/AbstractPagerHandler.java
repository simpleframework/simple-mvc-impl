package net.simpleframework.mvc.component.ui.pager;

import static net.simpleframework.common.I18n.$m;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.HttpUtils;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.common.element.Option;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentHandlerEx;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractPagerHandler extends ComponentHandlerEx implements IPagerHandler {

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter cp) {
		final Map<String, Object> parameters = super.getFormParameters(cp);
		for (final String k : new String[] { PagerUtils.BEAN_ID,
				(String) cp.getBeanProperty("pageNumberParameterName"),
				(String) cp.getBeanProperty("pageItemsParameterName") }) {
			parameters.put(k, cp.getParameter(k));
		}
		return parameters;
	}

	public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
		return null;
	}

	protected static final String QUERY_CACHE = "__query_cache";

	protected IDataQuery<?> getDataObjectQuery(final ComponentParameter cp) {
		IDataQuery<?> dataObjectQuery = (IDataQuery<?>) cp.getRequestAttr(QUERY_CACHE);
		if (dataObjectQuery == null) {
			cp.setRequestAttr(QUERY_CACHE, dataObjectQuery = createDataObjectQuery(cp));
			if (dataObjectQuery != null) {
				dataObjectQuery.setFetchSize(PagerUtils.getPageItems(cp) * 2);
			}
		}
		return dataObjectQuery;
	}

	@Override
	public int getCount(final ComponentParameter cp) {
		final IDataQuery<?> dataQuery = getDataObjectQuery(cp);
		if (dataQuery == null) {
			return 0;
		}
		doCount(cp, dataQuery);
		return dataQuery.getCount();
	}

	protected void doCount(final ComponentParameter cp, final IDataQuery<?> dataQuery) {
	}

	protected List<?> getData(final ComponentParameter cp, final int start) {
		final List<Object> data = new ArrayList<Object>();
		final IDataQuery<?> dataQuery = (IDataQuery<?>) cp.getRequestAttr(DATA_QUERY);
		if (dataQuery != null) {
			dataQuery.move(start - 1);
			Object object;
			int i = 0;
			final int pageItems = PagerUtils.getPageItems(cp);
			while ((object = dataQuery.next()) != null) {
				data.add(object);
				if (++i == pageItems) {
					break;
				}
			}
		}
		return data;
	}

	protected List<?> getCurrentPageData(final PageRequestResponse rRequest) {
		return PagerUtils.getCurrentPageData(rRequest);
	}

	@Override
	public String toPagerHTML(final ComponentParameter cp, final List<?> data) {
		return null;
	}

	protected static final String DATA_QUERY = "data_query";
	private static final String PROCESS_TIMES = "process_times";

	@Override
	public void process(final ComponentParameter cp, final int start) {
		final long l = System.currentTimeMillis();
		final IDataQuery<?> dataQuery = getDataObjectQuery(cp);
		cp.setRequestAttr(DATA_QUERY, dataQuery);
		cp.setRequestAttr(PAGER_CURRENT_DATA, getData(cp, start));
		cp.setSessionAttr(PROCESS_TIMES, System.currentTimeMillis() - l);
	}

	protected void wrapNavImage(final ComponentParameter cp, final StringBuilder sb) {
		final Object times = cp.getSessionAttr(PROCESS_TIMES);
		if (times != null) {
			sb.append(new SpanElement("( " + times + "ms )").addStyle("margin-left: 8px;"));
			cp.removeSessionAttr(PROCESS_TIMES);
		}
		sb.insert(0, "<div class=\"nav0_image\">");
		sb.append("</div>");
	}

	/**
	 * 获取分页的url
	 * 
	 * @param cp
	 * @param pagerPosition
	 * @param count
	 *        总数
	 * @param pageCount
	 *        显示的页数
	 * @param currentPageNumber
	 *        当前页码
	 * @param pageNumber
	 *        页码,仅在EPagerPosition.number时有效
	 * @return
	 */
	protected String getPagerUrl(final ComponentParameter cp, final EPagerPosition pagerPosition,
			final int pageCount, final int currentPageNumber, final int pageNumber) {
		final StringBuilder sb = new StringBuilder();
		sb.append("javascript:$Actions['").append(cp.getComponentName());
		sb.append("'].doPager(this, '");
		sb.append(cp.getBeanProperty("pageItemsParameterName")).append("=");
		if (pagerPosition == EPagerPosition.pageItems) {
			sb.append("' + ").append("$F(this)").append(" + '");
		} else {
			sb.append(PagerUtils.getPageItems(cp));
		}
		sb.append("&").append(cp.getBeanProperty("pageNumberParameterName")).append("=");
		if (pagerPosition == EPagerPosition.left2) {
			sb.append(1);
		} else if (pagerPosition == EPagerPosition.left) {
			sb.append(currentPageNumber > 1 ? (currentPageNumber - 1) : 1);
		} else if (pagerPosition == EPagerPosition.number || pagerPosition == EPagerPosition.skip) {
			sb.append(pageNumber);
		} else if (pagerPosition == EPagerPosition.right) {
			sb.append(currentPageNumber >= pageCount ? pageCount : currentPageNumber + 1);
		} else if (pagerPosition == EPagerPosition.right2) {
			sb.append(pageCount);
		} else if (pagerPosition == EPagerPosition.pageItems) {
			sb.append(1);
		}
		sb.append("');");
		return sb.toString();
	}

	@Override
	public String toPagerNavigationHTML(final ComponentParameter cp,
			final EPagerPosition pagerPosition, final int pageCount, final int currentPageNumber,
			final int pageNumber) {
		final String href = getPagerUrl(cp, pagerPosition, pageCount, currentPageNumber, pageNumber);
		final LinkElement link = new LinkElement();
		if (href.toLowerCase().startsWith("javascript:")) {
			link.setOnclick(href);
		} else {
			link.setHref(href);
		}
		if (pagerPosition == EPagerPosition.left2) {
			link.setClassName("p2").setTitle("#(pager_head.3)");
		} else if (pagerPosition == EPagerPosition.left) {
			link.setClassName("p1").setText("#(pager_head.4)");
		} else if (pagerPosition == EPagerPosition.number) {
			if (pageNumber != currentPageNumber) {
				link.setClassName("num");
			} else {
				link.setClassName("current num");
			}
			link.setText(pageNumber);
		} else if (pagerPosition == EPagerPosition.skip) {
			link.setClassName("num skip").setText(pageNumber);
		} else if (pagerPosition == EPagerPosition.right) {
			link.setClassName("n1").setText("#(pager_head.5)");
		} else if (pagerPosition == EPagerPosition.right2) {
			return "";
			// link.setClassName("n2").setTitle("#(pager_head.6)");
		}
		return link.toString();
	}

	protected AbstractElement<?> createHeadStatElement(final int count, final String pTitle) {
		return new SpanElement(
				$m("pager_head.1", new SpanElement(count).setClassName("num2"), pTitle))
				.addStyle("margin-left: 6px;");
	}

	@Override
	public String toPagerActionsHTML(final ComponentParameter cp, final int count,
			final int firstItem, final int lastItem) {
		final int pageItems = PagerUtils.getPageItems(cp);
		final StringBuilder sb = new StringBuilder();
		final boolean showEditPageItems = (Boolean) cp.getBeanProperty("showEditPageItems");
		if (count > pageItems) {
			final AbstractElement<?> ele = createHeadStatElement(count, firstItem + " - " + lastItem);
			if (ele != null) {
				sb.append(ele);
			}
		} else {
			final AbstractElement<?> ele = createHeadStatElement(count, $m("pager_head.2"));
			if (ele != null) {
				sb.append(ele);
			}
		}
		if (showEditPageItems) {
			sb.append(new SpanElement("/").addStyle("margin: 0px 2px;"));
			sb.append(createPageItemsInput(cp, pageItems));
		}
		String exportAction = (String) cp.getBeanProperty("exportAction");
		if (!StringUtils.hasText(exportAction)) {
			exportAction = "$Actions['" + cp.getComponentName() + "'].exportFile();";
		}
		if (!"false".equals(exportAction)) {
			sb.append(new SpanElement().setClassName("export_icon").setOnclick(exportAction));
		}
		return sb.toString();
	}

	protected AbstractElement<?> createPageItemsInput(final ComponentParameter cp,
			final int pageItems) {
		String onchange;
		final String href = ((AbstractPagerHandler) cp.getComponentHandler()).getPagerUrl(cp,
				EPagerPosition.pageItems, 0, 0, 0);
		if (href.toLowerCase().startsWith("javascript:")) {
			onchange = href.substring(11);
		} else {
			final String pageItemsParameterName = (String) cp
					.getBeanProperty("pageItemsParameterName");
			onchange = "$Actions.loc('" + HttpUtils.addParameters(href, pageItemsParameterName + "=")
					+ "' + $F(this));";
		}
		final InputElement input = InputElement
				.select()
				.setTitle("#(pager_head.0)")
				.addElements(new Option("15").setSelected(pageItems == 15),
						new Option("30").setSelected(pageItems == 30),
						new Option("50").setSelected(pageItems == 50),
						new Option("100").setSelected(pageItems == 100),
						new Option("500").setSelected(pageItems == 500))
				.addStyle("vertical-align: middle;").setOnchange(onchange);
		return input;
	}
}
