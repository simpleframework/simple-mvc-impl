<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.common.Convert"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.PagerUtils"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.EPagerPosition"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.IPagerHandler"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.PagerBean"%>
<%
	final ComponentParameter cp = PagerUtils.get(request, response);
	final IPagerHandler hdl = (IPagerHandler) cp.getComponentHandler();
	final int count = Convert.toInt(request.getParameter("_count"));
	int _firstItem = 0, _lastItem = 0;
	int _pageCount = 0;
	int _currentPageNumber = 0, _pageNumber = 0;
  int pageItems = PagerUtils.getPageItems(cp);
  int indexPages = Convert.toInt(cp.getBeanProperty("indexPages"));
%>
<div class="pager_head">
  <pg:pager items="<%=count%>" export="currentPageNumber=pageNumber" 
    maxPageItems="<%=pageItems%>" maxIndexPages="<%=indexPages%>">
    <pg:index export="pageCount">
      <pg:page export="firstItem,lastItem"><%
      	_firstItem = firstItem;
      	_lastItem = lastItem;
      	_pageCount = pageCount;
      	_currentPageNumber = currentPageNumber;
      %><%=
        hdl.toPagerNavigationHTML(cp, EPagerPosition.left2, _pageCount,
					  _currentPageNumber, _pageNumber)%><%=
        hdl.toPagerNavigationHTML(cp, EPagerPosition.left, _pageCount, 
            _currentPageNumber, _pageNumber)%><pg:pages><%
      	_pageNumber = pageNumber;
      %><%=
        hdl.toPagerNavigationHTML(cp, EPagerPosition.number, _pageCount,
			      _currentPageNumber, _pageNumber)%></pg:pages><pg:skip pages="10"><%=
        hdl.toPagerNavigationHTML(cp,
             EPagerPosition.skip, _pageCount, _currentPageNumber, pageNumber)%></pg:skip><%=
        hdl.toPagerNavigationHTML(cp,
						 EPagerPosition.right, _pageCount, _currentPageNumber, _pageNumber)%><%=
        hdl.toPagerNavigationHTML(cp,
						 EPagerPosition.right2, _pageCount, _currentPageNumber, _pageNumber)%></pg:page>
    </pg:index>
  </pg:pager>
  <%=hdl.toPagerActionsHTML(cp, count, _firstItem, _lastItem)%> 
</div>
