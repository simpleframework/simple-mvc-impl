package net.simpleframework.mvc.common.element;

import java.util.Random;

import net.simpleframework.common.NumberUtils;
import net.simpleframework.common.StringUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ProgressElement extends SpanElement {

	private static final Random random = new Random();

	private static String[] DEFAULT_COLORS = new String[] { "#999", "#900", "#090", "#990", "#099",
			"#909" };

	public static String getRandomColor() {
		return DEFAULT_COLORS[random.nextInt(DEFAULT_COLORS.length)];
	}

	// public String colorInvert(String hex) {
	// String R = null, G = null, B = null;
	// if (hex.charAt(0) == '#') {
	// hex = hex.substring(1);
	// }
	// final int l = hex.length();
	// if (l == 3) {
	// R = String.valueOf(hex.charAt(0));
	// R += R;
	// G = String.valueOf(hex.charAt(1));
	// G += G;
	// B = String.valueOf(hex.charAt(2));
	// B += B;
	// } else if (l == 6) {
	// R = hex.substring(0, 1);
	// G = hex.substring(2, 3);
	// B = hex.substring(4, 5);
	// }
	// if (R != null) {
	// R = Integer.toHexString(255 - Integer.parseInt(R, 16));
	// G = Integer.toHexString(255 - Integer.parseInt(G, 16));
	// B = Integer.toHexString(255 - Integer.parseInt(B, 16));
	// R = R.length() == 1 ? "0" + R : R;
	// G = G.length() == 1 ? "0" + G : G;
	// B = B.length() == 1 ? "0" + B : B;
	// return "#" + R + G + B;
	// }
	// return null;
	// }

	private double step;

	private int height = 13;

	private String color = "#aaa";

	private String linearStartColor = "#eee";

	public ProgressElement() {
	}

	public ProgressElement(final double step) {
		this.step = step;
	}

	public double getStep() {
		return step;
	}

	public ProgressElement setStep(final double step) {
		this.step = step;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public ProgressElement setHeight(final int height) {
		this.height = height;
		return this;
	}

	@Override
	public String getColor() {
		return color;
	}

	@Override
	public ProgressElement setColor(final String color) {
		this.color = color;
		return this;
	}

	public String getLinearStartColor() {
		return linearStartColor;
	}

	public ProgressElement setLinearStartColor(final String linearStartColor) {
		this.linearStartColor = linearStartColor;
		return this;
	}

	@Override
	public String getText() {
		final StringBuilder sb = new StringBuilder();
		final SpanElement ele = new SpanElement().setClassName("step");
		final double step = Math.min(getStep(), 1d);
		String ss;
		if (step > 0) {
			ss = NumberUtils.format((step * 100)) + "%";
			ele.addStyle("width: " + ss);
		} else {
			ss = "0.0%";
		}
		final String color = getColor();
		if (StringUtils.hasText(color)) {
			final String linearStartColor = getLinearStartColor();
			ele.addStyle("background-color: " + color
					+ ";background-image: linear-gradient(to bottom, " + linearStartColor + " 0, "
					+ color + " 50%, " + linearStartColor + " 100%);");
		}
		final SpanElement txt = new SpanElement(ss).setClassName("txt");
		final int height = getHeight();
		if (height > 0) {
			txt.addStyle("line-height: " + height + "px;");
		}
		sb.append(txt).append(ele);
		return sb.toString();
	}

	@Override
	protected void doAttri(final StringBuilder sb) {
		addClassName("simple_progress");
		final String color = getColor();
		if (StringUtils.hasText(color)) {
			addStyle("border-color: " + color);
		}
		final int height = getHeight();
		if (height > 0) {
			addStyle("height: " + height + "px;");
		}
		super.doAttri(sb);
	}
}
