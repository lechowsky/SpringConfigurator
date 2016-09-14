package com.mobiground.configuration.factory;

import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.mobiground.configuration.factory.bean.MobiBeanDefinition;

// TODO: Auto-generated Javadoc
/**
 * The Interface BeanRegisterDefinition.
 */
public interface BeanRegisterDefinition {

	/**
	 * Register.
	 *
	 * @param registry the registry
	 * @param beanDefinitions the bean definitions
	 */
	void register(BeanDefinitionRegistry registry, List<MobiBeanDefinition> beanDefinitions);
}
