var UI = {};

Object.extend(Class.Methods, {
	extend : Object.extend.methodize(),

	addMethods : Class.Methods.addMethods.wrap(function(proceed, source) {
		/* ensure we are not trying to add null or undefined */
		if (!source)
			return this;

		/* no callback, vanilla way */
		if (!source.hasOwnProperty('methodsAdded'))
			return proceed(source);

		var callback = source.methodsAdded;
		delete source.methodsAdded;
		proceed(source);
		callback.call(source, this);
		source.methodsAdded = callback;

		return this;
	}),

	addMethod : function(name, lambda) {
		var methods = {};
		methods[name] = lambda;
		return this.addMethods(methods);
	},

	method : function(name) {
		return this.prototype[name].valueOf();
	},

	classMethod : function() {
		$A(arguments).flatten().each(function(method) {
			this[method] = (function() {
				return this[method].apply(this, arguments);
			}).bind(this.prototype);
		}, this);
		return this;
	},

	undefMethod : function(name) {
		this.prototype[name] = undefined;
		return this;
	},

	removeMethod : function(name) {
		delete this.prototype[name];
		return this;
	},

	aliasMethod : function(newName, name) {
		this.prototype[newName] = this.prototype[name];
		return this;
	},

	aliasMethodChain : function(target, feature) {
		feature = feature.camelcase();

		this.aliasMethod(target + "Without" + feature, target);
		this.aliasMethod(target, target + "With" + feature);

		return this;
	}
});

Object.extend(Number.prototype, {
	snap : function(round) {
		return parseInt(round == 1 ? this : (this / round).floor() * round);
	}
});

Element.addMethods({
	getNumStyle : function(element, style) {
		var value = parseFloat($(element).getStyle(style));
		return isNaN(value) ? 0 : value;
	},
	
	borderDimensions : function(element) {
		return $w('top bottom left right').inject( {}, function(dims, key) {
			dims[key] = parseFloat(element.getStyle('border-' + key + '-width') || 0);
			return dims;
		});
	}
});

(function() {
	UI.Options = {
		methodsAdded : function(klass) {
			klass.classMethod($w(' setOptions allOptions optionsGetter optionsSetter optionsAccessor '));
		},

		setOptions : function(options) {
			if (!this.hasOwnProperty('options'))
				this.options = this.allOptions();

			this.options = Object.extend(this.options, options || {});
		},

		allOptions : function() {
			var superclass = this.constructor.superclass, ancestor = superclass && superclass.prototype;
			return (ancestor && ancestor.allOptions) ? Object.extend(ancestor.allOptions(), this.options) : Object
					.clone(this.options);
		},

		optionsGetter : function() {
			addOptionsAccessors(this, arguments, false);
		},

		optionsSetter : function() {
			addOptionsAccessors(this, arguments, true);
		},

		optionsAccessor : function() {
			this.optionsGetter.apply(this, arguments);
			this.optionsSetter.apply(this, arguments);
		}
	};

	// Internal
	function addOptionsAccessors(receiver, names, areSetters) {
		names = $A(names).flatten();

		if (names.empty())
			names = Object.keys(receiver.allOptions());

		names.each(function(name) {
			var accessorName = (areSetters ? 'set' : 'get') + name.camelcase();

			receiver[accessorName] = receiver[accessorName] || (areSetters ?

			function(value) {
				return this.options[name] = value;
			} : function() {
				return this.options[name];
			});
		});
	}
})();

UI.Window = Class.create(UI.Options, {
	options : {
		theme : "window_default",
		
		id : null,
		windowManager : null,

		top : null,
		left : null,
		width : 200,
		height : 300,
		minHeight : 100,
		minWidth : 200,
		maxHeight : null,
		maxWidth : null,
		altitude : "front",

		resizable : true,
		draggable : true,
		wired : false,

		activeOnClick : true,

		gridX : 1,
		gridY : 1,

		close : 'destroy',
		minimize : 'toggleFold',
		maximize : 'toggleMaximize'
	},

	initialize : function(options) {
		this.setOptions(options);
		this.windowManager = this.options.windowManager || UI.defaultWM;
		this.create();
		this.id = this.element.id;
		this.windowManager.register(this);
		this.render();
		if (this.options.activeOnClick)
			this.overlay.setStyle({
				zIndex : this.lastZIndex + 1
			}).show();
	},

	destroy : function($super) {
		this.hide();
		if (this.centerOptions)
			Event.stopObserving(this.windowManager.scrollContainer, "scroll",
					this.centerOptions.handler);
		this.windowManager.unregister(this);
		this.fire('destroyed');
	},

	fire : function(eventName, memo) {
		memo = memo || {};
		memo.window = this;
		return this.element.fire('window:' + eventName, memo);
	},

	observe : function(eventName, handler) {
		this.element.observe('window:' + eventName, handler.bind(this));
		return this;
	},

	show : function(modal) {
		if (this.visible)
			return this;

		this.fire('showing');
		this.addElements();
		$Effect.show(this.element, {
			effects : this.options.effects,
			afterFinish : function(effect) {
				this.fire('shown');
				this.visible = true;
			}.bind(this)
		});
		
		if (modal) {
			this.windowManager.startModalSession(this);
			this.modalSession = true;
		}
		return this;
	},

	hide : function() {
		if (!this.visible)
			return this;

		this.fire('hiding');
		$Effect.hide(this.element, {
			effects : this.options.effects,
			afterFinish : function(effect) {
			  this.windowManager.hide(this);
			  
				this.element.remove();
				this.fire('hidden');
				this.visible = false;
			}.bind(this)
		});
		
		if (this.modalSession) {
			this.windowManager.endModalSession(this);
			this.modalSession = false;
		}
		
		return this;
	},

	close : function() {
		return this.action('close');
	},

	activate : function() {
		return this.bringToFront().focus();
	},

	bringToFront : function() {
		return this.setAltitude('front');
	},

	sendToBack : function() {
		return this.setAltitude('back');
	},

	focus : function() {
		if (this.focused)
			return this;

		this.windowManager.focus(this);

		this.overlay.hide();

		this.element.addClassName(this.options.theme + '_focused');

		this.focused = true;
		this.fire('focused');
		return this;
	},

	blur : function() {
		if (!this.focused)
			return this;

		this.windowManager.blur(this);
		this.element.removeClassName(this.options.theme + '_focused');

		if (this.options.activeOnClick)
			this.overlay.setStyle({
				zIndex : this.lastZIndex + 1
			}).show();

		this.focused = false;
		this.fire('blurred');
		return this;
	},

	maximize : function() {
		if (this.maximized)
			return this;
		var bounds = this.getBounds();
		if (this.windowManager.maximize(this)) {
			this.disableButton('minimize').setResizable(false).setDraggable(
					false);

			this.activate();
			this.maximized = true;
			this.savedArea = bounds;
			var newBounds = Object.extend(this.windowManager.viewport
					.getDimensions(), {
				top : 0,
				left : 0
			});
			this[this.options.effects ? "morph" : "setBounds"](newBounds);
			this.fire('maximized');
			return this;
		}
	},

	restore : function() {
		if (!this.maximized)
			return this;

		if (this.windowManager.restore(this)) {
			this[this.options.effects ? "morph" : "setBounds"](this.savedArea);
			this.enableButton("minimize").setResizable(true).setDraggable(
					true);

			this.maximized = false;
			this.fire('restored');
			return this;
		}
	},

	toggleMaximize : function() {
		return this.maximized ? this.restore() : this.maximize();
	},

	adapt : function() {
		var dimensions = this.content.getScrollDimensions();
		this.setSize(dimensions.width, dimensions.height, true);	
		return this;
	},

	fold : function() {
		if (!this.folded) {
			var size = this.getSize(true);
			this.folded = true;
			this.savedInnerHeight = size.height;

			if (this.options.effects)
				this.morph({
					width : size.width,
					height : 0
				}, true);
			else
				this.setSize(size.width, 0, true);

			this.setResizable(false);
			this.fire("fold");
		}
		return this;
	},

	unfold : function() {
		if (this.folded) {
			var size = this.getSize(true);
			this.folded = false;

			if (this.options.effects)
				this.morph({
					width : size.width,
					height : this.savedInnerHeight
				}, true);
			else
				this.setSize(size.width, this.savedInnerHeight, true);

			this.setResizable(true);
			this.fire("unfold");
		}
		return this;
	},

	toggleFold : function() {
		return this.folded ? this.unfold() : this.fold();
	},

	setHeader : function(header) {
		this.header.update(header);
		return this;
	},

	setContent : function(content) {
		this.content.update(content);
		return this;
	},

	setFooter : function(footer) {
		this.footer.update(footer);
		return this;
	},
	
	getPosition : function() {
		return {
			left : this.options.left,
			top : this.options.top
		};
	},

	setPosition : function(top, left) {
		var pos = this.computePosition(top, left);
		this.options.top = pos.top;
		this.options.left = pos.left;

		var elementStyle = this.element.style;
		elementStyle.top = Math.max(pos.top, 0) + 'px';
		elementStyle.left = Math.max(pos.left, 0) + 'px';

		this.fire('position:changed');
		return this;
	},

	center : function(options) {
		var size = this.getSize(), windowManager = this.windowManager, viewport = windowManager.viewport;
		viewportArea = viewport.getDimensions(), offset = viewport
				.getScrollOffsets();

		if (options && options.auto) {
			this.centerOptions = Object.extend({
				handler : this.recenter.bind(this)
			}, options);
			Event.observe(this.windowManager.scrollContainer, "scroll",
					this.centerOptions.handler);
			Event.observe(window, "resize", this.centerOptions.handler);
		}

		options = Object.extend({
			top : Math.max((viewportArea.height - size.height) / 2, 5),
			left : Math.max((viewportArea.width - size.width) / 2, 5)
		}, options || {});

		return this.setPosition(options.top + offset.top, options.left
				+ offset.left);
	},

	getSize : function(innerSize) {
		if (innerSize)
			return {
				width : this.options.width - this.borderSize.width,
				height : this.options.height - this.borderSize.height
			};
		else
			return {
				width : this.options.width,
				height : this.options.height
			};
	},

	setSize : function(width, height, innerSize) {
		var size = this.computeSize(width, height, innerSize);
		var elementStyle = this.element.style, contentStyle = this.content.style;

		this.options.width = size.outerWidth;
		this.options.height = size.outerHeight;

		elementStyle.width = size.outerWidth + "px",
				elementStyle.height = size.outerHeight + "px";
		contentStyle.width = size.innerWidth + "px",
				contentStyle.height = size.innerHeight + "px";
		this.overlay.style.height = size.innerHeight + "px";

		this.fire('size:changed');
		return this;
	},

	getBounds : function(innerSize) {
		return Object.extend(this.getPosition(), this.getSize(innerSize));
	},

	setBounds : function(bounds, innerSize) {
		return this.setPosition(bounds.top, bounds.left).setSize(
				bounds.width, bounds.height, innerSize);
	},

	morph : function(bounds, innerSize) {
		bounds = Object.extend(this.getBounds(innerSize), bounds || {});

		if (this.centerOptions && this.centerOptions.auto)
			bounds = Object.extend(bounds, this.computeRecenter(bounds));

		if (innerSize) {
			bounds.width += this.borderSize.width;
			bounds.height += this.borderSize.height;
		}

		this.animating = true;

		new UI.Window.Effects.Morph(this, bounds, {
			duration : 0.2,
			afterFinish : function() {
				this.animating = false;
			}.bind(this)
		});

		Object.extend(this.options, bounds);

		return this;
	},

	getAltitude : function() {
		return this.windowManager.getAltitude(this);
	},

	setAltitude : function(altitude) {
		if (this.windowManager.setAltitude(this, altitude))
			this.fire('altitude:changed');
		return this;
	},

	setResizable : function(resizable) {
		this.options.resizable = resizable;

		var toggleClassName = (resizable ? 'add' : 'remove') + 'ClassName';

		this.element[toggleClassName]('resizable').select(
				'div[class*=_sizer]').invoke(resizable ? 'show' : 'hide');
		if (resizable)
			this.createResizeHandles();

		this.element.select('div.se').first()[toggleClassName]
				('se_resize_handle');

		return this;
	},

	setDraggable : function(draggable) {
		this.options.draggable = draggable;
		this.element[(draggable ? 'add' : 'remove') + 'ClassName']
				('draggable');
		return this;
	},

	getTheme : function() {
		return this.options.theme || this.windowManager.getTheme();
	},

	setTheme : function(theme, windowManagerTheme) {
		this.element.removeClassName(this.getTheme()).addClassName(theme);

		if (!windowManagerTheme)
			this.options.theme = theme;

		return this;
	}
});

UI.Window.optionsAccessor(
		$w(" minWidth minHeight maxWidth maxHeight gridX gridY altitude "));

// private
UI.Window.addMethods({
	style : 
		"position: absolute; top: 0; left: 0; width: 100%; height: 100%; border: 0;",

	action : function(name) {
		var action = this.options[name];
		if (action)
			Object.isString(action) ? this[action]() : action.call(this, this);
	},

	create : function() {
		function createDiv(className, options) {
			return new Element('div', Object.extend({
				className : className
			}, options));
		}

		// Main div
		this.element = createDiv("ui-window " + this.getTheme(), {
			id : this.options.id,
			style : "top:-10000px; left:-10000px"
		});
		this.element.window = this;

		// Create HTML window code
		this.header = createDiv('n move_handle').enableDrag();
		this.content = createDiv('content').appendText(' ');
		this.footer = createDiv('s move_handle').enableDrag();

		var header = createDiv('nw')
				.insert(createDiv('ne').insert(this.header));
		var content = createDiv('w').insert(createDiv('e', {
			style : "position:relative"
		}).insert(this.content));
		var footer = createDiv('sw').insert(
				createDiv(
						'se' + (this.options.resizable ? " se_resize_handle" : ""))
						.insert(this.footer));

		this.element.insert(header).insert(content).insert(footer).identify(
				'ui-window');
		this.header.observe('mousedown', this.activate.bind(this));

		this.setDraggable(this.options.draggable);
		this.setResizable(this.options.resizable);

		this.overlay = new Element('div', {
			style : this.style + "display: none"
		}).observe('mousedown', this.activate.bind(this));

		if (this.options.activeOnClick)
			this.content.insert({
				before : this.overlay
			});
	},

	createWiredElement : function() {
		this.wiredElement = this.wiredElement || new Element("div", {
			className : this.getTheme() + "_wired",
			style : "display: none; position: absolute; top: 0; left: 0"
		});
	},

	createResizeHandles : function() {
		$w(" n  w  e  s  nw  ne  sw  se ").each(function(id) {
			this.insert(new Element("div", {
				className : id + "_sizer resize_handle",
				drag_prefix : id
			}).enableDrag());
		}, this.element);
		this.createResizeHandles = Prototype.emptyFunction;
	},

	// First rendering, pre-compute window border size
	render : function() {
		this.addElements();

		this.computeBorderSize();
		this.updateButtonsOrder();
		this.element.hide().remove();

		// this.options contains top, left, width and height keys
		return this.setBounds(this.options);
	},

	// Adds window elements to the DOM
	addElements : function() {
		this.windowManager.container.appendChild(this.element);
	},

	// Set z-index to all window elements
	setZIndex : function(zIndex) {
		if (this.zIndex != zIndex) {
			this.zIndex = zIndex;
			[ this.element ].concat(this.element.childElements()).each(
					function(element) {
						element.style.zIndex = zIndex++;
					});
			this.lastZIndex = zIndex;
		}
		return this;
	},

	// re-compute window border size
	computeBorderSize : function() {
		if (this.element) {
			if (Browser.IEVersion >= 7)
				this.content.style.width = "100%";
			var dim = this.element.getDimensions(), pos = this.content
					.positionedOffset();
			this.borderSize = {
				top : pos[1],
				bottom : dim.height - pos[1] - this.content.getHeight(),
				left : pos[0],
				right : dim.width - pos[0] - this.content.getWidth()
			};
			this.borderSize.width = this.borderSize.left + this.borderSize.right;
			this.borderSize.height = this.borderSize.top + this.borderSize.bottom;
			if (Browser.IEVersion >= 7)
				this.content.style.width = "auto";
		}
	},

	computeSize : function(width, height, innerSize) {
		var innerWidth, innerHeight, outerWidth, outerHeight;
		if (innerSize) {
			outerWidth = width + this.borderSize.width;
			outerHeight = height + this.borderSize.height;
		} else {
			outerWidth = width;
			outerHeight = height;
		}
		// Check grid value
		if (!this.animating) {
			outerWidth = outerWidth.snap(this.options.gridX);
			outerHeight = outerHeight.snap(this.options.gridY);

			// Check min size
			if (!this.folded) {
				if (outerWidth < this.options.minWidth)
					outerWidth = this.options.minWidth;

				if (outerHeight < this.options.minHeight)
					outerHeight = this.options.minHeight;
			}

			// Check max size
			if (this.options.maxWidth && outerWidth > this.options.maxWidth)
				outerWidth = this.options.maxWidth;

			if (this.options.maxHeight && outerHeight > this.options.maxHeight)
				outerHeight = this.options.maxHeight;
		}

		if (this.centerOptions && this.centerOptions.auto)
			this.recenter();

		innerWidth = outerWidth - this.borderSize.width;
		innerHeight = outerHeight - this.borderSize.height;
		return {
			innerWidth : innerWidth,
			innerHeight : innerHeight,
			outerWidth : outerWidth,
			outerHeight : outerHeight
		};
	},

	computePosition : function(top, left) {
		if (this.centerOptions && this.centerOptions.auto)
			return this.computeRecenter(this.getSize());

		return {
			top : this.animating ? top : top.snap(this.options.gridY),
			left : this.animating ? left : left.snap(this.options.gridX)
		};
	},

	computeRecenter : function(size) {
		var viewport = this.windowManager.viewport, area = viewport
				.getDimensions(), offset = viewport.getScrollOffsets(), center = {
			top : Object.isUndefined(this.centerOptions.top) ? (area.height - size.height) / 2
					: this.centerOptions.top,
			left : Object.isUndefined(this.centerOptions.left) ? (area.width - size.width) / 2
					: this.centerOptions.left
		};

		return {
			top : parseInt(center.top + offset.top),
			left : parseInt(center.left + offset.left)
		};
	},

	recenter : function(event) {
		var pos = this.computeRecenter(this.getSize());
		this.setPosition(pos.top, pos.left);
	}
});

UI.URLWindow = Class.create(UI.Window, {
	options : {
		url : 'about:blank'
	},
	
	initialize : function($super, options) {
		$super(options);
		this.setUrl(this.options.url);
	},

	destroy : function($super) {
		this.iframe.src = null;
		$super();
	},

	getUrl : function() {
		return this.iframe.src;
	},

	setUrl : function(url, options) {
		this.iframe.src = url;
		return this;
	},

	create : function($super) {
		$super();

		this.iframe = new Element('iframe', {
			style : this.style,
			frameborder : 0,
			src : this.options.url,
			name : this.element.id + "_frame",
			id : this.element.id + "_frame"
		});

		this.content.insert(this.iframe);
	}
});

if (Browser.effects) {
	UI.Window.Effects = UI.Window.Effects || {};
	UI.Window.Effects.Morph = Class.create(Effect.Base, {
		initialize : function(window, bounds) {
			this.window = window;
			var options = Object.extend({
				fromBounds : this.window.getBounds(),
				toBounds : bounds,
				from : 0,
				to : 1
			}, arguments[2] || {});
			this.start(options);
		},

		update : function(position) {
			var t = this.options.fromBounds.top
					+ (this.options.toBounds.top - this.options.fromBounds.top)
					* position;
			var l = this.options.fromBounds.left
					+ (this.options.toBounds.left - this.options.fromBounds.left)
					* position;

			var ow = this.options.fromBounds.width
					+ (this.options.toBounds.width - this.options.fromBounds.width)
					* position;
			var oh = this.options.fromBounds.height
					+ (this.options.toBounds.height - this.options.fromBounds.height)
					* position;

			this.window.setBounds({
				top : t,
				left : l,
				width : ow,
				height : oh
			});
		}
	});
}

UI.Window.addMethods({
	startDrag : function(handle) {
		this.initBounds = this.getBounds();
		this.activate();

		if (this.options.wired) {
			this.createWiredElement();
			this.wiredElement.style.cssText = this.element.style.cssText;
			this.element.hide();
			this.saveElement = this.element;
			this.windowManager.container.appendChild(this.wiredElement);
			this.element = this.wiredElement;
		}

		handle.hasClassName('resize_handle') ? this.startResize(handle) : this
				.startMove();
	},

	endDrag : function() {
		this.element.hasClassName('resized') ? this.endResize() : this.endMove();

		if (this.options.wired) {
			this.saveElement.style.cssText = this.wiredElement.style.cssText;
			this.wiredElement.remove();
			this.element = this.saveElement;
		}
	},

	startMove : function() {
		// method used to drag
		this.drag = this.moveDrag;
		this.element.addClassName('moved');
		this.fire('move:started');
	},

	endMove : function() {
		this.element.removeClassName('moved');
		this.fire('move:ended');
	},

	startResize : function(handle) {
		this.drag = this[handle.readAttribute('drag_prefix') + 'Drag'];
		this.element.addClassName('resized');
		this.fire('resize:started');
	},

	endResize : function() {
		this.element.removeClassName('resized');
		this.fire('resize:ended');
	},

	moveDrag : function(dx, dy) {
		this.setPosition(this.initBounds.top + dy, this.initBounds.left + dx);
	},

	swDrag : function(dx, dy) {
		var initBounds = this.initBounds;
		this.setSize(initBounds.width - dx, initBounds.height + dy).setPosition(
				initBounds.top,
				initBounds.left + (initBounds.width - this.getSize().width));
	},

	seDrag : function(dx, dy) {
		this.setSize(this.initBounds.width + dx, this.initBounds.height + dy);
	},

	nwDrag : function(dx, dy) {
		var initBounds = this.initBounds;
		this.setSize(initBounds.width - dx, initBounds.height - dy).setPosition(
				initBounds.top + (initBounds.height - this.getSize().height),
				initBounds.left + (initBounds.width - this.getSize().width));
	},

	neDrag : function(dx, dy) {
		var initBounds = this.initBounds;
		this.setSize(initBounds.width + dx, initBounds.height - dy).setPosition(
				initBounds.top + (initBounds.height - this.getSize().height),
				initBounds.left);
	},

	wDrag : function(dx, dy) {
		var initBounds = this.initBounds;
		this.setSize(initBounds.width - dx, initBounds.height).setPosition(
				initBounds.top,
				initBounds.left + (initBounds.width - this.getSize().width));
	},

	eDrag : function(dx, dy) {
		this.setSize(this.initBounds.width + dx, this.initBounds.height);
	},

	nDrag : function(dx, dy) {
		var initBounds = this.initBounds;
		this.setSize(initBounds.width, initBounds.height - dy).setPosition(
				initBounds.top + (initBounds.height - this.getSize().height),
				initBounds.left);
	},

	sDrag : function(dx, dy) {
		this.setSize(this.initBounds.width, this.initBounds.height + dy);
	}
});

UI.Window.addMethods({
	methodsAdded : function(base) {
		base.aliasMethodChain('create', 'buttons');
		base.aliasMethodChain('destroy', 'buttons');
	},

	createWithButtons : function() {
		this.createWithoutButtons();

		if (!this.options.resizable) {
			this.options.minimize = false;
			this.options.maximize = false;
		}

		this.buttons = new Element("div", {
			className : "buttons"
		}).observe('click', this.onButtonsClick.bind(this)).observe('mouseover',
				this.onButtonsHover.bind(this)).observe('mouseout',
				this.onButtonsOut.bind(this));

		this.element.insert(this.buttons);

		this.defaultButtons.each(function(button) {
			if (this.options[button] !== false)
				this.addButton(button);
		}, this);
	},

	destroyWithButtons : function() {
		this.buttons.stopObserving();
		this.destroyWithoutButtons();
	},

	defaultButtons : $w(' minimize maximize close '),

	getButtonElement : function(buttonName) {
		return this.buttons.down("." + buttonName);
	},

	addButton : function(buttonName, action) {
		this.buttons.insert($UI.createLink(null, {
			className : buttonName
		}));

		if (action)
			this.options[buttonName] = action;

		return this;
	},

	removeButton : function(buttonName) {
		var ele = this.getButtonElement(buttonName);
		if (ele)
			ele.remove();
		return this;
	},

	disableButton : function(buttonName) {
		var ele = this.getButtonElement(buttonName);
		if (ele)
			ele.addClassName("disabled");
		return this;
	},

	enableButton : function(buttonName) {
		var ele = this.getButtonElement(buttonName);
		if (ele)
			ele.removeClassName("disabled");
		return this;
	},

	onButtonsClick : function(event) {
		var element = event.findElement('a:not(.disabled)');

		if (element)
			this.action(element.className);
		event.stop();
	},

	onButtonsHover : function(event) {
		this.buttons.addClassName("over");
	},

	onButtonsOut : function(event) {
		this.buttons.removeClassName("over");
	},

	updateButtonsOrder : function() {
		var buttons = this.buttons.childElements();

		buttons.inject(
				new Array(buttons.length),
				function(array, button) {
					array[parseInt(button.getStyle("padding-top"))] = button
							.setStyle("padding: 0");
					return array;
				}).each(function(button) {
			if (button) {
				this.buttons.appendChild(button);
			}
		}, this);
	}
});

UI.WindowManager = Class.create(UI.Options, {
	options : {
		container : null, // will default to document.body
		zIndex : 10,
		theme : "alphacube",
		
		positionningStrategy : function(win, area) {
			UI.WindowManager.DumbPositionningStrategy(win, area);
		}
	},

	initialize : function(options) {
		this.setOptions(options);

		this.container = $(this.options.container || document.body);

		if (this.container === $(document.body)) {
			this.viewport = document.viewport;
			this.scrollContainer = window;
		} else {
			this.viewport = this.scrollContainer = this.container;
		}

		this.container.observe('drag:started', this.onStartDrag.bind(this))
				.observe('drag:updated', this.onDrag.bind(this))
				.observe('drag:ended', this.onEndDrag.bind(this));

		this.stack = new UI.WindowManager.Stack();
		this.modalSessions = 0;

		this.createOverlays();
		this.resizeEvent = this.resize.bind(this);

		Event.observe(window, "resize", this.resizeEvent);
	},

	destroy : function() {
		this.windows().invoke('destroy');
		this.stack.destroy();
		Event.stopObserving(window, "resize", this.resizeEvent);
	},

	setTheme : function(theme) {
		this.stack.windows.select(function(w) {
			return !w.options.theme;
		}).invoke('setTheme', theme, true);
		this.options.theme = theme;
		return this;
	},

	register : function(win) {
		if (this.getWindow(win.id))
			return;

		this.handlePosition(win);
		this.stack.add(win);
		this.restartZIndexes();
	},

	unregister : function(win) {
		this.stack.remove(win);

		if (win == this.focusedWindow)
			this.focusedWindow = null;
	},

	getWindow : function(element) {
		element = $(element);

		if (!element)
			return;

		if (!element.hasClassName('ui-window'))
			element = element.up('.ui-window');

		var id = element.id;
		return this.stack.windows.find(function(win) {
			return win.id == id;
		});
	},

	windows : function() {
		return this.stack.windows.clone();
	},

	getFocusedWindow : function() {
		return this.focusedWindow;
	},

	startModalSession : function(win) {
		if (!this.modalSessions) {
			// this.removeOverflow();
			this.modalOverlay.className = win.getTheme() + "_overlay";
			this.container.appendChild(this.modalOverlay);

			if (!this.modalOverlay.opacity)
				this.modalOverlay.opacity = this.modalOverlay.getOpacity();
			this.modalOverlay.setStyle("height: " + 
					document.viewport.getScrollDimensions().height + "px");
			
			this.modalOverlay.show();
		}
		this.modalOverlay.setStyle({
			zIndex : win.zIndex - 1
		});
		this.modalSessions++;
	},

	endModalSession : function(win) {
		this.modalSessions--;
		if (this.modalSessions) {
			var w = this.stack.getPreviousWindow(win);
			while (w && !w.modalSession) {
				w = this.stack.getPreviousWindow(w);
			}
			if (w) {
				this.modalOverlay.setStyle({
					zIndex : w.zIndex - 1
				});
			}
		} else {
			// this.resetOverflow();
			this.modalOverlay.hide();
		}
	},

	moveHandleSelector : '.ui-window.draggable .move_handle',
	resizeHandleSelector : '.ui-window.resizable .resize_handle',

	onStartDrag : function(event) {
		var handle = event.element(), isMoveHandle = handle
				.match(this.moveHandleSelector), isResizeHandle = handle
				.match(this.resizeHandleSelector);

		// ensure dragged element is a window handle !
		if (isResizeHandle || isMoveHandle) {
			event.stop();

			// find the corresponding window
			var win = this.getWindow(event.findElement('.ui-window'));

			// render drag overlay
			this.container.insert(this.dragOverlay.setStyle({
				zIndex : this.getLastZIndex()
			}));

			win.startDrag(handle);
			this.draggedWindow = win;
		}
	},

	onDrag : function(event) {
		if (this.draggedWindow) {
			event.stop();
			this.draggedWindow.drag(event.memo.dx, event.memo.dy);
		}
	},

	onEndDrag : function(event) {
		if (this.draggedWindow) {
			event.stop();
			this.dragOverlay.remove();
			this.draggedWindow.endDrag();
			this.draggedWindow = null;
		}
	},

	maximize : function(win) {
		this.removeOverflow();
		this.maximizedWindow = win;
		return true;
	},

	restore : function(win) {
		if (this.maximizedWindow) {
			this.resetOverflow();
			this.maximizedWindow = false;
		}
		return true;
	},

	removeOverflow : function() {
		var container = this.container;

		container.savedOverflow = container.style.overflow || "auto";
		container.savedOffset = this.viewport.getScrollOffsets();
		container.style.overflow = "hidden";

		this.viewport.setScrollOffsets({
			top : 0,
			left : 0
		});

		if (this.container == document.body)
			this.cssRule = $UI.addCSS("html { overflow: hidden; }");
	},

	resetOverflow : function() {
		var container = this.container;

		if (container.savedOverflow) {
			if (this.container == document.body)
				this.cssRule.remove();

			container.style.overflow = container.savedOverflow;
			this.viewport.setScrollOffsets(container.savedOffset);

			container.savedOffset = container.savedOverflow = null;
		}
	},

	hide : function(win) {
		var previous = this.stack.getPreviousWindow(win) || this.stack.getFrontWindow();
		if (previous)
			previous.focus();
	},

	restartZIndexes : function() {
		var zIndex = this.getZIndex() + 1;
		this.stack.windows.each(function(w) {
			w.setZIndex(zIndex);
			zIndex = w.lastZIndex + 1;
		});
	},

	getLastZIndex : function() {
		return this.stack.getFrontWindow().lastZIndex + 1;
	},

	overlayStyle : "position: absolute; top: 0; left: 0; display: none; width: 100%;",

	createOverlays : function() {
		this.modalOverlay = new Element("div", {
			style : this.overlayStyle
		});
		this.dragOverlay = new Element("div", {
			style : this.overlayStyle + "height: 100%"
		});
	},

	focus : function(win) {
		if (this.focusedWindow)
			this.focusedWindow.blur();
		this.focusedWindow = win;
	},

	blur : function(win) {
		if (win == this.focusedWindow)
			this.focusedWindow = null;
	},

	setAltitude : function(win, altitude) {
		var stack = this.stack;

		if (altitude === "front") {
			if (stack.getFrontWindow() === win)
				return;
			stack.bringToFront(win);
		} else if (altitude === "back") {
			if (stack.getBackWindow() === win)
				return;
			stack.sendToBack(win);
		} else {
			if (stack.getPosition(win) == altitude)
				return;
			stack.setPosition(win, altitude);
		}

		this.restartZIndexes();
		return true;
	},

	getAltitude : function(win) {
		return this.stack.getPosition(win);
	},

	resize : function(event) {
		var area = this.viewport.getDimensions();

		if (this.maximizedWindow)
			this.maximizedWindow.setSize(area.width, area.height);

		if (this.modalOverlay.visible())
			this.modalOverlay.setStyle("height:" + area.height + "px");
	},

	handlePosition : function(win) {
		if (Object.isNumber(win.options.top)
				&& Object.isNumber(win.options.left))
			return;

		var strategy = this.options.positionningStrategy, area = this.viewport
				.getDimensions();

		Object.isFunction(strategy) ? strategy(win, area) : strategy
				.position(win, area);
	}
});

UI.WindowManager.DumbPositionningStrategy = function(win, area) {
	size = win.getSize();

	var top = area.height - size.height, left = area.width - size.width;

	top = top < 0 ? 0 : Math.random() * top;
	left = left < 0 ? 0 : Math.random() * left;

	win.setPosition(top, left);
};

UI.WindowManager.optionsAccessor('zIndex', 'theme');

UI.WindowManager.Stack = Class.create(Enumerable, {
	initialize : function() {
		this.windows = [];
	},

	each : function(iterator) {
		this.windows.each(iterator);
	},

	add : function(win, position) {
		this.windows.splice(position || this.windows.length, 0, win);
	},

	remove : function(win) {
		this.windows = this.windows.without(win);
	},

	sendToBack : function(win) {
		this.remove(win);
		this.windows.unshift(win);
	},

	bringToFront : function(win) {
		this.remove(win);
		this.windows.push(win);
	},

	getPosition : function(win) {
		return this.windows.indexOf(win);
	},

	setPosition : function(win, position) {
		this.remove(win);
		this.windows.splice(position, 0, win);
	},

	getFrontWindow : function() {
		return this.windows.last();
	},

	getBackWindow : function() {
		return this.windows.first();
	},

	getPreviousWindow : function(win) {
		return (win == this.windows.first()) ? null : this.windows[this.windows
				.indexOf(win) - 1];
	}
});

(function() {
	window.$win = function(ele) {
		ele = $(ele);
		if (ele) {
			if (!UI.defaultWM) {
				UI.defaultWM = new UI.WindowManager();
			}
			return UI.defaultWM.getWindow(ele.up(".ui-window"));
		}
	};
	
	window.$win.closeAll = function(pop) {
	  var wm = UI.defaultWM;
		if (wm && (!pop || !wm.modalOverlay)) {
		  wm.windows().each(function(win) {
		    win.close();
			});
		}
	};
})();

$ready(function() {
	if (!UI.defaultWM) {
		UI.defaultWM = new UI.WindowManager();
	}
});

function __window_actions_init(actionFunc, name, single) {
	var _visible = function() {
    var act;
    if (single && (act = $Actions[name]) && act.isVisible())
      return act;
  };
  
  var act = _visible();
  if (act)
    act.close();
  
  actionFunc.isVisible = function() {
    return actionFunc.window && actionFunc.window.visible;
  };
  
	actionFunc.close = function() {
		if (!actionFunc.window) {
			var fw = UI.defaultWM.getFocusedWindow();
			if (fw)
				fw.close();
		} else {
			actionFunc.window.close();
		}
	};

	actionFunc.observe = function(eventName, handle) {
		if (actionFunc.window) {
			actionFunc.window.observe(eventName, handle);
		}
	};

	actionFunc.setOptions = function(options) {
		Object.extend(actionFunc.options, options || {});
	};

	var _contentRef;

	actionFunc.refreshContentRef = function(params, destroyOnClose) {
		if (!_contentRef)
			return;
		var ar = Object.isString(_contentRef) ? $Actions[_contentRef] : _contentRef;
		if (ar) {
		  // 缓存
		  if (ar.container && !destroyOnClose) 
		    return;
		  
		  var _content = actionFunc.window.content;
		  if (_content.loading) 
		    return;
		  
			if (!ar.selector) 
				ar.selector = actionFunc.selector;
			ar.container = _content;
			
			_content.loading = true;
			ar.jsCompleteCallback = function() {
				_content.loading = false;
			};
			ar(params);
		}
	};

	actionFunc.createWindow = function(url, contentHtml, contentRef) {		
		if (_visible())
		  return;
		
		if (actionFunc.loading)
      return;
    actionFunc.loading = true;
		
		var win = actionFunc.window = url ? new UI.URLWindow(actionFunc.options)
				: new UI.Window(actionFunc.options);
		win.observe("shown", function(e) {
		  actionFunc.loading = undefined;
    });
		
		if (url) {
			win.setUrl(url);
		} else {
			if (contentHtml) {
			  win.setContent(contentHtml);
			}
			_contentRef = actionFunc.contentRef || contentRef;
		}
	};

	actionFunc.showWindow = function(title, popup, center, modal, destroyOnClose, params) {
		var ev;
		if (ev = document.getEvent()) 
			actionFunc.trigger = Event.element(ev);
		
		var win = actionFunc.window;
		
		var t = actionFunc.title || title;
		if (t) 
		  win.setHeader(t);
		
		actionFunc.refreshContentRef(params, destroyOnClose);
		
		if (popup) {
		  var opt = actionFunc.options;
			var p = $UI.getPopupOffsets({
				width : opt.width,
				height : opt.height
			}, actionFunc.trigger);
			win.setPosition(p[1] + opt.ydelta, p[0] + opt.xdelta).show().activate();
		} else {
			if (center) 
				win.center();
			else 
			  win.setPosition(opt.top + opt.ydelta, opt.left + opt.xdelta);
			win.show(modal).activate();
		}
	};
}
