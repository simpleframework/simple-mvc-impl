<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.common.StringUtils"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.EPagerBarLayout"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.PagerUtils"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.IPagerHandler"%>
<%@ page import="net.simpleframework.common.web.HttpUtils"%>
<%@ page import="net.simpleframework.common.web.JavascriptUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentRenderUtils"%>
<%@ page import="net.simpleframework.mvc.component.AbstractComponentBean"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.MVCUtils"%>
<%@ page import="net.simpleframework.mvc.UrlForward"%>
<%@ page import="net.simpleframework.mvc.component.ComponentHandlerException"%>
<%@ page import="net.simpleframework.common.I18n"%>
<%
	final ComponentParameter cp = PagerUtils.get(request, response);
	final IPagerHandler hdl = (IPagerHandler) cp.getComponentHandler();
  if (hdl == null) {
    throw ComponentHandlerException.of(I18n.$m("pager.1", cp.getComponentName()));
  }
  
	final int count = hdl.getCount(cp);
	final int pageNumber = PagerUtils.getPageNumber(cp);
	int start;
	if (pageNumber > 1) {
		start = (pageNumber - 1) * PagerUtils.getPageItems(cp);
		if (start >= count) {
			start = 0;
			PagerUtils.resetPageNumber(cp);
		}
	} else {
		start = 0;
	}
	HttpUtils.putParameter(request, "pager.offset", start);
	hdl.process(cp, start);
	final EPagerBarLayout layout = (EPagerBarLayout) cp
			.getBeanProperty("pagerBarLayout");
%>
<div class="pager"
  style="width:<%=StringUtils.text((String) cp.getBeanProperty("width"),
					"100%")%>">
  <%
  	out.write(ComponentRenderUtils.genParameters(cp));
  	final String title = (String) cp.getBeanProperty("title");
  	final boolean top = count > 0
  			&& (layout == EPagerBarLayout.top || layout == EPagerBarLayout.both);
  	if (top || StringUtils.hasText(title)) {
  %>
  <div class="pager_block_top">
    <div class="pager_title"><%=StringUtils.blank(title)%></div>
    <%
    	if (top) {
    %><jsp:include page="pager_head.jsp" flush="true">
      <jsp:param value="<%=start%>" name="pager.offset" />
      <jsp:param value="<%=count%>" name="_count" />
    </jsp:include>
    <%
    	}
    %>
    <div class="clearfix"></div>
  </div>
  <%
  	}
  	String dataPath = (String) cp.getBeanProperty("dataPath");
  	if (StringUtils.hasText(dataPath)) {
  		dataPath = HttpUtils.stripContextPath(request,
  				MVCUtils.doPageUrl(cp, dataPath));
  %><jsp:include page="<%=dataPath%>" flush="true"></jsp:include>
  <%
  	} else {
  		final String dataHtml = hdl.toPagerHTML(cp,
  				PagerUtils.getCurrentPageData(cp));
  		if (StringUtils.hasText(dataHtml)) {
  			out.write(dataHtml);
  		}
  	}
  	final boolean bottom = count > 0
  			&& (layout == EPagerBarLayout.bottom || layout == EPagerBarLayout.both);
  	final String stat = (String) cp.getBeanProperty("stat");
  	if (bottom || StringUtils.hasText(stat)) {
  %><div class="pager_block_bottom">
    <div class="pager_title"><%=StringUtils.blank(stat)%></div>
    <%
    	if (bottom) {
    %>
    <jsp:include page="pager_head.jsp" flush="true">
      <jsp:param value="<%=start%>" name="pager.offset" />
      <jsp:param value="<%=count%>" name="_count" />
    </jsp:include>
    <%
    	}
    %>
    <div class="clearfix"></div>
  </div>
  <%
  	}
  %>
</div>
<%
	Object noResultDesc;
	if (count == 0
			&& StringUtils.hasObject(noResultDesc = cp
					.getBeanProperty("noResultDesc"))) {
%>
<div class="pager_no_result"><%=noResultDesc%></div>
<%
	}
	String beanId = cp.hashId();
%>
<script type="text/javascript">
  $ready(function() {
    var action = $Actions["<%=cp.getComponentName()%>"];
    <%=ComponentRenderUtils.genJSON(cp, "action")%>
    action.selector = 
      "<%=cp.getBeanProperty("selector")%>";
    var ele = $("<%=AbstractComponentBean.FORM_PREFIX + beanId%>").up(".pager");
    action.pager = ele;
    action.hasData = <%=count > 0%>;
    action.beanId = "<%=beanId%>";
    
    ele.action = action;
    $call(window.pager_init_<%=beanId%>, action);
    
    action.refresh = function(params) { action(params); };
    
    if (action.jsLoadedCallback) {
      action.jsLoadedCallback();
    } else {
      <%final String jsLoadedCallback = (String) cp
          .getBeanProperty("jsLoadedCallback");
      if (StringUtils.hasText(jsLoadedCallback)) {
        out.write(jsLoadedCallback);
      }%>
    }
  });
</script>