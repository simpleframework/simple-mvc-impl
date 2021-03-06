package net.simpleframework.mvc.component.ui.tree;

import java.util.Collection;
import java.util.Iterator;

import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentRender(TreeRender.class)
@ComponentResourceProvider(TreeResourceProvider.class)
public abstract class AbstractTreeRegistry extends AbstractComponentRegistry {

	@Override
	protected void initComponentFromXml(final PageParameter pp,
			final AbstractComponentBean componentBean, final XmlElement element) {
		super.initComponentFromXml(pp, componentBean, element);

		final TreeBean treeBean = (TreeBean) componentBean;

		final Iterator<?> it = element.elementIterator("treenode");
		while (it.hasNext()) {
			final XmlElement ele = (XmlElement) it.next();
			setTreeNode(pp, treeBean, null, treeBean.getTreeNodes(), ele);
		}
	}

	protected abstract TreeNode createTreeNode(TreeBean treeBean, TreeNode parent);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setTreeNode(final PageParameter pp, final TreeBean treeBean, final TreeNode parent,
			final Collection children, final XmlElement element) {
		final TreeNode node = (TreeNode) createTreeNode(treeBean, parent).setElement(element);
		if (node == null) {
			return;
		}
		node.parseElement(pp.getScriptEval());
		children.add(node);
		final Iterator<?> it = element.elementIterator("treenode");
		while (it.hasNext()) {
			final XmlElement ele = (XmlElement) it.next();
			setTreeNode(pp, treeBean, node, node.children(), ele);
		}
	}
}
