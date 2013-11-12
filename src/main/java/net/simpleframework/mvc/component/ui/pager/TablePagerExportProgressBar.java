package net.simpleframework.mvc.component.ui.pager;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.progressbar.AbstractProgressBarHandler;
import net.simpleframework.mvc.component.ui.progressbar.ProgressState;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TablePagerExportProgressBar extends AbstractProgressBarHandler {
	public static final String MAX_PROGRESS = "__export_maxprogressvalue";

	public static final String STEP = "__export_step";

	@Override
	public void doProgressState(final ComponentParameter cp, final ProgressState state) {
		final Integer maxProgressValue = (Integer) cp.getSessionAttr(MAX_PROGRESS);
		if (maxProgressValue != null) {
			state.maxProgressValue = maxProgressValue;
			cp.removeSessionAttr(MAX_PROGRESS);
		} else {
			state.maxProgressValue = 0;
		}
		final Integer step = (Integer) cp.getSessionAttr(STEP);
		if (step != null) {
			state.step = step;
		}
	}
}
