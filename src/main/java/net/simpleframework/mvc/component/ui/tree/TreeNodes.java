package net.simpleframework.mvc.component.ui.tree;

import net.simpleframework.common.coll.AbstractArrayListEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TreeNodes extends AbstractArrayListEx<TreeNodes, TreeNode> {

	public static TreeNodes of(final TreeNode... items) {
		return new TreeNodes().append(items);
	}

	private static final long serialVersionUID = -7944837906822208912L;
}
