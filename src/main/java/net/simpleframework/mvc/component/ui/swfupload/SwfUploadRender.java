package net.simpleframework.mvc.component.ui.swfupload;

import net.simpleframework.mvc.component.ComponentHtmlRenderEx;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class SwfUploadRender extends ComponentHtmlRenderEx {
	public SwfUploadRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	protected String getRelativePath(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/jsp/swfupload.jsp?").append(SwfUploadUtils.BEAN_ID);
		sb.append("=").append(cp.hashId());
		return sb.toString();
	}
}
