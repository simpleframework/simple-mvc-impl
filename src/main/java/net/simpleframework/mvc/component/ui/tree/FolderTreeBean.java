package net.simpleframework.mvc.component.ui.tree;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FolderTreeBean extends TreeBean {

	private String rootFolderPath;

	private boolean showRoot = BeanDefaults.getBool(getClass(), "showRoot", true);

	private boolean showFile = BeanDefaults.getBool(getClass(), "showFile", false);

	public FolderTreeBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
		setDynamicLoading(true);
		setHandleClass(FolderTreeHandler.class);
	}

	public FolderTreeBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

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
}
