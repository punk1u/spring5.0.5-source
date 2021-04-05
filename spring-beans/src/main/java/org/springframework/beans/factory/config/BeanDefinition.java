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

package org.springframework.beans.factory.config;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.AttributeAccessor;
import org.springframework.lang.Nullable;

/**
 * A BeanDefinition describes a bean instance, which has property values,
 * constructor argument values, and further information supplied by
 * concrete implementations.
 *
 * <p>This is just a minimal interface: The main intention is to allow a
 * {@link BeanFactoryPostProcessor} such as {@link PropertyPlaceholderConfigurer}
 * to introspect and modify property values and other bean metadata.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 19.03.2004
 * @see ConfigurableListableBeanFactory#getBeanDefinition
 * @see org.springframework.beans.factory.support.RootBeanDefinition
 * @see org.springframework.beans.factory.support.ChildBeanDefinition
 */
/**
 * 作用同jdk中的Class，用于描述对象的属性
 * 但是因为Class描述不了如Scope等spring中的属性，
 * 所以Spring自己定义了BeanDefinition接口及其子接口和相关实现来描述对象属性
 *
 * BeanDefinition接口继承了AttributeAccessor、BeanMetadataElement两个接口
 */
public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

	/**
	 * 标准单例作用域的作用域标识符
	 * 所谓单例，就是Spring的IOC机制只创建该类的一个实例
	 * Scope identifier for the standard singleton scope: "singleton".
	 * <p>Note that extended bean factories might support further scopes.
	 * @see #setScope
	 */
	String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

	/**
	 * 标准原型作用域的作用域标识符
	 * 所谓原型，指的是每次调用时，会重新创建该类的一个实例，比较类似于每次new一个对象实例。
	 * Scope identifier for the standard prototype scope: "prototype".
	 * <p>Note that extended bean factories might support further scopes.
	 * @see #setScope
	 */
	String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;

	/**
	 * BeanDefinition中的Role用于指定该BeanDefinition在Spring中的地位，比如Spring本身带有的必须用到的bean是作为
	 * ROLE_INFRASTRUCTURE
	 */

	/**
	 * 对应程序员提供的bean的角色常量
	 * Role hint indicating that a {@code BeanDefinition} is a major part
	 * of the application. Typically corresponds to a user-defined bean.
	 */
	int ROLE_APPLICATION = 0;

	/**
	 * 通常是外部配置
	 * Role hint indicating that a {@code BeanDefinition} is a supporting
	 * part of some larger configuration, typically an outer
	 * {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
	 * {@code SUPPORT} beans are considered important enough to be aware
	 * of when looking more closely at a particular
	 * {@link org.springframework.beans.factory.parsing.ComponentDefinition},
	 * but not when looking at the overall configuration of an application.
	 */
	int ROLE_SUPPORT = 1;

	/**
	 * 用于表示是Spring内部自己定义的基础设施bean，是不可或缺的角色
	 * Role hint indicating that a {@code BeanDefinition} is providing an
	 * entirely background role and has no relevance to the end-user. This hint is
	 * used when registering beans that are completely part of the internal workings
	 * of a {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
	 */
	int ROLE_INFRASTRUCTURE = 2;


	// Modifiable attributes

	/**
	 * 设置父BeanDefinition的名称
	 * Set the name of the parent definition of this bean definition, if any.
	 */
	void setParentName(@Nullable String parentName);

	/**
	 * 获取父BeanDefinition的名称
	 * Return the name of the parent definition of this bean definition, if any.
	 */
	@Nullable
	String getParentName();

	/**
	 *设置bean的class的名称，即这个BeanDefinition所描述的类的名称
	 * Specify the bean class name of this bean definition.
	 * <p>The class name can be modified during bean factory post-processing,
	 * typically replacing the original class name with a parsed variant of it.
	 * @see #setParentName
	 * @see #setFactoryBeanName
	 * @see #setFactoryMethodName
	 */
	void setBeanClassName(@Nullable String beanClassName);

	/**
	 * 获取bean的class的名称
	 * Return the current bean class name of this bean definition.
	 * <p>Note that this does not have to be the actual class name used at runtime, in
	 * case of a child definition overriding/inheriting the class name from its parent.
	 * Also, this may just be the class that a factory method is called on, or it may
	 * even be empty in case of a factory bean reference that a method is called on.
	 * Hence, do <i>not</i> consider this to be the definitive bean type at runtime but
	 * rather only use it for parsing purposes at the individual bean definition level.
	 * @see #getParentName()
	 * @see #getFactoryBeanName()
	 * @see #getFactoryMethodName()
	 */
	@Nullable
	String getBeanClassName();

	/**
	 * 设置作用域（单例、原型）
	 * Override the target scope of this bean, specifying a new scope name.
	 * @see #SCOPE_SINGLETON
	 * @see #SCOPE_PROTOTYPE
	 */
	void setScope(@Nullable String scope);

	/**
	 * Return the name of the current target scope for this bean,
	 * or {@code null} if not known yet.
	 */
	@Nullable
	String getScope();

	/**
	 * 设置这个BeanDefinition描述的bean对象是否延迟加载
	 * Set whether this bean should be lazily initialized.
	 * <p>If {@code false}, the bean will get instantiated on startup by bean
	 * factories that perform eager initialization of singletons.
	 */
	void setLazyInit(boolean lazyInit);

	/**
	 * 判断这个BeanDefinition描述的bean对象是否需要延迟加载
	 * Return whether this bean should be lazily initialized, i.e. not
	 * eagerly instantiated on startup. Only applicable to a singleton bean.
	 */
	boolean isLazyInit();

	/**
	 * 设置这个BeanDefinition所描述的对象被实例化时所依赖的bean对象(可能依赖多个)
	 * Set the names of the beans that this bean depends on being initialized.
	 * The bean factory will guarantee that these beans get initialized first.
	 */
	void setDependsOn(@Nullable String... dependsOn);

	/**
	 * 获得这个BeanDefinition所描述的对象被实例化时所依赖的bean对象(可能依赖多个)
	 * Return the bean names that this bean depends on.
	 */
	@Nullable
	String[] getDependsOn();

	/**
	 * 设置这个BeanDefinition所描述的Bean对象是否作为自动装配的候选对象
	 * 即当其他bean对象依赖这个BeanDefinition描述的Bean对象时，这个BeanDefinition描述的Bean对象
	 * 是否作为自动装配的候选对象，如果是，则这个BeanDefinition描述的Bean对象会被注入到依赖它的Bean对象中
	 * Set whether this bean is a candidate for getting autowired into some other bean.
	 * <p>Note that this flag is designed to only affect type-based autowiring.
	 * It does not affect explicit references by name, which will get resolved even
	 * if the specified bean is not marked as an autowire candidate. As a consequence,
	 * autowiring by name will nevertheless inject a bean if the name matches.
	 */
	void setAutowireCandidate(boolean autowireCandidate);

	/**
	 * 判断这个BeanDefinition所描述的Bean对象是否作为自动装配的候选对象
	 * Return whether this bean is a candidate for getting autowired into some other bean.
	 */
	boolean isAutowireCandidate();

	/**
	 * 设置这个BeanDefinition描述的Bean对象为主对象，比如某个接口有两个实现类，可以指定其中一个为主对象，
	 * 当其他对象中需要注入该接口的实现类时，标注的对象是被标注为Primary主对象的Bean对象
	 * Set whether this bean is a primary autowire candidate.
	 * <p>If this value is {@code true} for exactly one bean among multiple
	 * matching candidates, it will serve as a tie-breaker.
	 */
	void setPrimary(boolean primary);

	/**
	 * 判断这个BeanDefinition描述的Bean对象为主对象
	 * Return whether this bean is a primary autowire candidate.
	 */
	boolean isPrimary();

	/**
	 * 设置产生这个BeanDefinition所描述的Bean对象的FactoryBean的名字
	 * Specify the factory bean to use, if any.
	 * This the name of the bean to call the specified factory method on.
	 * @see #setFactoryMethodName
	 */
	void setFactoryBeanName(@Nullable String factoryBeanName);

	/**
	 * 获取这个BeanDefinition描述的Bean对象的FactoryBean的名字
	 * Return the factory bean name, if any.
	 */
	@Nullable
	String getFactoryBeanName();

	/**
	 * 设置FactoryMethod，如果指定了FactoryMethod，则最终在Spring容器中，该bean对象是这个指定的
	 * FactoryMethod生成返回的对象
	 * Specify a factory method, if any. This method will be invoked with
	 * constructor arguments, or with no arguments if none are specified.
	 * The method will be invoked on the specified factory bean, if any,
	 * or otherwise as a static method on the local bean class.
	 * @see #setFactoryBeanName
	 * @see #setBeanClassName
	 */
	void setFactoryMethodName(@Nullable String factoryMethodName);

	/** 返回FactoryMethod
	 * 比如在一个bean中，通过@Bean注解标注的方法声明了其他bean对象。
	 * 那么这个方法即为可以生成bean对象的FactoryMethod工厂方法。
	 * 通过getFactoryMethodName()方法获取
	 * Return a factory method, if any.
	 */
	@Nullable
	String getFactoryMethodName();

	/**
	 * 获取这个BeanDefinition所描述的类对象的构造方法的参数值，实例化时会根据这个信息去调用对应的构造方法实例化Bean对象
	 * Return the constructor argument values for this bean.
	 * <p>The returned instance can be modified during bean factory post-processing.
	 * @return the ConstructorArgumentValues object (never {@code null})
	 */
	ConstructorArgumentValues getConstructorArgumentValues();

	/**
	 * 判断构造方法有没有传值
	 * Return if there are constructor argument values defined for this bean.
	 * @since 5.0.2
	 */
	default boolean hasConstructorArgumentValues() {
		return !getConstructorArgumentValues().isEmpty();
	}

	/**
	 * 获取这个BeanDefinition所描述的Bean对象中的属性信息
	 * Return the property values to be applied to a new instance of the bean.
	 * <p>The returned instance can be modified during bean factory post-processing.
	 * @return the MutablePropertyValues object (never {@code null})
	 */
	MutablePropertyValues getPropertyValues();

	/**
	 * 判断这个BeanDefinition所描述的Bean对象中是否有属性信息
	 * Return if there are property values values defined for this bean.
	 * @since 5.0.2
	 */
	default boolean hasPropertyValues() {
		return !getPropertyValues().isEmpty();
	}


	// Read-only attributes

	/**
	 * 判断这个BeanDefinition所描述的Bean对象是否是单例的
	 * Return whether this a <b>Singleton</b>, with a single, shared instance
	 * returned on all calls.
	 * @see #SCOPE_SINGLETON
	 */
	boolean isSingleton();

	/**
	 * 判断这个BeanDefinition所描述的Bean对象是否是原型的
	 * Return whether this a <b>Prototype</b>, with an independent instance
	 * returned for each call.
	 * @since 3.0
	 * @see #SCOPE_PROTOTYPE
	 */
	boolean isPrototype();

	/**
	 * 判断这个BeanDefinition所描述的Bean对象是否是抽象类的
	 * 抽象类不能被实例化，这个属性的作用是说明当前的这个BeanDefinition是处理通用逻辑的抽象类，
	 * 是可被其他BeanDefinition继承的
	 * 具体的使用例子:RootBeanDefinition和ChildBeanDefinition的继承使用
	 * Return whether this bean is "abstract", that is, not meant to be instantiated.
	 */
	boolean isAbstract();

	/**
	 * Get the role hint for this {@code BeanDefinition}. The role hint
	 * provides the frameworks as well as tools with an indication of
	 * the role and importance of a particular {@code BeanDefinition}.
	 * @see #ROLE_APPLICATION
	 * @see #ROLE_SUPPORT
	 * @see #ROLE_INFRASTRUCTURE
	 */
	int getRole();

	/**
	 * 获取被org.springframework.context.annotation.Description修饰的Bean对象的描述信息
	 * Return a human-readable description of this bean definition.
	 */
	@Nullable
	String getDescription();

	/**
	 * 返回对这个BeanDefinition所描述的对象的文件的描述
	 * Return a description of the resource that this bean definition
	 * came from (for the purpose of showing context in case of errors).
	 */
	@Nullable
	String getResourceDescription();

	/**
	 * Return the originating BeanDefinition, or {@code null} if none.
	 * Allows for retrieving the decorated bean definition, if any.
	 * <p>Note that this method returns the immediate originator. Iterate through the
	 * originator chain to find the original BeanDefinition as defined by the user.
	 */
	@Nullable
	BeanDefinition getOriginatingBeanDefinition();

}
