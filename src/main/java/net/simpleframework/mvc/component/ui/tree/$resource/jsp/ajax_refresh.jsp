<%@ page import="net.simpleframework.mvc.component.ComponentRenderUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ui.tree.TreeRender"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ui.tree.TreeUtils"%>
<%
	final ComponentParameter nCP = ComponentParameter.get(request,
	response, TreeUtils.BEAN_ID);
	final TreeRender render = (TreeRender) nCP.componentBean
	.getComponentRegistry().getComponentRender();

	final StringBuilder sb = new StringBuilder();
	sb.append("var act = $Actions['").append(nCP.getComponentName())
	.append("'];");
	sb.append("try {");
	sb.append(ComponentRenderUtils.genJSON(nCP, "act"));
	sb.append("act.createTree(");
	sb.append(render.jsonData(nCP)).append(");");
	sb.append("} catch(e) { }");

	out.write(sb.toString());
	out.flush();
%>