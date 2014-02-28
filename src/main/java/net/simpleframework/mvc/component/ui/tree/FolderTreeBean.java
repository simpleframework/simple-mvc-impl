package net.simpleframework.mvc.component.ui.tree;

import net.simpleframework.ctx.common.bean.BeanDefaults;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class FolderTreeBean extends TreeBean {

	private String rootFolderPath;

	private boolean showRoot = BeanDefaults.getBool(getClass(), "showRoot", true);

	private boolean showFile = BeanDefaults.getBool(getClass(), "showFile", false);

	public String getRootFolderPath() {
		return rootFolderPath;
	}

	public void setRootFolderPath(final String rootFolderPath) {
		this.rootFolderPath = rootFolderPath;
	}

	public boolean isShowRoot() {
		return showRoot;
	}

	public void setShowRoot(final boolean showRoot) {
		this.showRoot = showRoot;
	}

	public boolean isShowFile() {
		return showFile;
	}

	public void setShowFile(final boolean showFile) {
		this.showFile = showFile;
	}

	{
		setDynamicLoading(true);
		setHandlerClass(FolderTreeHandler.class);
	}
}
