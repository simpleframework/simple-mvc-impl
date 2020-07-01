/**
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
var $ready = function(callback) {
  if (document.loaded)
    callback.delay(0.1, document);
  else
    document.observe('dom:loaded', callback);
};

(function() {
  Object.extend(Browser, Prototype.Browser);
  Object.extend(Browser, {
    effects : window.IS_EFFECTS && !Object.isUndefined(window.Effect),

    WebKit419 : Browser.WebKit && !document.evaluate
  });
  if (Browser.IE) {
    Browser.IEVersion = parseFloat(navigator.appVersion.split(';')[1].trim().split(' ')[1]);
  }

  var $bak = $;
  window.$ = function(element) {
    if (typeof element == "string" && element.length > 0
        && (element[0] == '.' || element[0] == '#')) {
      var elements = $$(element);
      return elements.length > 0 ? elements[0] : undefined;
    } else {
      var elements;
      return $bak(element)
          || ((elements = $$(element)).length > 0 ? elements[0] : undefined);
    }
  };

  var $Fbak = $F;
  window.$F = function(element) {
    element = $(element);
    return element ? $Fbak(element) : "";
  };
})();

window.$Form = function(form) {
  return $Elements(form).inject([], function(results, o) {
    results.push(Form.Methods.serialize(o));
    return results;
  }).join("&");
};

window.$Elements = function(object) {
  if (Object.isString(object)) {
    return $$(object);
  } else if (Object.isArray(object)) {
    return object;
  } else if (Object.isElement(object)) {
    if (object = $(object)) {
      var r = [];
      r.push(object);
      return r;
    }
  }
  return [];
};

window.$Target = function(o) { // menu
  o = $(o);
  return (o && o.getTargetObject) ? o.getTargetObject() : o;
};

Object.extend($Actions, {
  valueBinding : function(json, callback) {
    if (!json)
      return;
    var f = function(key) {
      var element = $(key);
      var v = json[key];
      if (element && v != null && v !== 'undefined') {
        this.setValue(element, v);
      }
    }.bind(this);
    (function() {
      Object.keys(json).each(f);
      $call(callback);
    }).delay(0.1);
  },

  setValue : function(element, v, insert) {
    element = $(element);
    if (!element)
      return;
    if (element.htmlEditor) {
      if (insert) {
        if (Object.isElement(v)) {
          element.htmlEditor.insertElement(CKEDITOR.dom.element.get(v));
        } else {
          element.htmlEditor.insertHtml(v);
        }
      } else {
        element.htmlEditor.setData(v);
      }
    } else {
      var tag = element.tagName.toUpperCase();
      if (tag == "SELECT" && Object.isNumber(v)) {
        element.selectedIndex = v;
      } else if (tag == "SPAN" || tag == "LABEL") {
        element.innerHTML = v;
      } else if (tag == "TEXTAREA" && insert) {
        if (document.selection) { // ie
          element.focus();
          sel = document.selection.createRange();
          sel.text = v;
        } else if (element.selectionStart || element.selectionStart == '0') {
          var start = element.selectionStart;
          var end = element.selectionEnd;
          var v2 = element.value;
          element.setValue(v2.substring(0, start) + v
              + v2.substring(end, v2.length));
        } else {
          element.setValue(element.value + v);
        }
      } else {
        element.setValue(v);
      }
      
      if (tag == "SELECT" && element.hasAttribute('readonly')) {
        var r = new Element('span', { 'className' : 'readonly' });
        r.innerHTML = element.options[element.selectedIndex].text;
        element.replace(r);
      }
      if (element.getAttribute("autorows") == "true") {
        element.style.height = "0px";
        element.style.height = element.scrollHeight + "px";
      }
      element.focus();
    }
  },

  addHidden : function(form, params) {
    if (!form || !params) {
      return;
    }
    $H(params.toQueryParams()).each(
        function(pair) {
          var hidden = form.down("#" + pair.key);
          if (Object.isElement(hidden)
              && hidden.tagName.toUpperCase() == "INPUT"
              && hidden.type == "hidden") {
            hidden.value = pair.value;
          } else {
            form.insert(new Element("INPUT", {
              type : "hidden",
              id : pair.key,
              name : pair.key,
              value : pair.value
            }));
          }
        });
  },

  visibleToggle : function(element) {
    if (Object.isString(element)) {
      $$(element).each(function(eobj) {
        eobj.toggle();
      });
    } else if (Object.isElement(element)) {
      element.toggle();
    }
  },

  readonly : function(element) {
    if (Object.isString(element)) {
      $$(element).each(function(eobj) {
        $Actions.readonly(eobj);
      });
    } else if (Object.isElement(element)) {
      if (/input|textarea/i.test(element.tagName)) {
        element.readOnly = true;
      } else {
        Form.getElements(element).each(function(eobj) {
          $Actions.readonly(eobj);
        });
      }
    }
  },

  disable : function(element) {
    this._enable_disable(element);
  },

  enable : function(element) {
    this._enable_disable(element, true);
  },

  _enable_disable : function(element, enable) {
    var method = enable ? "enable" : "disable";
    if (Object.isString(element)) {
      $$(element).each(function(eobj) {
        $Actions[method](eobj);
      });
    } else if (Object.isElement(element)) {
      if (/input|select|textarea|button/i.test(element.tagName)) {
        element[method]();
      } else {
        Form.Methods[method](element);
      }
    }
  },

  observeSubmit : function(selector, sFunc) {
    $Elements(selector).invoke("observe", "keydown", function(ev) {
      var code = (ev.which) ? ev.which : ev.keyCode;
      if (code == Event.KEY_RETURN) {
        sFunc(Event.element(ev));
      }
    });
  }
});

Object.extend(Form.Element.Serializers, {
  textarea : function(element, value) {
    if (Object.isUndefined(value)) {
      if (element.htmlEditor) {
        element.htmlEditor.updateElement();
      }
      return element.value;
    } else {
      element.value = value;
    }
  }
});

/** String */
Object.extend(String.prototype, {
  addSelectorParameter : function(selector) {
    var str = this.toString();
    if (selector) {
      if (Object.isElement(selector)) {
        str = str.addParameter($Form(selector));
      } else {
        var elements = $$(selector);
        if (elements) {
          for (var i = 0; i < elements.length; i++)
            str = str.addParameter($Form(elements[i]));
        }
      }
    }
    return str;
  },

  camelcase : function() {
    var string = this.dasherize().camelize();
    return string.charAt(0).toUpperCase() + string.slice(1);
  }
});

Object.extend(document.viewport, {
  setScrollOffsets : function(offset) {
    Element.setScrollOffsets(Prototype.Browser.WebKit ? document.body
        : document.documentElement, offset);
  },

  getScrollDimensions : function() {
    return Element.getScrollDimensions(Prototype.Browser.WebKit ? document.body
        : document.documentElement);
  }
});

/** Element */
Element
    .addMethods({
      getScrollDimensions : function(element) {
        return {
          width : element.scrollWidth,
          height : element.scrollHeight
        };
      },

      setDimensions : function(element) {
        var h, w;
        var opt = arguments[1];
        if (typeof opt == "object") {
          h = opt.height;
          w = opt.width;
        } else {
          h = opt;
          w = arguments[2];
        }
        var f = function(p) {
          if (Object.isNumber(p)) {
            return p + "px";
          } else {
            return Object.isString(p) ? (p.strip().endsWith("%") ? p
                : parseInt(p) + "px") : "auto";
          }
        };
        var opt2 = {};
        if (h)
          opt2.height = f(h);
        if (w)
          opt2.width = f(w);
        if ($H(opt2).size() > 0) {
          element.setStyle(opt2);
        }
      },

      getScrollOffsets : function(element) {
        return Element._returnOffset(element.scrollLeft, element.scrollTop);
      },

      setScrollOffsets : function(element, offset) {
        if (arguments.length == 3)
          offset = {
            left : offset,
            top : arguments[2]
          };
        element.scrollLeft = offset.left;
        element.scrollTop = offset.top;
        return element;
      },

      appendText : function(element, text) {
        text = String.interpret(text);
        element.appendChild(document.createTextNode(text));
        return element;
      },

      createDrag : function(element) {
        element.enableDrag();
        var t = 0, l = 0;
        element.observe("drag:started", function(p) {
          t = parseInt(element.style.top);
          l = parseInt(element.style.left);
        }).observe("drag:updated", function(p) {
          element.style.top = t + p.memo.dy + "px";
          element.style.left = l + p.memo.dx + "px";
        });
      },

      enableDrag : function(element) {
        element.writeAttribute('draggable', 'true');
        return element;
      },

      disableDrag : function(element) {
        element.writeAttribute('draggable', null);
        return element;
      },

      isDraggable : function(element) {
        return element.readAttribute('draggable') == 'true';
      },

      fixOnSelectStart : function(element, remove) {
        if (remove) {
          if (Browser.Gecko) {
            element.style.removeProperty("-moz-user-select");
          } else {
            element.removeAttribute("onselectstart");
          }
        } else {
          if (Browser.Gecko) {
            element.setStyle("-moz-user-select: none;");
          } else {
            element.setAttribute("onselectstart", "return false;");
          }
        }
        return element;
      },

      setPngBackground : function(element, src) {
        var f = Object.extend({
          align : "top left",
          repeat : "no-repeat",
          sizingMethod : "scale",
          backgroundColor : ""
        }, arguments[2] || {});
        element.setStyle((Browser.IEVersion && Browser.IEVersion <= 6.0) ? {
          filter : "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"
              + src + "'', sizingMethod='" + f.sizingMethod + "')"
        } : {
          background : f.backgroundColor + " url(" + src + ") " + f.align + " "
              + f.repeat
        });
        return element;
      },

      observeImageLoad : function(img, callback) {
        if (img.tagName !== 'IMG')
          return;
        img.onreadystatechange = img.onload = function() {
          var state = img.readyState;
          if (callback
              && ((state && (state == "complete" || state == "loaded")) || img.complete == true)) {
            callback();
          }
        };
        return img;
      },

      clearImageLoad : function(img) {
        img.onreadystatechange = img.onload = null;
        return img;
      }
    });

Object.extend($Effect, {
  shake : function(element, options) {
    options = options || {};
    if (options.effects || Browser.effects) {
      var win;
      if (window.$win && (win = window.$win(element))) {
        win.content.setStyle("overflow: hidden;");
      }
      element.shake(Object.extend({
        duration : 0.5
      }, options || {}));
      (function() {
        if (win) {
          win.content.setStyle("overflow: auto;");
        }
      }).delay(0.6);
    }
    return element;
  },
  
  highlight : function(element, color, options) {
    if (element.set_style) {
      return element;
    }
    element.set_style = true;
    options = options || {};
    if (options.effects || Browser.effects) {
      var aFinish = options.afterFinish;
      options.afterFinish = function() {
        $call(aFinish);
        element.set_style = false;
      };
      var ocolor = element.getStyle('background-color').parseColor("#FFFFFF");
      element.highlight(Object.extend({
        duration : 1,
        startcolor : ocolor,
        endcolor : color
      }, options));
    } else {
      var bg = element.getStyle('background');
      element.setStyle("background: " + color);
      (function() {
        element.setStyle("background: " + (bg ? bg : "none"));
        $call(options.afterFinish);
        element.set_style = false;
      }).delay(1);
    }
    return element;
  },
  
  opacity : function(element, options) {
    options = options || {};
    if (options.effects || Browser.effects) {
      new Effect.Opacity(element, Object.extend({
        duration : 0.1,
        transition : Effect.Transitions.linear
      }, options || {}));
    }
    return element;
  },
  
  slideDown : function(element, options) {
    if (element.visible()) {
      return;
    }
    options = options || {};
    if (options.effects || Browser.effects) {
      element.slideDown(Object.extend({
        duration : 0.2
      }, options));
    } else {
      element.show();
      $call(options.afterFinish);
    }
    return element;
  },
  
  blindUp : function(element, options) {
    if (!element.visible()) {
      return;
    }
    options = options || {};
    if (options.effects || Browser.effects) {
      element.blindUp(Object.extend({
        duration : 0.2
      }, options));
    } else {
      element.hide();
      $call(options.afterFinish);
    }
    return element;
  },
  
  style : function(element, style, options) {
    options = options || {};
    if (options.effects || Browser.effects) {
      new Effect.Morph(element, Object.extend({
        "duration" : 0.4,
        "style" : style
      }, options));
    } else {
      element.setStyle(style);
      $call(options.afterFinish);
    }
    return element;
  },
  
  remove : function(element) {
    return this.blindUp(element, {
      duration : 0.2,
      afterFinish : function() {
        element.remove();
      }
    });
  },
  
  show : function(element, options) {
    if (element.visible()) {
      return;
    }
    options = options || {};
    if (options.effects || Browser.effects) {
      element.appear(Object.extend({
        duration : 0.25
      }, options));
    } else {
      element.show();
      $call(options.afterFinish);
    }
    return element;
  },
  
  hide : function(element, options) {
    if (!element.visible()) {
      return;
    }
    options = options || {};
    if (options.effects || Browser.effects) {
      element.fade(Object.extend({
        duration : 0.2
      }, options));
    } else {
      element.hide();
      $call(options.afterFinish);
    }
    return element;
  },
  
  toggle : function(element, options) {
    return this[element.visible() ? "hide" : "show"](element, options);
  }
});

/**
 * 添加可拖动的元素，方法是设置Element的属性draggable=true 参考enableDrag, createDrag
 */
(function() {
  var initPointer, currentDraggable, dragging = false;

  function onMousedown(event) {
    var draggable = event.findElement('[draggable="true"]');
    if (!draggable) {
      return;
    }
    var element = event.element();
    if (element && element.tagName == "DIV") {
      Event.stop(event);

      currentDraggable = draggable;
      initPointer = event.pointer();

      document.observe("mousemove", onMousemove).observe("mouseup", onMouseup);
    }
  }

  function onMousemove(event) {
    Event.stop(event);

    if (dragging)
      fire('drag:updated', event);
    else {
      dragging = true;
      fire('drag:started', event);
    }
  }

  function onMouseup(event) {
    Event.stop(event);

    document.stopObserving('mousemove', onMousemove).stopObserving('mouseup',
        onMouseup);
    if (dragging) {
      dragging = false;
      fire('drag:ended', event);
    }
  }

  function fire(eventName, mouseEvent) {
    var pointer = mouseEvent.pointer();

    currentDraggable.fire(eventName, {
      dx : pointer.x - initPointer.x,
      dy : pointer.y - initPointer.y,
      mouseEvent : mouseEvent
    });
  }

  document.observe("keydown", function(ev) {
    var code = (ev.which) ? ev.which : ev.keyCode;
    if (ev.ctrlKey && code == Event.KEY_RETURN) {
      var arr = $$("input[key=ctrlReturn]");
      if (arr && arr.length > 0) {
        arr[0].click();
      }
    }
  });

  document.observe('mousedown', onMousedown);
})();

(function() {
  var eventMatchers = {
    'HTMLEvents' : /^(?:load|unload|abort|error|select|change|submit|reset|focus|blur|resize|scroll)$/,
    'MouseEvents' : /^(?:click|mouse(?:down|up|over|move|out))$/
  };
  var defaultOptions = {
    pointerX : 0,
    pointerY : 0,
    button : 0,
    ctrlKey : false,
    altKey : false,
    shiftKey : false,
    metaKey : false,
    bubbles : true,
    cancelable : true
  };

  Event.simulate = function(element, eventName) {
    var options = Object.extend(defaultOptions, arguments[2] || {});
    var oEvent, eventType = null;

    element = $(element);

    for ( var name in eventMatchers) {
      if (eventMatchers[name].test(eventName)) {
        eventType = name;
        break;
      }
    }

    if (!eventType)
      throw new SyntaxError(
          'Only HTMLEvents and MouseEvents interfaces are supported');

    if (document.createEvent) {
      oEvent = document.createEvent(eventType);
      if (eventType == 'HTMLEvents') {
        oEvent.initEvent(eventName, options.bubbles, options.cancelable);
      } else {
        oEvent.initMouseEvent(eventName, options.bubbles, options.cancelable,
            document.defaultView, options.button, options.pointerX,
            options.pointerY, options.pointerX, options.pointerY,
            options.ctrlKey, options.altKey, options.shiftKey, options.metaKey,
            options.button, element);
      }
      element.dispatchEvent(oEvent);
    } else {
      options.clientX = options.pointerX;
      options.clientY = options.pointerY;
      oEvent = Object.extend(document.createEventObject(), options);
      element.fireEvent('on' + eventName, oEvent);
    }
    return element;
  };

  Element.addMethods({
    simulate : Event.simulate
  });
})();