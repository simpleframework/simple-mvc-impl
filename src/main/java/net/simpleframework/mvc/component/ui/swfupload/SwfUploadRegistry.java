package net.simpleframework.mvc.component.ui.swfupload;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentHtmlRenderEx;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(SwfUploadRegistry.SWFUPLOAD)
@ComponentBean(SwfUploadBean.class)
@ComponentRender(SwfUploadRender.class)
@ComponentResourceProvider(SwfUploadResourceProvider.class)
public class SwfUploadRegistry extends AbstractComponentRegistry {
	public static final String SWFUPLOAD = "swfupload";

	@Override
	public SwfUploadBean createComponentBean(final PageParameter pp, final Object attriData) {
		final SwfUploadBean swfUpload = (SwfUploadBean) super.createComponentBean(pp, attriData);
		ComponentHtmlRenderEx.createAjaxRequest(ComponentParameter.get(pp, swfUpload));
		return swfUpload;
	}
}
