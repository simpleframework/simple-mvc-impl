package net.simpleframework.mvc.component.ui.htmleditor;

import java.util.Arrays;
import java.util.Locale;

import net.simpleframework.common.I18n;
import net.simpleframework.common.JsonUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.IComponentRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class HtmlEditorRender extends ComponentJavascriptRender {
	public HtmlEditorRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		final String textarea = (String) cp.getBeanProperty("textarea");
		final boolean hasTextarea = StringUtils.hasText(textarea);
		sb.append("if (CKEDITOR._loading) return; CKEDITOR._loading = true;");
		if (hasTextarea) {
			sb.append(actionFunc).append(".editor = CKEDITOR.replace(\"");
			sb.append(textarea).append("\", {");
		} else {
			sb.append(ComponentRenderUtils.initContainerVar(cp));
			sb.append(actionFunc).append(".editor = CKEDITOR.appendTo(")
					.append(ComponentRenderUtils.VAR_CONTAINER).append(", {");
		}
		sb.append("contentsCss: [\"").append(ComponentUtils.getCssResourceHomePath(cp))
				.append("/contents.css").append("\"],");
		sb.append("smiley_path: \"").append(ComponentUtils.getResourceHomePath(HtmlEditorBean.class))
				.append("/smiley/\",");

		sb.append("enterMode: ")
				.append(getLineMode((EEditorLineMode) cp.getBeanProperty("enterMode"))).append(",");
		sb.append("shiftEnterMode: ")
				.append(getLineMode((EEditorLineMode) cp.getBeanProperty("shiftEnterMode")))
				.append(",");

		sb.append("language: \"").append(getLanguage()).append("\",");
		sb.append("autoUpdateElement: false,");
		String[] removePlugins = new String[] {};
		if (!(Boolean) cp.getBeanProperty("elementsPath")) {
			removePlugins = ArrayUtils.add(removePlugins, "elementspath");
		}
		if (removePlugins.length > 0) {
			sb.append("removePlugins: '").append(StringUtils.join(removePlugins, ",")).append("',");
		}
		sb.append("startupFocus: ").append(cp.getBeanProperty("startupFocus")).append(",");
		sb.append("toolbarCanCollapse: ").append(cp.getBeanProperty("toolbarCanCollapse"))
				.append(",");
		sb.append("resize_enabled: ").append(cp.getBeanProperty("resizeEnabled")).append(",");
		final String height = (String) cp.getBeanProperty("height");
		if (StringUtils.hasText(height)) {
			sb.append("height: \"").append(height).append("\",");
		}

		sb.append("on: {");
		sb.append("  instanceReady: function(ev) { CKEDITOR._loading = false; ");
		final String jsLoadedCallback = (String) cp.getBeanProperty("jsLoadedCallback");
		if (StringUtils.hasText(jsLoadedCallback)) {
			sb.append(jsLoadedCallback);
		}
		sb.append("  }");
		sb.append("}");
		sb.append("});");
		final Toolbar toolbar = (Toolbar) cp.getBeanProperty("toolbar");
		int size;
		if (toolbar != null && (size = toolbar.size()) > 0) {
			sb.append("CKEDITOR.config.toolbar = [");
			for (int i = 0; i < size; i++) {
				if (i > 0) {
					sb.append(",");
				}
				final String[] sArr = toolbar.get(i);
				if (sArr.length == 0) {
					sb.append("'/'");
				} else {
					sb.append(JsonUtils.toJSON(Arrays.asList(sArr)));
				}
			}
			sb.append("];");
		}

		if (hasTextarea) {
			sb.append("$(\"").append(textarea).append("\").htmlEditor = ");
			sb.append(actionFunc).append(".editor;");
		}

		final String htmlContent = (String) cp.getBeanProperty("htmlContent");
		if (StringUtils.hasText(htmlContent)) {
			sb.append(actionFunc).append(".editor.setData(\"");
			sb.append(JavascriptUtils.escape(htmlContent)).append("\");");
		}

		// for SyntaxHighlighter
		sb.append(actionFunc).append(".editor.syntaxhighlighter = 'sh_").append(cp.hashId())
				.append("';");

		final StringBuilder sb2 = new StringBuilder();
		sb2.append("var act = $Actions[\"").append(cp.getComponentName()).append("\"];");
		sb2.append("if (act && act.editor) { CKEDITOR.remove(act.editor); }");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString(), sb2.toString());
	}

	private String getLanguage() {
		final Locale l = I18n.getLocale();
		if (l.equals(Locale.SIMPLIFIED_CHINESE)) {
			return "zh-cn";
		} else {
			return "en";
		}
	}

	private String getLineMode(final EEditorLineMode lineMode) {
		if (lineMode == EEditorLineMode.br) {
			return "CKEDITOR.ENTER_BR";
		} else if (lineMode == EEditorLineMode.div) {
			return "CKEDITOR.ENTER_DIV";
		} else {
			return "CKEDITOR.ENTER_P";
		}
	}
}
