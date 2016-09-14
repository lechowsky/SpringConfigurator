package com.mobiground.configuration.factory;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.mobiground.configuration.factory.bean.MobiBeanDefinition;

// TODO: Auto-generated Javadoc
/**
 * The Interface Orchestrator.
 */
public interface Orchestrator {

	/**
	 * Orchestrate all the logic that read properties and configuration classes to generate bean definition os spring.
	 *
	 * @return the list
	 */
	List<MobiBeanDefinition> orchestrate();

	/**
	 * Gets the mobi bean definition generate by orchestrate method.
	 *
	 * @return the mobi bean definition
	 */
	List<MobiBeanDefinition> getMobiBeanDefinition();

	/**
	 * Gets the bean candidate names of bean to be injected.
	 *
	 * @return the bean names
	 */
	List<String> getBeanNames();

	/**
	 * Adds the dependencies. <br>
	 * See {@link BeanInjector#addDependencies(MobiBeanDefinition, ApplicationContext)} BeanInjec.
	 *
	 * @param beanDefinition the bean definition
	 * @param context the context
	 * @return the mobi bean definition
	 */
	MobiBeanDefinition addDependencies(MobiBeanDefinition beanDefinition, ApplicationContext context);
}
