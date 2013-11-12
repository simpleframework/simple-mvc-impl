/**
 * @author 陈侃(cknet@126.com, 13910090885)
 */
$UI.FormEditor = Class.create( {
	inputs : [ "text", "hidden", "checkbox", "password", "button" ],

	initialize : function(container, datas, options) {
		this.options = {
			title : null,
			onLoaded : Prototype.emptyFunction
		};
		Object.extend(this.options, options || {});

		container = $(container);
		
		var formeditor = new Element("DIV", {
			className : "formeditor"
		}).insert($UI.createTable());
		
		if (this.options.title) {
			var t = new Element("div", {
				className : "formeditor_title t1"
			});
			container.insert(t.insert(this.options.title));
			if (this.options.titleToggle) {
				var img = new Element("img", {
					src : HOME_PATH + "/css/" + SKIN + "/images/toggle.png"
				});
				t.insert(new Element("div", {
					className : "fe_expand"
				}).update(img));
				$UI.doImageToggle(img, formeditor);
			}
		}

		container.insert(formeditor);
		var tbody = container.down("tbody");
		datas = datas || [];
		var i = 0;
		datas.each(function(data) {
			var tr = new Element("tr");
			tbody.insert(tr);
			this.createTip(tr, data.desc);

			var row = (i++ == 0) ? "fe_firstrow " : "fe_row ";
			if (i == datas.length)
				row += "fe_lastrow ";
			var td0 = new Element("td", {
				className : row + "fe_label"
			}).update(data.label ? data.label : "");
			if (data.labelStyle) {
				td0.setStyle(data.labelStyle);
			}
			var td1 = new Element("td", {
				className : row + "fe_value"
			});
			tr.insert(td0).insert(td1);
			(data.components || []).each(function(d) {
				if (d.type == "textButton") {
					var ele = $UI.createButtonInputField();
					this._setId(ele.textInput, d);
					this._setValue(ele.textInput, d);
					ele.addClassName("text_comp");
					this._setAttributes(ele.textInput, d);
					this._setEvent(ele.textButton, d);
					td1.insert(ele);
				} else if (d.type == "file") {
					var ele = $UI.createFileInputField();
					this._setId(ele.file, d);
					ele.textButton.addClassName("text_comp");
					this._setAttributes(ele, d);
					td1.insert(ele);
				} else {
					var c;
					if (this.inputs.include(d.type)) {
						c = new Element("input", {
							type : d.type
						});
						if (d.type == "text" || d.type == "password") {
							c.addClassName("text_comp");
						}
						if (d.type == "checkbox") {
							c.setStyle("margin-left: 6px;");
						}
					} else if (d.type == "textarea") {
						c = new Element("textarea", {
							rows : 3
						});
						c.addClassName("text_comp");
					} else if (d.type == "select") {
						c = new Element("select", {
							style : "margin-left: 2px;"
						});
					} else if (d.type == "div") {
						c = new Element("div", {
							style : "margin: 2px;"
						});
					}
					if (c) {
						this._setId(c, d);
						this._setAttributes(c, d);
						this._setValue(c, d);
						this._setEvent(c, d);
						td1.insert(c);
					}
				}
			}.bind(this));
		}.bind(this));

		var lo = this.options.onLoaded;
		if (lo)
			lo();
	},

	_setId : function(c, d) {
		if (d.name) {
			c.writeAttribute("id", d.name).writeAttribute("name", d.name);
		}
	},

	_setAttributes : function(c, d) {
		if (d.style) {
			c.setStyle(d.style);
		}
		if (d.attributes) {
			Object.keys(d.attributes).each(function(key) {
				c.writeAttribute(key, d.attributes[key]);
			});
		}
	},

	_setValue : function(c, d) {
		if (d.defaultValue) {
			if (d.type == "select") {
				var s = d.defaultValue;
				$A(s.include("\n") ? s.split("\n") : s.split(";")).each(function(opt) {
					var a = opt.strip().split("=");
					if (a.length == 2) {
						c.insert(new Element("option", {
							value : a[0]
						}).update(a[1]));
					} else {
						c.insert(new Element("option").update(a[0]));
					}
				});
			} else if (d.type == "div") {
				c.update(d.defaultValue);
			} else {
				c.value = d.defaultValue;
			}
		}
	},

	_setEvent : function(c, d) {
		if (!d.events)
			return;
		Object.keys(d.events).each(function(key) {
			c.observe(key, function(e) {
				eval(d.events[key]);
			});
		});
	},

	createTip : function(ele, tip) {
		if (tip) {
			new Tip(ele, tip, {
				stem : 'topLeft',
				hook : {
					target : 'bottomLeft'
				},
				offset : {
					x : 4,
					y : 1
				}
			});
		}
	}
});