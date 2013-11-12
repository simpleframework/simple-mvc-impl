package net.simpleframework.mvc.component.ui.tree;

import java.util.Map;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.component.AbstractComponentHandler;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractTreeHandler extends AbstractComponentHandler implements ITreeHandler {

	@Override
	public TreeNodes getTreenodes(final ComponentParameter cp, final TreeNode parent) {
		return null;
	}

	@Override
	public Map<String, Object> getTreenodeAttributes(final ComponentParameter cp,
			final TreeNode treeNode, final TreeNodes children) {
		return new KVMap();
	}

	@Override
	public boolean doDragDrop(final ComponentParameter cp, final TreeNode drag, final TreeNode drop) {
		return false;
	}
}
