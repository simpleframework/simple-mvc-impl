<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.PagerUtils"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.TablePagerUtils"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.TablePagerColumn"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%
	final ComponentParameter nCP = PagerUtils.get(request, response);
	final TablePagerColumn col = TablePagerUtils.getSelectedColumn(nCP);
	if (col == null) {
%>
<div class="simple_toolbar3 f2" style="text-align: center;">#(tablepager_filter.1)</div>
<%
	return;
	}
%>
<div class="filter_window simple_window_tcb">
  <div class="c">
    <input type="hidden" name="<%=TablePagerUtils.PARAM_FILTER_CUR_COL%>"
      value="<%=col.getColumnName()%>">
    <table style="width: 100%;">
      <tr>
        <td width="80"><%=TablePagerUtils.toFilterSelectHTML(col, "tp_filter_r1")%></td>
        <td><%=TablePagerUtils.toFilterInputHTML(col, "tp_filter_v1")%></td>
      </tr>
      <tr>
        <td></td>
        <td style="padding: 4px;"><%=TablePagerUtils.toFilterRelationHTML(col)%></td>
      </tr>
      <tr>
        <td><%=TablePagerUtils.toFilterSelectHTML(col, "tp_filter_r2")%></td>
        <td><%=TablePagerUtils.toFilterInputHTML(col, "tp_filter_v2")%></td>
      </tr>
    </table>
  </div>
  <div class="b">
    <input type="submit" id="idTablePagerFilterSave" value="#(Button.Ok)" class="button2"
      onclick="tp_filter_save()" /> <input type="button" value="#(Button.Cancel)"
      onclick="$win(this).close();" />
  </div>
</div>
<script type="text/javascript">
  function tp_filter_click() {
    var obj = $("tp_filter_op0");
    $Actions[!obj.checked ? 'enable' : 'disable'](obj.up('tr').next());
  }

  function tp_filter_save() {
    var act = $Actions['ajaxTablePagerFilterSave'];
    act();
  }
</script>