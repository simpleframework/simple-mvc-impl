package net.simpleframework.mvc.component.ui.propeditor;

import java.util.Iterator;

import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.ctx.script.IScriptEval;
import net.simpleframework.ctx.script.ScriptEvalUtils;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.EElementEvent;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentException;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(PropEditorRegistry.PROPEDITOR)
@ComponentBean(PropEditorBean.class)
@ComponentRender(PropEditorRender.class)
@ComponentResourceProvider(PropEditorResourceProvider.class)
public class PropEditorRegistry extends AbstractComponentRegistry {
	public static final String PROPEDITOR = "propEditor";

	@Override
	protected void initComponentFromXml(final PageParameter pp,
			final AbstractComponentBean componentBean, final XmlElement element) {
		super.initComponentFromXml(pp, componentBean, element);

		final PropEditorBean formEditor = (PropEditorBean) componentBean;
		final IScriptEval scriptEval = pp.getScriptEval();

		final Iterator<?> it = element.elementIterator("field");
		while (it.hasNext()) {
			final XmlElement ele = (XmlElement) it.next();
			final PropField propField = (PropField) new PropField().setElement(ele);
			propField.parseElement(scriptEval);
			formEditor.getFormFields().add(propField);

			final Iterator<?> it2 = ele.elementIterator("component");
			while (it2.hasNext()) {
				final XmlElement ele2 = (XmlElement) it2.next();
				final InputComp inputComp = (InputComp) new InputComp().setElement(ele2);
				inputComp.parseElement(scriptEval);

				final Iterator<?> it3 = ele2.elementIterator("event");
				while (it3.hasNext()) {
					final XmlElement ele3 = (XmlElement) it3.next();
					try {
						final String name = ScriptEvalUtils.replaceExpr(scriptEval,
								ele3.attributeValue("name"));
						final EElementEvent e = Enum.valueOf(EElementEvent.class, name);
						inputComp.getEventCallback().put(e,
								ScriptEvalUtils.replaceExpr(scriptEval, ele3.getText()));
					} catch (final Exception e) {
						throw ComponentException.of(e);
					}
				}
				propField.getInputComponents().add(inputComp);
			}
		}
	}
}
