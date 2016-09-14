package com.mobiground.configuration.factory.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import com.mobiground.configuration.factory.Orchestrator;
import com.mobiground.configuration.factory.bean.MobiBeanDefinition;
import com.mobiground.ecap.configuration.Configuration;
import com.mobiground.ecap.util.string.UString;

/**
 * The Class SpringConfigurerBeanDefinitionRegistryPostProcessor.
 */
// @Component
public class SpringConfigurerBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	/** The orchestrator. */
	private Orchestrator orchestrator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.
	 * beans.factory.config.ConfigurableListableBeanFactory)
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		beanFactory.registerSingleton(UString.uncapitalize(orchestrator.getClass().getSimpleName()), orchestrator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry(
	 * org.springframework.beans.factory.support.BeanDefinitionRegistry)
	 */
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		orchestrator = new GenericOrchestrator<Configuration>();
		List<MobiBeanDefinition> beanDefinitions = orchestrator.orchestrate();
		register(registry, beanDefinitions);
	}

	/**
	 * This method get all beans and adds them to BeanDefinitionRegistry .
	 *
	 * @param registry the registry
	 * @param beanDefinitions the bean definitions
	 */
	private void register(BeanDefinitionRegistry registry, List<MobiBeanDefinition> beanDefinitions) {
		Iterator<MobiBeanDefinition> iterator = beanDefinitions.iterator();
		while (iterator.hasNext()) {
			MobiBeanDefinition mobiBeanDefinition = (MobiBeanDefinition) iterator.next();
			registry.registerBeanDefinition(mobiBeanDefinition.getBeanName(), mobiBeanDefinition);
		}

	}

}
