package net.simpleframework.mvc.component.ui.videoplayer;

import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class VideoPlayerBean extends AbstractContainerBean {
	private String videoUrl;

	private boolean autoPlay;

	private String jsLoadedCallback;

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(final String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public boolean isAutoPlay() {
		return autoPlay;
	}

	public void setAutoPlay(final boolean autoPlay) {
		this.autoPlay = autoPlay;
	}

	public String getJsLoadedCallback() {
		return jsLoadedCallback;
	}

	public void setJsLoadedCallback(final String jsLoadedCallback) {
		this.jsLoadedCallback = jsLoadedCallback;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsLoadedCallback" };
	}

	{
		setWidth("420");
		setHeight("300");
	}
}
