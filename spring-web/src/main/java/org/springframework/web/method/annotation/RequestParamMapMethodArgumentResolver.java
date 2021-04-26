/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.web.method.annotation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 解析用@{@link RequestParam}注解标注的的{@link Map}方法参数，其中注解未指定请求参数名称。
 * 请参阅{@link RequestParamMethodArgumentResolver}，以解析带有请求参数名称的{@link Map}方法参数。
 * Resolves {@link Map} method arguments annotated with an @{@link RequestParam}
 * where the annotation does not specify a request parameter name.
 * See {@link RequestParamMethodArgumentResolver} for resolving {@link Map}
 * method arguments with a request parameter name.
 *
 * 创建的{@link Map}包含所有请求参数名称/值对。如果方法参数类型是{@link MultiValueMap}，
 * 那么创建的映射包含所有请求参数以及请求参数有多个值的情况下的所有值。
 * <p>The created {@link Map} contains all request parameter name/value pairs.
 * If the method parameter type is {@link MultiValueMap} instead, the created
 * map contains all request parameters and all there values for cases where
 * request parameters have multiple values.
 *
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 * @since 3.1
 * @see RequestParamMethodArgumentResolver
 */
public class RequestParamMapMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
		return (requestParam != null && Map.class.isAssignableFrom(parameter.getParameterType()) &&
				!StringUtils.hasText(requestParam.name()));
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

		Class<?> paramType = parameter.getParameterType();

		Map<String, String[]> parameterMap = webRequest.getParameterMap();
		if (MultiValueMap.class.isAssignableFrom(paramType)) {
			MultiValueMap<String, String> result = new LinkedMultiValueMap<>(parameterMap.size());
			parameterMap.forEach((key, values) -> {
				for (String value : values) {
					result.add(key, value);
				}
			});
			return result;
		}
		else {
			Map<String, String> result = new LinkedHashMap<>(parameterMap.size());
			parameterMap.forEach((key, values) -> {
				if (values.length > 0) {
					result.put(key, values[0]);
				}
			});
			return result;
		}
	}
}
