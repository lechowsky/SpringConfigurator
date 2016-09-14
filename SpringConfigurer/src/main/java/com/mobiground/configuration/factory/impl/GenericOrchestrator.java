package com.mobiground.configuration.factory.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.ApplicationContext;

import com.mobiground.configuration.factory.BeanInjector;
import com.mobiground.configuration.factory.ConfigurationClassResolver;
import com.mobiground.configuration.factory.Orchestrator;
import com.mobiground.configuration.factory.PropertyResolver;
import com.mobiground.configuration.factory.PropertySetter;
import com.mobiground.configuration.factory.bean.MobiBeanDefinition;
import com.mobiground.ecap.configuration.Configuration;
import com.mobiground.ecap.util.string.UString;

/**
 * The Class GenericOrchestrator.
 *
 * @param <CONFIG_CLASS> the generic type
 */
public class GenericOrchestrator<CONFIG_CLASS extends Configuration> implements Orchestrator {

	/** The property setter. */
	private PropertySetter propertySetter;

	/** The property resolver. */
	private PropertyResolver propertyResolver;

	/** The bean injector. */
	private BeanInjector beanInjector;
	/** The configuration class resolver. */
	private ConfigurationClassResolver configurationClassResolver;

	/** The bean definition. */
	private List<MobiBeanDefinition> beanDefinition;

	/** The config class instances. */
	private List<CONFIG_CLASS> configClassInstances;

	/** The bean names. */
	private List<String> beanNames;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiground.configuration.factory.Orchestrator#orchestrate()
	 */
	@Override
	public List<MobiBeanDefinition> orchestrate() {
		setUpBeans();
		Iterator<Class<? extends Configuration>> configClassessIt = searchConfigurationClasses();
		while (configClassessIt.hasNext()) {
			Class<CONFIG_CLASS> clazz = (Class<CONFIG_CLASS>) configClassessIt.next();
			Map<String, Properties> propertiesMapClass = propertyResolver.getConfigProperties(clazz);
			try {
				configWithProperties(clazz, propertiesMapClass);
				defaultProperties(clazz);
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO mirar excepciones aquí
				e.printStackTrace();
			}

		}
		setBeanDefinition();
		setBeanNames();
		return this.beanDefinition;
	}

	/**
	 * Default properties.
	 *
	 * @param clazz the clazz
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	private void defaultProperties(Class<CONFIG_CLASS> clazz) throws InstantiationException, IllegalAccessException {
		CONFIG_CLASS instanceClass = null;
		if (instanceClass == null) {
			instanceClass = clazz.newInstance();
			instanceClass.setConfigName(UString.EMPTY);
			configClassInstances.add(instanceClass);
		}
	}

	/**
	 * Config with properties.
	 *
	 * @param clazz the clazz
	 * @param propertiesMapClass the properties map class
	 * @return the config class
	 */
	private CONFIG_CLASS configWithProperties(Class<CONFIG_CLASS> clazz, Map<String, Properties> propertiesMapClass) {
		CONFIG_CLASS instanceClass = null;
		for (String key : propertiesMapClass.keySet()) {
			instanceClass = propertySetter.fillProperties(propertiesMapClass.get(key), clazz);
			instanceClass.setConfigName(UString.substringAfterLast(key, UString.DOT));
			configClassInstances.add(instanceClass);
		}
		return instanceClass;
	}

	/**
	 * Sets the bean definition.
	 */
	private void setBeanDefinition() {
		this.beanDefinition = new ArrayList<MobiBeanDefinition>();
		for (CONFIG_CLASS classInstance : configClassInstances) {
			beanDefinition.addAll(beanInjector.getBeanDefinition(classInstance));
		}
	}

	/**
	 * Sets the bean names.
	 */
	private void setBeanNames() {
		this.beanNames = new ArrayList<String>();
		for (MobiBeanDefinition mobiSpringBean : beanDefinition) {
			beanNames.add(mobiSpringBean.getBeanName());
		}
	}

	/**
	 * Search configuration classes.
	 *
	 * @return the iterator< class<? extends configuration>>
	 */
	private Iterator<Class<? extends Configuration>> searchConfigurationClasses() {
		Set<Class<? extends Configuration>> configClasses = configurationClassResolver.getDefaultConfigurationClasses();
		Iterator<Class<? extends Configuration>> configClassessIt = configClasses.iterator();
		configClassInstances = new ArrayList<CONFIG_CLASS>();
		return configClassessIt;
	}

	/**
	 * Sets the up beans.
	 */
	private void setUpBeans() {
		if (isAnyNotInizialized()) {
			configurationClassResolver = new ReflectionsConfigurationClassResolver();
			propertyResolver = new SpringPropertyResolver();
			propertySetter = new JacksonPorpertySetter();
			beanInjector = new SpringBeanInjector();
		}
	}

	/**
	 * Checks if is any not inizialized.
	 *
	 * @return true, if is any not inizialized
	 */
	private boolean isAnyNotInizialized() {
		return configurationClassResolver == null || propertyResolver == null || propertySetter == null
				|| beanInjector == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiground.configuration.factory.Orchestrator#getMobiBeanDefinition()
	 */
	@Override
	public List<MobiBeanDefinition> getMobiBeanDefinition() {
		return this.beanDefinition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiground.configuration.factory.Orchestrator#getBeanNames()
	 */
	@Override
	public List<String> getBeanNames() {
		return this.beanNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiground.configuration.factory.Orchestrator#addDependencies(com.mobiground.configuration.factory.bean.
	 * MobiBeanDefinition, org.springframework.context.ApplicationContext)
	 */
	@Override
	public MobiBeanDefinition addDependencies(MobiBeanDefinition beanDefinition, ApplicationContext context) {
		BeanInjector beanInjector = new SpringBeanInjector();
		return beanInjector.addDependencies(beanDefinition, context);
	}

}
