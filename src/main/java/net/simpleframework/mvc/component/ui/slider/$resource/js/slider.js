var EVENT_MOUSE_DOWN = 'mousedown touchstart pointerdown MSPointerDown';
var EVENT_MOUSE_MOVE = 'mousemove touchmove pointermove MSPointerMove';
var EVENT_MOUSE_UP = 'mouseup touchend touchcancel pointerup pointercancel'
    + ' MSPointerUp MSPointerCancel';

$UI.Slider = Class.create({
  sliders : [],

  _bar : null,
  _arrow : null,

  initialize : function(id, settings) {
    this.id = id;
    this.settings = {
      xMinValue : 0,
      xMaxValue : 100,
      yMinValue : 0,
      yMaxValue : 100,
      arrowImage : ''
    };
    Object.extend(this.settings, settings || {});

    this.xValue = 0;
    this.yValue = 0;

    this._bar = $(this.id);

    this._bar.addClassName("slider_bar");
    this._arrow = new Element('img', {
      src : this.settings.arrowImage,
      className : "slider_img"
    });
    this._bar.insert({
      after : this._arrow
    });

    this._arrow.observeImageLoad(function() {
      this.setArrowPositionFromValues();
    }.bind(this));

    Event.observe(window, "resize", function() {
      this.setArrowPositionFromValues();
    }.bind(this));

    this.setArrowPositionFromValues();

    var slider = this;

    // this.setPositioningVariables();

    this._event_docMouseMove = this._docMouseMove.bindAsEventListener(this);
    this._event_docMouseUp = this._docMouseUp.bindAsEventListener(this);

    this.addListener(this._bar, EVENT_MOUSE_DOWN, this._bar_mouseDown
        .bindAsEventListener(this));
    this.addListener(this._arrow, EVENT_MOUSE_DOWN, this._arrow_mouseDown
        .bindAsEventListener(this));

    if (this.onValuesChanged)
      this.onValuesChanged(this);

    this.sliders.push(this);
  },

  addListener : function(element, type, _handler) {
    var types = type.split(' ');

    types.each(function(t) {
      Event.observe(element, t, _handler);
    });
  },

  removeListener : function(element, type, _handler) {
    var types = type.split(' ');

    types.each(function(t) {
      Event.stopObserving(element, t, _handler);
    });
  },

  _bar_mouseDown : function(e) {
    this._mouseDown(e);
  },

  _arrow_mouseDown : function(e) {
    this._mouseDown(e);
  },

  _mouseDown : function(e) {
    $UI.ActiveSlider = this;

    this.setValuesFromMousePosition(e);

    this.addListener(document, EVENT_MOUSE_MOVE, this._event_docMouseMove);
    this.addListener(document, EVENT_MOUSE_UP, this._event_docMouseUp);

    Event.stop(e);
  },

  _docMouseMove : function(e) {
    this.setValuesFromMousePosition(e);

    Event.stop(e);
  },

  _docMouseUp : function(e) {
    this.removeListener(document, EVENT_MOUSE_UP, this._event_docMouseUp);
    this.removeListener(document, EVENT_MOUSE_MOVE, this._event_docMouseMove);
    Event.stop(e);
  },
  
  setPositioningVariables : function() {
    this._barWidth = this._bar.getWidth();
    this._barHeight = this._bar.getHeight();

    var pos = this._bar.positionedOffset();
    this._barTop = pos.top;
    this._barLeft = pos.left;

    this._barBottom = this._barTop + this._barHeight;
    this._barRight = this._barLeft + this._barWidth;

    this._arrow = $(this._arrow);
    this._arrowWidth = this._arrow.getWidth();
    this._arrowHeight = this._arrow.getHeight();

    this.MinX = this._barLeft;
    this.MinY = this._barTop;

    this.MaxX = this._barRight;
    this.MinY = this._barBottom;
  },

  setArrowPositionFromValues : function(e) {
    this.setPositioningVariables();

    var arrowOffsetX = 0;
    var arrowOffsetY = 0;

    if (this.settings.xMinValue != this.settings.xMaxValue) {
      if (this.xValue == this.settings.xMinValue) {
        arrowOffsetX = 0;
      } else if (this.xValue == this.settings.xMaxValue) {
        arrowOffsetX = this._barWidth - 1;
      } else {
        var xMax = this.settings.xMaxValue;
        if (this.settings.xMinValue < 1) {
          xMax = xMax + Math.abs(this.settings.xMinValue) + 1;
        }
        var xValue = this.xValue;

        if (this.xValue < 1)
          xValue = xValue + 1;

        arrowOffsetX = xValue / xMax * this._barWidth;

        if (parseInt(arrowOffsetX) == (xMax - 1))
          arrowOffsetX = xMax;
        else
          arrowOffsetX = parseInt(arrowOffsetX);

        if (this.settings.xMinValue < 1) {
          arrowOffsetX = arrowOffsetX - Math.abs(this.settings.xMinValue) - 1;
        }
      }
    }

    if (this.settings.yMinValue != this.settings.yMaxValue) {
      if (this.yValue == this.settings.yMinValue) {
        arrowOffsetY = 0;
      } else if (this.yValue == this.settings.yMaxValue) {
        arrowOffsetY = this._barHeight - 1;
      } else {
        var yMax = this.settings.yMaxValue;
        if (this.settings.yMinValue < 1) {
          yMax = yMax + Math.abs(this.settings.yMinValue) + 1;
        }
        var yValue = this.yValue;
        if (this.yValue < 1)
          yValue = yValue + 1;
        var arrowOffsetY = yValue / yMax * this._barHeight;
        if (parseInt(arrowOffsetY) == (yMax - 1))
          arrowOffsetY = yMax;
        else
          arrowOffsetY = parseInt(arrowOffsetY);
        if (this.settings.yMinValue < 1) {
          arrowOffsetY = arrowOffsetY - Math.abs(this.settings.yMinValue) - 1;
        }
      }
    }

    this._setArrowPosition(arrowOffsetX, arrowOffsetY);
  },

  _setArrowPosition : function(offsetX, offsetY) {
    if (offsetX < 0)
      offsetX = 0;
    if (offsetX > this._barWidth)
      offsetX = this._barWidth;
    if (offsetY < 0)
      offsetY = 0;
    if (offsetY > this._barHeight)
      offsetY = this._barHeight;

    var posX = this._barLeft + offsetX;
    var posY = this._barTop + offsetY;

    if (this._arrowWidth > this._barWidth) {
      posX = posX - (this._arrowWidth / 2 - this._barWidth / 2);
    } else {
      posX = posX - parseInt(this._arrowWidth / 2);
    }
    if (this._arrowHeight > this._barHeight) {
      posY = posY - (this._arrowHeight / 2 - this._barHeight / 2);
    } else {
      posY = posY - parseInt(this._arrowHeight / 2);
    }
    this._arrow.style.left = (posX + this._bar.measure("margin-left")) + 'px';
    this._arrow.style.top = (posY + this._bar.measure("margin-top")) + 'px';
  },
  
  setValuesFromMousePosition : function(e) {
    var x, y;
    if (e.touches) {
      var t = e.touches[0];
      x = t.pageX;
      y = t.pageY;
    } else {
      var mouse = Event.pointer(e);
      x = mouse.x;
      y = mouse.y;
    }

    var pos = this._bar.cumulativeOffset();
    var pos2 = this._bar.positionedOffset();
    var mx = x - pos.left + pos2.left;
    var my = y - pos.top + pos2.top;

    var relativeX = 0;
    var relativeY = 0;

    if (mx < this._barLeft)
      relativeX = 0;
    else if (mx > this._barRight)
      relativeX = this._barWidth;
    else
      relativeX = mx - this._barLeft + 1;

    if (my < this._barTop)
      relativeY = 0;
    else if (my > this._barBottom)
      relativeY = this._barHeight;
    else
      relativeY = my - this._barTop + 1;

    var newXValue = parseInt(relativeX / this._barWidth * this.settings.xMaxValue);
    var newYValue = parseInt(relativeY / this._barHeight * this.settings.yMaxValue);

    this.xValue = newXValue;
    this.yValue = newYValue;

    if (this.settings.xMaxValue == this.settings.xMinValue)
      relativeX = 0;
    if (this.settings.yMaxValue == this.settings.yMinValue)
      relativeY = 0;

    this._setArrowPosition(relativeX, relativeY);

    if (this.onValuesChanged)
      this.onValuesChanged(this);
  }
});
