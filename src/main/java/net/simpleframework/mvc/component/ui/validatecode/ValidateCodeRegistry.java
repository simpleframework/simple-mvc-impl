package net.simpleframework.mvc.component.ui.validatecode;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
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
@ComponentName(ValidateCodeRegistry.VALIDATECODE)
@ComponentBean(ValidateCodeBean.class)
@ComponentRender(ValidateCodeRender.class)
@ComponentResourceProvider(ValidateCodeResourceProvider.class)
public class ValidateCodeRegistry extends AbstractComponentRegistry {

	public static final String VALIDATECODE = "validateCode";

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pp, final Object attriData) {
		final ValidateCodeBean validateCode = (ValidateCodeBean) super.createComponentBean(pp,
				attriData);
		ComponentHtmlRenderEx.createAjaxRequest(ComponentParameter.get(pp, validateCode));
		return validateCode;
	}
}
