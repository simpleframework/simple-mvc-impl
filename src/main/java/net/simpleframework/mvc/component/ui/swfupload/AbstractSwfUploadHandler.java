package net.simpleframework.mvc.component.ui.swfupload;

import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.component.AbstractComponentHandler;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractSwfUploadHandler extends AbstractComponentHandler
		implements ISwfUploadHandler {

	@Override
	public String toBtnsHTML(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		if ("PluploadBean".equals(cp.componentBean.getClass().getSimpleName())) {
			sb.append(LinkButton.corner(cp.getBeanProperty("uploadText"))
					.setId("placeholder_" + cp.hashId()));
		} else {
			sb.append("<span id='placeholder_").append(cp.hashId()).append("'></span>");
		}
		return sb.toString();
	}
}
