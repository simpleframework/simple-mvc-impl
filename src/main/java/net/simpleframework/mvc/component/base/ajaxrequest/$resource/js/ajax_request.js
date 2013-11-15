/**
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
$UI.AjaxRequest = Class.create({
	initialize : function(container, responseText, id, isJSON, permission) {
		if (Object.isUndefined(responseText) || isJSON)
			return;
		
		container = $(container);
		if (container && !permission) {
			container.update(responseText.stripStylesheets().stripScripts());
		}
		
		// 添加样式
		$UI.addStylesheet(responseText.toStylesheets());
		
		// 添加脚本
		var textScript = '';
		var jsFiles = [];
		var scripts = responseText.toScripts();
		for ( var i = 0; i < scripts.length; i++) {
			var script = scripts[i].makeElement();
			
			if (script.getAttribute("src")) {
				jsFiles.push(script);
			} else {
			  var code = scripts[i].extractScripts();
			  if (code)
			    textScript += code;
			}
		}
		$UI.addScript(jsFiles, textScript, id);
	}
});

var $Loading = {
	id : 'ajax_request_loading',

	show : function(modal) {
		var ele = $(this.id);
		var f = function() {
			ele.style.top = (document.viewport.getScrollOffsets().top + 4) + "px";
		};
		if (!ele) {
			ele = new Element('div', {
				style : 'display: none;'
			}).appendText($MessageConst['$Loading.0']);
			ele.id = this.id;
			document.body.appendChild(ele);
			Event.observe(window, 'scroll', function() {
				if (ele.visible()) {
					f();
				}
			});
			ele.modalOverlay = new Element('div', {
				className : "loading_overlay",
				style : "display: none;"
			});
			document.body.appendChild(ele.modalOverlay);
		}
		if (ele.executer) {
			ele.executer.stop();
			ele.executer = null;
		}
		if (!ele.visible()) {
			f();
			ele.showing = true;
			if (modal) {
				ele.modalOverlay.setStyle("height: "
						+ document.viewport.getScrollDimensions().height + "px");
				ele.modalOverlay.show();
			}
			ele.$show({
				afterFinish : function() {
					ele.showing = false;
				}
			});
		}
	},

	hide : function() {
		var ele = $(this.id);
		if (!ele)
			return;
		if (!ele.executer) {
			ele.executer = new PeriodicalExecuter(function() {
				if (ele.showing) {
					return;
				}
				ele.executer.stop();
				ele.executer = null;
				ele.hide();
				if (ele.modalOverlay.visible()) {
					ele.modalOverlay.hide();
				}
			}, 0.1);
		}
	}
};

function __ajax_actions_init(actionFunc, name) {
	var triggerAct = function(disabled) {
		if (!actionFunc.trigger) {
			return;
		}
		actionFunc.trigger.disabled = disabled;
	};
	
	actionFunc.doInit = function(containerId, confirmMessage, parallel,
			showLoading, loadingModal, disabledTriggerAction) {
		var ev = document.getEvent();
		if (ev) {
			actionFunc.trigger = Event.element(ev);
		}
		
		var c = actionFunc.container = $(actionFunc.container || containerId);	
		if (c && actionFunc.cache) {
		  c.update(actionFunc.cache.stripStylesheets());
			return true;
		}
		
		confirmMessage = actionFunc.confirmMessage || confirmMessage;
		if (confirmMessage && !confirm(confirmMessage)) 
			return true;

		if (!parallel && actionFunc.loading) 
			return true;
		if (showLoading)
			$Loading.show(loadingModal);
		if (!parallel) 
			actionFunc.loading = true;

		actionFunc.disabledTriggerAction = disabledTriggerAction;
		if (disabledTriggerAction) 
			triggerAct(true);
	};

	actionFunc.doComplete = function(req, containerRef, updateContainerCache, alert) {
		try {
			var rJSON = req.responseText.evalJSON();
			var rt = rJSON.rt;
			
			if (updateContainerCache) 
				actionFunc.cache = rt;
			
			if (!actionFunc.container) {
			  var act = $Actions[containerRef];
			  if (act && act.window)
			    actionFunc.container = act.window.content;
			}
			new $UI.AjaxRequest(actionFunc.container, rt, rJSON.id, rJSON.isJSON, rJSON.permission);

			(function(req, responseText, json, trigger) {
				var err = json["exception"];
				if (err) {
					$error(err, alert);
					return;
				}
				if (rJSON.permission)
					return;
				if (rJSON.isJavascript) {
				  $call(responseText);
				} else {
					actionFunc.__callback(req, responseText, json, trigger);
				}
			}).defer(req, rt, rt.isJSON() ? rt.evalJSON() : {}, actionFunc.trigger);
		} finally {
			actionFunc.doLoaded(req);
		}
	};

	actionFunc.doLoaded = function(req) {
		$Loading.hide();
		actionFunc.loading = false;
		if (actionFunc.disabledTriggerAction) {
			triggerAct();
		}
	};
}
