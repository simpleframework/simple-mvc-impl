package net.simpleframework.mvc.component.ui.tree;

import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(FolderTreeRegistry.FOLDERTREE)
@ComponentBean(FolderTreeBean.class)
public class FolderTreeRegistry extends AbstractTreeRegistry {
	public static final String FOLDERTREE = "folderTree";

	@Override
	protected TreeNode createTreeNode(final TreeBean treeBean, final TreeNode parent) {
		return null;
	}
}
