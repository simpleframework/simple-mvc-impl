<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ui.syntaxhighlighter.SyntaxHighlighterUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.common.StringUtils"%>
<%@ page import="net.simpleframework.common.I18n"%>
<%@ page import="net.simpleframework.mvc.common.element.Checkbox"%>
<%
	final ComponentParameter nCP = SyntaxHighlighterUtils.get(request,
			response);
%>
<div class="sh_window">
  <div class="tb">
    <div style="float: right;">
      <div>
        <%=new Checkbox("sh_window_opt1", "#(sh_window.0)")
					.setChecked(true)%>
      </div>
      <div>
        <%=new Checkbox("sh_window_opt2", "#(sh_window.4)")%>
      </div>
    </div>
    <div style="float: left;">
      <label>#(sh_window.1)</label> <select id="sh_window_lang">
        <%
        	for (String lang : new String[] { "Java", "Javascript", "CSS",
        			"HTML", "XML", "SQL", "Groovy", "C++", "C", "C#", "PHP",
        			"Python", "Ruby" }) {
        		out.write("<option value='");
        		out.write(lang.toLowerCase());
        		out.write("'>");
        		out.write(lang);
        		out.write("</option>");
        	}
        %>
      </select>
    </div>
    <div class="clearfix"></div>
  </div>
  <div class="ta">
    <textarea rows="13" id="sh_window_textarea"></textarea>
  </div>
  <div class="btm">
    <input type="button" value="#(sh_window.2)" onclick="sh_comp.insert();" /> <input type="button"
      value="#(Button.Cancel)" onclick="sh_comp.close();" />
  </div>
</div>
<%
	String jsSelectedCallback = (String) nCP
			.getBeanProperty("jsSelectedCallback");
%>
<script type="text/javascript">
  var _sh_window = $Actions['window_<%=nCP.hashId()%>'];
  
  var sh_comp = {
    insert : function() {
      var script = ('<pre class=\"brush: ' + $F('sh_window_lang').toLowerCase()
          + '; gutter: ' + $('sh_window_opt1').checked + '; html-script: '
          + $('sh_window_opt2').checked + ';\">'
          + $F('sh_window_textarea').escapeHTML() + '</pre>').makeElement();
          
      var act = $Actions['<%=nCP.getComponentName()%>'];    
      if (act.jsSelectedCallback) {
        if (act.jsSelectedCallback(script))
          this.close();
      } else {
        <%if (StringUtils.hasText(jsSelectedCallback)) {%>
          if ((function(script) {
            <%=jsSelectedCallback%>
          })(script))
            this.close();
        <%} else {%>
          alert('#(sh_window.3)');
        <%}%>
      }
    },
    
    close : function() {
      _sh_window.close();
    }
  };
  
  (function() {
    var ta = $("sh_window_textarea");
    $UI.fixTextareaTab(ta);
    
    if (_sh_window._sh_data) {
      var d = _sh_window._sh_data;
      $Actions.setValue(ta, d.ta);
      $Actions.setValue("sh_window_lang", d.brush);
      $Actions.setValue("sh_window_opt1", d.gutter);
      $Actions.setValue("sh_window_opt2", d["html-script"]);
    }
  })();
</script>