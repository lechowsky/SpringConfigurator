package com.mobiground.configuration.factory.impl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.mobiground.configuration.factory.BeanAutowirer;
import com.mobiground.ecap.configuration.Configuration;

@Deprecated
public class SpringBeanAutowirer implements BeanAutowirer {

	@Override
	public <CONFIG_CLASS extends Configuration> CONFIG_CLASS injectDependencies(ConfigurableApplicationContext context,
			CONFIG_CLASS configurationClass) {
		Iterator<Field> dependencyIterator = getFieldToBeInjected(configurationClass);
		while (dependencyIterator.hasNext()) {
			Field dependency = (Field) dependencyIterator.next();
			fillDependency(context, configurationClass, dependency);
		}
		return null;
	}

	private <CONFIG_CLASS> Iterator<Field> getFieldToBeInjected(CONFIG_CLASS configurationInstance) {
		Field[] dependencies = configurationInstance.getClass().getDeclaredFields();
		List<Field> dependenciesList = Arrays.asList(dependencies);
		Iterator<Field> dependencyIterator = dependenciesList.iterator();
		return dependencyIterator;
	}

	private <CONFIG_CLASS> void fillDependency(ConfigurableApplicationContext context,
			CONFIG_CLASS configurationInstance, Field dependency) {
		Object bean = context;
		if (!dependency.getType().isAssignableFrom(ApplicationContext.class)) {
			bean = context.getBean(dependency.getType());
		}
		setField(dependency, bean, configurationInstance);

	}

	private <CONFIG_CLASS> Field setField(Field beanField, Object bean, CONFIG_CLASS configurationInstance) {
		Field field = beanField;
		try {
			field.setAccessible(Boolean.TRUE);
			field.set(configurationInstance, bean);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			// TODO: implementar exception
			e.printStackTrace();
		}
		return field;
	}

}
