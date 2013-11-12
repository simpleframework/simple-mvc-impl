package net.simpleframework.mvc.component.ui.validatecode;

import net.simpleframework.mvc.component.AbstractComponentHandler;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultValidateCodeHandler extends AbstractComponentHandler implements
		IValidateCodeHandler {

	@Override
	public void doValidateCode(final ComponentParameter cp, final String code) {
		cp.setSessionAttr(GEN_CODE, code);
	}
}
