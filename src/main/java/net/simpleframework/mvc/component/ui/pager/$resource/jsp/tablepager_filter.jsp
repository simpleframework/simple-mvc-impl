<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.PagerUtils"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.TablePagerColumn"%>
<%@ page import="net.simpleframework.mvc.component.ui.pager.TablePagerUtils"%>
<%@ page import="net.simpleframework.ado.EFilterRelation"%>
<%@ page import="net.simpleframework.mvc.common.element.CalendarInput"%>
<%@ page import="net.simpleframework.mvc.common.element.Checkbox"%>
<%@ page import="net.simpleframework.common.I18n"%>
<%@ page import="net.simpleframework.mvc.common.element.Radio"%>
<%@ page import="net.simpleframework.mvc.common.element.InputElement"%>
<%
	final ComponentParameter nCP = PagerUtils.get(request, response);
	final TablePagerColumn col = TablePagerUtils.getSelectedColumn(nCP);
	if (col == null) {
%>
<div class="simple_toolbar3 f2" style="text-align: center;">#(tablepager_filter.1)</div>
<%
	return;
	}
	final boolean isDate = Date.class.isAssignableFrom(col
			.propertyClass());
	final String fm = col.getFormat();
%>
<div class="tablepager_filter simple_window_tcb">
  <div class="c">
    <input type="hidden" name="<%=TablePagerUtils.PARAM_FILTER_CUR_COL%>"
      value="<%=col.getColumnName()%>">
    <table style="width: 100%;">
      <tr>
        <td width="80"><select name="tp_filter_r1" id="tp_filter_r1">
            <%=TablePagerUtils.toOptionHTML(col)%>
        </select></td>
        <td>
          <%
          	if (isDate) {
          		out.write(new CalendarInput("tp_filter_v1")
          				.setCalendarComponent("calendarTablePagerFilter")
          				.setDateFormat(fm).toString());
          	} else {
          		out.write(new InputElement("tp_filter_v1").toString());
          	}
          %>
        </td>
      </tr>
      <tr>
        <td></td>
        <td style="padding: 4px;">
          <%
          	for (Checkbox r : new Checkbox[] {
          			new Radio("tp_filter_op0", I18n.$m("tablepager_filter.0"))
          					.setText("none").setChecked(true),
          			new Radio("tp_filter_op1", I18n.$m("tablepager_filter.2"))
          					.setText("and"),
          			new Radio("tp_filter_op2", I18n.$m("tablepager_filter.3"))
          					.setText("or") }) {
          		r.setName("tp_filter_op").setOnclick("tp_filter_click();");
          		out.write(r.toString());
          	}
          %>
        </td>
      </tr>
      <tr>
        <td><select name="tp_filter_r2" id="tp_filter_r2"><%=TablePagerUtils.toOptionHTML(col)%></select></td>
        <td>
          <%
          	if (isDate) {
          		out.write(new CalendarInput("tp_filter_v2")
          				.setCalendarComponent("calendarTablePagerFilter")
          				.setDateFormat(fm).toString());
          	} else {
          		out.write(new InputElement("tp_filter_v2").toString());
          	}
          %>
        </td>
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