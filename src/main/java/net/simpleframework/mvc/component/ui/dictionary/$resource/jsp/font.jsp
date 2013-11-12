<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ui.dictionary.DictionaryUtils"%>
<%@ page import="net.simpleframework.mvc.component.ui.dictionary.DictionaryRender"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%
	final ComponentParameter nCP = DictionaryUtils.get(request,
			response);
	final String beanId = nCP.hashId();
%>
<div class="dictionary_font">
  <div id="fontEditor<%=beanId%>"></div>
  <jsp:include page="okcancel_inc.jsp" flush="true"></jsp:include>
</div>
<script type="text/javascript">
  $Actions.callSafely('_dict_fontEditor', null, function(action) {
    action.container = $("fontEditor<%=beanId%>");
  });
  <%=DictionaryRender.fontSelected(nCP)%>
</script>
