package net.simpleframework.mvc;

import static net.simpleframework.common.I18n.$m;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.ComponentHandlerException;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;
import net.simpleframework.mvc.component.base.submit.AbstractSubmitHandler;
import net.simpleframework.mvc.component.base.submit.SubmitBean;
import net.simpleframework.mvc.component.base.validation.ValidationBean;
import net.simpleframework.mvc.component.ui.calendar.CalendarBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;
import net.simpleframework.mvc.component.ui.listbox.AbstractListboxHandler;
import net.simpleframework.mvc.component.ui.listbox.ListItems;
import net.simpleframework.mvc.component.ui.listbox.ListboxBean;
import net.simpleframework.mvc.component.ui.menu.AbstractMenuHandler;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.menu.MenuItems;
import net.simpleframework.mvc.component.ui.pager.ITablePagerHandler;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.tree.AbstractTreeHandler;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;
import net.simpleframework.mvc.component.ui.window.WindowBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractBasePage extends AbstractMVELTemplatePage {

	/* AjaxRequestBean */
	protected AjaxRequestBean addAjaxRequest(final PageParameter pp, final String name) {
		return (AjaxRequestBean) addComponentBean(pp, name, AjaxRequestBean.class)
				.setRole(getPageRole(pp)).setHandlerClass(getClass());
	}

	/* AjaxRequestBean */
	protected AjaxRequestBean addAjaxRequest(final PageParameter pp, final String name,
			final Class<? extends AbstractMVCPage> pageClass) {
		return addComponentBean(pp, name, AjaxRequestBean.class).setRole(getPageRole(pp))
				.setUrlForward(url(pageClass));
	}

	protected AjaxRequestBean addDeleteAjaxRequest(final PageParameter pp, final String name) {
		return addAjaxRequest(pp, name).setConfirmMessage($m("Confirm.Delete"))
				.setHandlerMethod("doDelete");
	}

	/* ValidationBean */
	protected ValidationBean addValidationBean(final PageParameter pp, final String name) {
		return addComponentBean(pp, name, ValidationBean.class);
	}

	/* TreeBean */
	protected TreeBean addTreeBean(final PageParameter pp, final String name) {
		return (TreeBean) addComponentBean(pp, name, TreeBean.class).setHandlerClass(getClass());
	}

	/* ListboxBean */
	protected ListboxBean addListBoxBean(final PageParameter pp, final String name) {
		return (ListboxBean) addComponentBean(pp, name, ListboxBean.class)
				.setHandlerClass(getClass());
	}

	/* TablePagerBean */
	protected TablePagerBean addTablePagerBean(final PageParameter pp, final String name,
			final Class<? extends ITablePagerHandler> tblClass) {
		final TablePagerBean tablePager = (TablePagerBean) addComponentBean(pp, name,
				TablePagerBean.class).setResize(false).setPageItems(30).setRole(getPageRole(pp))
						.setExportAction("false");
		if (tblClass != null) {
			tablePager.setHandlerClass(tblClass);
		}
		return tablePager;
	}

	/* WindowBean */
	protected WindowBean addWindowBean(final PageParameter pp, final String name) {
		return addComponentBean(pp, name, WindowBean.class);
	}

	protected WindowBean addWindowBean(final PageParameter pp, final String name,
			final AjaxRequestBean ajaxRequest) {
		return addWindowBean(pp, name).setContentRef(ajaxRequest.getName());
	}

	/* DictionaryBean */
	protected DictionaryBean addDictionaryBean(final PageParameter pp, final String name) {
		return addComponentBean(pp, name, DictionaryBean.class);
	}

	protected DictionaryBean addDictionaryBean(final PageParameter pp, final String name,
			final boolean multiple) {
		return addComponentBean(pp, new KVMap().add("name", name).add("multiple", multiple),
				DictionaryBean.class);
	}

	/* CalendarBean */
	protected CalendarBean addCalendarBean(final PageParameter pp, final String name) {
		return addComponentBean(pp, name, CalendarBean.class);
	}

	public static class AjaxRequest extends DefaultAjaxRequestHandler {

		@Override
		@SuppressWarnings("unchecked")
		public IForward ajaxProcess(final ComponentParameter cp) {
			try {
				final AjaxRequestBean ajaxRequestBean = (AjaxRequestBean) cp.componentBean;
				final Class<AbstractMVCPage> handlerClass = (Class<AbstractMVCPage>) ClassUtils
						.forName(ajaxRequestBean.getHandlerClass());
				return (IForward) handlerClass
						.getMethod(StringUtils.text(ajaxRequestBean.getHandlerMethod(), "ajaxProcess"),
								ComponentParameter.class)
						.invoke(AbstractMVCPage.get(cp), cp);
			} catch (final Exception e) {
				throw ComponentHandlerException.of(e);
			}
		}
	}

	public static class Submit extends AbstractSubmitHandler {

		@Override
		@SuppressWarnings("unchecked")
		public AbstractUrlForward submit(final ComponentParameter cp) {
			try {
				final SubmitBean submitBean = (SubmitBean) cp.componentBean;
				final Class<AbstractMVCPage> handlerClass = (Class<AbstractMVCPage>) ClassUtils
						.forName(submitBean.getHandlerClass());
				return (AbstractUrlForward) handlerClass
						.getMethod(submitBean.getHandlerMethod(), ComponentParameter.class)
						.invoke(AbstractMVCPage.get(cp), cp);
			} catch (final Exception e) {
				throw ComponentHandlerException.of(e);
			}
		}
	}

	public static class Tree extends AbstractTreeHandler {

		@Override
		@SuppressWarnings("unchecked")
		public TreeNodes getTreenodes(final ComponentParameter cp, final TreeNode treeNode) {
			try {
				final TreeBean treeBean = (TreeBean) cp.componentBean;
				final Class<AbstractMVCPage> handlerClass = (Class<AbstractMVCPage>) ClassUtils
						.forName(treeBean.getHandlerClass());
				return (TreeNodes) handlerClass
						.getMethod("getTreenodes", ComponentParameter.class, TreeNode.class)
						.invoke(AbstractMVCPage.get(cp), cp, treeNode);
			} catch (final Exception e) {
				throw ComponentHandlerException.of(e);
			}
		}
	}

	public static class Listbox extends AbstractListboxHandler {
		@SuppressWarnings("unchecked")
		@Override
		public ListItems getListItems(final ComponentParameter cp) {
			try {
				final ListboxBean listbox = (ListboxBean) cp.componentBean;
				final Class<AbstractMVCPage> handlerClass = (Class<AbstractMVCPage>) ClassUtils
						.forName(listbox.getHandlerClass());
				return (ListItems) handlerClass.getMethod("getListItems", ComponentParameter.class)
						.invoke(AbstractMVCPage.get(cp), cp);
			} catch (final Exception e) {
				throw ComponentHandlerException.of(e);
			}
		}
	}

	public static class Menu extends AbstractMenuHandler {

		@Override
		@SuppressWarnings("unchecked")
		public MenuItems getMenuItems(final ComponentParameter cp, final MenuItem menuItem) {
			try {
				final MenuBean menuBean = (MenuBean) cp.componentBean;
				final Class<AbstractMVCPage> handlerClass = (Class<AbstractMVCPage>) ClassUtils
						.forName(menuBean.getHandlerClass());
				return (MenuItems) handlerClass
						.getMethod("getMenuItems", ComponentParameter.class, MenuItem.class)
						.invoke(AbstractMVCPage.get(cp), cp, menuItem);
			} catch (final Exception e) {
				throw ComponentHandlerException.of(e);
			}
		}
	}

	@Override
	public IComponentHandler createComponentHandler(final ComponentParameter cp) {
		final AbstractComponentBean componentBean = cp.componentBean;
		if (componentBean instanceof AjaxRequestBean) {
			return singleton(AjaxRequest.class);
		} else if (componentBean instanceof SubmitBean) {
			return singleton(Submit.class);
		} else if (componentBean instanceof TreeBean) {
			return singleton(Tree.class);
		} else if (componentBean instanceof ListboxBean) {
			return singleton(Listbox.class);
		} else if (componentBean instanceof MenuBean) {
			return singleton(Menu.class);
		}
		return null;
	}
}
