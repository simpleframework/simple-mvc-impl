/**
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
$UI.Tabs = Class.create({
	initialize : function(container, datas, options) {
		this.tabs = [];
		this.options = Object.extend({
			activeIndex : 0
		}, options);
		
		container = $(container);
		container.update(this.element = new Element("div", {
			className : "tabs"
		}));
		
		var ul = new Element("ul", {
			className : "simple_tabs"
		});
		this.element.insert(ul);
		this.content = new Element("div", {
			className : 'simple_tabs_content'
		});
		this.element.insert(this.content);

		var i = 0;
		$A(datas || []).each(function(data) {
			var a = $UI.createLink(data.title, null, function(ev) {
				if (this.current != tab)
					this.active(tab.index);
			}.bind(this));
			var li = new Element("li").insert(a);
			ul.insert(li);
			var tab = {
				'data' : data,
				'li' : li,
				'index' : i++
			};
			this.tabs.push(tab);
		}.bind(this));

		this.active(this.options.activeIndex);
	},

	active : function(index) {
		if (this.tabs.length == 0 || this.doingActive)
			return;
		this.doingActive = true;
		var a = function(tab) {
			return tab.li.down();
		};

		var idPrefix = this.content.identify() + "_";
		var otab = this.current;
		var tab_c;
		if (otab) {
			a(otab).removeClassName("active");
			tab_c = this.content.down("#" + idPrefix + otab.index);
			if (tab_c) {
				if (otab.data.cache) {
					tab_c.hide();
				} else {
					tab_c.remove();
				}
			}
		}

		var tab = this.current = this.tabs[index];
		a(tab).addClassName("active");
		tab_c = this.content.down("#" + idPrefix + index);
		var new_tab = !tab_c;
		if (new_tab) {
			tab_c = new Element("div", {
				id : idPrefix + index
			}).update(tab.data.content ? tab.data.content : "&nbsp;");
			if (tab.data.contentStyle) {
				tab_c.setStyle(tab.data.contentStyle);
			}
			tab_c.hide();
			this.content.insert(tab_c);
			if (tab.data.onContentLoaded)
				$call(tab.data.onContentLoaded);
		}
		if (tab.data.contentRef && new_tab) {
			$Actions.callSafely(tab.data.contentRef, this.options.parameters,
					function(action) {
						action.container = tab_c;
					});
		}
		tab_c.$show({
			effects : this.options.effects,
			afterFinish : function(effect) {
				if (tab.data.onActive)
					$call(tab.data.onActive);
				this.doingActive = false;
			}.bind(this)
		});
	}
});
