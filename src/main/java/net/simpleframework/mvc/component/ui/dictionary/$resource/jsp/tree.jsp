<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ui.dictionary.DictionaryBean"%>
<%@ page import="net.simpleframework.mvc.component.ui.dictionary.DictionaryBean.DictionaryTreeBean"%>
<%@ page import="net.simpleframework.mvc.component.ui.dictionary.DictionaryUtils"%>
<%@ page import="net.simpleframework.mvc.component.ui.dictionary.DictionaryRender"%>
<%@ page import="net.simpleframework.mvc.component.ui.dictionary.IDictionaryHandle"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ComponentRenderUtils"%>
<%@ page import="net.simpleframework.common.StringUtils"%>
<%
	final ComponentParameter nCP = DictionaryUtils.get(request,
			response);
	final IDictionaryHandle hdl = (IDictionaryHandle) nCP
			.getComponentHandler();
	final String beanId = nCP.hashId();
	final String name = (String) nCP.getComponentName();
	final DictionaryTreeBean tree = (DictionaryTreeBean) ((DictionaryBean) nCP.componentBean)
			.getDictionaryTypeBean();
	final String barHTML = hdl == null ? "" : hdl.toToolbarHTML(nCP);
%>
<div class="dictionary">
  <%=ComponentRenderUtils.genParameters(nCP)%>
  <% if (StringUtils.hasText(barHTML)) { %>
  <div class="toptb"><%=barHTML%></div>
  <% } %>
  <div class="tree" id="tree<%=beanId%>"></div>
  <jsp:include page="okcancel_inc.jsp" flush="true"></jsp:include>
</div>
<script type="text/javascript">
  treeCheck<%=beanId%> = function(branch, checked, ev) {  
    if (!TafelTreeManager.ctrlOn(ev)) {
      branch._manageCheckThreeState(branch, checked);
    }
  };
      
  selected<%=beanId%> = function(branch, ev) {
    var selects = $tree_getSelects($Actions['<%=tree.getRef()%>'].tree, branch, ev);
    if (selects && selects.length > 0) {
      var act = $Actions['<%=name%>'];
      if (act.jsSelectCallback) {
        if (act.jsSelectCallback(selects))
          act.close();
      } else {
        <%=DictionaryRender.genSelectCallback(nCP, "selects")%>
      }
    } else {
      alert('#(okcancel_inc.jsp.0)');
    }
  };
  
  $ready(function() {
    var t = $('tree<%=beanId%>');
    var w = $Actions['<%=name%>'].window;
    w.content.setStyle("overflow:hidden;");
    var b = t.up(".dictionary").down(".toptb");
    var s = function() {
      var h = w.getSize(true).height;
      if (b) {
        h -= b.getHeight();
      }
      t.setStyle('height: ' + (h - 49) + 'px;');
    };
    s();
    w.observe("resize:ended", s);
  });
</script>
