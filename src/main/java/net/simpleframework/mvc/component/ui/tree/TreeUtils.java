package net.simpleframework.mvc.component.ui.tree;

import java.util.Collection;
import java.util.LinkedList;

import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.common.logger.Log;
import net.simpleframework.common.logger.LogFactory;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JsonForward;
import net.simpleframework.mvc.MVCContext;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class TreeUtils {
	static Log log = LogFactory.getLogger(TreeUtils.class);

	public static final String BEAN_ID = "tree_@bid";

	public static IForward dropHandler(final ComponentParameter cp) {
		final JsonForward json = new JsonForward();
		final String treeName = cp.getComponentName();
		final IForward forward = MVCContext.get().getPermission().accessForward(cp,
				cp.getBeanProperty("roleDrop"));
		if (forward != null) {
			json.put("responseText", forward.getResponseText(cp));
			json.put("ajaxRequestId", treeName);
			json.put("dropOk", false);
		} else {
			final ITreeHandler tHandle = (ITreeHandler) cp.getComponentHandler();
			if (tHandle != null) {
				final TreeNode drag = getTreenodeById(cp, cp.getParameter("drag_id"));
				final TreeNode drop = getTreenodeById(cp, cp.getParameter("drop_id"));
				json.put("dropOk", tHandle.doDragDrop(cp, drag, drop));
			} else {
				json.put("dropOk", false);
			}
		}
		return json;
	}

	public static String nodeHandler(final ComponentParameter cp) {
		final TreeBean treeBean = (TreeBean) cp.componentBean;
		final TreeRender render = (TreeRender) treeBean.getComponentRegistry().getComponentRender();
		final TreeNode node = getTreenodeById(cp, cp.getParameter("nodeId"));
		if (node == null) {
			return "[]";
		} else {
			return render.jsonData(cp, getTreenodes(cp, node));
		}
	}

	public static TreeNodes getTreenodes(final ComponentParameter cp) {
		final ITreeHandler treeHandle = (ITreeHandler) cp.getComponentHandler();
		TreeNodes nodes = null;
		if (treeHandle != null) {
			nodes = treeHandle.getTreenodes(cp, null);
		}
		if (nodes == null) {
			nodes = ((TreeBean) cp.componentBean).getTreeNodes();
		}
		return nodes;
	}

	public static TreeNodes getTreenodes(final ComponentParameter cp, final TreeNode treeNode) {
		TreeNodes nodes = null;
		final ITreeHandler treeHandle = (ITreeHandler) cp.getComponentHandler();
		if (treeHandle != null) {
			nodes = treeHandle.getTreenodes(cp, treeNode);
		}
		if (nodes == null) {
			nodes = treeNode.children();
		}
		return nodes;
	}

	public static TreeNode getTreenodeById(final ComponentParameter cp, final String id) {
		return findTreenode(cp, new LinkedList<String>(ArrayUtils.asList(StringUtils.split(id, "_"))),
				getTreenodes(cp));
	}

	private static TreeNode findTreenode(final ComponentParameter cp, final LinkedList<String> ll,
			final Collection<TreeNode> coll) {
		if (ll.size() == 0 || coll.size() == 0) {
			return null;
		}
		final String id = ll.get(0);
		for (final TreeNode node : coll) {
			if (id.equals(node.getId())) {
				ll.removeFirst();
				if (ll.size() == 0) {
					return node;
				}
				final Collection<TreeNode> treeNodes = getTreenodes(cp, node);
				final TreeNode find = findTreenode(cp, ll, treeNodes);
				if (find != null) {
					return find;
				}
			}
		}
		return null;
	}

	public static boolean isDynamicLoading(final ComponentParameter cp, final TreeNode treeNode) {
		return Convert.toBool(cp.getBeanProperty("dynamicLoading")) || treeNode.isDynamicLoading();
	}

	static String genEvent2(final Object bean, final String prefix, final String[] properties) {
		if (properties == null) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		for (final String property : properties) {
			final String event = (String) BeanUtils.getProperty(bean, property);
			if (StringUtils.hasText(event)) {
				sb.append(StringUtils.blank(prefix)).append(property).append(" = \"");
				sb.append(JavascriptUtils.escape(event)).append("\";");
			}
		}
		return sb.toString();
	}
}
