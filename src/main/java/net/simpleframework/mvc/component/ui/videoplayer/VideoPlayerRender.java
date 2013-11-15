package net.simpleframework.mvc.component.ui.videoplayer;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.IComponentRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class VideoPlayerRender extends ComponentJavascriptRender {

	public VideoPlayerRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final VideoPlayerBean videoPlayer = (VideoPlayerBean) cp.componentBean;

		final StringBuilder sb = new StringBuilder();
		sb.append(ComponentRenderUtils.initContainerVar(cp));
		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		sb.append(actionFunc).append(".videoPlayer = $f(").append(ComponentRenderUtils.VAR_CONTAINER)
				.append(", {");
		sb.append("buffering: true,");
		sb.append("src: \"");
		sb.append(ComponentUtils.getResourceHomePath(VideoPlayerBean.class)).append(
				"/flash/flowplayer.swf\"");
		sb.append("}, {");
		final String onload = videoPlayer.getJsLoadedCallback();
		if (StringUtils.hasText(onload)) {
			sb.append("onLoad: function() {");
			sb.append(onload);
			sb.append("},");
		}
		sb.append("logo: null,");
		// sb.append(ComponentRenderUtils.jsonAttri(cp, "height"));
		// sb.append(ComponentRenderUtils.jsonAttri(cp, "width"));
		sb.append("clip: {");
		sb.append("autoPlay: ").append(videoPlayer.isAutoPlay()).append(",");
		sb.append("scaling: \"fit\",");
		String videoUrl = null;
		final IVideoPlayerHandler handle = (IVideoPlayerHandler) cp.getComponentHandler();
		if (handle != null) {
			videoUrl = handle.getVideoUrl(cp);
		}
		if (!StringUtils.hasText(videoUrl)) {
			videoUrl = videoPlayer.getVideoUrl();
		}
		sb.append("url: \"").append(cp.getContextPath()).append(videoUrl).append("\"");
		sb.append("}");
		sb.append("});");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString());
	}
}
