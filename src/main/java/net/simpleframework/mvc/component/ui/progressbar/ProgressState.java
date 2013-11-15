package net.simpleframework.mvc.component.ui.progressbar;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ProgressState implements Serializable {
	private static final long serialVersionUID = -5194114792906218671L;

	public int maxProgressValue;

	public int step;

	public ArrayList<Object> messages = new ArrayList<Object>();

	public boolean abort;

	public void step(final String message) {
		step++;
		messages.add(message);
	}
}
