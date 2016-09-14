package com.mobiground.configuration.factory.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;

import com.mobiground.configuration.factory.Orchestrator;
import com.mobiground.configuration.factory.bean.MobiBeanDefinition;

// TODO: Auto-generated Javadoc
/**
 * The Class SpringPostProcessor.
 */
// @Component
public class SpringPostProcessor implements BeanPostProcessor, Ordered {

	/** The context. */
	@Autowired
	private ApplicationContext context;

	/** The orchestrator. */
	@Autowired
	private Orchestrator orchestrator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object,
	 * java.lang.String)
	 */
	@Override
	public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
		Object wrappedBean = bean;

		List<MobiBeanDefinition> beanDefinitions = orchestrator.getMobiBeanDefinition();
		if (orchestrator.getBeanNames().contains(beanName)) {
			MobiBeanDefinition beanDefinition = getBeanDefinition(beanName, beanDefinitions);
			beanDefinition = orchestrator.addDependencies(beanDefinition, context);
			wrappedBean = beanDefinition.factoryMethod();
		}
		return wrappedBean;
	}

	/**
	 * Gets the bean definition.
	 *
	 * @param beanName the bean name
	 * @param beanDefinitions the bean definitions
	 * @return the bean definition
	 */
	private MobiBeanDefinition getBeanDefinition(final String beanName,
			final List<MobiBeanDefinition> beanDefinitions) {
		MobiBeanDefinition mobiBeanDefinition = null;
		for (Iterator<MobiBeanDefinition> iterator = beanDefinitions.iterator(); iterator.hasNext()
				&& mobiBeanDefinition == null;) {
			MobiBeanDefinition element = (MobiBeanDefinition) iterator.next();
			if (element.getBeanName().equalsIgnoreCase(beanName)) {
				mobiBeanDefinition = element;
			}
		}
		return mobiBeanDefinition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object,
	 * java.lang.String)
	 */
	@Override
	public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
		return bean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.core.Ordered#getOrder()
	 */
	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
