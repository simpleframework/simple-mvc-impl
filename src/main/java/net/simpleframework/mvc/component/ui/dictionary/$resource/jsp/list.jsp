<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ui.dictionary.DictionaryBean"%>
<%@ page import="net.simpleframework.mvc.component.ui.dictionary.DictionaryBean.DictionaryListBean"%>
<%@ page import="net.simpleframework.mvc.component.ui.dictionary.DictionaryUtils"%>
<%@ page import="net.simpleframework.mvc.component.ui.dictionary.DictionaryRender"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.common.StringUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentRenderUtils"%>
<%
	final ComponentParameter nCP = DictionaryUtils.get(request,
			response);
	final DictionaryBean dictionaryBean = (DictionaryBean) nCP.componentBean;
	final String beanId = dictionaryBean.hashId();
	final String name = (String) nCP.getComponentName();
	final DictionaryListBean list = (DictionaryListBean) dictionaryBean
			.getDictionaryTypeBean();
%>
<div class="dictionary" style="padding-left: 0;">
  <%=ComponentRenderUtils.genParameters(nCP)%>
  <div id="list<%=beanId%>"></div>
  <jsp:include page="okcancel_inc.jsp" flush="true"></jsp:include>
</div>
<script type="text/javascript">
  var selected<%=beanId%> = function(item, json, ev) {
    var selects = [];
    var create = function(b) {
      return Object.extend(
          {'id':b.getId(), 'text':b.getText(), 'item':b}, 
          b.data.attributes || {}
      );
    };
    
    var listbox = $Actions['<%=list.getRef()%>'].listbox;
    if (item) {
      if (item.checkbox) item.check(true);
     	else selects.push(create(item));   
    } 
    
    if (listbox.options.checkbox) {
    	listbox.getCheckedItems().each(function(b) { selects.push(create(b)); });
    } else if (!item) {
    	listbox.getSelectedItems().each(function(b) { selects.push(create(b)); });
    }
    
    if (selects && selects.length > 0) {
      var act = $Actions['<%=name%>'];
      if (act.jsSelectCallback) {
        if (act.jsSelectCallback(selects))
          act.close();
      } else {
      	<%=DictionaryRender.genSelectCallback(nCP, "selects")%>
      }
    } else {
      alert('#(okcancel_inc.jsp.0)');
    }
  };

  $ready(function() {
    var listAction = $Actions['<%=list.getRef()%>'];
    listAction();
    var win = $Actions['<%=name%>'].window;
    win.content.setStyle("overflow:hidden;");
    var endedAction = function() {
      listAction.listbox.setStyle('height: ' + (win.getSize(true).height - 41) + 'px;');
    };
    endedAction();
    win.observe("resize:ended", endedAction);
  });
</script>
