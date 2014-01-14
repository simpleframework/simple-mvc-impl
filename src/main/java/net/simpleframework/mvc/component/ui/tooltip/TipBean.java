package net.simpleframework.mvc.component.ui.tooltip;

import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.common.xml.AbstractElementBean;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.common.element.EElementEvent;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TipBean extends AbstractElementBean {

	public TipBean(final XmlElement xmlElement, final TooltipBean tooltipBean) {
		super(xmlElement);
	}

	public TipBean(final TooltipBean tooltipBean) {
		this(null, tooltipBean);
	}

	private ETipStyle tipStyle;

	private String selector, title, content;

	private int width = 200;

	private String contentRef;

	private boolean cache;

	private int radius = 6;

	private boolean fixed;

	private double delay = 0.14;

	private EElementEvent showOn;

	private double hideAfter = 0.1;

	private boolean hideOthers = true;

	private HideOn hideOn;

	private Hook hook;

	private ETipPosition stem;

	private int offsetX, offsetY;

	private String target;

	private String jsTipCreate;

	public ETipStyle getTipStyle() {
		return tipStyle != null ? tipStyle : ETipStyle.tipDefault;
	}

	public TipBean setTipStyle(final ETipStyle tipStyle) {
		this.tipStyle = tipStyle;
		return this;
	}

	public String getSelector() {
		return selector;
	}

	public TipBean setSelector(final String selector) {
		this.selector = selector;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public TipBean setTitle(final String title) {
		this.title = title;
		return this;
	}

	public String getContent() {
		return StringUtils.blank(content);
	}

	public TipBean setContent(final String content) {
		this.content = content;
		return this;
	}

	public String getContentRef() {
		return contentRef;
	}

	public TipBean setContentRef(final String contentRef) {
		this.contentRef = contentRef;
		return this;
	}

	public boolean isCache() {
		return cache;
	}

	public TipBean setCache(final boolean cache) {
		this.cache = cache;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public TipBean setWidth(final int width) {
		this.width = width;
		return this;
	}

	public int getRadius() {
		return radius;
	}

	public TipBean setRadius(final int radius) {
		this.radius = radius;
		return this;
	}

	public boolean isFixed() {
		return fixed;
	}

	public TipBean setFixed(final boolean fixed) {
		this.fixed = fixed;
		return this;
	}

	public double getDelay() {
		return delay;
	}

	public TipBean setDelay(final double delay) {
		this.delay = delay;
		return this;
	}

	public EElementEvent getShowOn() {
		return showOn;
	}

	public TipBean setShowOn(final EElementEvent showOn) {
		this.showOn = showOn;
		return this;
	}

	public ETipPosition getStem() {
		return stem;
	}

	public TipBean setStem(final ETipPosition stem) {
		this.stem = stem;
		return this;
	}

	public double getHideAfter() {
		return hideAfter;
	}

	public TipBean setHideAfter(final double hideAfter) {
		this.hideAfter = hideAfter;
		return this;
	}

	public boolean isHideOthers() {
		return hideOthers;
	}

	public TipBean setHideOthers(final boolean hideOthers) {
		this.hideOthers = hideOthers;
		return this;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public TipBean setOffsetX(final int offsetX) {
		this.offsetX = offsetX;
		return this;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public TipBean setOffsetY(final int offsetY) {
		this.offsetY = offsetY;
		return this;
	}

	public String getTarget() {
		return target;
	}

	public TipBean setTarget(final String target) {
		this.target = target;
		return this;
	}

	public HideOn getHideOn() {
		return hideOn;
	}

	public TipBean setHideOn(final HideOn hideOn) {
		this.hideOn = hideOn;
		return this;
	}

	public Hook getHook() {
		return hook;
	}

	public TipBean setHook(final Hook hook) {
		this.hook = hook;
		return this;
	}

	public String getJsTipCreate() {
		return jsTipCreate;
	}

	public TipBean setJsTipCreate(final String jsTipCreate) {
		this.jsTipCreate = jsTipCreate;
		return this;
	}

	public static class HideOn extends AbstractElementBean {

		public HideOn(final XmlElement xmlElement) {
			super(xmlElement);
		}

		public HideOn() {
			this(null);
		}

		public HideOn(final ETipElement tipElement, final EElementEvent event) {
			this(null);
			setTipElement(tipElement);
			setEvent(event);
		}

		private ETipElement tipElement;

		private EElementEvent event;

		public ETipElement getTipElement() {
			return tipElement == null ? ETipElement.target : tipElement;
		}

		public HideOn setTipElement(final ETipElement tipElement) {
			this.tipElement = tipElement;
			return this;
		}

		public EElementEvent getEvent() {
			return event == null ? EElementEvent.mouseout : event;
		}

		public HideOn setEvent(final EElementEvent event) {
			this.event = event;
			return this;
		}
	}

	public static class Hook extends AbstractElementBean {

		public Hook() {
		}

		public Hook(final ETipPosition target, final ETipPosition tip) {
			setTarget(target);
			setTip(tip);
		}

		private ETipPosition target;

		private ETipPosition tip;

		private boolean mouse;

		public ETipPosition getTarget() {
			return target == null ? ETipPosition.topLeft : target;
		}

		public Hook setTarget(final ETipPosition target) {
			this.target = target;
			return this;
		}

		public ETipPosition getTip() {
			return tip == null ? ETipPosition.bottomLeft : tip;
		}

		public Hook setTip(final ETipPosition tip) {
			this.tip = tip;
			return this;
		}

		public boolean isMouse() {
			return mouse;
		}

		public Hook setMouse(final boolean mouse) {
			this.mouse = mouse;
			return this;
		}
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsTipCreate", "content" };
	}
}
