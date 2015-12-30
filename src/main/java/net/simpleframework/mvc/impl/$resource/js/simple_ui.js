/**
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
(function() {
	/**
	 * 覆盖系统的alert, confirm等函数
	 */
	window.$alert = function(msg, title, h) {
		var w = new UI.Window({
			minimize : false,
			maximize : false,
			resizable : false,
			height : (h || 160),
			width : 420,
			theme : "popup"
		});
		w.element.setStyle({
			zIndex : 99999
		});

		var c = ("<div class='sy_alert_dialog'>"
				+ "<div class='top'><div class='img'></div>"
				+ "<div class='txt wrap_text' style='height: " 
				+ (w.options.height - 105) + "px;'>" + msg 
				+ "</div></div><div class='btn'></div></div>").makeElement();
		c.down(".btn").insert(new Element("input", {
			type : "button",
			value : $MessageConst["Button.Ok"]
		}).observe("click", function(evn) {
			w.close();
		}));
		w.setHeader(title || $MessageConst["Alert.Title"]).setContent(c);
		w.center().show(true).activate();
	};

	window.$confirm = function(msg, okCallback, cancleCallback) {
		var w = new UI.Window({
			minimize : false,
			maximize : false,
			resizable : false,
			height : 160,
			width : 450,
			theme : "popup"
		});
		var c = ("<div class='sy_confirm_dialog'>"
				+ "<div class='top'><div class='img'></div>"
				+ "<div class='txt wrap_text'>" + msg + "</div></div><div class='btn'></div></div>")
				.makeElement();
		c.down(".btn").insert(new Element("input", {
			type : "button",
			value : $MessageConst["Button.Ok"]
		}).observe("click", function(evn) {
			$call(okCallback);
			w.close();
		})).insert(new Element("input", {
			type : "button",
			style : "margin-left: 4px;",
			value : $MessageConst["Button.Cancel"]
		}).observe("click", function(evn) {
			$call(cancleCallback);
			w.close();
		}));
		w.setHeader($MessageConst["Confirm.Title"]).setContent(c);
		w.center().show(true).activate();
	};

	window.$error = function(err, alertType) {
		if (!WINDOW_UI_ENABLE || alertType) {
			alert(err.title);
		} else {
			if (typeof err == 'string') {
				alert(err);
				return;
			}
			if (!this.errWindows) {
				this.errWindows = {};
			}
			var _WIDTH = 600;
			var _HEIGHT = 160;
			var w;
			if (this.errWindows[err.hash]) {
				w = this.errWindows[err.hash];
			} else {
				w = this.errWindows[err.hash] = new UI.Window({
					minimize : false,
					maximize : false,
					width : _WIDTH,
					height : _HEIGHT,
					theme : "popup"
				});
				w.observe("hidden", function() {
					delete this.errWindows[err.hash];
				}.bind(this));
			}
			
			var c = new Element("div", {
				className : "sy_error_dialog"
			}).insert(new Element("div", {
				className : "et wrap_text"
			}).update((err.title || "").convertHtmlLines()));

			var right = new Element("div", {
				style : "float: right;"
			});
			
			if (err['more']) {
			  var detail = new Element("div");
			  c.insert(detail);
			  
			  right.insert(new Element("input", {
	        type : "button",
	        style : "margin-right: 6px;",
	        value : $MessageConst["Button.More"]
	      }).observe("click", function() {
	        if (detail.down()) {
	          detail.update("");
	          this.value = $MessageConst["Button.More"];
	          w.setSize(_WIDTH, _HEIGHT).adapt();
	        } else {
	          var ta = new Element("textarea", {
	            readonly : "readonly"
	          });
	          ta.setValue(err.detail);
	          detail.update(new Element("div", {
	            className : "ta"
	          }).insert(ta));
	          this.value = $MessageConst["Button.Less"];
	          w.adapt();
	        }
	        w.center();
	      }));
			}
			
			right.insert(new Element("input", {
				type : "button",
				value : $MessageConst["Button.Cancel"]
			}).observe("click", function() {
				w.close();
			}));

			c.insert(new Element("div", {
				className : "eb"
			}).insert(right).insert(
					new Element("div", {
						style : "float: left"
					}).insert(new Element("a", {
						href : "#"
					}).update($MessageConst["Button.Location.Reload"]).observe("click",
							function() {
								location.reload();
							}))));

			w.setHeader($MessageConst["Error.Title"]).setContent(c);
			w.center().show(true).activate();
		}
	};

	$ready(function() {
		window.WINDOW_UI_ENABLE = window.UI && window.UI.Window;

		/**
		 * 覆盖alert的实现
		 */
		var $alert_bak = window.alert;
		window.alert = function(msg) {
			if (!WINDOW_UI_ENABLE)
				$alert_bak(msg);
			else
				$alert(msg);
		};
	});
})();

/**
 * 给$UI添加一些常用函数
 */
Object.extend($UI, {
	/**
	 * 获取弹出窗口的偏移量
	 */
	getPopupOffsets : function(popup, trigger) {
		if (!trigger) {
			var e = document.getEvent();
			if (!e || !(trigger = Event.element(e)))
				return [ 0, 0 ];
		}
		
		if (window.$Target)
			trigger = $Target(trigger);
		
		var tl = trigger.cumulativeOffset();
		var valueT = tl.top, valueL = tl.left;

		valueT = valueT + trigger.getDimensions().height;

		tl = trigger.cumulativeScrollOffset();
		valueT -= tl.top, valueL -= tl.left;

		var bodyDelta = 35;
		var vpDim = document.viewport.getDimensions();
		var bodyWidth = vpDim.width - bodyDelta;
		var bodyHeight = vpDim.height - bodyDelta;

		var popupWidth, popupHeight;
		if (Object.isElement(popup)) {
			popup = $(popup);
			popupWidth = popup.getWidth();
			popupHeight = popup.getHeight();
		} else {
			popupWidth = popup.width;
			popupHeight = popup.height;
		}

		if (valueL + popupWidth > bodyWidth) {
			var d = bodyWidth - popupWidth;
			valueL = d < 0 ? 2 : d;
		}
		if (valueT + popupHeight > bodyHeight) {
			var d = bodyHeight - popupHeight;
			valueT = d < 0 ? 2 : d;
		}

		var vpOff = document.viewport.getScrollOffsets();
		valueT += vpOff.top;
		valueL += vpOff.left;
		return [ valueL, valueT + 1 ];
	},

	/**
	 * 通过图标使指定目标可折叠
	 */
	doImageToggle : function(image, target, options) {
		if (!(image = $(image)))
			return;
		if (!(target = $(target))) {
			target = image.up().next();
		}
		var src = image.src;
		if (!src)
			return;

		image.setStyle("cursor:pointer;border:0px");
		options = Object.extend({
			cookie : false,
			open : true,
			onShow : Prototype.emptyFunction,
			onHide : Prototype.emptyFunction
		}, options);

		var p = src.lastIndexOf("/") + 1;
		var path = src.substring(0, p);
		var file = src.substring(p);
		var doImage = function() {
			if (target.visible()) {
				if (!file.startsWith("p_")) {
					file = "p_" + file;
				}
			} else {
				if (file.startsWith("p_")) {
					file = file.substring(2);
				}
			}
			image.src = path + file;
			if (options.cookie) {
				document.setCookie("toggle_" + image.identify(), target.visible());
			}
		};
		
		var _show = options.open;
		if (options.cookie) {
		  _show = (document.getCookie("toggle_" + image.identify()) == "true");
		}
		if (_show) {
      target.show();
    } else {
      target.hide();
    }
		
		image.observe("click", function() {
			if (target.visible()) {
				target.hide();
				doImage();
				if (options.onHide)
					options.onHide();
			} else {
				target.show();
				doImage();
				if (options.onShow)
					options.onShow();
			}
		});
		doImage();
	},
	
	/**
	 * 加入背景文字
	 */
	addBackgroundTitle : function(txt, TITLE) {
		txt = $(txt);
		if (!txt)
			return;
		if ("placeholder" in txt) {
			txt.setAttribute("placeholder", TITLE);
			return;
		}
		var c = txt.getStyle("color");
		var f = function(ev) {
			if ($F(txt) == "") {
				txt.setValue(TITLE);
			}
			if ($F(txt) == TITLE) {
				txt.setStyle("color: #ddd;");
			}
		};
		txt.observe("focus", function(ev) {
			if ($F(txt) == TITLE) {
				txt.setValue("");
				txt.setStyle("color:" + c);
			}
		});
		txt.observe("blur", f);
		f();
	},

	addReturnEvent : function(txt, func) {
		txt.observe("keypress", function(ev) {
			if (((ev.which) ? ev.which : ev.keyCode) != Event.KEY_RETURN) {
				return;
			}
			if (func)
				func(ev);
		});
	},

	/**
	 * 使容器支持拖拽
	 */
	enableDragDrop : function(container, hoverclass, callback) {
		if (!Object.isArray(container)) {
			container = $(container);
			if (!container)
				return;
			container = container.select(".drag_image");
		}
		callback = callback || {};
		container.each(function(a) {
			new Draggable(a, {
				revert : true,
				scroll : callback.scroll || window,
				onStart : function() {
					a.addClassName("drag_tooltip");
					// backup drops
					a.drops = Droppables.drops.clone();
					Droppables.drops = Droppables.drops.reject(function(d) {
						return d.hoverclass != hoverclass;
					});
					if (callback.onStart)
						callback.onStart(a);
				},
				onEnd : function() {
					a.removeClassName("drag_tooltip");
					a.update("");
					Droppables.drops = a.drops;
					if (callback.onEnd)
						callback.onEnd(a);
				}
			});
		});
	},

	shakeMsg : function(o, msg) {
		o.innerHTML = msg;
		$Effect.shake(o, {
			afterFinish : function() {
				o.innerHTML = "";
			}.delay(5)
		});
	}
});

Object.extend($UI, {
	createLink : function(text, options, click) {
		options = Object.extend({
			href : (Browser.IEVersion && Browser.IEVersion <= 6.0) ? "###"
					: "javascript:void(0);"
		}, options || {});
		if (Browser.IEVersion && Browser.IEVersion <= 7.0) {
			options = Object.extend(options, {
				hideFocus : true
			});
		}
		var a = new Element("a", options);
		if (text)
			a.update(text);
		if (click)
			a.observe("click", click);
		return a;
	},

	createTable : function(options) {
		options = Object.extend({
			width : "100%",
			cellpadding : "0",
			cellspacing : "0"
		}, options || {});
		return new Element("table", options).insert(new Element("tbody"));
	},

	createSplitbar : function(bar, left, callback) {
		if (!(bar = $(bar)) || !(left = $(left))) {
			return;
		}
		var p, w, ow = left.getWidth();
		bar.observe("dblclick", function(evt) {
			left.setStyle("width: " + ow + "px");
		});
		var mousemove = function(evt) {
			if (!p)
				return;
			var nw = w - p.x + evt.pointer().x;
			if (nw < 50 || nw > (document.viewport.getWidth() - 50))
				return;
			left.setStyle("width: " + nw + "px");
			$call(callback, nw);
			bar.fire("size:splitbar");
			Event.stop(evt);
		};
		var mouseup = function(evt) {
			if (!p)
				return;
			$(document.body).fixOnSelectStart(true);
			document.stopObserving("mousemove", mousemove);
			document.stopObserving("mouseup", mouseup);
			p = null;
		};
		bar.observe("mousedown", function(evt) {
			$(document.body).fixOnSelectStart();
			p = evt.pointer();
			w = left.getWidth();
			document.observe("mousemove", mousemove);
			document.observe("mouseup", mouseup);
		});
	},

	createButtonInputField : function(textId, bFunc, readonly, editable) {
		var element = new Element("div", {
			className : "text text_button"
		});

		element.textInput = new Element("input", {
			type : "text"
		});
		if (readonly || !editable)
		  element.textInput.readOnly = true;
		
		if (textId) {
			element.textInput.id = element.textInput.name = textId;
		}
		
		element.insert(new Element("div", {
			className : "d1"
		}).insert(new Element("div", {
			className : "d2"
		}).insert(element.textInput)));
		
		if (!readonly)
  		element.insert(element.textButton = new Element("div", {
        className : "sbtn"
      }).observe("click", function(ev) {
        if (bFunc)
          bFunc(ev);
      }));
		return element;
	},

	createFileInputField : function() {
		var tb = this.createButtonInputField();
		var f = new Element("input", {
			type : "file",
			hidefocus : "hidefocus",
			size : "1"
		}).observe("change", function(e) {
			tb.textInput.value = f.value;
		});
		
		tb.textInput.observe("change", function(e) {
			if (tb.textInput.value == '') {
				var up = f.up();
				new Element("form").insert(f).reset();
				up.insert(f);
			}
		});

		var ele = new Element("div", {
			className : "text_file_button"
		}).insert(tb).insert(f);
		ele.file = f;
		ele.textButton = tb;
		return ele;
	},
	
	createMultiSelectInputField : function(textId, bFunc, readonly) {
		var r = new Element("div", {
			className : "multi_dselect"
		});	
		var hidden = new Element("input", {
			type : "hidden"
		});
		if (textId) {
			hidden.id = hidden.name = textId;
		}
		
		r.insert(hidden);
		if (!readonly)
  		r.insert(r.textButton = new Element("SPAN", {
  			className : "sbtn"
  		}).observe("click", function(ev) {
  			if (bFunc)
  				bFunc(r, ev);
  		}));
		
		this.multiSelectInputField_init(r, readonly);
		return r;
	},
	
	multiSelectInputField_init : function(r, readonly) {
	  var updateHidden = function() {
	    r.down("input").value = r.select(".item").inject([], function(ret, o) {
        ret.push(o.id);
        return ret;
      }).join(";");
    };
    
    r.insertItem = function(id, txt) {
      if (r.select(".item").find(function(o) {
        return o.id == id;
      })) {
        return;
      }    
      
      var item;
      r.insert(item = new Element("SPAN", {
        className : "item",
        id : id
      }).update(txt));
      
      if (!readonly) {
        item.insert(new Element("SPAN", {
          className : "del"
        }).observe("click", function(ev) {
          item.remove();
          updateHidden();
        }));
      } else {
        item.setStyle("padding-right: 8px;");
      }
      
      updateHidden();
    };
	},
	
	pwdStrength_update : function(m, input, effects) {
    input = $(input); 
    if (!input) return;
    m = $(m); 
    if (!m) return;
    
    input.observe("keyup", function() {  
      var score = 0;
      var passwd = $F(input);
      for ( var i = 0; i < passwd.length; i++) {
        var c = passwd.charAt(i);
        if (c >= 'a' && c <= 'z')
          score += 2;
        if (c >= '0' && c <= '9')
          score += 3;
        if (c >= 'A' && c <= 'Z')
          score += 4;
        if (c.match(/[!,@#$%^&*?_~]/))
          score += 5;
      }

      var width = score * 2.2;
      if (width > 100)
        width = 100;
      var pwds = m.down(".pwds");      
      $Effect.style(pwds, "width:" + width + "px", {
        "effects" : effects
      });
      var lbl = m.down(".lbl");
      if (score > 0) {
        var i;
        if (score < 4) i = 0;
        else if (score <= 10) i = 1;
        else if (score <= 15) i = 2;
        else if (score <= 20) i = 3;
        else if (score <= 25) i = 4;
        else if (score <= 30) i = 5;
        else if (score <= 40) i = 6;
        else i = 7;
        lbl.update($MessageConst["PwdStrength.0"][i]);
      } else {
        lbl.update("");
      }
    });
  },
  
  doTabButtonsTooltip : function(selector) {
    if (!window.Tip)
      return;
    $$(selector).each(function(li) {
      var d = li.down('.tt');
      if (!d)
        return;
      new Tip(li, d.innerHTML, {
        stem : 'rightTop',
        hideOthers : true,
        delay : 0.5,
        hideAfter : 0.5,
        width : 210,
        hook : {
          target : 'leftTop',
          tip : 'rightTop'
        },
        hideOn : {
          element : 'tip',
          event : 'mouseleave'
        }
      });
    });
  }
});

Object.extend($UI, {
  hackCheckbox: function(selector) {
    $Elements(selector).each(function(c) {
      c.select("input[type=checkbox]").invoke("observe", "click",
      function(evn) {
        evn.stopPropagation();
      });
    });
  },

  fixTextareaTab: function(selector) {
    $Elements(selector).each(function(ta) {
      var tag = ta.tagName.toUpperCase();
      if (tag != "TEXTAREA") return;
      ta.observe("keydown",
      function(evn) {
        var keyCode = evn.keyCode || evn.which;
        if (keyCode == 9) {
          if (document.selection) {
            var range = document.selection.createRange();
            range.text = '\t';
          } else {
            var val = this.value,
            start = this.selectionStart,
            end = this.selectionEnd;
            this.value = val.substring(0, start) + '\t' + val.substring(end);
            this.selectionStart = this.selectionEnd = start + 1;
          }
          evn.preventDefault();
        }
      });
    });
  }
});
