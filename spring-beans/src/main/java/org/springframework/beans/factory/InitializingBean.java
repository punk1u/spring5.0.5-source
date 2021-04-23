/*
 * Copyright 2002-2016 the original author or authors.
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

package org.springframework.beans.factory;

/**
 * 由Bean实现的接口，这些Bean需要在BeanFactory设置完所有属性后做出反应：例如，执行自定义初始化，或者仅仅检查是否设置了所有强制属性。
 * Interface to be implemented by beans that need to react once all their
 * properties have been set by a BeanFactory: for example, to perform custom
 * initialization, or merely to check that all mandatory properties have been set.
 *
 * 实现InitializingBean的另一种方法是指定自定义init方法，例如在XMLBean定义中。
 * 有关所有bean生命周期方法的列表，请参见{@link BeanFactory BeanFactory javadocs}。
 * <p>An alternative to implementing InitializingBean is specifying a custom
 * init-method, for example in an XML bean definition.
 * For a list of all bean lifecycle methods, see the
 * {@link BeanFactory BeanFactory javadocs}.
 *
 * @author Rod Johnson
 * @see BeanNameAware
 * @see BeanFactoryAware
 * @see BeanFactory
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getInitMethodName
 * @see org.springframework.context.ApplicationContextAware
 */
public interface InitializingBean {

	/**
	 * 由BeanFactory在设置了提供的所有bean属性（并满足BeanFactoryAware和ApplicationContextAware）后调用。
	 * 此方法允许bean实例仅在设置了所有bean属性时执行初始化，
	 * 并在配置错误时抛出异常@在错误配置（如设置基本属性失败）或初始化失败时引发异常。
	 *
	 * Invoked by a BeanFactory after it has set all bean properties supplied
	 * (and satisfied BeanFactoryAware and ApplicationContextAware).
	 * <p>This method allows the bean instance to perform initialization only
	 * possible when all bean properties have been set and to throw an
	 * exception in the event of misconfiguration.
	 * @throws Exception in the event of misconfiguration (such
	 * as failure to set an essential property) or if initialization fails.
	 */
	void afterPropertiesSet() throws Exception;

}
