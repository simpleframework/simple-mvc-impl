<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ui.swfupload.SwfUploadUtils"%>
<%
	out.write(SwfUploadUtils.upload(request, response));
%>
