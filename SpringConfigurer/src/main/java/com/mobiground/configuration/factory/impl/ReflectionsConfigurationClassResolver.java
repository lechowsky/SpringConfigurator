package com.mobiground.configuration.factory.impl;

import java.util.Set;

import org.reflections.Reflections;

import com.mobiground.configuration.factory.ConfigurationClassResolver;
import com.mobiground.ecap.configuration.Configuration;

/**
 * The Class ReflectionsConfigurationClassResolver.
 */
public class ReflectionsConfigurationClassResolver implements ConfigurationClassResolver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiground.configuration.factory.ConfigurationClassResolver#getDefaultConfigurationClasses()
	 */
	public Set<Class<? extends Configuration>> getDefaultConfigurationClasses() {
		return this.getConfigurationClassesByPath("com.mobiground");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mobiground.configuration.factory.ConfigurationClassResolver#getConfigurationClassesByPath(java.lang.String)
	 */
	@Override
	public Set<Class<? extends Configuration>> getConfigurationClassesByPath(String rootSearchPath) {
		Reflections reflections = new Reflections(rootSearchPath);
		return reflections.getSubTypesOf(Configuration.class);
	}

}
