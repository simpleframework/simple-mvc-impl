<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ui.tree.TreeUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%
	final ComponentParameter nCP = ComponentParameter.get(request, response,
			TreeUtils.BEAN_ID);
	out.write(TreeUtils.nodeHandler(nCP));
	out.flush();
%>
