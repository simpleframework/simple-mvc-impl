package net.simpleframework.mvc.component.ui.imageslide;

import net.simpleframework.common.coll.AbstractArrayListEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ImageItems extends AbstractArrayListEx<ImageItems, ImageItem> {

	public static ImageItems of(final ImageItem... items) {
		return new ImageItems().append(items);
	}

	private static final long serialVersionUID = -839900770605313772L;
}
