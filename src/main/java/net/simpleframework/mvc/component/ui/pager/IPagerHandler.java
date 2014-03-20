package net.simpleframework.mvc.component.ui.pager;

import java.util.List;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IPagerHandler extends IComponentHandler {

	static final String PAGER_CURRENT_DATA = "__pager_currentdata";

	/**
	 * 处理从start开始的数据
	 * 
	 * @param compParameter
	 * @param start
	 */
	void process(ComponentParameter cp, int start);

	/**
	 * 获取数据的数量
	 * 
	 * @param compParameter
	 * @return
	 */
	int getCount(ComponentParameter cp);

	/**
	 * 返回要分页的内容
	 * 
	 * @param cParameter
	 * @param data
	 * @return
	 */
	String toPagerHTML(ComponentParameter cp, List<?> data);

	/**
	 * 返回导航
	 * 
	 * @param cp
	 * @param pagerPosition
	 * @param pageCount
	 * @param currentPageNumber
	 * @param pageNumber
	 * @return
	 */
	String toPagerNavigationHTML(ComponentParameter cp, EPagerPosition pagerPosition, int pageCount,
			int currentPageNumber, int pageNumber);

	/**
	 * 返回导航的Actions
	 * 
	 * @param cp
	 * @param count
	 * @param firstItem
	 * @param lastItem
	 * @return
	 */
	String toPagerActionsHTML(ComponentParameter cp, int count, int firstItem, int lastItem);
}
