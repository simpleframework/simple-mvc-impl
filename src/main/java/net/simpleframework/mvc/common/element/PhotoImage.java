package net.simpleframework.mvc.common.element;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PhotoImage extends ImageElement {

	public static PhotoImage icon24(final String src) {
		return (PhotoImage) new PhotoImage(src).addClassName("icon24");
	}

	public static PhotoImage icon16(final String src) {
		return (PhotoImage) new PhotoImage(src).addClassName("icon16");
	}

	public PhotoImage() {
	}

	public PhotoImage(final String src) {
		setSrc(src);
	}

	@Override
	public String toString() {
		addClassName("photo_icon");
		return super.toString();
	}
}
