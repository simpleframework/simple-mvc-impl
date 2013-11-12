package net.simpleframework.mvc.component.ui.tree;

import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@ComponentName(TreeRegistry.TREE)
@ComponentBean(TreeBean.class)
public class TreeRegistry extends AbstractTreeRegistry {
	public static final String TREE = "tree";

	@Override
	protected TreeNode createTreeNode(final XmlElement xmlElement, final TreeBean treeBean,
			final TreeNode parent) {
		return new TreeNode(xmlElement, treeBean, parent);
	}
}
