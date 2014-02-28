package net.simpleframework.mvc.component.ui.swfupload;

import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class SwfUploadBean extends AbstractContainerBean {

	private String fileSizeLimit; // 单位： B、KB、MB、GB

	private int fileQueueLimit = BeanDefaults.getInt(getClass(), "fileQueueLimit", 0);

	private String fileTypes; // *.jpg;*.jpeg;*.gif;*.png;*.bmp

	private String fileTypesDesc;

	private boolean multiFileSelected = BeanDefaults.getBool(getClass(), "multiFileSelected", false);

	private String jsCompleteCallback;

	private String roleUpload;

	public String getFileSizeLimit() {
		return fileSizeLimit;
	}

	public SwfUploadBean setFileSizeLimit(final String fileSizeLimit) {
		this.fileSizeLimit = fileSizeLimit;
		return this;
	}

	public int getFileQueueLimit() {
		return fileQueueLimit;
	}

	public SwfUploadBean setFileQueueLimit(final int fileQueueLimit) {
		this.fileQueueLimit = fileQueueLimit;
		return this;
	}

	public String getFileTypes() {
		return fileTypes;
	}

	public SwfUploadBean setFileTypes(final String fileTypes) {
		this.fileTypes = fileTypes;
		return this;
	}

	public String getFileTypesDesc() {
		return fileTypesDesc;
	}

	public SwfUploadBean setFileTypesDesc(final String fileTypesDesc) {
		this.fileTypesDesc = fileTypesDesc;
		return this;
	}

	public boolean isMultiFileSelected() {
		return multiFileSelected;
	}

	public SwfUploadBean setMultiFileSelected(final boolean multiFileSelected) {
		this.multiFileSelected = multiFileSelected;
		return this;
	}

	public String getJsCompleteCallback() {
		return jsCompleteCallback;
	}

	public SwfUploadBean setJsCompleteCallback(final String jsCompleteCallback) {
		this.jsCompleteCallback = jsCompleteCallback;
		return this;
	}

	public String getRoleUpload() {
		return StringUtils.hasText(roleUpload) ? roleUpload : settings.getDefaultRole();
	}

	public SwfUploadBean setRoleUpload(final String roleUpload) {
		this.roleUpload = roleUpload;
		return this;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsCompleteCallback" };
	}
}
