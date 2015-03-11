package net.simpleframework.mvc.component.ui.tree;

import java.util.Collection;
import java.util.Map;

import net.simpleframework.common.JsonUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.web.HttpUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.common.web.html.HtmlUtils;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.ComponentUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TreeRender extends ComponentJavascriptRender {

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		final TreeBean treeBean = (TreeBean) cp.componentBean;
		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		sb.append(actionFunc).append(".treeUrl = function(jsp, p) {");
		final KVMap params = new KVMap().add(TreeUtils.BEAN_ID, cp.hashId());
		for (final Map.Entry<String, Object> e : ComponentUtils.toFormParameters(cp).entrySet()) {
			params.add(e.getKey(), String.valueOf(e.getValue()));
		}
		sb.append("var url = \"").append(ComponentUtils.getResourceHomePath(TreeBean.class));
		sb.append("/jsp/\" + jsp + \"?").append(HttpUtils.toQueryString(params)).append("\";");
		ComponentRenderUtils.appendParameters(sb, cp, "url");
		sb.append("if (p) url = url.addParameter(p);");
		sb.append("return url;");
		sb.append("};");

		sb.append(actionFunc).append(".default_options.onDropAjax.push(").append(actionFunc)
				.append(".treeUrl(\"ajax_drop.jsp\"));");
		sb.append(actionFunc).append(".createTree = function(nodes) {");
		sb.append(ComponentRenderUtils.initContainerVar(cp));
		sb.append(ComponentRenderUtils.VAR_CONTAINER).append(".update(\"\");");
		sb.append(actionFunc).append(".tree = new TafelTree(")
				.append(ComponentRenderUtils.VAR_CONTAINER);
		sb.append(", nodes, Object.extend(").append(actionFunc).append(".default_options, {");

		sb.append("\"imgBase\": \"").append(ComponentUtils.getCssResourceHomePath(cp))
				.append("/images/\",");
		final String imgHome = (String) cp.getBeanProperty("imgHome");
		if (StringUtils.hasText(imgHome)) {
			sb.append("\"imgHome\" : \"").append(imgHome).append("\",");
		}
		sb.append("\"lineStyle\" : \"").append(cp.getBeanProperty("lineStyle")).append("\",");

		sb.append("\"checkboxes\": ").append(cp.getBeanProperty("checkboxes")).append(",");
		sb.append("\"checkboxesThreeState\": ").append(cp.getBeanProperty("checkboxesThreeState"))
				.append(",");

		final String dragScroll = (String) cp.getBeanProperty("dragScroll");
		if (StringUtils.hasText(dragScroll)) {
			sb.append("\"dragScroll\": \"").append(dragScroll).append("\",");
		}

		sb.append("\"onLoad\": function() {");
		sb.append("var cb = ").append(actionFunc).append(".jsLoadedCallback;");
		sb.append("if (cb) { $call(cb); }");
		final String jsLoadedCallback = (String) cp.getBeanProperty("jsLoadedCallback");
		if (StringUtils.hasText(jsLoadedCallback)) {
			sb.append("else {").append(jsLoadedCallback).append("}");
		}
		sb.append("$Loading.hide();");
		sb.append("},");
		sb.append("\"checkedCookies\": ").append(cp.getBeanProperty("checkedCookies")).append(",");
		sb.append("\"cookies\": ").append(cp.getBeanProperty("cookies"));
		sb.append("}));");

		// event
		sb.append(TreeUtils.genEvent2(treeBean, (actionFunc + ".tree."), new String[] {
				"jsClickCallback", "jsDblclickCallback", "jsCheckCallback" }));

		sb.append("};");
		sb.append("try { ").append(actionFunc).append(".createTree(");
		sb.append(jsonData(cp)).append("); }");
		sb.append("catch(e) { }");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString(), "__tree_actions_init("
				+ actionFunc + ");");
	}

	public String jsonData(final ComponentParameter cp) {
		return jsonData(cp, TreeUtils.getTreenodes(cp));
	}

	public String jsonData(final ComponentParameter cp, final Collection<TreeNode> children) {
		if (children == null) {
			return "[]";
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		int i = 0;
		for (final TreeNode child : children) {
			// items
			final TreeNodes items = TreeUtils.getTreenodes(cp, child);
			if (i++ > 0) {
				sb.append(",");
			}
			sb.append("{");
			sb.append("\"id\": \"").append(child.getId()).append("\",");
			String str = child.getText();
			sb.append("\"txt\": \"").append(JavascriptUtils.escape(str)).append("\",");
			str = child.getImage();
			if (StringUtils.hasText(str)) {
				sb.append("\"img\": \"").append(str).append("\",");
			}
			str = child.getImageClose();
			if (StringUtils.hasText(str)) {
				sb.append("\"imgclose\": \"").append(str).append("\",");
			}
			str = child.getImageOpen();
			if (StringUtils.hasText(str)) {
				sb.append("\"imgopen\": \"").append(str).append("\",");
			}
			sb.append("\"open\": ").append(child.isOpened()).append(",");
			sb.append("\"select\": ").append(child.isSelect()).append(",");
			sb.append("\"checkbox\": ").append(child.isCheckbox()).append(",");
			sb.append("\"check\": ").append(child.getCheck()).append(",");
			sb.append("\"draggable\": ").append(child.isDraggable()).append(",");
			sb.append("\"acceptdrop\": ").append(child.isAcceptdrop()).append(",");
			str = child.getTooltip();
			if (StringUtils.hasText(str) && (Boolean) cp.getBeanProperty("showTip")) {
				sb.append("\"tooltip\": \"")
						.append(JavascriptUtils.escape(HtmlUtils.convertHtmlLines(str))).append("\",");
			}

			str = child.getPostfixText();
			if (StringUtils.hasText(str)) {
				sb.append("\"postfix\": \"").append(JavascriptUtils.escape(str)).append("\",");
			}

			TreeNode node = child;
			String contextMenu = node.getContextMenu();
			if (!"none".equalsIgnoreCase(contextMenu)) {
				while (!StringUtils.hasText(contextMenu)) {
					node = node.getParent();
					if (node == null) {
						break;
					}
					contextMenu = node.getContextMenu();
				}
				if (!StringUtils.hasText(contextMenu) || "none".equalsIgnoreCase(contextMenu)) {
					contextMenu = (String) cp.getBeanProperty("contextMenu");
				}
				if (StringUtils.hasText(contextMenu)) {
					sb.append("\"contextMenu\": \"").append(contextMenu).append("\",");
				}
			}

			str = child.getJsClickCallback();
			if (StringUtils.hasText(str)) {
				sb.append("\"jsClickCallback\": \"").append(JavascriptUtils.escape(str)).append("\",");
			}
			str = child.getJsDblclickCallback();
			if (StringUtils.hasText(str)) {
				sb.append("\"jsDblclickCallback\": \"").append(JavascriptUtils.escape(str))
						.append("\",");
			}
			str = child.getJsCheckCallback();
			if (StringUtils.hasText(str)) {
				sb.append("\"jsCheckCallback\": \"").append(JavascriptUtils.escape(str)).append("\",");
			}

			final ITreeHandler tHandle = (ITreeHandler) cp.getComponentHandler();
			if (tHandle != null) {
				final Map<String, Object> attributes = tHandle.getTreenodeAttributes(cp, child, items);
				if (attributes != null && attributes.size() > 0) {
					sb.append("\"attributes\": ").append(JsonUtils.toJSON(attributes)).append(",");
				}
			}

			// child.get
			if (TreeUtils.isDynamicLoading(cp, child) && items != null && items.size() > 0) {
				sb.append("\"canhavechildren\": true,");
				sb.append("\"openlink\": ($Actions[\"").append(cp.getComponentName()).append("\"] || ")
						.append(ComponentRenderUtils.actionFunc(cp))
						.append(").treeUrl(\"ajax_node.jsp\", \"nodeId=")
						.append(HttpUtils.encodeUrl(child.nodeId())).append("\"),");
				sb.append("\"onopenpopulate\": ").append(
						"function(branch, response) { return response; }");
			} else {
				sb.append("\"items\": ").append(jsonData(cp, items));
			}
			sb.append("}");
		}
		sb.append("]");
		return sb.toString();
	}
}
