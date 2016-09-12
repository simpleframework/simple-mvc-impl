package net.simpleframework.mvc.component.ui.autocomplete;

import java.util.Iterator;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.CollectionUtils.AbstractIterator;
import net.simpleframework.ctx.permission.IPermissionHandler;
import net.simpleframework.ctx.permission.PermissionDept;
import net.simpleframework.ctx.permission.PermissionUser;
import net.simpleframework.mvc.MVCContext;
import net.simpleframework.mvc.component.AbstractComponentHandler;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractAutocompleteHandler extends AbstractComponentHandler
		implements IAutocompleteHandler {

	protected Iterator<AutocompleteData> newMIterator(final ComponentParameter cp,
			final Iterator<PermissionUser> it, final String val, final String val2) {
		final String sepChar = (String) cp.getBeanProperty("sepChar");
		return new AbstractIterator<AutocompleteData>() {
			private PermissionUser user;

			@Override
			public boolean hasNext() {
				while (it.hasNext()) {
					final PermissionUser user2 = it.next();
					if (user2.getName().contains(val2)) {
						user = user2;
						return true;
					}
				}
				return false;
			}

			@Override
			public AutocompleteData next() {
				return createAutocompleteData(user, sepChar);
			}
		};
	}

	protected AutocompleteData createAutocompleteData(final PermissionUser user,
			final String sepChar) {
		final String name = user.getName();
		final String text = user.getText() + " (" + name + ")";
		String _name = text;
		if (StringUtils.hasText(sepChar)) {
			_name += sepChar;
		}
		final AutocompleteData data = new AutocompleteData(_name, text);
		final StringBuilder txt2 = new StringBuilder();
		final IPermissionHandler permission = MVCContext.get().getPermission();
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