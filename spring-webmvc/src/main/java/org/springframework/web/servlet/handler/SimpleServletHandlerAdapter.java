/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.web.servlet.handler;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

/**
 * 适配器将Servlet接口与通用DispatcherServlet一起使用。调用Servlet的{@code service}方法来处理请求。
 * Adapter to use the Servlet interface with the generic DispatcherServlet.
 * Calls the Servlet's {@code service} method to handle a request.
 *
 * Last-modified 的检查不受显式支持：这通常由Servlet实现本身处理（通常从HttpServlet基类派生）。
 * <p>Last-modified checking is not explicitly supported: This is typically
 * handled by the Servlet implementation itself (usually deriving from
 * the HttpServlet base class).
 *
 * 默认情况下不激活此适配器；它需要在DispatcherServlet上下文中定义为bean。它将自动应用于实现Servlet接口的映射处理程序bean。
 * <p>This adapter is not activated by default; it needs to be defined as a
 * bean in the DispatcherServlet context. It will automatically apply to
 * mapped handler beans that implement the Servlet interface then.
 *
 * 请注意，定义为bean的Servlet实例不会接收初始化和销毁回调，
 * 除非在DispatcherServlet上下文中定义了特殊的后处理器（如SimpleServletPostProcessor）。
 * <p>Note that Servlet instances defined as bean will not receive initialization
 * and destruction callbacks, unless a special post-processor such as
 * SimpleServletPostProcessor is defined in the DispatcherServlet context.
 *
 * 或者，考虑使用Spring的ServletWrappingController包装Servlet。
 * 这对于现有的Servlet类特别合适，允许指定Servlet初始化参数等。
 * <p><b>Alternatively, consider wrapping a Servlet with Spring's
 * ServletWrappingController.</b> This is particularly appropriate for
 * existing Servlet classes, allowing to specify Servlet initialization
 * parameters etc.
 *
 * @author Juergen Hoeller
 * @since 1.1.5
 * @see javax.servlet.Servlet
 * @see javax.servlet.http.HttpServlet
 * @see SimpleServletPostProcessor
 * @see org.springframework.web.servlet.mvc.ServletWrappingController
 */
public class SimpleServletHandlerAdapter implements HandlerAdapter {

	@Override
	public boolean supports(Object handler) {
		return (handler instanceof Servlet);
	}

	@Override
	@Nullable
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		((Servlet) handler).service(request, response);
		return null;
	}

	@Override
	public long getLastModified(HttpServletRequest request, Object handler) {
		return -1;
	}

}
