package net.simpleframework.mvc.component.ui.tree;

import java.util.LinkedList;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.common.ItemUIBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TreeNode extends ItemUIBean<TreeNode> {

	private final TreeBean treeBean;

	private TreeNodes children;

	private TreeNode parent;

	private boolean dynamicLoading;

	private String image, imageClose, imageOpen;

	private boolean draggable, acceptdrop;

	private boolean opened;

	private String postfixText;

	private boolean checkbox = true;

	private int check; // 1 -1 0

	private String contextMenu;

	// event
	private String jsCheckCallback;

	public TreeNode(final TreeBean treeBean, final TreeNode parent, final Object data) {
		super(data);
		this.parent = parent;
		this.treeBean = treeBean;
	}

	public TreeNode(final TreeBean treeBean, final TreeNode parent) {
		this(treeBean, parent, null);
	}

	public TreeNode(final TreeBean treeBean, final Object data) {
		this(treeBean, null, data);
	}

	public boolean isDynamicLoading() {
		return dynamicLoading;
	}

	public TreeNode setDynamicLoading(final boolean dynamicLoading) {
		this.dynamicLoading = dynamicLoading;
		return this;
	}

	public String getImage() {
		return image;
	}

	public TreeNode setImage(final String image) {
		this.image = image;
		return this;
	}

	public String getImageClose() {
		return StringUtils.text(imageClose, getImage());
	}

	public TreeNode setImageClose(final String imageClose) {
		this.imageClose = imageClose;
		return this;
	}

	public String getImageOpen() {
		return StringUtils.text(imageOpen, getImage());
	}

	public TreeNode setImageOpen(final String imageOpen) {
		this.imageOpen = imageOpen;
		return this;
	}

	public boolean isDraggable() {
		return draggable;
	}

	public TreeNode setDraggable(final boolean draggable) {
		this.draggable = draggable;
		return this;
	}

	public boolean isAcceptdrop() {
		return acceptdrop;
	}

	public TreeNode setAcceptdrop(final boolean acceptdrop) {
		this.acceptdrop = acceptdrop;
		return this;
	}

	public boolean isOpened() {
		return opened;
	}

	public TreeNode setOpened(final boolean opened) {
		this.opened = opened;
		return this;
	}

	public String getPostfixText() {
		return postfixText;
	}

	public TreeNode setPostfixText(final String postfixText) {
		this.postfixText = postfixText;
		return this;
	}

	public boolean isCheckbox() {
		return checkbox;
	}

	public TreeNode setCheckbox(final boolean checkbox) {
		this.checkbox = checkbox;
		return this;
	}

	public int getCheck() {
		return check;
	}

	public TreeNode setCheck(final int check) {
		this.check = check;
		return this;
	}

	public String getJsCheckCallback() {
		return jsCheckCallback;
	}

	public TreeNode setJsCheckCallback(final String jsCheckCallback) {
		this.jsCheckCallback = jsCheckCallback;
		return this;
	}

	public String getContextMenu() {
		return contextMenu;
	}

	public TreeNode setContextMenu(final String contextMenu) {
		this.contextMenu = contextMenu;
		return this;
	}

	public TreeNode getParent() {
		return parent;
	}

	public TreeNode setParent(final TreeNode parent) {
		this.parent = parent;
		return this;
	}

	public TreeBean getTreeBean() {
		return treeBean;
	}

	public TreeNodes children() {
		if (children == null) {
			children = TreeNodes.of();
		}
		return children;
	}

	public int getLevel() {
		int i = -1;
		TreeNode node = getParent();
		while (node != null) {
			i--;
			node = node.getParent();
		}
		return Math.abs(i);
	}

	public String nodeId() {
		TreeNode parent = getParent();
		if (parent == null) {
			return getId();
		} else {
			final LinkedList<String> ll = new LinkedList<String>();
			ll.addFirst(getId());
			while (parent != null) {
				ll.addFirst(parent.getId());
				parent = parent.getParent();
			}
			return StringUtils.join(ll, "_");
		}
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "text", "tooltip", "jsClickCallback", "jsDblclickCallback",
				"jsCheckCallback" };
	}
}
