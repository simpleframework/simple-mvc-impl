package net.simpleframework.mvc.component.ui.swfupload;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.common.FileUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.mvc.MVCContext;
import net.simpleframework.mvc.MultipartPageRequest;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class SwfUploadUtils {
	public static final String BEAN_ID = "swfupload_@bid";

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	private static String msg(final String message, final Object... args) {
		return JavascriptUtils.escape($m(message, args));
	}

	public static String genJavascript(final ComponentParameter cp) {
		final SwfUploadBean swfUpload = (SwfUploadBean) cp.componentBean;
		final String beanId = swfUpload.hashId();
		final String homePath = ComponentUtils.getResourceHomePath(SwfUploadBean.class);

		final StringBuilder sb = new StringBuilder();
		sb.append("var act = $Actions['" + cp.getComponentName() + "'];");
		sb.append("var swf=").append("new SWFUpload({");
		sb.append("upload_url: \"").append(homePath).append("/jsp/swfupload_action.jsp;jsessionid=")
				.append(cp.getSessionId());
		// slb
		final String serverid = cp.getCookie("SERVERID");
		if (StringUtils.hasText(serverid)) {
			sb.append("?SERVERID=").append(serverid);
		}
		sb.append("\",");

		sb.append("flash_url: \"").append(homePath).append("/flash/swfupload.swf\",");
		sb.append("use_query_string: true,");

		final String fileSizeLimit = (String) cp.getBeanProperty("fileSizeLimit");
		if (StringUtils.hasText(fileSizeLimit)) {
			sb.append("file_size_limit: \"").append(fileSizeLimit).append("\",");
		}
		sb.append("file_queue_limit: ").append(cp.getBeanProperty("fileQueueLimit")).append(",");

		final String fileTypes = (String) cp.getBeanProperty("fileTypes");
		if (StringUtils.hasText(fileTypes)) {
			sb.append("file_types: \"").append(fileTypes).append("\",");
			sb.append("file_types_description: \"").append(cp.getBeanProperty("fileTypesDesc"))
					.append("\",");
		}

		sb.append("button_placeholder_id: \"placeholder_").append(beanId).append("\",");
		sb.append("button_window_mode: \"transparent\",");
		sb.append("button_cursor: SWFUpload.CURSOR.HAND,");
		sb.append("button_width: ").append(cp.getBeanProperty("btnWidth")).append(",");
		sb.append("button_height: ").append(cp.getBeanProperty("btnHeight")).append(",");
		String btnImageUrl = (String) cp.getBeanProperty("btnImageUrl");
		if (!StringUtils.hasText(btnImageUrl)) {
			btnImageUrl = cp.getCssResourceHomePath(SwfUploadBean.class) + "/images/upload_act.png";
		}
		sb.append("button_image_url: '").append(btnImageUrl).append("',");
		final String uploadText = (String) cp.getBeanProperty("uploadText");
		if (StringUtils.hasText(uploadText)) {
			sb.append("button_text: \"<span class='swf-button'>").append(uploadText)
					.append("<\\/span>\",");
			sb.append("button_text_style: \".swf-button { color: #014060; text-align: center; }\",");
			sb.append("button_text_top_padding: ")
					.append(cp.getRequestHeader("User-Agent").indexOf("AppleWebKit/") > -1 ? 4 : 2)
					.append(",");
		}
		if (swfUpload.isMultiFileSelected()) {
			sb.append("button_action: SWFUpload.BUTTON_ACTION.SELECT_FILES,");
		} else {
			sb.append("button_action: SWFUpload.BUTTON_ACTION.SELECT_FILE,");
		}

		// file_queued_handler
		sb.append("file_queued_handler: function(file) {");
		sb.append("  var queue = $(\"fileQueue_").append(beanId).append("\");");
		sb.append("  var html =\"<div id='item_\" + file.id + \"' class='item'>");
		sb.append("    <table width='100%' cellpadding='0' cellspacing='0'>");
		sb.append("      <tr><td>");
		sb.append("        <table width='100%' cellpadding='0' cellspacing='0'><tr>");
		sb.append("          <td><span>\" + file.name + \"<\\/span>");
		sb.append("            <span class='fs'>\" + file.size.toFileString() + \"<\\/span><\\/td>");
		sb.append("          <td width='30px;' align='center'>");
		sb.append("            <div class='delete_image'><\\/div>");
		sb.append("          <\\/td>");
		sb.append("        <\\/tr><\\/table>");
		sb.append("      <\\/td><\\/tr>");
		sb.append("      <tr><td>");
		sb.append("        <div class='bar'><\\/div>");
		sb.append("      <\\/td><\\/tr>");
		sb.append("      <tr><td>");
		sb.append("        <div class='message'><\\/div>");
		sb.append("      <\\/td><\\/tr>");
		sb.append("    <\\/table><\\/div>\";");
		sb.append("  queue.insert(html);");
		sb.append("  var item = $(\"item_\" + file.id);");
		sb.append("  item.down(\".delete_image\").observe(\"click\", function(e) {");
		sb.append("    var fo = swf.getFile(file.id);");
		sb.append("    if (!fo) {");
		sb.append("      $Effect.remove(item);");
		sb.append("      return;");
		sb.append("    }");
		sb.append("    if (fo.filestatus == SWFUpload.FILE_STATUS.IN_PROGRESS && !confirm(\"")
				.append($m("SwfUploadUtils.5")).append("\")) return;");
		sb.append("    swf.cancelUpload(file.id);");
		sb.append("    $Effect.remove(item);");
		sb.append("  });");
		sb.append("  item.bar = new $UI.ProgressBar(item.down(\".bar\"), {");
		sb.append("    maxProgressValue: file.size,");
		sb.append("    startAfterCreate: false,");
		sb.append("    showAbortAction: false,");
		sb.append("    showDetailAction: false");
		sb.append("  });");
		sb.append("},");

		// file_dialog_complete_handler
		sb.append(
				"file_dialog_complete_handler: function(numFilesSelected, numFilesQueued, numFilesInQueue) {");
		sb.append("act.startUpload();");
		sb.append("},");

		// upload_progress_handler
		sb.append("upload_progress_handler: function(file, bytesComplete, bytesTotal) {");
		sb.append("  var item = $(\"item_\" + file.id);");
		sb.append("  if (item.bar) {");
		sb.append("    item.bar.setProgress(bytesComplete);");
		sb.append("  }");
		sb.append("  if (bytesComplete >= bytesTotal) {");
		sb.append("    item.down(\".message\").update(\"").append($m("SwfUploadUtils.1"))
				.append("\");");
		sb.append("  }");
		sb.append("},");

		// file_queue_error_handler
		sb.append("file_queue_error_handler: function(file, errorCode, message) {");
		sb.append("  var msgc = $(\"message_").append(beanId).append("\");");
		sb.append("  if (errorCode == SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT) {");
		sb.append("    msgc.update('\"' + file.name + '\" ")
				.append(msg("SwfUploadUtils.2", fileSizeLimit)).append("');");
		sb.append("  } else if (errorCode == SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE) {");
		sb.append("    msgc.update(\"").append(msg("SwfUploadUtils.3")).append("\");");
		sb.append("  } else if (errorCode == SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {");
		sb.append("    msgc.update(\"").append(msg("SwfUploadUtils.4")).append("\");");
		sb.append("  } else {");
		sb.append("    msgc.update(message);");
		sb.append("  }");
		sb.append("  $Effect.shake(msgc);");
		sb.append("  (function() { msgc.update(\"\"); }).delay(2);");
		sb.append("},");

		// upload_error_handler
		sb.append("upload_error_handler: function(file, errorCode, message) {");
		sb.append("  var msgc = $(\"message_").append(beanId).append("\");");
		sb.append("  if (errorCode == SWFUpload.UPLOAD_ERROR.FILE_CANCELLED) {");
		sb.append("    msgc.update(\"").append(msg("SwfUploadUtils.7")).append("\");");
		sb.append("  } else {");
		sb.append("    msgc.update(message);");
		sb.append("  }");
		sb.append("  (function() {msgc.update(\"\");}).delay(2);");
		sb.append("},");

		// upload_success_handler
		sb.append("upload_success_handler: function(file, serverData, responseReceived) {");
		sb.append("  var json = serverData.evalJSON();");
		sb.append("  var item = $(\"item_\" + file.id);");
		sb.append("  var message = item.down(\".message\");");
		sb.append("  if (json[\"error\"]) {");
		sb.append("    message.update(json[\"error\"]);");
		sb.append("  } else {");
		sb.append("    message.update(\"").append($m("SwfUploadUtils.6")).append("\");");
		sb.append("    (function() { $Effect.remove(item); }).delay(2);");
		sb.append("  }");
		sb.append("  var hasQueued = message.up(\".queue\").select(\".item\").any(function(item) {");
		sb.append("	   var fo = swf.getFile(item.id.substring(5));");
		sb.append("    return fo && fo.filestatus == SWFUpload.FILE_STATUS.QUEUED;");
		sb.append("  });");
		sb.append(StringUtils.blank(cp.getBeanProperty("jsCompleteCallback")));
		sb.append("},");

		// upload_complete_handler
		sb.append("upload_complete_handler: function(file) {");
		sb.append("  var queue = $(\"fileQueue_").append(beanId).append("\");");
		sb.append("  queue.select(\".item\").any(function(item) {");
		sb.append("    var fo = swf.getFile(item.id.substring(5));");
		sb.append("    if (fo && fo.filestatus == SWFUpload.FILE_STATUS.QUEUED) {");
		sb.append("	     swf.startUpload(fo.id); ");
		sb.append("      return true;");
		sb.append("    }");
		sb.append("	 });");
		sb.append("}");

		sb.append("});");

		sb.append("act.updateUI = function() {");
		sb.append("  $$(\"#fileQueue_").append(beanId).append(" .item\").each(function(item) {");
		sb.append("  	if (item.bar) item.bar.updateUI();");
		sb.append("  });");
		sb.append("};");

		sb.append("act.startUpload = function() {");
		sb.append("var params = \"").append(BEAN_ID).append("=").append(beanId)
				.append("\".addSelectorParameter(\"");
		sb.append(cp.getBeanProperty("selector")).append("\");");
		sb.append("swf.setPostParams(params.toQueryParams());");
		sb.append("swf.startUpload();");
		sb.append("};");

		return JavascriptUtils.wrapWhenReady(sb.toString());
	}

	public static String upload(final HttpServletRequest request, final HttpServletResponse response)
			throws IOException {
		return upload(get(request, response));
	}

	public static String upload(final ComponentParameter cp) throws IOException {
		final ISwfUploadHandler uHandle = (ISwfUploadHandler) cp.getComponentHandler();
		final KVMap variables = new KVMap();
		if (uHandle != null) {
			final HttpServletRequest request = cp.request;
			try {
				request.setCharacterEncoding("UTF-8");
			} catch (final UnsupportedEncodingException e) {
			}

			try {
				final int fileSizeLimit = (int) FileUtils
						.toFileSize((String) cp.getBeanProperty("fileSizeLimit"));
				uHandle.upload(cp,
						((MultipartPageRequest) (cp.request = MVCContext.get()
								.createMultipartPageRequest(request, fileSizeLimit))).getFile("Filedata"),
						variables);
			} catch (final Throwable ex) {
				variables.add("error", MVCContext.get().getThrowableMessage(ex));
			}
		}
		return variables.toJSON();
	}
}
