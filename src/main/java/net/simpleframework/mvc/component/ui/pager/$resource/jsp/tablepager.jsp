<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.PagerUtils"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.ITablePagerHandler"%>
<%
	final ComponentParameter nCP = PagerUtils.get(request, response);
	final ITablePagerHandler tphdl = (ITablePagerHandler) nCP
			.getComponentHandler();
	out.write(tphdl.toTableHTML(nCP));
%>
<script type="text/javascript">
  var pager_init_<%=nCP.hashId()%>
  = function(action) {
    $table_pager_addMethods(action);
  };
</script>

