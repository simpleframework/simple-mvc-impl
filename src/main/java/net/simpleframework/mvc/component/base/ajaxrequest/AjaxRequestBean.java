package net.simpleframework.mvc.component.base.ajaxrequest;

import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.component.AbstractComponentBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class AjaxRequestBean extends AbstractComponentBean {

	/* 定义组件在业务handler中的执行方法，缺省为ajaxProcess。要注意的是参数和返回值必须和ajaxProcess的定义一样 */
	private String handleMethod;

	/* ajax请求的编码，默认为UTF-8 */
	private String encoding;

	/* 返回更新的容器id */
	private String updateContainerId;

	/* 返回更新的window组件 */
	private String windowRef;

	/* 返回更新是否缓存 */
	private boolean updateContainerCache;

	/* 是否提示确认消息 */
	private String confirmMessage;

	/* 抛出异常的方式，见EThrowException */
	private EThrowException throwException = (EThrowException) BeanDefaults.get(getClass(),
			"throwException", EThrowException.window);

	/* 定义是否显示装载进度提示，一般会在浏览器的右上角加上进度提示条 */
	private boolean showLoading = BeanDefaults.getBool(getClass(), "showLoading", true);

	/* 调用时，使页面模态，即不能点击任何页面内容 */
	private boolean loadingModal = BeanDefaults.getBool(getClass(), "loadingModal", false);

	/* 是否允许并行执行，即该组件是否能被并行调用 */
	private boolean parallel = BeanDefaults.getBool(getClass(), "parallel", false);

	/* 请求时，禁止来源，目的是避免用户的多次点击操作 */
	private boolean disabledTriggerAction = BeanDefaults.getBool(getClass(),
			"disabledTriggerAction", true);

	/* ajax请求返回的路径，如果定义了该属性，业务hanlder就不需要定义了，优先级也高于业务hanlder */
	private String urlForward;

	/* 定义组件的id，内部使用，为定义则取其名称 */
	private String ajaxRequestId;

	/* 定义组件的访问角色 */
	private String role;

	/* 组件调用完成后，前端执行的js */
	private String jsCompleteCallback;

	public String getHandleMethod() {
		return handleMethod;
	}

	public AjaxRequestBean setHandleMethod(final String handleMethod) {
		this.handleMethod = handleMethod;
		return this;
	}

	public boolean isUpdateContainerCache() {
		return updateContainerCache;
	}

	public AjaxRequestBean setUpdateContainerCache(final boolean updateContainerCache) {
		this.updateContainerCache = updateContainerCache;
		return this;
	}

	public String getWindowRef() {
		return windowRef;
	}

	public AjaxRequestBean setWindowRef(final String windowRef) {
		this.windowRef = windowRef;
		return this;
	}

	public String getEncoding() {
		return encoding;
	}

	public AjaxRequestBean setEncoding(final String encoding) {
		this.encoding = encoding;
		return this;
	}

	public boolean isShowLoading() {
		return showLoading;
	}

	public AjaxRequestBean setShowLoading(final boolean showLoading) {
		this.showLoading = showLoading;
		return this;
	}

	public boolean isLoadingModal() {
		return loadingModal;
	}

	public AjaxRequestBean setLoadingModal(final boolean loadingModal) {
		this.loadingModal = loadingModal;
		return this;
	}

	public boolean isParallel() {
		return parallel;
	}

	public AjaxRequestBean setParallel(final boolean parallel) {
		this.parallel = parallel;
		return this;
	}

	public boolean isDisabledTriggerAction() {
		return disabledTriggerAction;
	}

	public AjaxRequestBean setDisabledTriggerAction(final boolean disabledTriggerAction) {
		this.disabledTriggerAction = disabledTriggerAction;
		return this;
	}

	public String getUrlForward() {
		return urlForward;
	}

	public AjaxRequestBean setUrlForward(final String urlForward) {
		this.urlForward = urlForward;
		return this;
	}

	public String getConfirmMessage() {
		return confirmMessage;
	}

	public AjaxRequestBean setConfirmMessage(final String confirmMessage) {
		this.confirmMessage = confirmMessage;
		return this;
	}

	public EThrowException getThrowException() {
		return throwException;
	}

	public AjaxRequestBean setThrowException(final EThrowException throwException) {
		this.throwException = throwException;
		return this;
	}

	public String getUpdateContainerId() {
		return updateContainerId;
	}

	public AjaxRequestBean setUpdateContainerId(final String updateContainerId) {
		this.updateContainerId = updateContainerId;
		return this;
	}

	public String getJsCompleteCallback() {
		return jsCompleteCallback;
	}

	public AjaxRequestBean setJsCompleteCallback(final String jsCompleteCallback) {
		this.jsCompleteCallback = jsCompleteCallback;
		return this;
	}

	public String getAjaxRequestId() {
		return StringUtils.text(ajaxRequestId, getName());
	}

	public AjaxRequestBean setAjaxRequestId(final String ajaxRequestId) {
		this.ajaxRequestId = ajaxRequestId;
		return this;
	}

	public String getRole() {
		return StringUtils.text(role, settings.getDefaultRole());
	}

	public AjaxRequestBean setRole(final String role) {
		this.role = role;
		return this;
	}

	@Override
	public String getHandleClass() {
		final String handleClass = super.getHandleClass();
		if (StringUtils.hasText(getUrlForward())) {
			setHandleMethod("doUrlForward");
			if (!StringUtils.hasText(handleClass)) {
				return DefaultAjaxRequestHandler.class.getName();
			}
		}
		return handleClass;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "confirmMessage", "jsCompleteCallback", "urlForward" };
	}

	{
		setRunImmediately(false);
	}
}
