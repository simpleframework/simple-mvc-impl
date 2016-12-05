package net.simpleframework.mvc.component.ui.pager;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.validation.AbstractValidationHandler;
import net.simpleframework.mvc.component.base.validation.ValidationBean;
import net.simpleframework.mvc.component.base.validation.Validators;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TablePagerFilterValidation extends AbstractValidationHandler {

	@Override
	public Validators getValidators(final ComponentParameter cp) {
		final ComponentParameter nCP = PagerUtils.get(cp);
		final Validators validators = ((ValidationBean) cp.componentBean).getValidators();
		final ITablePagerHandler tHandle = (ITablePagerHandler) nCP.getComponentHandler();
		if (tHandle instanceof AbstractTablePagerHandler) {
			final TablePagerColumn tpColumn = TablePagerUtils.getSelectedColumn(nCP);
			final Validators coll = ((AbstractTablePagerHandler) tHandle)
					.getFilterColumnValidators(nCP, tpColumn);
			if (coll != null) {
				coll.addAll(validators);
			}
			return coll;
		}
		return validators;
	}
}
