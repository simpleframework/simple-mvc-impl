/**
 * @author 陈侃(cknet@126.com, 13910090885)
 */
$UI.Autocomplete = Class.create({

  initialize : function(inputField, options) {
    this.inputField = $(inputField);
    this.options = {};
    Object.extend(this.options, options || {});

    this.inputField.observe('keypress', this._keyPress
        .bindAsEventListener(this));
    this.inputField.observe('keyup', this._keyUp.bindAsEventListener(this));
    this.inputField.observe('blur', this._blur.bindAsEventListener(this));
  },

  _blur : function(ev) {
    if ((r = this.results) && r.visible()) {
      r.hide();
    }
  },

  _keyUp : function(ev) {
    var code = (ev.which) ? ev.which : ev.keyCode;
    if (code == Event.KEY_LEFT || code == Event.KEY_RIGHT
        || code == Event.KEY_UP || code == Event.KEY_DOWN) {
      return;
    }

    var input = this.inputField;
    var txt = $F(input).trim();

    var opts = this.options;

    var r;
    if (!this.results) {
      r = new Element("div", {
        className : "Autocomplete",
        style : "display: none; width: " + opts.width + "px; height: "
            + opts.height + "px"
      });
      document.body.appendChild(this.results = r);
    } else {
      r = this.results;
      if (txt.length == 0) {
        r.hide();
        return;
      }
    }

    var act = $Actions[opts.ajax];
    act.jsCompleteCallback = function(req, responseText, json) {
      if (!json.data) {
        return;
      }

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
    };
    act('val=' + txt);
  },

  _keyPress : function(ev) {
    var code = (ev.which) ? ev.which : ev.keyCode;
    if (code == Event.KEY_UP) {
      this.markSelected(true);
    } else if (code == Event.KEY_DOWN) {
      this.markSelected(false);
    }
  },

  markSelected : function(up) {
    if (!(r = this.results)) {
      return;
    }
    var arr = r.select("li");
    if ((l = arr.length) > 0) {
      var s = r.down(".selected");
      if (!s) {
        var item = arr[up ? l - 1 : 0];
        item.addClassName("selected");
        item.scrollIntoView(up);
      } else {
        var item = up ? s.previous() : s.next();
        if (!item) {
          item = arr[up ? l - 1 : 0];
        }
        item.addClassName("selected");
        s.removeClassName("selected");
        item.scrollIntoView(up);
      }
    }
  }
});