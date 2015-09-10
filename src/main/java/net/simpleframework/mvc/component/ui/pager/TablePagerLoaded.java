package net.simpleframework.mvc.component.ui.pager;

import static net.simpleframework.common.I18n.$m;

import java.util.List;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.calendar.CalendarBean;
import net.simpleframework.mvc.component.ui.menu.EMenuEvent;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.window.WindowBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TablePagerLoaded extends PagerLoaded {

	@Override
	public void onBeforeComponentRender(final PageParameter pp) {
		super.onBeforeComponentRender(pp);

		final ComponentParameter nCP = PagerUtils.get(pp);
		final ITablePagerHandler tptbl = (ITablePagerHandler) nCP.getComponentHandler();
		final String hashId = nCP.hashId();

		// boolean bTooltip = false;
		// for (final TablePagerColumn col :
		// TablePagerUtils.getTablePagerSchema(nCP)
		// .getTablePagerColumns(nCP)) {
		// if (StringUtils.hasText(col.getTooltip())) {
		// bTooltip = true;
		// break;
		// }
		// }
		// if (bTooltip) {
		// // 列头的 tip
		// final TooltipBean tooltip = nCP.addComponentBean("tpTooltip_" + hashId,
		// TooltipBean.class);
		// tooltip.getTips().add(
		// new TipBean(tooltip).setStem(ETipPosition.bottomMiddle).setDelay(0.5)
		// .setHideAfter(0.5).setOffsetY(5)
		// .setHook(new Hook(ETipPosition.topMiddle, ETipPosition.bottomMiddle))
		// .setHideOn(new HideOn(ETipElement.target, EElementEvent.mouseleave))
		// .setWidth(240));
		// }

		if (tptbl instanceof AbstractTablePagerHandler) {
			// 添加菜单组件
			MenuBean menuBean = (MenuBean) nCP
					.addComponentBean("ml_" + hashId + "_Menu", MenuBean.class)
					.setMenuEvent(EMenuEvent.click)
					.setJsBeforeShowCallback("TableUtils.contextMenu_ShowCallback(menu);")
					.setHandlerClass(TablePagerMenu.class);
			List<MenuItem> items = TablePagerMenu.getContextMenu(nCP, menuBean);
			if (items == null) {
				nCP.removeComponentBean(menuBean);
			}

			menuBean = (MenuBean) nCP.addComponentBean("ml_" + hashId + "_Menu2", MenuBean.class)
					.setMenuEvent(EMenuEvent.click).setMinWidth("140")
					.setHandlerClass(TablePagerMenu2.class);
			items = TablePagerMenu2.getHeaderMenu(nCP, menuBean);
			if (items == null) {
				nCP.removeComponentBean(menuBean);
			}
		}

		if ((Boolean) nCP.getBeanProperty("filter")) {
			// 删除过滤
			nCP.addComponentBean("tpFilterDelete_" + hashId, AjaxRequestBean.class)
					.setHandlerMethod("doFilterDelete").setHandlerClass(TablePagerAction.class);

			// 过滤
			nCP.addComponentBean("tpFilter_" + hashId, AjaxRequestBean.class)
					.setHandlerMethod("doFilter2").setHandlerClass(TablePagerAction.class);

			// 过滤高级窗口
			nCP.addComponentBean("tpFilterPage_" + hashId, AjaxRequestBean.class).setUrlForward(
					ComponentUtils.getResourceHomePath(TablePagerBean.class)
							+ "/jsp/tablepager_filter.jsp");
			nCP.addComponentBean("tpFilterWindow_" + hashId, WindowBean.class)
					.setContentRef("tpFilterPage_" + hashId).setTitle($m("TablePagerLoaded.0"))
					.setHeight(220).setWidth(420);
		}

		if ((Boolean) nCP.getBeanProperty("editable")) {
			nCP.addComponentBean("rowEditorCalendar", CalendarBean.class);

			// 编辑行
			nCP.addComponentBean("tpRowEdit_" + hashId, AjaxRequestBean.class)
					.setHandlerMethod("doRowEdit").setHandlerClass(TablePagerAction.class);

			// 添加行
			nCP.addComponentBean("tpRowAdd_" + hashId, AjaxRequestBean.class).setParallel(true)
					.setHandlerMethod("doRowAdd").setHandlerClass(TablePagerAction.class);

			// 保存行数据
			final AjaxRequestBean ajaxRequest = (AjaxRequestBean) nCP
					.addComponentBean("tpRowSave_" + hashId, AjaxRequestBean.class)
					.setHandlerMethod("doRowSave").setHandlerClass(TablePagerAction.class);
			final String msg = (String) nCP.getBeanProperty("rowSaveConfirmMessage");
			if (StringUtils.hasText(msg)) {
				ajaxRequest.setConfirmMessage(msg);
			}
		}
	}
}
