package net.simpleframework.mvc.component.ui.swfupload;

import static net.simpleframework.common.I18n.$m;

import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class SwfUploadBean extends AbstractContainerBean {
	private static final long serialVersionUID = 7966186549060752903L;

	public static String IMAGES_FILETYPES = "*.jpg;*.jpeg;*.gif;*.png;*.bmp";
	/* 文件大小限制，单位： B、KB、MB、GB */
	private String fileSizeLimit = BeanDefaults.getString(getClass(), "fileSizeLimit", "10MB");
	/* 文件队列限制 */
	private int fileQueueLimit = BeanDefaults.getInt(getClass(), "fileQueueLimit", 0);

	/* 可选择的文件类型 */
	private String fileTypes; // *.jpg;*.jpeg;*.gif;*.png;*.bmp
	/* 文件类型描述 */
	private String fileTypesDesc;

	/* 是否允许多选 */
	private boolean multiFileSelected = BeanDefaults.getBool(getClass(), "multiFileSelected", false);

	/* 上传按钮的宽度 */
	private int btnWidth = BeanDefaults.getInt(getClass(), "btnWidth", 75);
	/* 上传按钮的高度 */
	private int btnHeight = BeanDefaults.getInt(getClass(), "btnHeight", 24);

	/* alert方式显示错误 */
	private boolean alertError;
	/* 按钮背景图片 */
	private String btnImageUrl;

	/* 按钮文本 */
	private String uploadText = BeanDefaults.getString(getClass(), "uploadText",
			$m("SwfUploadUtils.0"));

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

	public int getBtnWidth() {
		return btnWidth;
	}

	public SwfUploadBean setBtnWidth(final int btnWidth) {
		this.btnWidth = btnWidth;
		return this;
	}

	public int getBtnHeight() {
		return btnHeight;
	}

	public SwfUploadBean setBtnHeight(final int btnHeight) {
		this.btnHeight = btnHeight;
		return this;
	}

	public String getBtnImageUrl() {
		return btnImageUrl;
	}

	public SwfUploadBean setBtnImageUrl(final String btnImageUrl) {
		this.btnImageUrl = btnImageUrl;
		return this;
	}

	public String getUploadText() {
		return uploadText;
	}

	public SwfUploadBean setUploadText(final String uploadText) {
		this.uploadText = uploadText;
		return this;
	}

	public String getJsCompleteCallback() {
		return jsCompleteCallback;
	}

	public SwfUploadBean setJsCompleteCallback(final String jsCompleteCallback) {
		this.jsCompleteCallback = jsCompleteCallback;
		return this;
	}

	public boolean isAlertError() {
		return alertError;
	}

	public SwfUploadBean setAlertError(final boolean alertError) {
		this.alertError = alertError;
		return this;
	}

	public String getRoleUpload() {
		return StringUtils.hasText(roleUpload) ? roleUpload : mvcSettings.getDefaultRole();
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
