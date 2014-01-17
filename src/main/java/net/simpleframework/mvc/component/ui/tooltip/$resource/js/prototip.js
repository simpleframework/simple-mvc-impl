//  http://www.nickstakenburg.com/projects/prototip2/
var Prototip = {
  Version: '2.2.3'
};

Prototip.Styles = {
  'default': {
    images: 'default/',
    className: 'default',
    borderColor: '#d0d0d0',
    stem: { height: 12, width: 15 }
  },
  
  'protoblue': {
  	images: 'protoblue/',
    className: 'protoblue',
    borderColor: '#116497',
    stem: { height: 12, width: 15 }
  },

  'darkgrey': {
  	images: 'darkgrey/',
    className: 'darkgrey',
    borderColor: '#666',
    stem: { height: 12, width: 15 }
  }
};

var Tips = {
  options: {
    paths: {
      images: 'images/',
      javascript: ''
    },
    zIndex: 6000
  }
};

Object.extend(Prototip, {
  REQUIRED_Prototype: "1.7",
  
  convertVersionString: function(ver) {
    var iVer = ver.replace(/_.*|\./g, "");
    iVer = parseInt(iVer + "0".times(4 - iVer.length));
    return ver.indexOf("_") > -1 ? iVer - 1 : iVer;
  },
  
  require: function(jsLib) {
    if (typeof window[jsLib] == "undefined" || 
    		this.convertVersionString(window[jsLib].Version) < this.convertVersionString(this["REQUIRED_" + jsLib])) {
      throw "Prototip requires " + jsLib + " >= " + this["REQUIRED_" + jsLib];
    }
  },
  
  start: function() {
    this.require("Prototype");
    var rule = /prototip([\w\d-_.]+)?\.js(.*)/;
    this.path = (($$("script[src]").find(function(a) {
      return a.src.match(rule);
    }) || {}).src || "").replace(rule, "");
    
    Tips.paths = function(c) {
      return {
        images: /^(https?:\/\/|\/)/.test(c.images) ? c.images: this.path + c.images,
        javascript: /^(https?:\/\/|\/)/.test(c.javascript) ? c.javascript: this.path + c.javascript
      };
    }.bind(this)(Tips.options.paths);
     
    if (!Browser.support.canvas) {
    	if (document.documentMode < 8 || document.namespaces.ns_vml) {
    		$ready(function() {
		      var c = document.createStyleSheet();
		      c.cssText = "ns_vml\\:*{behavior:url(#default#VML)}";
		    });
    	} else {
    		document.namespaces.add("ns_vml", "urn:schemas-microsoft-com:vml", "#default#VML");	
    	}
    }
      
    Tips.initialize();
    Element.observe(window, "unload", this.unload);
  },
  
  toggleInt: function(b) {
    return b > 0 ? -1 * b: b.abs();
  },
  
  unload: function() {
    Tips.removeAll();
  }
});

Object.extend(Tips, function() {
  function _remove(tip) {
  	if (tip) {
  		tip.deactivate();
  		if (tip.tooltip) {
  			tip.wrapper.remove();
  			if (Tips.fixIE) {
  				tip.iframeShim.remove();
  			}
  		}
  		Tips.tips = Tips.tips.without(tip);
  	}
  }

  return {
    tips: [],
    
    visible: [],
    
    initialize: function() {
      this.zIndexTop = this.zIndex;
    },
    
    _inverse: {
      left: "right",
      right: "left",
      top: "bottom",
      bottom: "top",
      middle: "middle",
      horizontal: "vertical",
      vertical: "horizontal"
    },
    
    _stemTranslation: {
      width: "horizontal",
      height: "vertical"
    },
    
    inverseStem: function(c) {
      return ! arguments[1] ? c: this._inverse[c];
    },
    
    fixIE: function(d) {
    	var agent = (new RegExp("MSIE ([\\d.]+)")).exec(d);
    	return agent ? parseFloat(agent[1]) < 7 : false;
    }(navigator.userAgent),
    
    add: function(tip) {
    	this.tips.push(tip);
    },
    
    remove: function(element) {
      var rTip, removes = [];
      for (var j = 0, i = this.tips.length; j < i; j++) {
        var tip = this.tips[j];
        if (rTip || tip.element != $(element)) {
        	if (!tip.element.parentNode) {
        		removes.push(tip);
        	}
        } else {
        	rTip = tip;
        }
      }
      _remove(rTip);
      for (var j = 0, i = removes.length; j < i; j++) {
        _remove(removes[j]);
      }
      element.prototip = null;
    },
    
    removeAll: function() {
      for (var a = 0, d = this.tips.length; a < d; a++) {
      	_remove(this.tips[a]);
      }
    },
    
    raise: function(tip) {
      if (tip != this._highest) {
        if (this.visible.length === 0) {
          this.zIndexTop = this.options.zIndex;
          for (var d = 0,
          f = this.tips.length; d < f; d++) {
            this.tips[d].wrapper.setStyle({
              zIndex: this.options.zIndex
            });
          }
        }
        tip.wrapper.setStyle({
          zIndex: this.zIndexTop++
        });
        this._highest = tip;
      }
    },
    
    addVisibile: function(c) {
      this.removeVisible(c);
      this.visible.push(c);
    },
    
    removeVisible: function(c) {
      this.visible = this.visible.without(c);
    },
    
    hideAll: function() {
      Tips.visible.invoke("hide");
    },
    
    hook: function(tip, target) {
    	tip = $(tip),
      target = $(target);
      var opt = Object.extend({
        offset: {
          x: 0,
          y: 0
        },
        
        position: false
      }, arguments[2] || {});
      
      var s = opt.mouse || target.cumulativeOffset();
      s.left += opt.offset.x;
      s.top += opt.offset.y;
      var r = opt.mouse ? [0, 0] : target.cumulativeScrollOffset(),
      		vpOffset = document.viewport.getScrollOffsets(),
      		p = opt.mouse ? "mouseHook" : "target";
      
      s.left += -1 * (r[0] - vpOffset[0]),
      s.top += -1 * (r[1] - vpOffset[1]);
      
      if (opt.mouse) {
        var o = [0, 0];
        o.width = 0;
        o.height = 0;
      }
      
      var n = {
        element: tip.getDimensions()
      },
      m = {
        element: Object.clone(s)
      };
      
      n[p] = opt.mouse ? o: target.getDimensions(),
      m[p] = Object.clone(s);
      
      for (var l in m) {
        switch (opt[l]) {
        case "topRight":
        case "rightTop":
          m[l].left += n[l].width;
          break;
        case "topMiddle":
          m[l].left += n[l].width / 2;
          break;
        case "rightMiddle":
          m[l].left += n[l].width,
          m[l].top += n[l].height / 2;
          break;
        case "bottomLeft":
        case "leftBottom":
          m[l].top += n[l].height;
          break;
        case "bottomRight":
        case "rightBottom":
          m[l].left += n[l].width,
          m[l].top += n[l].height;
          break;
        case "bottomMiddle":
          m[l].left += n[l].width / 2,
          m[l].top += n[l].height;
          break;
        case "leftMiddle":
          m[l].top += n[l].height / 2;
        }
      }
      
      s.left += -1 * (m.element.left - m[p].left);
      s.top += -1 * (m.element.top - m[p].top);
      if (opt.position) {
      	tip.setStyle({
          left: s.left + "px",
          top: s.top + "px"
        });
      }
      return s;
    }
  };
}());

Tips.initialize();

/** tip */

var Tip = Class.create({
  initialize: function(element, content) {
    this.element = $(element);
    if (!this.element) {
    	throw "Prototip: Element not available, cannot create a tooltip.";
    }
    Tips.remove(this.element);
    
    var b = Object.isString(content) || Object.isElement(content);
    var	opt = b ? arguments[2] || [] : content;
    this.content = b ? content : null;
    
    this.options = Object.extend(Object.extend({
      border: 6,
      borderColor: "#ccc",
      radius: 6,
      className: Tips.options.className,
      closeButton: Tips.options.closeButtons,
      delay: !opt.showOn || opt.showOn != "click" ? 0.14 : false,
      hideOn: "mouseleave",
      hideOthers: true,
      hook: opt.hook,
      offset: opt.hook ? {
        x: 0,
        y: 0
      }: {
        x: 16,
        y: 16
      },
      fixed: opt.hook && !opt.hook.mouse ? true : false,
      showOn: 'mouseenter',
      stem: false,
      style: "default",
      target: this.element,
      title: false,
      viewport: opt.hook && !opt.hook.mouse ? false : true,
      width: false
    }, Prototip.Styles[opt.style ? opt.style : "default"]), opt);
    
    this.target = $(this.options.target);
    this.radius = this.options.radius;
    this.border = this.radius > this.options.border ? this.radius: this.options.border;
    if (this.options.images) {
    	this.images = this.options.images.include("://") ? 
    			this.options.images : Tips.paths.images + this.options.images;
    } else {
    	this.images = Tips.paths.images + "styles/" + (this.options.style || "") + "/";
    }
    if (!this.images.endsWith("/")) {
    	this.images += "/";
    }
    
    if (Object.isString(this.options.stem)) {
    	this.options.stem = {
    		position: this.options.stem
    	};	
    }
    
    if (this.options.stem.position) {
    	this.options.stem = Object.extend(
    			Object.clone(Prototip.Styles[this.options.style].stem) || {},
    	    this.options.stem);
    	this.options.stem.position = [this.options.stem.position.match(/[a-z]+/)[0].toLowerCase(), 
    	                              this.options.stem.position.match(/[A-Z][a-z]+/)[0].toLowerCase()]; 
    	this.options.stem.orientation = ["left", "right"].member(this.options.stem.position[0]) ? "horizontal": "vertical"; 
    	this.stemInverse = {
    		horizontal: false,
    		vertical: false
    	};
    }
    
    if (this.options.hook.mouse) {
      var h = this.options.hook.tip.match(/[a-z]+/)[0].toLowerCase();
      this.mouseHook = Tips._inverse[h] + 
      	Tips._inverse[this.options.hook.tip.match(/[A-Z][a-z]+/)[0].toLowerCase()].capitalize();
    }
    
    this.setup();
    Tips.add(this);
    this.activate();
    
    Prototip.extend(this);
  },
  
  setup: function() {
    this.wrapper = (new Element("div", {
      className: "prototip"
    })).setStyle({
      zIndex: Tips.options.zIndex
    });
    
    this.wrapper.hide();
    
    if (Tips.fixIE) {
    	(this.iframeShim = new Element("iframe", {
        className: "iframeShim",
        src: "javascript:false;",
        frameBorder: 0
      })).setStyle({
        display: "none",
        zIndex: Tips.options.zIndex - 1,
        opacity: 0
      });
    }
   
    this.tip = new Element("div", {
      className: "content"
    });
    this.title = (new Element("div", {
      className: "title"
    })).hide();
    if (this.options.closeButton || this.options.hideOn.element && this.options.hideOn.element == "closeButton") {
      this.closeButton = (new Element("div", {
        className: "close"
      })).setPngBackground(this.images + "close.png");
    }
  },
  
  build: function() {
    if (document.loaded) {
      this._build(),
      this._isBuilding = true;
      return true;
    }
    if (!this._isBuilding) {
    	$ready(this._build);
      return false;
    }
  },
  
  _createCorner : function(element, pi) { /* pi = tl tr bl br */
		var opt = arguments[2] || this.options;
		var radius = opt.radius;
		var border = opt.border;
		var p = {
			top : pi.charAt(0) == "t",
			left : pi.charAt(1) == "l"
		};

		if (Browser.support.canvas) {
			var canvas = new Element("canvas", {
				className : "cornerCanvas" + pi.capitalize(),
				width : border + "px",
				height : border + "px"
			});
			element.insert(canvas);
			var ctx = canvas.getContext("2d");
			ctx.fillStyle = opt.backgroundColor;
			ctx.arc(p.left ? radius : border - radius, p.top ? radius : border
					- radius, radius, 0, Math.PI * 2, true);
			ctx.fill();
			ctx.fillRect(p.left ? radius : 0, 0, border - radius, border);
			ctx.fillRect(0, p.top ? radius : 0, border, border - radius);
		} else {
			var div = new Element("div");
			div.setStyle({
				width : border + "px",
				height : border + "px",
				margin : 0,
				padding : 0,
				display : "block",
				position : "relative",
				overflow : "hidden"
			});
			element.insert(div);
			var vml = (new Element("ns_vml:roundrect", {
				fillcolor : opt.backgroundColor,
				strokeWeight : "1px",
				strokeColor : opt.backgroundColor,
				arcSize : (radius / border * 0.5).toFixed(2)
			})).setStyle({
				width : 2 * border - 1 + "px",
				height : 2 * border - 1 + "px",
				position : "absolute",
				left : (p.left ? 0 : -1 * border) + "px",
				top : (p.top ? 0 : -1 * border) + "px"
			});
			div.insert(vml);
			vml.outerHTML = vml.outerHTML;
		}
	},
	
  _build: function() {
    $(document.body).insert(this.wrapper);
    if (Tips.fixIE) {
    	$(document.body).insert(this.iframeShim);
    }
       
    var eleName = "wrapper";
    if (this.options.stem.position) {
      this.stem = (new Element("div", {
        className: "prototip_Stem"
      })).setStyle({
        height: this.options.stem[this.options.stem.orientation == "vertical" ? 
        		"height": "width"] + "px"
      });
      
      var h = this.options.stem.orientation == "horizontal";
      this[eleName].insert(this.stemWrapper = (new Element("div", {
        className: "prototip_StemWrapper clearfix"
      })).insert(this.stemBox = new Element("div", {
        className: "prototip_StemBox clearfix"
      })));
      
      this.stem.insert(this.stemImage = (new Element("div", {
        className: "prototip_StemImage"
      })).setStyle({
        height: this.options.stem[h ? "width": "height"] + "px",
        width: this.options.stem[h ? "height": "width"] + "px"
      }));
      
      if (Tips.fixIE && 
      		!this.options.stem.position[1].toUpperCase().include("MIDDLE")) {
      	this.stemImage.setStyle({
          display: "inline"
        });
      }
      eleName = "stemBox";
    }
    if (this.border) {
      var b = this.border, corners;
      this[eleName].insert(this.borderFrame = (new Element("ul", {
        className: "borderFrame"
      })).insert(this.borderTop = (new Element("li", {
        className: "borderTop borderRow"
      })).setStyle("height: " + b + "px").insert((new Element("div", {
        className: "prototip_CornerWrapper prototip_CornerWrapperTopLeft"
      })).insert(new Element("div", {
        className: "prototip_Corner"
      }))).insert(corners = (new Element("div", {
        className: "prototip_BetweenCorners"
      })).setStyle({
        height: b + "px"
      }).insert((new Element("div", {
        className: "prototip_Between"
      })).setStyle({
        margin: "0 " + b + "px",
        height: b + "px"
      }))).insert((new Element("div", {
        className: "prototip_CornerWrapper prototip_CornerWrapperTopRight"
      })).insert(new Element("div", {
        className: "prototip_Corner"
      })))).insert(this.borderMiddle = (new Element("li", {
        className: "borderMiddle borderRow"
      })).insert(this.borderCenter = (new Element("div", {
        className: "borderCenter"
      })).setStyle("padding: 0 " + b + "px"))).insert(this.borderBottom = (new Element("li", {
        className: "borderBottom borderRow"
      })).setStyle("height: " + b + "px").insert((new Element("div", {
        className: "prototip_CornerWrapper prototip_CornerWrapperBottomLeft"
      })).insert(new Element("div", {
        className: "prototip_Corner"
      }))).insert(corners.cloneNode(true)).insert((new Element("div", {
        className: "prototip_CornerWrapper prototip_CornerWrapperBottomRight"
      })).insert(new Element("div", {
        className: "prototip_Corner"
      })))));
      
      eleName = "borderCenter";
      var cornerArr = this.borderFrame.select(".prototip_Corner");
      $w("tl tr bl br").each(function(pi, index) {
      	var c = cornerArr[index];
      	if (this.radius > 0) {
        	this._createCorner(c, pi, {
	          backgroundColor: this.options.borderColor,
	          border: b,
	          radius: this.options.radius
	        });
        } else {
        	c.addClassName("prototip_Fill");
        }
        c.setStyle({
          width: b + "px",
          height: b + "px"
        }).addClassName("prototip_Corner" + pi.capitalize());
      }.bind(this));
      
      this.borderFrame.select(
      		".prototip_Between", ".borderMiddle", ".prototip_Fill").invoke("setStyle", {
        backgroundColor: this.options.borderColor
      });
    }
    
    this[eleName].insert(this.tooltip = (new Element("div", {
      className: "tooltip " + this.options.className
    })).insert(this.toolbar = (new Element("div", {
      className: "toolbar"
    })).insert(this.title)));
    
    if (this.options.width) {
      var w = this.options.width;
      if (Object.isNumber(w)) {
      	w += "px";
      }
      this.tooltip.setStyle("width:" + w);
    }
    
    if (this.stem) {
      var orientation = {};
      orientation[this.options.stem.orientation == "horizontal" ? "top": "bottom"] = this.stem;
      this.wrapper.insert(orientation);
      this.positionStem();
    }
    
    this.tooltip.insert(this.tip);
    
    this._update({
      title: this.options.title,
      content: this.content
    });
  },
  
  _update: function(tip) {
    var v = this.wrapper.getStyle("visibility");
    this.wrapper.setStyle("height:auto;width:auto;visibility:hidden").show();
    if (this.border) {
    	this.borderTop.setStyle("height:0");
    }
    
    if (tip.title) {
    	this.title.show().update(tip.title);
    	this.toolbar.show();
    } else {
    	if (!this.closeButton) {
    		this.title.hide(); 
    		this.toolbar.hide();	
    	}
    }
    
    if (Object.isElement(tip.content)) {
    	tip.content.show();
    }
    if (Object.isString(tip.content) || Object.isElement(tip.content)) {
    	this.tip.update(tip.content);
    }
    
    this.tooltip.setStyle({
      width: this.tooltip.getWidth() + "px"
    });
  
    this.wrapper.setStyle("visibility:visible").show();
    this.tooltip.show();
    
    var dim = this.tooltip.getDimensions();
    var oStyle = {
      width: dim.width + "px"
    };
    var eles = [this.wrapper];
    if (Tips.fixIE) {
    	eles.push(this.iframeShim);
    }
    if (this.closeButton) {
    	this.title.show().insert({
        top: this.closeButton
      });
    	this.toolbar.show();
    }
    if (tip.title || this.closeButton) {
    	this.toolbar.setStyle("width: 100%");
    }
    oStyle.height = null;
    this.wrapper.setStyle({
      visibility: v
    });
    this.tip.addClassName("clearfix");
    
    if (tip.title || this.closeButton) {
    	this.title.addClassName("clearfix");
    }
    if (this.border) {
    	this.borderTop.setStyle("height:" + this.border + "px");
    	oStyle = "width: " + (dim.width + 2 * this.border) + "px";
    	eles.push(this.borderFrame);
    }
  
    eles.invoke("setStyle", oStyle);
    
    if (this.stem) {
    	this.positionStem();
    	if (this.options.stem.orientation == "horizontal") {
    		this.wrapper.setStyle({
          width: this.wrapper.getWidth() + this.options.stem.height + "px"
        });
    	}
    }

    this.wrapper.hide();
  },
  
  activate: function() {
    this.eventShow = this.showDelayed.bindAsEventListener(this);
    this.eventHide = this.hide.bindAsEventListener(this);
    if (this.options.fixed && this.options.showOn == "mousemove") {
    	this.options.showOn = "mouseover";
    }
    if (this.options.showOn && this.options.showOn == this.options.hideOn) {
    	this.eventToggle = this.toggle.bindAsEventListener(this);
    	this.element.observe(this.options.showOn, this.eventToggle);
    }
    
    if (this.closeButton) {
    	this.closeButton.observe("mouseover",
    	    function(b) {
    	      b.setPngBackground(this.images + "close_hover.png");
    	    }.bind(this, this.closeButton)).observe("mouseout",
    	    function(b) {
    	      b.setPngBackground(this.images + "close.png");
    	    }.bind(this, this.closeButton));
    }
    
    var e = {
      element: this.eventToggle ? [] : [this.element],
      target: this.eventToggle ? [] : [this.target],
      tip: this.eventToggle ? [] : [this.wrapper],
      closeButton: [],
      none: []
    };
    var hideElement = this.options.hideOn.element;
    this.hideElement = hideElement || (this.options.hideOn ? "element": "none");
    this.hideTargets = e[this.hideElement];
    if (!this.hideTargets && hideElement && Object.isString(hideElement)) {
    	this.hideTargets = this.tip.select(hideElement);
    }
    
    $w("show hide").each(function(method) {
      var evn = this.options[method + "On"].event || this.options[method + "On"];
      if (evn == "mouseover") {
      	evn = "mouseenter";
      } else {
      	if (evn == "mouseout") {
      		evn = "mouseleave";
      	}
      }
      this[method + "Action"] = evn;
    }.bind(this));
    
    if (!this.eventToggle && this.options.showOn) {
    	this.element.observe(this.options.showOn, this.eventShow);
    }
    if (this.hideTargets && this.options.hideOn) {
    	this.hideTargets.invoke("observe", this.hideAction, this.eventHide);
    }
    if (!this.options.fixed && this.options.showOn == "click") {
    	this.eventPosition = this.position.bindAsEventListener(this);
    	this.element.observe("mousemove", this.eventPosition);
    } 
    
    this.buttonEvent = this.hide.wrap(function(proceed, element) {
      var closeBtn = element.findElement(".close");
      if (closeBtn) {
      	closeBtn.blur(); 
      	element.stop(); 
      	proceed(element);
      }
    }).bindAsEventListener(this);
    
    if (this.closeButton || this.options.hideOn && this.options.hideOn.element == ".close") {
    	this.wrapper.observe("click", this.buttonEvent);
    }
    if (this.options.showOn != "click" && this.hideElement != "element") {
    	this.eventCheckDelay = function() {
        this.clearTimer("show");
      }.bindAsEventListener(this);
      this.element.observe("mouseleave", this.eventCheckDelay);
    }
     
    if (this.options.hideOn || this.options.hideAfter) {
      var eles = [this.element, this.wrapper];
      this.activityEnter = function() {
        Tips.raise(this);
        this.cancelHideAfter();
      }.bindAsEventListener(this);
      
      this.activityLeave = this.hideAfter.bindAsEventListener(this);
      eles.invoke("observe", "mouseenter", this.activityEnter).
      	invoke("observe", "mouseleave", this.activityLeave);
    }
  },
  
  deactivate: function() {
  	if (this.options.showOn && this.options.showOn == this.options.hideOn) {
  		this.element.stopObserving(this.options.showOn, this.eventToggle);
  	} else {
  		if (this.options.showOn) {
  			this.element.stopObserving(this.options.showOn, this.eventShow);
  		}
  		if (this.hideTargets && this.options.hideOn && this.hideAction && this.eventHide) {
  			this.hideTargets.invoke("stopObserving", this.hideAction, this.eventHide);
  		}
  	}
  	
  	if (this.eventPosition) {
  		this.element.stopObserving("mousemove", this.eventPosition);
  	}
    if (this.eventCheckDelay) {
    	this.element.stopObserving("mouseout", this.eventCheckDelay);
    }
    
    this.wrapper.stopObserving();
    if (this.options.hideOn || this.options.hideAfter) {
    	this.element.stopObserving("mouseenter", this.activityEnter).
    		stopObserving("mouseleave", this.activityLeave);
    }
  },
  
  showDelayed: function(opt) {
    if (!this.tooltip) {
      if (!this.build()) {
        return
      }
    }
    this.position(opt);
    if (!this.wrapper.visible()) {
    	this.clearTimer("show");
    	this.showTimer = this.show.bind(this).delay(this.options.delay);
    }
  },
  
  clearTimer: function(method) {
  	if (this[method + "Timer"]) {
  		clearTimeout(this[method + "Timer"]);
  	}
  },
  
  show: function() {
  	if (!this.wrapper.visible()) {
  		if (Tips.fixIE) {
  			this.iframeShim.show();
  		}
  		if (this.options.hideOthers) {
  			Tips.hideAll();
  		}
  		Tips.addVisibile(this);
  		
  		$Effect.show(this.wrapper, {
  			effects : this.options.effects,
  			afterFinish : function(effect) {
  				this.tooltip.show();
  				if (this.stem) {
  	  			this.stem.show();
  	  		}
  	  		this.element.fire("prototip:shown");
  			}.bind(this)
  		});		
  	}
  },
  
  hideAfter: function(b) {
    if (this.options.hideAfter) {
    	this.cancelHideAfter();
    	this.hideAfterTimer = this.hide.bind(this).delay(this.options.hideAfter);	
    }
  },
  
  cancelHideAfter: function() {
  	if (this.options.hideAfter) {
  		this.clearTimer("hideAfter");
  	}
  },
  
  hide: function() {
    this.clearTimer("show");
    if (this.wrapper.visible()) {
    	this.afterHide();
    }
  },
  
  afterHide: function() {
  	if (Tips.fixIE) {
  		this.iframeShim.hide();
  	}
  	$Effect.hide(this.wrapper, {
    	effects : this.options.effects,
    	afterFinish : function(effect) {
    		(this.borderFrame || this.tooltip).show();
        Tips.removeVisible(this);
        this.element.fire("prototip:hidden");
			}.bind(this)
    });
  },
  
  toggle: function(opt) {
  	if (this.wrapper && this.wrapper.visible()) {
  		this.hide(opt);
  	} else {
  		this.showDelayed(opt);
  	}
  },
  
  positionStem: function() {
    var stem = this.options.stem;
    var stemInverse = arguments[0] || this.stemInverse;
    var p1 = Tips.inverseStem(stem.position[0], stemInverse[stem.orientation]);
    var p2 = Tips.inverseStem(stem.position[1], stemInverse[Tips._inverse[stem.orientation]]);
    var radius = this.radius || 0;
    this.stemImage.setPngBackground(this.images + p1 + p2 + ".png");
    if (stem.orientation == "horizontal") {
      var i = p1 == "left" ? stem.height: 0;
      this.stemWrapper.setStyle("left: " + i + "px;"),
      this.stemImage.setStyle({
        "float": p1
      });
      this.stem.setStyle({
        left: 0,
        top: p2 == "bottom" ? "100%": p2 == "middle" ? "50%": 0,
        marginTop: (p2 == "bottom" ? -1 * stem.width : p2 == "middle" ? 
        		-0.5 * stem.width: 0) + (p2 == "bottom" ? -1 * radius : p2 == "top" ? radius : 0) + "px"
      });
    } else {
      this.stemWrapper.setStyle(p1 == "top" ? 
      		"margin: 0; padding: " + stem.height + "px 0 0 0;" : 
      		"padding: 0; margin: 0 0 " + stem.height + "px 0;");
      this.stem.setStyle(p1 == "top" ? "top: 0; bottom: auto;": "top: auto; bottom: 0;");
      this.stemImage.setStyle({
        margin: 0,
        "float": p2 != "middle" ? p2: "none"
      });
      if (p2 == "middle") {
      	this.stemImage.setStyle("margin: 0 auto;");
      }else {
      	this.stemImage.setStyle("margin-" + p2 + ": " + radius + "px;");
      }
      if (Browser.WebKit419) {
      	if (p1 == "bottom") {
      		this.stem.setStyle({
            position: "relative",
            clear: "both",
            top: "auto",
            bottom: "auto",
            "float": "left",
            width: "100%",
            margin: -1 * stem.height + "px 0 0 0"
          });
      		this.stem.style.display = "block";
      	} else  {
      		this.stem.setStyle({
            position: "absolute",
            "float": "none",
            margin: 0
          });
      	}
      }
    }
    this.stemInverse = stemInverse;
  },
  
  position: function(opt) {
    if (!this.tooltip) {
      if (!this.build()) {
        return
      }
    }
    Tips.raise(this);
    if (Tips.fixIE) {
      var dim = this.wrapper.getDimensions(); 
      if (!this.iframeShimDimensions || 
      		this.iframeShimDimensions.height != dim.height || 
      		this.iframeShimDimensions.width != dim.width) {
      	this.iframeShim.setStyle({
          width: dim.width + "px",
          height: dim.height + "px"
        });	
      }
      this.iframeShimDimensions = dim;
    }
    if (this.options.hook) {
      var opt2, p;
      if (this.mouseHook) {
        var v = document.viewport.getScrollOffsets();
        var fakePointer = opt.fakePointer || {};
        var mOffset, delta = 2;
        switch (this.mouseHook.toUpperCase()) {
        	case "LEFTTOP":
        	case "TOPLEFT":
        		mOffset = {
        			x: 0 - delta,
        			y: 0 - delta
          	};
        		break;
        	case "TOPMIDDLE":
        		mOffset = {
        			x: 0,
        			y: 0 - delta
          	};
        		break;
        	case "TOPRIGHT":
        	case "RIGHTTOP":
        		mOffset = {
        			x: delta,
        			y: 0 - delta
          	};
        		break;
	        case "RIGHTMIDDLE":
	        	mOffset = {
	            x: delta,
	            y: 0
	          };
	          break;
	        case "RIGHTBOTTOM":
	        case "BOTTOMRIGHT":
	        	mOffset = {
	            x: delta,
	            y: delta
	          };
	          break;
	        case "BOTTOMMIDDLE":
	        	mOffset = {
	            x: 0,
	            y: delta
	          };
	          break;
	        case "BOTTOMLEFT":
	        case "LEFTBOTTOM":
	        	mOffset = {
	            x: 0 - delta,
	            y: delta
	          };
	          break;
	        case "LEFTMIDDLE":
	        	mOffset = {
	            x: 0 - delta,
	            y: 0
	          };
        }
        mOffset.x += this.options.offset.x;
        mOffset.y += this.options.offset.y;
        opt2 = Object.extend({
          offset: mOffset
        }, {
          element: this.options.hook.tip,
          mouseHook: this.mouseHook,
          mouse: {
            top: fakePointer.pointerY || Event.pointerY(opt) - v.top,
            left: fakePointer.pointerX || Event.pointerX(opt) - v.left
          }
        });
        p = Tips.hook(this.wrapper, this.target, opt2);
        if (this.options.viewport) {
          var r = this.getPositionWithinViewport(p);
          var stemInverse = r.stemInverse;
          p = r.position;
          p.left += stemInverse.vertical ? 
          		2 * Prototip.toggleInt(mOffset.x - this.options.offset.x) : 0;
          p.top += stemInverse.vertical ? 
          		2 * Prototip.toggleInt(mOffset.y - this.options.offset.y) : 0;
          if (this.stem && 
          		(this.stemInverse.horizontal != stemInverse.horizontal || 
          		 this.stemInverse.vertical != stemInverse.vertical)) {
          	this.positionStem(stemInverse);
          }
        }
        p = {
          left: p.left + "px",
          top: p.top + "px"
        };
        this.wrapper.setStyle(p);
      } else {
      	opt2 = Object.extend({
          offset: this.options.offset
        }, {
          element: this.options.hook.tip,
          target: this.options.hook.target
        });
        p = Tips.hook(this.wrapper, this.target, Object.extend({
          position: true
        }, opt2));
        p = {
        	left: Math.max(p.left, 5) + "px",
          top: Math.max(p.top, 5) + "px"
        };
        this.wrapper.setStyle(p);
      }
      if (Tips.fixIE) {
      	this.iframeShim.setStyle(p);
      }
    } else {
    	/* no hook */
      var targetOffset = this.target.cumulativeOffset();
      var fakePointer = opt.fakePointer || {};
      var p = {
        left: (this.options.fixed ? targetOffset[0] : 
        	fakePointer.pointerX || Event.pointerX(opt)) + this.options.offset.x,
        top: (this.options.fixed ? targetOffset[1] : 
        	fakePointer.pointerY || Event.pointerY(opt)) + this.options.offset.y
      };
      if (!this.options.fixed && this.element !== this.target) {
        var elementOffset = this.element.cumulativeOffset();
        p.left += -1 * (elementOffset[0] - targetOffset[0]);
        p.top += -1 * (elementOffset[1] - targetOffset[1]);
      }
      if (!this.options.fixed && this.options.viewport) {
        var r = this.getPositionWithinViewport(p);
        var q = r.stemInverse;
        p = r.position;
        if (this.stem && 
        		(this.stemInverse.horizontal != stemInverse.horizontal || 
        		this.stemInverse.vertical != stemInverse.vertical)) {
        	this.positionStem(stemInverse);
        }
      }
      p = {
        left: p.left + "px",
        top: p.top + "px"
      };
      this.wrapper.setStyle(p);
      if (Tips.fixIE) {
      	this.iframeShim.setStyle(p);
      }
    }
  },
  
  getPositionWithinViewport: function(p) {
    var dim = this.wrapper.getDimensions();
    var vpOffsets = document.viewport.getScrollOffsets();
    var vpDim = document.viewport.getDimensions(); 
    var _stemInverse = {
      horizontal: false,
      vertical: false
    };
    var lt = {
      left: "width",
      top: "height"
    };
    for (var attri in lt) {
    	var wh = lt[attri];
    	if (p[attri] + dim[wh] - vpOffsets[attri] > vpDim[wh]) {
    		p[attri] = p[attri] - (dim[wh] + 2 * this.options.offset[attri == "left" ? "x": "y"]);
    		if (this.stem) {
    			_stemInverse[Tips._stemTranslation[wh]] = true;
    		}
    	}
    }
    return {
      position: p,
      stemInverse: _stemInverse
    };
  }
});

Prototip.Methods = {
  hold: function(tip) {
    if (tip.element && !tip.element.parentNode) {
    	return true;
    }
    return false;
  },
  
  show: function() {
    if (!Prototip.Methods.hold(this)) {
      Tips.raise(this);
      this.cancelHideAfter();
      
      var opt = {};
      if (this.options.hook && !this.options.hook.mouse) {
        opt.fakePointer = {
          pointerX: 0,
          pointerY: 0
        };
      } else {
        var e = this.target.cumulativeOffset(),
        	h = this.target.cumulativeScrollOffset(),
        	g = document.viewport.getScrollOffsets();
        
        e.left += -1 * (h[0] - g[0]);
        e.top += -1 * (h[1] - g[1]);
        
        opt.fakePointer = {
          pointerX: e.left,
          pointerY: e.top
        };
      }
      this.showDelayed(opt);
      this.hideAfter();
    }
  }
};

Prototip.extend = function(b) {
  b.element.prototip = {},
  
  Object.extend(b.element.prototip, {
    show: Prototip.Methods.show.bind(b),
    
    hide: b.hide.bind(b),
    
    remove: Tips.remove.bind(Tips, b.element)
  });
};

Prototip.start();

function __prototip_actions_init(actionFunc) {
	actionFunc.createTip = function(selector, contentRef, cache, contentHtml, 
			jsTipCreate, optionIndex) {
		if (!selector) {
			return;
		}
		Tips.hideAll();
		$Elements(selector).each(function(element) {
			if (element.prototip) 
				return;
			var c = new Element("div");
			
			if (Object.isFunction(jsTipCreate))
				jsTipCreate(element, c);
			
			contentRef = contentRef || actionFunc.contentRef;
			if (contentRef) {
				c.update($MessageConst["$Loading.0"]);			
				element.observe("prototip:shown", function() {
					if (cache && c.loaded)
						return;
					var ar = Object.isString(contentRef) ? 
							$Actions[contentRef] : contentRef;
					ar.container = c;
					ar.doCallback = function(req, responseText, json) {
						tip.position();
					};
					ar(element.getAttribute("params"));
					c.loaded = true;
				});
				actionFunc.contentRef = contentRef;
			} else {
				if (contentHtml) {
					c.update(contentHtml);
				} else {
					var attr = element.next("tipContent") || element.next();
					if (attr && !attr.visible()) {	
						c.update(attr.innerHTML); 
					} else { 
						return; 
					}
				}
			}
			
			var tip = new Tip(element, c, 
					actionFunc.options[optionIndex > 0 ? optionIndex : 0]);
			tip.wrapper.tip = tip;
		});
	};
	
	actionFunc.show = function(element) {
		if (!(element = ($(element) || $$(element)[0]))) 
			return; 
		element.prototip.show();
	};
	
	actionFunc.hide = function(element) {
		if (!(element = ($(element) || $$(element)[0]))) 
			return; 
		element.prototip.hide();
	};
	
	actionFunc.target = function(tipElement) {
		return $(tipElement).up(".prototip").tip.element;
	};
}
