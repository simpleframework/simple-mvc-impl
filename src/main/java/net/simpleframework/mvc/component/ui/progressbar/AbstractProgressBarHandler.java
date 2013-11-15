package net.simpleframework.mvc.component.ui.progressbar;

import net.simpleframework.mvc.component.AbstractComponentHandler;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractProgressBarHandler extends AbstractComponentHandler implements
		IProgressBarHandler {

	@Override
	public void doProgressState(final ComponentParameter cp, final ProgressState state) {
	}
}