/**
 * @author 陈侃(cknet@126.com, 13910090885)
 */
$UI.Autocomplete = Class.create({

  initialize : function(inputField, options) {
    this.inputField = $(inputField);
    this.options = {};
    Object.extend(this.options, options || {});

    this.inputField.observe('keyup', this._keyUp.bindAsEventListener(this));
    this.inputField.observe('blur', this._blur.bindAsEventListener(this));
  },

  _blur : function(ev) {
    if ((r = this.results) && r.visible()) {
      r.hide();
    }
  },

  _keyUp : function(ev) {
    var r;
    if (!this.results) {
      r = new Element("div", {
        className : "Autocomplete",
        style : "display: none"
      });
      document.body.appendChild(this.results = r);
    } else {
      r = this.results;
    }

    var txt = $F(this.inputField).trim();
    if (txt.length > 0 && !r.visible()) {
      var p = $UI.getPopupOffsets(r, this.inputField);
      r.style.left = p[0] + "px";
      r.style.top = p[1] + "px";
      r.show();
    }

    if (txt.length == 0) {
      r.hide();
    }

    var act = $Actions[this.options.ajax];
    act.jsCompleteCallback = function(req, responseText, json) {
    };
    act();
  }
});