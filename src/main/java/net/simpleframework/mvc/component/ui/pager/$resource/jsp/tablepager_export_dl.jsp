<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.TablePagerUtils"%>
<%
	try {
		TablePagerUtils.doExport(request, response);
	} finally {
		try {
			out.clear();
		} catch (Throwable th) {
		}
	}
%>