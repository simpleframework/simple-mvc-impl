<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ui.colorpalette.ColorPaletteUtils"%>
<div class="colorpalette">
<table cellpadding="2" cellspacing="0">
  <tr>
    <td valign="top">
    <div id="_ColorMap" class="map"></div>
    </td>
    <td valign="top">
    <div id="_ColorBar" class="bar"></div>
    </td>
    <td valign="top">
    <table>
      <tr>
        <td colspan="3" align="center">
        <div id="_Preview" class="preview"><br />
        </div>
        </td>
      </tr>
      <tr>
        <td><input type="radio" id="_HueRadio" name="_Mode" value="0" /></td>
        <td><label>H:</label></td>
        <td><input type="text" id="_Hue" value="0" style="width: 40px;" /> &deg;</td>
      </tr>
      <tr>
        <td><input type="radio" id="_SaturationRadio" name="_Mode" value="1" /></td>
        <td><label>S:</label></td>
        <td><input type="text" id="_Saturation" value="100" style="width: 40px;" /> %</td>
      </tr>
      <tr>
        <td><input type="radio" id="_BrightnessRadio" name="_Mode" value="2" /></td>
        <td><label>V:</label></td>
        <td><input type="text" id="_Brightness" value="100" style="width: 40px;" /> %</td>
      </tr>
      <tr>
        <td colspan="3" height="5"></td>
      </tr>
      <tr>
        <td><input type="radio" id="_RedRadio" name="_Mode" value="r" /></td>
        <td><label>R:</label></td>
        <td><input type="text" id="_Red" value="255" style="width: 40px;" /></td>
      </tr>
      <tr>
        <td><input type="radio" id="_GreenRadio" name="_Mode" value="g" /></td>
        <td><label>G:</label></td>
        <td><input type="text" id="_Green" value="0" style="width: 40px;" /></td>
      </tr>
      <tr>
        <td><input type="radio" id="_BlueRadio" name="_Mode" value="b" /></td>
        <td><label>B:</label></td>
        <td><input type="text" id="_Blue" value="0" style="width: 40px;" /></td>
      </tr>
      <tr>
        <td>#:</td>
        <td colspan="2"><input type="text" id="_Hex" value="FF0000" style="width: 60px;" /></td>
      </tr>
    </table>
    </td>
  </tr>
</table>
</div>
<script type="text/javascript">
<%=ColorPaletteUtils.jsColorpicker(ColorPaletteUtils
					.get(request, response))%>
</script>
