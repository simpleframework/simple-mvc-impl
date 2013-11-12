<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ui.swfupload.SwfUploadUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentRenderUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%
	final ComponentParameter nCP = SwfUploadUtils
	.get(request, response);
	final String uploadName = (String) nCP.getComponentName();
	final String beanId = nCP.hashId();
%>
<div class="swfupload">
  <%=ComponentRenderUtils.genParameters(nCP)%>
  <div class="swf_btns">
    <span id="placeholder_<%=beanId%>"></span>
  </div>
  <div id="message_<%=beanId%>" class="message"></div>
  <div id="fileQueue_<%=beanId%>" class="queue"></div>
</div>
<script type="text/javascript">
  <%=SwfUploadUtils.genJavascript(nCP)%>
</script>