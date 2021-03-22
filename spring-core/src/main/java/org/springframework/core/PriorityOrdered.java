/*
 * Copyright 2002-2015 the original author or authors.
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

package org.springframework.core;

/**
 * 接口的扩展，表示优先级顺序：实现这个接口的对象执行时的优先级始终在实现Ordered接口的对象之前，
 * 设计上PriorityOrdered接口是Ordered的子接口
 * Extension of the {@link Ordered} interface, expressing a <em>priority</em>
 * ordering: order values expressed by {@code PriorityOrdered} objects
 * always apply before same order values expressed by <em>plain</em>
 * {@link Ordered} objects.
 *
 * <p>This is primarily a special-purpose interface, used for objects where
 * it is particularly important to recognize <em>prioritized</em> objects
 * first, without even obtaining the remaining objects. A typical example:
 * prioritized post-processors in a Spring
 * {@link org.springframework.context.ApplicationContext}.
 *
 * 实现了PriorityOrdered的后置处理器bean在一个特殊的阶段初始化，先于其他后处理器bean。
 * 这微妙地影响了它们的自动连接行为：它们将只针对bean进行自动连接，而bean不需要为类型匹配进行紧急初始化。
 *
 * eager:adj 热切的;渴望的;渴求的
 * <p>Note: {@code PriorityOrdered} post-processor beans are initialized in
 * a special phase, ahead of other post-processor beans. This subtly
 * affects their autowiring behavior: they will only be autowired against
 * beans which do not require eager initialization for type matching.
 *
 * @author Juergen Hoeller
 * @since 2.5
 * @see org.springframework.beans.factory.config.PropertyOverrideConfigurer
 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
 */
public interface PriorityOrdered extends Ordered {

}
