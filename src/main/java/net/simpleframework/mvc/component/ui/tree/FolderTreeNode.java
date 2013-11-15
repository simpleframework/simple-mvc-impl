package net.simpleframework.mvc.component.ui.tree;

import java.io.File;

import net.simpleframework.common.StringUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class FolderTreeNode extends TreeNode {

	public FolderTreeNode(final FolderTreeBean treeBean, final FolderTreeNode parent,
			final File folder) {
		super(treeBean, parent, folder);
	}

	public File getFolder() {
		return (File) getDataObject();
	}

	@Override
	public String getId() {
		return StringUtils.encodeHex(getFolder().getAbsolutePath().getBytes());
	}

	@Override
	public String getText() {
		return getFolder().getName();
	}
}
