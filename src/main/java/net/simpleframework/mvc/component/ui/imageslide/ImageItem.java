package net.simpleframework.mvc.component.ui.imageslide;

import net.simpleframework.ctx.common.xml.AbstractElementBean;
import net.simpleframework.ctx.common.xml.XmlElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ImageItem extends AbstractElementBean {

	private String imageUrl;

	private String title;

	private String link;

	public ImageItem(final XmlElement xmlElement) {
		super(xmlElement);
	}

	public ImageItem(final String imageUrl, final String link, final String title) {
		super(null);
		this.imageUrl = imageUrl;
		this.link = link;
		this.title = title;
	}

	public ImageItem(final String imageUrl) {
		this(imageUrl, null, null);
	}

	public ImageItem() {
		this(null, null, null);
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public ImageItem setImageUrl(final String imageUrl) {
		this.imageUrl = imageUrl;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public ImageItem setTitle(final String title) {
		this.title = title;
		return this;
	}

	public String getLink() {
		return link;
	}

	public ImageItem setLink(final String link) {
		this.link = link;
		return this;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "imageUrl", "title", "link" };
	}
}
