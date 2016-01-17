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

        var act = $Actions[opts.ajax];
        if (!act.jsCompleteCallback) {
          act.jsCompleteCallback = (function(req, responseText, json) {
            var data = json.data;
            if (!data || data.length == 0) {
              if (r.visible())
                r.hide();
              return;
            }
            var v = json.val;
            var d = "<ul>";
            data.each(function(li) {
              var _data = li.data;
              var txt = li.txt;
              var txt2 = li.txt2;
              d += "<li _data=\"" + _data.replace("\"", "&quot;") + "\">";
              if (v) {
                txt = txt.replace(v, "<span class='match'>" + v + "</span>");
              }
              d += txt;
              if (txt2) {
                d += "<div class='txt2'>" + txt2 + "</div>"
              }
              d += "</li>";
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
        }

        (function() {
          act(('val=' + txt).addParameter(opts.params));
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
          this._select(ev);
        } else if (code == Event.KEY_BACKSPACE) {
          this._backspace(ev);
        }
      },

      _backspace : function(ev) {
        var sep = this.options.sepChar;
        if (!sep || sep == '') {
          return;
        }
        
        var input = this.inputField;
        var txt = $F(input);        
        if (typeof input.selectionStart == 'number') {
          var p = input.selectionStart;
          if (input.selectionEnd > p)
            return;
          var slen = sep.length;
          var s = p - slen;
          if (s >= 0 && txt.substring(s, p) == sep) {
            var p2 = txt.lastIndexOf(sep, s - 1);
            if (p2 > 0) {
              p2 += slen;
            }
            input.setSelectionRange((p2 > 0 ? p2  : 0), p); 
            Event.stop(ev);
          }
        } else {
          // ie8
        }
      },
      
      _val : function(txt) {
        var input = this.inputField;
        var act = $Actions[this.options.ajax];
        if (act.jsVal) {
          act.jsVal(input, txt);
        } else {
          input.value = txt;
        }
      },

      _select : function(ev) {
        if (this.currentItem && (r = this.results)) {
          var input = this.inputField;
          var sep = this.options.sepChar;
          var val = this.currentItem.getAttribute("_data");
          if (!sep || sep == '') {
            input.value = val;
          } else {
            var txt = $F(input);
            if (txt.endsWith(sep)) {
              this._val(txt + val);
            } else {
              var p = txt.lastIndexOf(sep);
              if (p > 0) {
                this._val(txt.substring(0, p + sep.length) + val);
              } else {
                this._val(val);
              }
            }
          }
          r.hide();
          if (this._mouse)
            (function() {
              input.focus();
            }).delay(0.1);
        }
        Event.stop(ev);
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
