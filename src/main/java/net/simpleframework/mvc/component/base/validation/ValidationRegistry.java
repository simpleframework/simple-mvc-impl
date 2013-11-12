package net.simpleframework.mvc.component.base.validation;

import java.util.Iterator;

import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.ctx.script.IScriptEval;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@ComponentName(ValidationRegistry.VALIDATION)
@ComponentBean(ValidationBean.class)
@ComponentRender(ValidationRender.class)
@ComponentResourceProvider(ValidationResourceProvider.class)
public class ValidationRegistry extends AbstractComponentRegistry {
	public static final String VALIDATION = "validation";

	@Override
	protected void initComponentFromXml(final PageParameter pp,
			final AbstractComponentBean componentBean, final XmlElement xmlElement) {
		super.initComponentFromXml(pp, componentBean, xmlElement);

		final IScriptEval scriptEval = pp.getScriptEval();

		final Iterator<?> it = xmlElement.elementIterator("validator");
		while (it.hasNext()) {
			final XmlElement ele = (XmlElement) it.next();
			final Validator validator = new Validator(ele);
			validator.parseElement(scriptEval);
			final String message = ele.elementText("message");
			if (StringUtils.hasText(message)) {
				validator.setMessage(message.trim());
			}
			((ValidationBean) componentBean).getValidators().add(validator);
		}
	}
}
