/**
 * @author 陈侃(cknet@126.com, 13910090885) 
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
$UI.AjaxRequest = Class.create({
  initialize : function(container, responseText, id, isJSON, bUpdate) {
    if (Object.isUndefined(responseText) || isJSON)
      return;

    container = $(container);
    if (container && bUpdate) {
      container.update(responseText.stripStylesheets().stripScripts());
    }

    // 添加样式
    $UI.addStylesheet(responseText.toStylesheets());

    // 添加脚本
    var textScript = '';
    var jsFiles = [];
    var scripts = responseText.toScripts();
    for (var i = 0; i < scripts.length; i++) {
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
  
  show : function(loadingModal, centerLoading) {
    var ele = $(this.id);
    var f = function() {
      ele.style.top = (document.viewport.getScrollOffsets().top + 4) + "px";
    };
    if (!ele) {
      ele = new Element('div', {
        style : 'display: none;'
      });
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
    	if (isMobile.any()) {
      	if (centerLoading) {
      		ele.className = "loading_m_ajax2";
      		ele.innerHTML = centerLoading;
      	} else {
      		ele.className = "loading_m_ajax";
      	}
      } else {
        ele.className = "loading_ajax";
        ele.innerHTML = $MessageConst['$Loading.0'];
      }
    	
    	f();
      ele.showing = true;
      if (loadingModal) {
//        ele.modalOverlay.setStyle("height: "
//            + document.viewport.getScrollDimensions().height + "px");
        ele.modalOverlay.show();
      }
      $Effect.show(ele, {
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
    var t = actionFunc.trigger;
    if (!t) {
      return;
    }

    if (t.tagName == 'INPUT') {
      t.disabled = disabled;
    } else {
      if (Browser.IEVersion <= 8) {
        // ie8
      } else {
        if (disabled) {
          t._ajax_onclick = t.onclick;
          t.onclick = function(e) { Event.stop(e); };
//          t._onclick = t.getAttribute('onclick');
//          if (t._onclick) {
//            t.removeAttribute("onclick");
//          }
        } else {
          t.onclick = t._ajax_onclick;
//          if (t._onclick) {
//            t.setAttribute('onclick', t._onclick);
//          }
        }
      }
    }
  };

  actionFunc.doInit = function(containerId, confirmMessage, parallel,
      showLoading, loadingModal, centerLoading, disabledTriggerAction) {
    var ev = document.getEvent();
    if (ev) {
      var element = Event.element(ev);
      if (element && Object.isElement(element)) {
        actionFunc.trigger = element;
        if (element.hasClassName("mmore")) {
        	showLoading = false;
        }
      }
    }

    var c = actionFunc._container = $(actionFunc.container || containerId);
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
      $Loading.show(loadingModal, centerLoading);
    if (!parallel)
      actionFunc.loading = true;

    actionFunc.disabledTriggerAction = disabledTriggerAction;
    if (disabledTriggerAction)
      triggerAct(true);
  };

  actionFunc.doComplete = function(req, windowRef, updateContainerCache, alert) {
    try {
      var rJSON = req.responseText.evalJSON();
      var rt = rJSON.rt;

      if (updateContainerCache)
        actionFunc.cache = rt;

      var c = actionFunc._container;
      if (!c) {
        var act = $Actions[windowRef];
        if (act && act.window)
          c = act.window.content;
      }

      var bUpdate = !rJSON.isJavascript && !rJSON.hasPermission;
      new $UI.AjaxRequest(c, rt, rJSON.id, rJSON.isJSON, bUpdate);

      (function(req, responseText, json, trigger) {
        var err = json["exception"];
        if (err) {
          $error(err, alert);
          return;
        }
        if (rJSON.hasPermission)
          return;
        if (rJSON.isJavascript) {
          new Function("req", "json", "trigger", responseText)(req, json,
              trigger);
          // $call(responseText);
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
      setTimeout(triggerAct, 500);
    }
  };
}
