package net.simpleframework.mvc.component.ui.dictionary;

import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean.AbstractDictionaryTypeBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean.DictionaryColorBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean.DictionaryFontBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean.DictionaryListBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean.DictionarySmileyBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean.DictionaryTreeBean;
import net.simpleframework.mvc.component.ui.window.WindowRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(DictionaryRegistry.DICTIONARY)
@ComponentBean(DictionaryBean.class)
@ComponentRender(DictionaryRender.class)
@ComponentResourceProvider(DictionaryResourceProvider.class)
public class DictionaryRegistry extends WindowRegistry {
	public static final String DICTIONARY = "dictionary";

	@Override
	public DictionaryBean createComponentBean(final PageParameter pp, final Object attriData) {
		final DictionaryBean dictionaryBean = (DictionaryBean) super.createComponentBean(pp,
				attriData);

		final ComponentParameter nCP = ComponentParameter.get(pp, dictionaryBean);
		// 虚拟一个AjaxRequestBean
		createAjaxRequest(nCP);
		return dictionaryBean;
	}

	public static final String ATTRI_AJAXREQUEST = "$$ajaxRequest";

	private AjaxRequestBean createAjaxRequest(final ComponentParameter cp) {
		final DictionaryBean dictionaryBean = (DictionaryBean) cp.componentBean;

		final String ajaxRequestName = "ajaxRequest_" + dictionaryBean.hashId();
		final AjaxRequestBean ajaxRequest = (AjaxRequestBean) cp
				.addComponentBean(ajaxRequestName, AjaxRequestBean.class).setShowLoading(false)
				.setParameters(DictionaryUtils.BEAN_ID + "=" + dictionaryBean.hashId())
				.setIncludeRequestData("pa").setHandleClass(DictionaryAction.class)
				.setAttr("$$dictionary", dictionaryBean);
		cp.addComponentBean(ajaxRequest);

		dictionaryBean.setContentRef(ajaxRequestName)
				.setContent(AbstractComponentRegistry.getLoadingContent())
				.setAttr(ATTRI_AJAXREQUEST, ajaxRequest);
		return ajaxRequest;
	}

	@Override
	protected void initComponentFromXml(final PageParameter pp,
			final AbstractComponentBean componentBean, final XmlElement xmlElement) {
		super.initComponentFromXml(pp, componentBean, xmlElement);

		final DictionaryBean dictionaryBean = (DictionaryBean) componentBean;

		AbstractDictionaryTypeBean type = null;
		XmlElement ele = xmlElement.element("tree");
		if (ele != null) {
			type = new DictionaryTreeBean(dictionaryBean, ele);
		} else if ((ele = xmlElement.element("list")) != null) {
			type = new DictionaryListBean(dictionaryBean, ele);
		} else if ((ele = xmlElement.element("color")) != null) {
			type = new DictionaryColorBean(dictionaryBean, ele);
		} else if ((ele = xmlElement.element("font")) != null) {
			type = new DictionaryFontBean(dictionaryBean, ele);
		} else if ((ele = xmlElement.element("smiley")) != null) {
			type = new DictionarySmileyBean(dictionaryBean, ele);
		}
		if (type != null) {
			type.parseElement(pp.getScriptEval());
			dictionaryBean.setDictionaryTypeBean(ComponentParameter.get(pp, dictionaryBean), type);
		}
	}
}
