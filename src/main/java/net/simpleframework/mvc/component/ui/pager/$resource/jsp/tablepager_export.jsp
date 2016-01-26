<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.TablePagerUtils"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.ITablePagerHandler"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.PagerUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%
	final ComponentParameter nCP = PagerUtils.get(request, response);
	final ITablePagerHandler hdl = (ITablePagerHandler) nCP
			.getComponentHandler();
%>
<div class="tablepager_export">
  <div class="pbar">
    <div>
      <span style="padding: 0 3px">#(tablepager_export.1)</span>
      <%=hdl.getExportSelectElement(nCP)%>
    </div>
    <div id="tablepager_export_pbar"></div>
    <div style="text-align: right;">
      <input type="button" value="#(tablepager_export.2)" onclick="do_tablepager_export(this);" />
    </div>
  </div>
  <div class="f3 lbl">#(tablepager_export.3)</div>
  <%=TablePagerUtils.toExportColumnsHTML(nCP)%>
</div>
<script type="text/javascript">
  (function() {
    $("col_check_all").observe(
        "click",
        function(evt) {
          var all = this;
          all.up(".tablepager_export").select("table input[type=checkbox]")
              .each(function(box) {
                box.checked = all.checked;
              });
        });
  })();

  function do_tablepager_export(o) {
    var act = $Actions["ajaxTablePagerExport"];
    act.selector = $Actions["<%=nCP.getComponentName()%>"].selector;
    var checked = [];
    o.up(".tablepager_export").select("table input[type=checkbox]").each(
        function(box) {
          if (box.checked)
            checked.push(box.getAttribute("name").substring(4));
        });
    act("filetype=" + $F("tablepager_export_filetype") + "&v="
        + checked.join(";"));
    $Actions["tablepager_export_pbar"].start();
  }
</script>