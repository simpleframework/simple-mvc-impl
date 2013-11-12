/**
 * @author 陈侃(cknet@126.com, 13910090885)
 */
$UI.ListBox = Class.create({
	initialize : function(container, datas, options) {
		this.options = Object.extend({
			checkbox : false,
			tooltip : true
		}, options);

		this.current = null;
		this.items = [];

		this.container = $(container);

		this.datas = datas || [];

		this.container.onselectstart = function() {
			return false;
		};

		this.element = new Element("div", {
			className : "listbox"
		});

		this.container.update(this.element);
		for ( var i = 0; i < this.datas.length; i++) {
			this.add(this.datas[i]);
		}

		this.cssUpdate();

		Event.observe(document, 'keydown', this._keyPress
				.bindAsEventListener(this));
		// Event.observe(document, 'mouseup', (function() {
		// if (this.current != null)
		// this.current.unselect();
		// }).bind(this));
	},

	_upDownAction : function(up) {
		if (this.current != null) {
			var sibling = up ? this.current.element.previousSibling
					: this.current.element.nextSibling;
			if (sibling && sibling.item) {
				sibling.item.select();
			}
		}
	},

	_returnAction : function() {
		if (this.current != null) {
			this.current.check(!this.current.isCheck());
		}
	},

	_keyPress : function(ev) {
		var code = (ev.which) ? ev.which : ev.keyCode;
		switch (code) {
		case Event.KEY_UP:
			this._upDownAction(true);
			break;
		case Event.KEY_DOWN:
			this._upDownAction(false);
			break;
		case Event.KEY_RETURN:
			this._returnAction();
			break;
		}
	},

	cssUpdate : function() {
		var i = 0;
		this.items.each(function(box) {
			box.element.addClassName((i++ % 2) == 0 ? 'odd' : 'even');
		});
	},

	setStyle : function(style) {
		this.element.setStyle(style);
	},

	add : function(data) {
		var box = new ListItem(this, data);
		this.items.push(box);
		return box;
	},

	getCheckedItems : function() {
		var checked = [];
		for ( var i = 0; i < this.items.length; i++) {
			if (this.items[i].isCheck()) {
				checked.push(this.items[i]);
			}
		}
		return checked;
	},

	getSelectedItems : function() {
		var selected = [];
		for ( var i = 0; i < this.items.length; i++) {
			if (this.items[i].isSelected()) {
				selected.push(this.items[i]);
			}
		}
		return selected;
	},

	get : function(id) {
		return items.find(function(item) {
			return item.id == id;
		});
	},

	remove : function(item) {
	}
});

ListItem = Class.create({
	initialize : function(listbox, data) {
		this.listbox = listbox;
		this.data = data;

		var tbl = "<table width='100%' height='100%' cellpadding='0' cellspacing='0'><tr>";
		if (listbox.options.checkbox) {
			tbl += "<td style='width: 19px;' valign='middle'>";
			tbl += "<div class='box uncheck'></div></td>";
		}
		tbl += "<td><div class='data'></div></td></tr></table>";
		this.element = new Element("div", {
			className : 'item'
		}).update(tbl);
		listbox.element.insert(this.element);
		this.element.select(".data")[0].update(data.text || "");

		this.element.item = this;

		this.element.observe("click", (function(ev) {
			this.select();
			if (this.data.click) {
				this.data.click(this, ev);
			}
		}).bind(this));
		this.element.observe("dblclick", (function(ev) {
			this.select();
			if (this.data.dblclick) {
				this.data.dblclick(this, ev);
			}
		}).bind(this));

		var box = this.element.select(".box");
		if (box && box.length == 1)
			this.checkbox = box[0];
		if (this.checkbox) {
			this.checkbox.observe("click", (function(ev) {
				this.check(!this.isCheck());
				Event.stop(ev);
			}).bind(this));
		}
		if (data.tip && listbox.options.tooltip) {
			new Tip(this.element, data.tip, {
				stem : 'leftTop'
			});
		}
	},

	getId : function() {
		return this.data.id;
	},

	getText : function() {
		return this.data.text;
	},

	select : function() {
		if (this.listbox.current) {
			this.listbox.current.element.removeClassName("select");
		}
		this.element.addClassName("select");
		this.listbox.current = this;
	},

	unselect : function() {
		this.element.removeClassName("select");
		if (this.listbox.current == this.element) {
			this.listbox.current = null;
		}
	},
	
	isSelected : function() {
		return this.element.hasClassName("select");
	},

	check : function(checked) {
		if (!this.listbox.options.checkbox) {
			return;
		}
		if (checked) {
			this.checkbox.removeClassName("uncheck");
			this.checkbox.addClassName("check");
			this.element.addClassName("checked");
		} else {
			this.checkbox.removeClassName("check");
			this.checkbox.addClassName("uncheck");
			this.element.removeClassName("checked");
		}
	},
	
	isCheck : function() {
		return this.checkbox && this.checkbox.hasClassName("check");
	}
});