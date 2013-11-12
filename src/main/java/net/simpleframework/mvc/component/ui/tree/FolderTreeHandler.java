package net.simpleframework.mvc.component.ui.tree;

import java.io.File;
import java.util.Collection;

import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FolderTreeHandler extends AbstractTreeHandler {

	@Override
	public TreeNodes getTreenodes(final ComponentParameter cp, final TreeNode treeNode) {
		final FolderTreeBean folderTree = (FolderTreeBean) cp.componentBean;
		final TreeNodes nodes = TreeNodes.of();
		if (treeNode == null) {
			final File root = new File(folderTree.getRootFolderPath());
			if (folderTree.isShowRoot()) {
				nodes.add(new FolderTreeNode(folderTree, null, root));
			} else {
				addTreeNodes(root, nodes, folderTree, null);
			}
		} else {
			final FolderTreeNode folderNode = (FolderTreeNode) treeNode;
			addTreeNodes(folderNode.getFolder(), nodes, folderTree, folderNode);
		}
		return nodes;
	}

	private void addTreeNodes(final File parent, final Collection<TreeNode> nodes,
			final FolderTreeBean folderTree, final FolderTreeNode folderNode) {
		final File[] files = parent.listFiles();
		if (files == null) {
			return;
		}
		for (final File file : files) {
			if (file.isFile() && !folderTree.isShowFile()) {
				continue;
			}
			nodes.add(new FolderTreeNode(folderTree, folderNode, file));
		}
	}
}
