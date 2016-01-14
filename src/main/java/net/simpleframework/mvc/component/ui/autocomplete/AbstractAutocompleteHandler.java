package net.simpleframework.mvc.component.ui.autocomplete;

import net.simpleframework.ctx.permission.IPermissionHandler;
import net.simpleframework.ctx.permission.PermissionDept;
import net.simpleframework.ctx.permission.PermissionUser;
import net.simpleframework.mvc.component.AbstractComponentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractAutocompleteHandler extends AbstractComponentHandler implements
		IAutocompleteHandler {

	protected AutocompleteData createAutocompleteData(final PermissionUser user, final String sepChar) {
		final String name = user.getName();
		final AutocompleteData data = new AutocompleteData(name + sepChar, user.getText() + " ("
				+ name + ")");
		final StringBuilder txt2 = new StringBuilder();

		final IPermissionHandler permission = getModuleContext().getPermission();
		PermissionDept dept = permission.getDept(user.getDomainId());
		if (dept.exists()) {
			txt2.append(dept);
		}
		dept = user.getDept();
		if (dept.exists()) {
			txt2.append(" - ").append(dept);
		}
		if (txt2.length() > 0) {
			data.setTxt2(txt2.toString());
		}
		return data;
	}
}