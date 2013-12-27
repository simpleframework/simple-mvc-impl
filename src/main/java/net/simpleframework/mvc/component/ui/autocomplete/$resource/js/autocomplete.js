/**
 * @author 陈侃(cknet@126.com, 13910090885)
 */
$UI.Autocomplete = Class
    .create({
      initialize : function(inputField, options) {
        this.inputField = $(inputField);
        this.options = {
          sepChar : ' '
        };
        Object.extend(this.options, options || {});

        this.inputField.observe('keydown', this._keyDown
            .bindAsEventListener(this));
        this.inputField.observe('keyup', this._keyUp.bindAsEventListener(this));
        this.inputField.observe('blur', this._blur.bindAsEventListener(this));
      },

      _blur : function(ev) {
        if ((r = this.results) && r.visible())
          r.hide();
      },

      _mouseOver : function(ev) {
        if (this._mouse)
          this._setSelected(Event.findElement(ev, 'li'));
      },

      _mouseMove : function(ev) {
        this._mouse = true;
      },

      _keyUp : function(ev) {
        var code = (ev.which) ? ev.which : ev.keyCode;
        if (code == Event.KEY_RETURN)
          return;

        var input = this.inputField;
        var txt = $F(input);
        if (txt == input._txt)
          return;

        input._txt = txt;

        var opts = this.options;
        var r;
        if (!this.results) {
          var _style = "display: none; width: " + opts.width + "px; height: "
              + opts.height + "px";
          r = new Element("div", {
            className : "Autocomplete",
            style : _style
          });
          r.observe('mouseover', this._mouseOver.bindAsEventListener(this));
          r.observe('mousemove', this._mouseMove.bindAsEventListener(this));
          r.observe('mousedown', this._select.bindAsEventListener(this));
          document.body.appendChild(this.results = r);
        } else {
          r = this.results;
          if (txt.length == 0) {
            r.hide();
            return;
          }
        }

        var callback = (function(req, responseText, json) {
          if (!json.data)
            return;

          var d = "<ul>";
          json.data.each(function(li) {
            d += "<li>" + li + "</li>";
          });
          d += "</ul>";
          r.innerHTML = d;

          if (!r.visible()) {
            var p = $UI.getPopupOffsets(r, input);
            r.style.left = p[0] + "px";
            r.style.top = p[1] + "px";
            r.show();
          }
          this._markSelected(false);
        }).bind(this);

        (function() {
          var act = $Actions[opts.ajax];
          act.jsCompleteCallback = callback;
          act('val=' + txt);
        }).delay(0.1);
      },

      _keyDown : function(ev) {
        var code = (ev.which) ? ev.which : ev.keyCode;
        if (code == Event.KEY_UP) {
          this._markSelected(true);
          Event.stop(ev);
        } else if (code == Event.KEY_DOWN) {
          this._markSelected(false);
          Event.stop(ev);
        } else if (code == Event.KEY_RETURN) {
          this._select();
          Event.stop(ev);
        }
      },

      _select : function(ev) {
        if (this.currentItem && (r = this.results)) {
          var input = this.inputField;
          var sep = this.options.sepChar;
          if (!sep || sep == '') {
            input.value = this.currentItem.innerHTML;
          } else {
            var txt = $F(input);
            if (txt.endsWith(sep)) {
              input.value = txt + this.currentItem.innerHTML;
            } else {
              var p = txt.lastIndexOf(sep);
              if (p > 0) {
                input.value = txt.substring(0, p + 1) + this.currentItem.innerHTML;
              } else {
                input.value = this.currentItem.innerHTML;
              }
            }
          }     
          r.hide();
          if (this._mouse)
            (function() {
              input.focus();
            }).delay(0.1);
        }
      },

      _setSelected : function(item) {
        if (this.currentItem)
          this.currentItem.removeClassName("selected");
        if (item)
          item.addClassName("selected");
        this.currentItem = item;
      },

      _markSelected : function(up) {
        if (!(r = this.results))
          return;

        var c = r.down();
        if (!c._arr)
          c._arr = c.select("li");
        if ((l = c._arr.length) == 0)
          return;

        var item;
        var s = r.down(".selected");
        if (s)
          item = up ? s.previous() : s.next();
        if (!item)
          item = c._arr[up ? l - 1 : 0];
        this._setSelected(item);

        this._mouse = false;
        item.scrollIntoView(false);
      }
    });