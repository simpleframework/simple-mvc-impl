package net.simpleframework.mvc.component.ui.tree;

import static net.simpleframework.common.I18n.$m;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.common.element.LinkElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class LinkAddTreeNode extends LinkElement {

	public LinkAddTreeNode(final String onclick) {
		setText($m("Add"));
		setOnclick(onclick);
	}

	@Override
	public String toString() {
		addClassName("addbtn a2");
		String onclick = getOnclick();
		if (StringUtils.hasText(onclick)) {
			if (!onclick.endsWith(";")) {
				onclick += ";";
			}
			setOnclick(onclick + "Event.stop(event);");
		}
		return super.toString();
	}

	private static final long serialVersionUID = 2466551769408431365L;
}
