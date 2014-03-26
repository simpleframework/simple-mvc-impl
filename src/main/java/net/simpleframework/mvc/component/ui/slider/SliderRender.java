package net.simpleframework.mvc.component.ui.slider;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.ComponentUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class SliderRender extends ComponentJavascriptRender {

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final SliderBean sliderBean = (SliderBean) cp.componentBean;

		final StringBuilder sb = new StringBuilder();
		sb.append(ComponentRenderUtils.initContainerVar(cp));
		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		sb.append(actionFunc).append(".slider = ").append("new $UI.Slider(")
				.append(ComponentRenderUtils.VAR_CONTAINER).append(", {");
		sb.append("xMinValue: ").append(sliderBean.getXminValue()).append(",");
		sb.append("xMaxValue: ").append(sliderBean.getXmaxValue()).append(",");
		sb.append("yMinValue: ").append(sliderBean.getYminValue()).append(",");
		sb.append("yMaxValue: ").append(sliderBean.getYmaxValue()).append(",");

		sb.append("arrowImage: \"");
		final String arrowImage = sliderBean.getArrowImage();
		if (StringUtils.hasText(arrowImage)) {
			sb.append(cp.wrapContextPath(arrowImage));
		} else {
			sb.append(ComponentUtils.getCssResourceHomePath(cp));
			sb.append("/images/");
			final int xmaxValue = sliderBean.getXmaxValue();
			final int ymaxValue = sliderBean.getYmaxValue();
			if (xmaxValue > 0 && ymaxValue > 0) {
				sb.append("arrows_point.gif");
			} else if (xmaxValue > 0) {
				sb.append("arrows_h.gif");
			} else {
				sb.append("arrows_v.gif");
			}
		}
		sb.append("\"");
		sb.append("});");
		final String jsChangeCallback = (String) cp.getBeanProperty("jsChangeCallback");
		if (StringUtils.hasText(jsChangeCallback)) {
			sb.append(actionFunc).append(".slider.onValuesChanged = function(slider) {");
			sb.append("var callback = function(x, y) {");
			sb.append(jsChangeCallback);
			sb.append("};");
			sb.append("callback(slider.xValue, slider.yValue);");
			sb.append("};");
		}
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString());
	}
}
