package net.simpleframework.mvc.component.ui.tree;

import java.util.Map;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface ITreeHandler extends IComponentHandler {

	/**
	 * 得到指定节点孩子列表
	 * 
	 * @param cParameter
	 * @param parent
	 *           父节点，null表示第一层
	 * @return
	 */
	TreeNodes getTreenodes(ComponentParameter cp, TreeNode parent);

	/**
	 * 获取树节点的附加属性
	 * 
	 * @param cp
	 * @param parent
	 * @param children
	 * @return
	 */
	Map<String, Object> getTreenodeAttributes(ComponentParameter cp, TreeNode parent,
			TreeNodes children);

	/**
	 * 拖放逻辑
	 * 
	 * @param cParameter
	 * @param drag
	 * @param drop
	 * @return
	 */
	boolean doDragDrop(ComponentParameter cp, TreeNode drag, TreeNode drop);
}
