package net.simpleframework.mvc.component.ui.dictionary;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.ui.htmleditor.HtmlEditorBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class SmileyUtils {
	private static final Pattern smileyPattern = Pattern
			.compile("[\\s\\S]*\\[:em(\\d+)\\][\\s\\S]*");

	public static String replaceSmiley(final String content) {
		final StringBuilder sb = new StringBuilder();
		sb.append(content);
		while (true) {
			final Matcher matcher = smileyPattern.matcher(sb.toString());
			if (matcher.matches()) {
				final MatchResult result = matcher.toMatchResult();
				final String group = result.group(1);
				final String tmp = sb.toString();
				sb.setLength(0);
				sb.append(tmp.substring(0, result.start(1) - 4));
				sb.append("<img style=\"vertical-align: middle;\" src=\"").append(
						ComponentUtils.getResourceHomePath(HtmlEditorBean.class));
				sb.append("/smiley/").append(group).append(".gif\">");
				sb.append(tmp.substring(result.end(1) + 1));
			} else {
				break;
			}
		}
		return sb.toString();
	}
}
