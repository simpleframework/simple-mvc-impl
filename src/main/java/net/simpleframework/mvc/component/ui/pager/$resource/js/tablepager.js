/**
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
function $table_pager_addMethods(pa) {
	if (!pa.sort) {
		pa.sort = function(col) {
			var sort = $F(pa.hiddenInput("sort"));
			var col2 = $F(pa.hiddenInput("sort_col"));

			if (col != col2)
				sort = "";
			if (sort == "up")
				sort = "down";
			else if (sort == "down")
				sort = "";
			else
				sort = "up";
			pa("sort=" + sort + "&sort_col=" + col);
		};

		pa.checkAll = function(cb) {
		  var _checked = cb.checked;
			pa.pager.select(".cb input[type='checkbox']").each(function(c) {
				if (cb != c) {				  
					c.checked = _checked;
					var row = pa.row(c);
					if (row)
					  pa.rowSelect(row, _checked);
					if (c.clickFunc) {
						try {
							c.clickFunc();
						} catch (e) {
						}
					}
				}
			});
		};

		pa.checkId = function(actionFunc) {
			var ids = "";
			pa.pager.select(".cb input[type='checkbox']").each(function(c) {
				if (c.value && c.value != "on" && c.checked) {
					ids += ";" + c.value;
				}
			});
			if (ids.length > 0) {
				if (actionFunc)
					actionFunc(ids.substring(1));
			} else {
				alert($MessageConst["Error.delete2"]);
			}
		};

		pa._moveAction = function(moveAction, rowIds) {
		  if (Object.isString(moveAction)) {
        moveAction = $Actions[moveAction];
      }
		  moveAction.selector = pa.selector;
      moveAction("rowIds=" + rowIds.join(";"));
		};
		
		pa.move = function(up, moveAction, o) {
			var row = pa.row(o);
			var row2 = up ? row.previous() : row.next();
			if (!row2) {
			  alert($MessageConst["Error.Move"]);
        return;
			}
			pa._moveAction(moveAction, [ pa.rowId(row), pa.rowId(row2) ]);
		};

		pa.move2 = function(up, moveAction, o) {
			var row = pa.row(o);
			var rowIds = [];
			while (row) {
			  rowIds.push(pa.rowId(row));
			  row = up ? row.previous() : row.next();
			}
			if (rowIds.length < 2) {
			  alert($MessageConst["Error.Move"]);
        return;
			}
			pa._moveAction(moveAction, rowIds);
		};

		pa.row = function(o) {
			if (!o) {
				return pa.currentRow;
			}
			return o.hasClassName("titem") ? o : $Target(o).up(".titem");
		};

		pa.rowId = function(o) {
			return (o = pa.row(o)) ? o.readAttribute("rowId") : null;
		};

		pa.rowData = function(i, o) {
			return pa.row(o).select("td")[i].innerHTML.stripTags().strip();
		};
		
		pa.rowSelect = function(o, b) {
		  var row = pa.row(o);
		  row[b ? "addClassName" : "removeClassName"]("titem_selected");
    };

		pa.add_row = function(params) {
			var tp = pa.pager.down(".tablepager");

			var act = $Actions["tpRowAdd_" + pa.beanId];
			act.selector = pa.selector;
			act.jsCompleteCallback = function(req, responseText, json) {
			  var tbody = tp.down(".tbody");
				tbody.insert({
					top : json["row"]
				});

				pa._create_saveBar(tp, json["tb"]);
				tbody.down().adding = true;
			};

			act(params);
		};

		pa.edit_row = function(o, params) {
			var row = pa.row(o);
			if (row.editing) {
		//		pa.unedit(o);
				return;
			}
			var act = $Actions["tpRowEdit_" + pa.beanId];
			act.selector = pa.selector;
			act.jsCompleteCallback = function(req, responseText, json) {
				var columns = row.select("td");
				var i = 0;
				var tp = row.up(".tablepager");
				tp.select(".thead td").each(function(col2) {
				  var col = columns[i++];
				  col.setAttribute("valign", "top");
					var columnName = col2.getAttribute("columnName");
					if (!columnName || !json[columnName])
						return;
					var lbl = col.down(".lbl");
					lbl.$bak_innerHTML = lbl.innerHTML;
					lbl.innerHTML = json[columnName];
				});

				pa._create_saveBar(tp, json["tb"]);
				row.editing = true;
			};
			act(('rowId=' + pa.rowId(o)).addParameter(params));
		};
		
		pa._create_saveBar = function(tp, tb) {
			var rowedit = tp.down(".rowedit");
			if (!rowedit) {
				rowedit = new Element("DIV", {
					className : "rowedit"
				}).update(tb);
				tp.down(".tbody").insert({
					before : rowedit
				});
			}
		};

		pa.save_rows = function(o) {
			var act = $Actions["tpRowSave_" + pa.beanId];
			act.selector = pa.selector;
			act($Form(o.up(".tablepager").down(".tbody")));
		};

		pa.unedit = function(o) {
			var row = pa.row(o);
			
			var tp = row.up(".tablepager");
			pa._unedit(row);
			
			var rowedit = tp.down(".rowedit");
			if (rowedit && !tp.select(".titem").find(function(r) {
				return r.editing || r.adding;
			})) {
				rowedit.remove();
			}
		};

		pa._unedit = function(row) {
			if (row.editing) {
				row.select("td").each(function(col) {
				  col.removeAttribute("valign");
					var lbl = col.down(".lbl");
					if (lbl && lbl.$bak_innerHTML) {
						lbl.innerHTML = lbl.$bak_innerHTML;
					}
				});
				row.editing = undefined;
			}
			if (row.adding) {
				row.remove();
			}
		};

		pa.uneditAll = function(o) {
			var tp = o.up(".tablepager");
			tp.select(".titem").each(function(row) {
				pa._unedit(row);
			});

			var rowedit = tp.down(".rowedit");
			if (rowedit) {
				rowedit.remove();
			}
		};

		pa.bindMenu = function(menuAction) {
			$Actions.callSafely(menuAction, null, function(act) {
				act.bindEvent(pa.pager.select(".m"));
				return true;
			});
			$Actions.callSafely(menuAction + "2", null, function(act) {
				act.bindEvent(pa.pager.select(".m2"));
				return true;
			});
		};

		pa.checkArr = function(c) {
			c = c || pa.pager;
			return c.select(".tbody .titem").inject([], function(results, item) {
				var o = item.down("td.cb input");
				if (o) {
					if (o.checked) {
						results.push(item);
					}
				} else if (item.hasClassName("titem_selected")) {
					results.push(item);
				}
				return results;
			});
		};

		pa.checkParams = function(c) {
			return pa.checkArr(c).collect(function(item) {
				return pa.rowId(item);
			}).join(";");
		};

		pa.exportFile = function(params) {
			var ep = $Actions["tpExportWindow_" + pa.beanId];
			ep.selector = pa.selector;
			ep(params);
		};

		pa.hiddenInput = function(id) {
			var form = pa.pager.down(".parameters");
			var o = form.down("#" + id);
			if (!o) {
				o = new Element("input", {
					"type" : "hidden",
					"name" : id,
					"id" : id
				});
				form.insert(o);
			}
			return o;
		};

		pa.setHeight = function(height) {
			if (!height) {
				return;
			}
			pa.pager.down(".tbody").setStyle(
					"height: " + (pa.hasData ? parseInt(height) : 0) + "px");
			pa.hiddenInput("tbl_tbody_height").setValue(height);
			pa._fixScrollWidth();
		};

		// 当tbody出现滚动条时，thead需要加上滚动条的宽度
		pa._fixScrollWidth = function() {
			var tbody = pa.pager.down(".tbody");
			var thead = pa.pager.down(".thead");
			var w = pa.hasData && (tbody.scrollHeight - 1) > tbody.clientHeight ? 17
					: 0;
			var tbl = thead.down("table");
			if (w != parseInt(tbl.getStyle("padding-right"))) {
				var padding = "padding-right: " + w + "px;";
				tbl.setStyle(padding);
				var tfilter = thead.next(".tfilter");
				if (tfilter) {
					tfilter.down("table").setStyle(padding);
				}
				pa.hiddenInput("tbl_thead_margin_right").setValue(w);
			}
		};
		
		pa.doAct = function(act, idKey, params) {
			var str = pa.checkParams();
			if (str == "") {
				alert($MessageConst["TablePager.1"]);
				return;
			}
			var oAct = typeof act == "string" ? $Actions[act] : act;
			oAct(((idKey || "id") + "=" + str).addParameter(params));
		};
	}

	var tablepager = pa.pager.down(".tablepager");
	var thead = tablepager.down(".thead");
	var tbody = tablepager.down(".tbody");

	pa.bindMenu("ml_" + pa.beanId + "_Menu");

	var jsRowClick = pa.json["jsRowClick"];
	if (jsRowClick) {
		tablepager.select('.titem').invoke('observe', 'click', function(evn) {
			(function click(item) {
				eval(jsRowClick);
			})(this);
		});
	}
	var jsRowDblclick = pa.json["jsRowDblclick"];
	if (jsRowDblclick) {
		tablepager.select('.titem').invoke('observe', 'dblclick', function(evn) {
			(function click(item) {
				eval(jsRowDblclick);
			})(this);
		});
	}

	// isGroup
	if (pa.json["isGroup"]) {
		tablepager.select('.toggle>img').each(function(img) {
			$UI.doImageToggle(img, img.up('.group_t').next());
		});
	}
	// detailField
	if (pa.json["isDetailField"]) {
		tablepager.select('.plus>img').each(function(img) {
			$UI.doImageToggle(img, img.up('.titem').down(".tdetail"), {
				cookie : false,
				open : pa.json["isExpandDetailField"]
			});
		});
	}

	// tooltip
	var tt = $Actions["tpTooltip_" + pa.beanId];
	if (tt) {
		tt.createTip(thead.select(".box"));
	}

	// filter
	var tfilter = thead.next(".tfilter");
	if (tfilter) {
   pa.doReturn = function(txt) {
      var v = $F(txt);
      v = v.stripScripts();
      if (v == "") {
        pa.filterDelete(txt, txt.getAttribute("params"));
      } else if (v != title) {       
        var act = $Actions["tpFilter_" + pa.beanId];
        act.jsCompleteCallback = function(req, responseText, json) {
          pa(json["filter"]);
        };
        act(txt.readAttribute("params") + "&v=" + v);
      }
      // 获取td的位置
      pa.focusIndex = tfilter.select("td").indexOf(txt.up("td"));
    };
    
		var title = $MessageConst["TablePager.0"];
		tfilter.select("input").each(function(txt) {
		  if (txt.readOnly)
		    return;
			$UI.addBackgroundTitle(txt, title);
			$UI.addReturnEvent(txt, function(ev) {
			  pa.doReturn(txt);
			});
		});
		
		if (typeof pa.focusIndex == "number" && pa.focusIndex >= 0) {
		  var input = tfilter.select("td")[pa.focusIndex].down("input");
		  if (input) {
		    $UI.moveCursorToEnd(input);
		    input.focus();
		  }
		  pa.focusIndex = undefined;
		}

		pa.filterDelete = function(obj, params) {
			var act = $Actions["tpFilterDelete_" + pa.beanId];
			act.selector = obj.up(".pager").down(".parameters");
			act(params);
		};

		pa.filterWindow = function(obj, params) {
			var act = $Actions["tpFilterWindow_" + pa.beanId];
			act.selector = obj.up(".pager").down(".parameters");
			act(params);
		};
	}

	// scroll head
	var scrollHead = tablepager.readAttribute("scrollHead");
	var scroll;
	if ((scrollHead == "true") && (scroll = thead.getOffsetParent())) {
		// 需要父元素设置 overflow: auto; position: relative;
		scroll._scrollTop = thead.offsetTop;
		if (!scroll.thead) {
			scroll.observe("scroll", function() {
				var delta = scroll.scrollTop - scroll._scrollTop - 1;
				scroll.thead.style.top = Math.max(delta, 0) + "px";
			});
		}
		scroll.scrollTop = 0;
		scroll.thead = thead;
	}

	// 当设置tbody的宽度时
	tbody.observe("scroll", function() {
		thead.scrollLeft = tbody.scrollLeft;
		if (tfilter) {
			tfilter.scrollLeft = tbody.scrollLeft;
		}
	});

	pa._fixScrollWidth();

	// highlight
	var items = tablepager.select(".titem");
	items.each(function(item) {
    var cb = item.down(".cb input[type=checkbox]");
    if (cb && cb.checked) {
      pa.rowSelect(item, true);
    }
  });
	tablepager.select(".cb input[type=checkbox]").invoke("observe", "click",
			function(evn) {
				var b = this.checked;
				var item = this.up(".titem");
				if (item) {
				  pa.rowSelect(item, b);
				} else {
					items.each(function(i) {
					  pa.rowSelect(i, b);
					});
				}
				evn.stopPropagation();
			});

	// fix column height
	// var boxColl = thead.select(".box");
	// var maxHeight = 0, minHeight = Number.MAX_VALUE;
	// boxColl.each(function(box) {
	// var nHeight = parseInt(box.getStyle("height"));
	// if (nHeight > maxHeight)
	// maxHeight = nHeight;
	// if (nHeight < minHeight)
	// minHeight = nHeight;
	// });
	// if (maxHeight != minHeight) {
	// boxColl.each(function(box) {
	// box.setStyle({ "height" : maxHeight + "px" });
	// });
	// }

	// resize
	var i = 0;
	thead.select("td").each(function(td) {
		var columnIndex = i++;
		var sep = td.down(".sep");
		if (!sep)
			return;
		var col = td.previous();
		if (col.readAttribute("resize") != 'true')
			return;

		var p, w;
		sep.setStyle("cursor: e-resize;");
		var getw = function() {
			return Browser.WebKit ? col.getWidth() : col.measure("width");
		};
		var dow = function(width) {
			if (!width)
				return;
			width = parseInt(width);
			if (width < Math.min(40, parseInt(col.readAttribute("owidth"))))
				return;
			col.setStyle("width: " + width + "px");

			if (tfilter) {
				var col2 = tfilter.select("td")[columnIndex - 1];
				if (col2)
					col2.setStyle("width: " + width + "px");
			}

			items.each(function(titem) {
				var col2 = titem.select(".itr>td")[columnIndex - 1];
				if (col2)
					col2.setStyle("width: " + width + "px");
			});
		};
		var mousemove = function(evt) {
			if (!p)
				return;
			dow(w - p.x + evt.pointer().x);
			Event.stop(evt);
		};
		var mouseup = function(evt) {
			if (!p)
				return;
			var body = $(document.body);
			body.setStyle("cursor: default;");
			body.fixOnSelectStart(true);
			document.stopObserving("mousemove", mousemove);
			document.stopObserving("mouseup", mouseup);
			p = null;
		};
		sep.observe("mousedown", function(evt) {
			var body = $(document.body);
			body.fixOnSelectStart();
			body.setStyle("cursor: e-resize;");
			p = evt.pointer();
			w = getw();
			document.observe("mousemove", mousemove);
			document.observe("mouseup", mouseup);
			var owidth = col.readAttribute("owidth");
			if (!owidth) {
				col.writeAttribute("owidth", w);
			}
		});
		sep.observe("dblclick", function(evt) {
			dow(col.readAttribute("owidth"));
		});
	});
}

var TableUtils = {
	init_RowDraggable : function(containerId, hoverclass, callback) {
		callback = callback || {};
		$UI.enableDragDrop(containerId, hoverclass, {
			scroll : callback.scroll,
			onStart : function(a) {
				a.innerHTML = $MessageConst["Table.Draggable.0"]
						+ TableUtils.checked(a).length
						+ $MessageConst["Table.Draggable.1"];
				if (callback.onStart)
					callback.onStart(a);
			},
			onEnd : function(a) {
				if (callback.onEnd)
					callback.onEnd(a);
			}
		});
	},

	checked : function(a) {
		var arr = [];
		a.up(".tablepager").select(".titem input").each(function(c) {
			if (c.value && c.value != "on" && c.checked) {
				arr.push(c.value);
			}
		});
		if (arr.length == 0) {
			arr.push(a.up(".titem").down("input").value);
		}
		return arr;
	},
	
  contextMenu_ShowCallback : function(menu) {
    var items = menu.getItems();
    items.each(function(item) {
      item.setItemDisabled(false)
    });
    var row = menu.getTargetObject().up(".titem");
    var p = row.getAttribute("menu-disabled");
    if (p && p.length > 0) {
      p.split(";").each(function(a) {
        var i = parseInt(a);
        if (!isNaN(i)) {
          items[i].setItemDisabled(true);
        }
      });
    }
  }
};