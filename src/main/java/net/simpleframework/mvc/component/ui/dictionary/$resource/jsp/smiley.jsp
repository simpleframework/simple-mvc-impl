<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ComponentUtils"%>
<%@ page import="net.simpleframework.mvc.component.ui.dictionary.DictionaryBean"%>
<%@ page import="net.simpleframework.mvc.component.ui.dictionary.DictionaryUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.common.StringUtils"%>
<%
	final ComponentParameter nCP = DictionaryUtils.get(request,
			response);
	final String beanId = nCP.hashId();
	final String rPath = ComponentUtils
			.getResourceHomePath(DictionaryBean.class);
%>
<div class="dictionary_smiley">
  <%
  	for (int i = 0; i < 90; i++) {
  		out.append("<img onclick='smiley_").append(beanId).append("(this);' src='");
  		out.append(rPath).append("/smiley/").append(String.valueOf(i)).append(".gif' />");
  	}
  %>
</div>
<script type="text/javascript">
  smiley_<%=beanId%> = function(img) {
    <%final String callback = (String) nCP.getBeanProperty("jsSelectCallback");
			if (StringUtils.hasText(callback)) {
				out.append(callback);
			} else {
				final String binding = StringUtils.text(
						(String) nCP.getBeanProperty("bindingId"),
						(String) nCP.getBeanProperty("bindingText"));
				if (StringUtils.hasText(binding)) {
					out.append("var b = $('").append(binding).append("');");
					out.append("if (b) { var s = img.src; ");
					out.append("var j = s.substring(s.lastIndexOf('/') + 1, s.lastIndexOf('.'));");
					out.append("$Actions.setValue(b, '[:em' + j + ']', true); $Actions['");
					out.append((String) nCP.getComponentName());
					out.append("'].close(); b.focus(); }");
				}
			}%>
  };
</script>
