/**
 * @author 陈侃(cknet@126.com, 13910090885)
 */
$UI.ProgressBar = Class.create( {
	initialize : function(container, options) {
		this.options = {
			afterChange : Prototype.emptyFunction,
			interval : 1,
			maxProgressValue : 100,
			url : "",
			showText : true,
			startAfterCreate : true,
			showAbortAction : true,
			showDetailAction : true,
			detailHeight : "120px"
		};
		Object.extend(this.options, options || {});

		container = $(container);
		
		this.step = this.options.step || 0;

		this.showMenu = this.options.showAbortAction || this.options.showDetailAction;
		this.menuWidth = this.showMenu ? 10 : 0;

		var bg = new Element("div", {
			className : "bg",
			style : "right:" + this.menuWidth + "px"
		});
		
		this.progress = new Element("div", {
			className : "progressbar"
		}).insert(bg);
		if (this.options.showText) {
      var text = new Element("div", {
        className : "pb_text",
        style : "line-height:" + (container.getHeight() - 1) + "px"
      }).update("&nbsp;");
      this.progress.insert(text);
    }
		container.update(this.progress);

		var items = [];
		if (this.options.showAbortAction)
			items.push( {
				name : $MessageConst["Progressbar.0"],
				onSelect : this._abortAction.bind(this)
			});
		if (this.options.showDetailAction) {
			items.push( {
				name : $MessageConst["Progressbar.1"],
				onSelect : this._detailAction.bind(this)
			});
		}
		if (items.length > 0) {
			var btn = new Element("div", {
				className : "btn"
			}).update("&nbsp;");
			this.progress.insert(btn);
			new $UI.Menu(null, {
				selector : "#" + btn.identify(),
				menuEvent : "click",
				minWidth : "100px",
				menuItems : items
			});
		}

		bg.setStyle("width:" + this._getBackgroundWidth() + "px");
		if (this.step > 0) 
			this.setProgress(this.step);
		if (this.options.startAfterCreate)
			this.start();
	},

	_abortAction : function(item, e) {
	},

	_detailAction : function(item, e) {
		var detail = this.progress.next(".progressbar_detail");
		if (!detail) {
			detail = new Element("div", {
				className : "progressbar_detail",
				style : "display:none"
			}).insert(new Element("div", {
				className : "menu"
			})).insert(new Element("div", {
				className : "content",
				style : "height:" + this.options.detailHeight
			}));

			this.progress.insert( {
				after : detail
			});
			this._createDetailMenu(detail);
		}
		$Effect.slideDown(detail, {
			effects : this.options.effects
		});
	},

	_createDetailMenu : function(detail) {
		var content = detail.down(".content");
		var items = [ {
			name : $MessageConst["Progressbar.2"],
			onSelect : function(item, e) {
				this._removeMessages(content);
			}.bind(this)
		}, {
			name : $MessageConst["Progressbar.3"],
			onSelect : function(item, e) {
				var url = this.options.url;
				url = url.addParameter("messages=true");
				new Ajax.Request(url, {
					onComplete : function(request) {
						var messages = request.responseText.evalJSON();
						if (messages) {
							this._removeMessages(content);
							$A(messages).each(function(message) {
								this._updateMessage(content, message);
							}.bind(this));
						}
					}.bind(this)
				});
			}.bind(this)
		}, {
			name : $MessageConst["Progressbar.4"],
			checkbox : true,
			checked : this.noScroll,
			onCheck : function(item, e) {
				this.noScroll = !item.isItemChecked();
				item.setItemChecked(this.noScroll);
			}.bind(this)
		}, {
			separator : true
		}, {
			name : $MessageConst["Progressbar.5"],
			onSelect : function(item, e) {
				$Effect.remove(detail, {
					effects : this.options.effects
				});
			}.bind(this)
		} ];
		new $UI.Menu(null, {
			selector : "#" + detail.down(".menu").identify(),
			menuEvent : "click",
			minWidth : "150px",
			menuItems : items
		});
	},

	_ajaxComplete : function(request) {
		var json = request.responseText.evalJSON();
		if (!json) {
			this.stop();
			return;
		}
		if (json.abort) {
			this.stop();
			return;
		}
		if (json.maxProgressValue > 0) {
			this.options.maxProgressValue = json.maxProgressValue;
		}
		this.setProgress(json.step, function() {
			var detail = this.progress.next(".progressbar_detail");
			if (detail && json.message) {
				this._updateMessage(detail.down(".content"), json.message);
			}
		}.bind(this));
	},

	_removeMessages : function(content) {
		content.select(".msg").invoke("remove");
		content.scrollTop = 0;
	},

	_updateMessage : function(content, message) {
		content.insert(new Element("div", {
			className : "msg m1"
		}).update(message));
		if (!this.noScroll)
			content.scrollTop = content.scrollHeight;
	},

	_getBackgroundWidth : function() {
		return this.progress.getWidth() - this.menuWidth - 2;
	},
	
	updateUI : function() {
		this._draw();
	},
	
	_draw : function() {
		if (this.drawing) return;
		var w = this._getBackgroundWidth();
		var bg = this.progress.down(".bg");
		w = w - Math.floor((this.step / this.options.maxProgressValue) * w);
		$Effect.style(bg, "width:" + w + "px", {
			effects : this.options.effects,
			afterFinish : function(effect) {
				this.drawing = false;
			}.bind(this)
		});
		var t = Math.round((this.step / this.options.maxProgressValue) * 10000) / 100;
		t = Math.min(t, 100);
		t = Math.max(t, 0);
		bg.setStyle("border-left-width: " + (t < 100 ? 1 : 0) + "px;");
		var txt = this.progress.down(".pb_text");
		if (txt)
		  txt.update(t + "%");
	},

	setProgress : function(value, callback) {
		if (!this.progress.descendantOf(document)) {
			this.stop();
		} else {
			this.step = value || 0;
			if (this.step >= this.options.maxProgressValue) {
				this.stop();
			} 
			this._draw();
			$call(callback);
		}
	},

	start : function() {
		this.starting = true;
		if (!this.executer) {
			this.executer = new PeriodicalExecuter(function() {
				var url = this.options.url;
				if (this.starting) {
					url = url.addParameter("starting=true");
					this.starting = false;
				}
				new Ajax.Request(url, {
					onComplete : this._ajaxComplete.bind(this)
				});
			}.bind(this), this.options.interval);
		}
	},
	
	isStop : function() {
		return !this.executer || !this.executer.timer;
	},
	
	stop : function() {
		if (this.executer) {
			this.executer.stop();
		}
	}
});

function __progressbar_actions_init(actionFunc) {
	actionFunc.start = function() {
		actionFunc.progressbar.start();
	};
	
	actionFunc.stop = function() {
		actionFunc.progressbar.stop();
	};
	
	actionFunc.updateUI = function() {
		actionFunc.progressbar.updateUI();
	};
}
