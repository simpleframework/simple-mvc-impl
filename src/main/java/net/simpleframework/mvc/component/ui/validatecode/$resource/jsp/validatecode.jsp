<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ui.validatecode.ValidateCodeUtils"%>
<%
	ValidateCodeUtils.doRender(request, response);
%>