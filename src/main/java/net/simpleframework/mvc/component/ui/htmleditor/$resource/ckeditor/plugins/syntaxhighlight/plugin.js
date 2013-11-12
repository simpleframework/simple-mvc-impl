CKEDITOR.plugins.add('syntaxhighlight', {

  lang : [ 'zh-cn', 'en' ],

  init : function(editor) {
    editor.addCommand('Code', {

      exec : function(editor) {
        var act = $Actions[editor.syntaxhighlighter];

        var ele = function() {
          var selection = editor.getSelection();
          var element = selection.getStartElement();
          if (element) {
            element = element.getAscendant('pre', true);
          }
          return element;
        };

        act.jsSelectedCallback = function(script) {
          var pre = new CKEDITOR.dom.element(script);
          var element = ele();
          if (!element || element.getName() !== 'pre') { // insert
            editor.insertElement(pre);
          } else {
            pre.replace(element);
          }
          return true;
        };

        var element = ele();
        if (element) {
          var attri = {};
          (element.getAttribute("class") || "").split(";").each(function(s) {
            var arr = s.split(':');
            if (arr.length == 2) {
              var v = arr[1].trim();
              if ("false" == v)
                v = false;
              else if ("true" == v)
                v = true;
              attri[arr[0].trim()] = v;
            }
          });
          attri.ta = element.getHtml();
          act.showEditor(attri);
        } else {
          act.showEditor();
        }
      }
    });

    editor.ui.addButton('Code', {
      label : editor.lang.syntaxhighlight.addCode,
      command : 'Code',
      icon : this.path + 'icons/code.png',
    });
  }
});