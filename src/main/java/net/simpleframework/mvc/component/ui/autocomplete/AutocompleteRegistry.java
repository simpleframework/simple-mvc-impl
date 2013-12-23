package net.simpleframework.mvc.component.ui.autocomplete;

import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(AutocompleteRegistry.AUTOCOMPLETE)
@ComponentBean(AutocompleteBean.class)
@ComponentRender(AutocompleteRender.class)
@ComponentResourceProvider(AutocompleteResourceProvider.class)
public class AutocompleteRegistry extends AbstractComponentRegistry {

	public static final String AUTOCOMPLETE = "autocomplete";

	@Override
	public AutocompleteBean createComponentBean(final PageParameter pp, final Object attriData) {
		final AutocompleteBean autocomplete = (AutocompleteBean) super.createComponentBean(pp,
				attriData);
		final ComponentParameter nCP = ComponentParameter.get(pp, autocomplete);
		pp.addComponentBean("ajax_" + nCP.getComponentName(), AjaxRequestBean.class).setHandleClass(
				AutocompleteAjaxRequestHandler.class);
		return autocomplete;
	}

	public static class AutocompleteAjaxRequestHandler extends DefaultAjaxRequestHandler {
		@Override
		public IForward ajaxProcess(final ComponentParameter cp) {
			System.out.println(cp);
			return null;
		}
	}
}
