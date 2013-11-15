package net.simpleframework.mvc.component.ui.dictionary;

import java.util.Map;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.tree.AbstractTreeHandler;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DictionaryTreeHandler extends AbstractTreeHandler {

	public static final String TN_ATTRI_SELECT_DISABLE = "select_disable";

	@Override
	public Map<String, Object> getTreenodeAttributes(final ComponentParameter cp,
			final TreeNode treeNode, final TreeNodes children) {
		final KVMap kv = (KVMap) super.getTreenodeAttributes(cp, treeNode, children);
		if ((Boolean) cp.getBeanProperty("checkboxes") && !treeNode.isCheckbox()) {
			kv.put(TN_ATTRI_SELECT_DISABLE, Boolean.TRUE);
		}
		return kv;
	}
}
