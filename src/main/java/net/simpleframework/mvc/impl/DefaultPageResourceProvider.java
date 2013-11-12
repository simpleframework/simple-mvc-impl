package net.simpleframework.mvc.impl;

import net.simpleframework.common.I18n;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.mvc.IPageResourceProvider.MVCPageResourceProvider;
import net.simpleframework.mvc.PageParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultPageResourceProvider extends MVCPageResourceProvider {
	// final static String S2_FILE = "/js/s2.js?v=2.0.0_b1";

	public final static String SIZZLE_FILE = "/js/sizzle.js";

	public final static String PROTOTYPE_FILE = "/js/prototype.js?v=1.7.1";

	public final static String EFFECTS_FILE = "/js/effects.js?v=1.9";

	public final static String DRAGDROP_FILE = "/js/dragdrop.js?v=1.9";

	@Override
	public String[] getCssPath(final PageParameter pp) {
		return ArrayUtils.add(super.getCssPath(pp), getCssResourceHomePath(pp) + "/default.css");
	}

	@Override
	public String[] getJavascriptPath(final PageParameter pp) {
		final String rPath = getResourceHomePath();
		final String[] jsArr = new String[] { rPath + SIZZLE_FILE, rPath + PROTOTYPE_FILE,
				rPath + EFFECTS_FILE, rPath + "/js/simple_" + I18n.getLocale().toString() + ".js",
				rPath + "/js/simple.js", rPath + "/js/simple_ui.js" };
		// final UserAgentParser parser = pp.getUserAgentParser();
		// if (parser.isIE() && parser.getBrowserFloatVersion() <= 8) {
		// jsArr = ArrayUtils.add(jsArr, rPath + "/js/excanvas.js");
		// }
		return ArrayUtils.add(super.getJavascriptPath(pp), jsArr);
	}
}
