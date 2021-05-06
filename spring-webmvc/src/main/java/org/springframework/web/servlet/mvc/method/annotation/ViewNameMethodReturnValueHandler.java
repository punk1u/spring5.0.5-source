/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.web.servlet.mvc.method.annotation;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.RequestToViewNameTranslator;

/**
 * 处理{@code void}和{@code String}类型的返回值，将它们解释为视图名称引用。从4.2开始，
 * 它还处理一般的{@code CharSequence}类型，例如{@code StringBuilder}或Groovy的{@code GString}，
 * 同样作为视图名使用。
 * Handles return values of types {@code void} and {@code String} interpreting them
 * as view name reference. As of 4.2, it also handles general {@code CharSequence}
 * types, e.g. {@code StringBuilder} or Groovy's {@code GString}, as view names.
 *
 * 返回值为{@code null}，可能是由于{@code void}返回类型造成的，
 * 也可能是由于实际返回值保留为允许配置的{@link RequestToViewNameTranslator}按约定选择视图名称。
 * <p>A {@code null} return value, either due to a {@code void} return type or
 * as the actual return value is left as-is allowing the configured
 * {@link RequestToViewNameTranslator} to select a view name by convention.
 *
 * 根据{@code @ModelAttribute}或{@code @ResponseBody}等注解的存在，可以用多种方式解释字符串类型的处理结果返回值。
 * 因此，应该在支持这些注解的处理程序之后调用此处理程序。
 * <p>A String return value can be interpreted in more than one ways depending
 * on the presence of annotations like {@code @ModelAttribute} or
 * {@code @ResponseBody}. Therefore this handler should be configured after
 * the handlers that support these annotations.
 *
 * @author Rossen Stoyanchev
 * @author Juergen Hoeller
 * @since 3.1
 */
public class ViewNameMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

	@Nullable
	private String[] redirectPatterns;


	/**
	 * Configure one more simple patterns (as described in
	 * {@link PatternMatchUtils#simpleMatch}) to use in order to recognize
	 * custom redirect prefixes in addition to "redirect:".
	 * <p>Note that simply configuring this property will not make a custom
	 * redirect prefix work. There must be a custom View that recognizes the
	 * prefix as well.
	 * @since 4.1
	 */
	public void setRedirectPatterns(@Nullable String... redirectPatterns) {
		this.redirectPatterns = redirectPatterns;
	}

	/**
	 * The configured redirect patterns, if any.
	 */
	@Nullable
	public String[] getRedirectPatterns() {
		return this.redirectPatterns;
	}


	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		/**
		 * 获取到请求处理完成后的响应结果的类型
		 */
		Class<?> paramType = returnType.getParameterType();
		return (void.class == paramType || CharSequence.class.isAssignableFrom(paramType));
	}

	/**
	 * 处理响应结果到具体页面的跳转逻辑
	 * @param returnValue the value returned from the handler method
	 * @param returnType the type of the return value. This type must have
	 * previously been passed to {@link #supportsReturnType} which must
	 * have returned {@code true}.
	 * @param mavContainer the ModelAndViewContainer for the current request
	 * @param webRequest the current request
	 * @throws Exception
	 */
	@Override
	public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {

		if (returnValue instanceof CharSequence) {
			String viewName = returnValue.toString();
			mavContainer.setViewName(viewName);
			if (isRedirectViewName(viewName)) {
				mavContainer.setRedirectModelScenario(true);
			}
		}
		else if (returnValue != null){
			// should not happen
			throw new UnsupportedOperationException("Unexpected return type: " +
					returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
		}
	}

	/**
	 * Whether the given view name is a redirect view reference.
	 * The default implementation checks the configured redirect patterns and
	 * also if the view name starts with the "redirect:" prefix.
	 * @param viewName the view name to check, never {@code null}
	 * @return "true" if the given view name is recognized as a redirect view
	 * reference; "false" otherwise.
	 */
	protected boolean isRedirectViewName(String viewName) {
		return (PatternMatchUtils.simpleMatch(this.redirectPatterns, viewName) || viewName.startsWith("redirect:"));
	}

}
