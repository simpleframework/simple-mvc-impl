<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ui.validatecode.ValidateCodeUtils"%>
<%@ page import="net.simpleframework.mvc.IMVCContextVar"%>
<%
	try {
		ValidateCodeUtils.doRender(request, response);
	} catch (Throwable th) {
		System.out.println(IMVCContextVar.ctx.getThrowableMessage(th));
	}
%>