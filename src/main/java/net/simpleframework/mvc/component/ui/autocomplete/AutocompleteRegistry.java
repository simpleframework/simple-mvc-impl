package net.simpleframework.mvc.component.ui.autocomplete;

import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JsonForward;
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
		pp.addComponentBean("ajax_" + nCP.getComponentName(), AjaxRequestBean.class)
				.setHandlerClass(AutocompleteAjaxRequestHandler.class)
				.setAttr("_ajaxComponent", autocomplete);
		return autocomplete;
	}

	public static class AutocompleteAjaxRequestHandler extends DefaultAjaxRequestHandler {
		@Override
		public IForward ajaxProcess(final ComponentParameter cp) {
			final AutocompleteBean autocomplete = (AutocompleteBean) cp.componentBean
					.getAttr("_ajaxComponent");
			final ComponentParameter nCP = ComponentParameter.get(cp, autocomplete);

			final String val = nCP.getParameter("val");
			String val2 = val;
			final String sepChar = (String) nCP.getBeanProperty("sepChar");
			if (StringUtils.hasText(sepChar)) {
				int p;
				if ((p = val.lastIndexOf(sepChar)) > -1) {
					val2 = val.substring(p + sepChar.length());
				}
			}
			val2 = val2.trim();

			final JsonForward json = new JsonForward();
			if (val2.length() == 0) {
				return json;
			}

			AutocompleteData[] data = null;
			final IAutocompleteHandler aHandler = ((IAutocompleteHandler) nCP.getComponentHandler());
			if (aHandler != null && (data = aHandler.getData(nCP, val, val2)) != null) {
				final int maxResults = Convert.toInt(nCP.getBeanProperty("maxResults"));
				if (maxResults > 0 && maxResults < data.length) {
					final AutocompleteData[] dest = new AutocompleteData[maxResults];
					System.arraycopy(data, 0, dest, 0, maxResults);
					json.put("data", dest);
				} else {
					json.put("data", data);
				}
			}

			return json.put("val", val2);
		}
	}
}
