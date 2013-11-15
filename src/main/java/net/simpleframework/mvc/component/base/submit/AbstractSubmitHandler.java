package net.simpleframework.mvc.component.base.submit;

import net.simpleframework.mvc.AbstractUrlForward;
import net.simpleframework.mvc.IMultipartFile;
import net.simpleframework.mvc.MultipartPageRequest;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.AbstractComponentHandler;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractSubmitHandler extends AbstractComponentHandler implements
		ISubmitHandler {

	@Override
	public AbstractUrlForward submit(final ComponentParameter cp) {
		return null;
	}

	protected IMultipartFile getMultipartFile(final PageRequestResponse rRequest,
			final String filename) {
		return rRequest.request instanceof MultipartPageRequest ? ((MultipartPageRequest) rRequest.request)
				.getFile(filename) : null;
	}
}
