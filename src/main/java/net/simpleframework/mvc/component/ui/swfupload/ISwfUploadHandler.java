package net.simpleframework.mvc.component.ui.swfupload;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.mvc.IMultipartFile;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface ISwfUploadHandler extends IComponentHandler {

	/**
	 * 上传逻辑
	 * 
	 * @param cp
	 * @param multipartFile
	 * @param variables
	 *        post变量
	 * @throws IOException
	 */
	void upload(ComponentParameter cp, IMultipartFile multipartFile, Map<String, Object> variables)
			throws IOException;
}
