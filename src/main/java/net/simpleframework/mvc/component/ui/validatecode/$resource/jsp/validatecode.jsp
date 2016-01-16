<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ui.validatecode.ValidateCodeUtils"%>
<%@ page import="net.simpleframework.mvc.MVCContext"%>
<%
	try {
		ValidateCodeUtils.doRender(request, response);
	} catch (Throwable th) {
		System.out.println(MVCContext.get().getThrowableMessage(th));
	}
%>