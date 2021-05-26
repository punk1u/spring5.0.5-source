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

package org.springframework.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于表示要导入的一个或多个{@link Configuration @Configuration}类。
 * Indicates one or more {@link Configuration @Configuration} classes to import.
 *
 * 提供与Spring Xml中的{@code<import/>}元素等效的功能。允许导入{@code @Configuration}类、{@link ImportSelector}和{@link ImportBeanDefinitionRegistrar}实现，
 * 以及常规组件类（从4.2开始；类似于{@link AnnotationConfigApplicationContext#register}）。
 *
 * <p>Provides functionality equivalent to the {@code <import/>} element in Spring XML.
 * Allows for importing {@code @Configuration} classes, {@link ImportSelector} and
 * {@link ImportBeanDefinitionRegistrar} implementations, as well as regular component
 * classes (as of 4.2; analogous to {@link AnnotationConfigApplicationContext#register}).
 *
 * 应使用{@link org.springframework.beans.factory.annotation.Autowired@Autowired}注入来访问导入的{@code @Configuration}类中声明的{@code @Bean}定义。
 * bean本身可以自动连接，或者声明bean的配置类实例可以自动连接。后一种方法允许在{@code @Configuration}类方法之间进行显式的、IDE友好的导航。
 *
 * <p>{@code @Bean} definitions declared in imported {@code @Configuration} classes should be
 * accessed by using {@link org.springframework.beans.factory.annotation.Autowired @Autowired}
 * injection. Either the bean itself can be autowired, or the configuration class instance
 * declaring the bean can be autowired. The latter approach allows for explicit, IDE-friendly
 * navigation between {@code @Configuration} class methods.
 *
 * 可以在类级别声明或作为元注解声明。
 * <p>May be declared at the class level or as a meta-annotation.
 *
 * 如果需要导入XML或其他非{@code @Configuration}bean定义资源，请改用{@link ImportResource@ImportResource}注释。
 * <p>If XML or other non-{@code @Configuration} bean definition resources need to be
 * imported, use the {@link ImportResource @ImportResource} annotation instead.
 *
 * 在@Import注解的参数中可以填写类名，例如@Import(Abc.class)，根据类Abc的不同类型，spring容器有以下四种处理方式：
 *
 * 1. 如果Abc类实现了ImportSelector接口，spring容器就会实例化Abc类，并且调用其selectImports方法；
 * 2. DeferredImportSelector是ImportSelector的子类，如果Abc类实现了DeferredImportSelector接口，spring容器就会实例化Abc类，并且调用其selectImports方法，
 * 	  和ImportSelector的实例不同的是，DeferredImportSelector的实例的selectImports方法调用时机晚于ImportSelector的实例，要等到@Configuration注解中相关的业务全部都处理完了才会调用（具体逻辑在ConfigurationClassParser.processDeferredImportSelectors方法中），想了解更多DeferredImportSelector和ImportSelector的区别，请参考《ImportSelector与DeferredImportSelector的区别（spring4） 》；
 * 3. 如果Abc类实现了ImportBeanDefinitionRegistrar接口，spring容器就会实例化Abc类，并且调用其registerBeanDefinitions方法；
 * 4. 如果Abc没有实现ImportSelector、DeferredImportSelector、ImportBeanDefinitionRegistrar等其中的任何一个，spring容器就会实例化Abc类，官方说明在这里:
 * https://docs.spring.io/spring-framework/docs/4.3.19.RELEASE/spring-framework-reference/htmlsingle/#beans-java-using-import
 *
 * Import注解使用场景：当项目中需要引用一些第三方的依赖，依赖中的类的包名和自己应用程序中的包名不一样，无法直接被扫描到的时候，可以使用@Import注解引入相应的依赖包中的类
 *
 * 使用示例：
 * public class TestA {
 *
 *     public void fun(String str) {
 *         System.out.println(str);
 *     }
 *
 *     public void printName() {
 *         System.out.println("类名 ：" + Thread.currentThread().getStackTrace()[1].getClassName());
 *     }
 * }
 *
 * @Configuration
 * public class TestB {
 *     public void fun(String str) {
 *         System.out.println(str);
 *     }
 *
 *     public void printName() {
 *         System.out.println("类名 ：" + Thread.currentThread().getStackTrace()[1].getClassName());
 *     }
 * }
 *
 * public class TestC {
 *     public void fun(String str) {
 *         System.out.println(str);
 *     }
 *
 *     public void printName() {
 *         System.out.println("类名 ：" + Thread.currentThread().getStackTrace()[1].getClassName());
 *     }
 * }
 *
 * public class SelfImportSelector implements ImportSelector {
 *     @Override
 *     public String[] selectImports(AnnotationMetadata importingClassMetadata) {
 *         return new String[]{"com.test.importdemo.TestC"};
 *     }
 * }
 *
 * public class TestD {
 *     public void fun(String str) {
 *         System.out.println(str);
 *     }
 *
 *     public void printName() {
 *         System.out.println("类名 ：" + Thread.currentThread().getStackTrace()[1].getClassName());
 *     }
 * }
 *
 * public class SelfImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
 *     @Override
 *     public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
 *         RootBeanDefinition root = new RootBeanDefinition(TestD.class);
 *         registry.registerBeanDefinition("testD", root);
 *     }
 * }
 *
 * @Import({TestA.class,TestB.class,SelfImportSelector.class,
 *         SelfImportBeanDefinitionRegistrar.class})
 * @Configuration
 * public class ImportConfig {
 * }
 *
 * 以上几种方式都能把类加载到TestA、TestB、TestC、TestD这几个类（不管有没有添加@Import注解）加载到Spring容器中
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @since 3.0
 * @see Configuration
 * @see ImportSelector
 * @see ImportResource
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Import {

	/**
	 * {@link Configuration}, {@link ImportSelector}, {@link ImportBeanDefinitionRegistrar}
	 * or regular component classes to import.
	 */
	Class<?>[] value();

}
