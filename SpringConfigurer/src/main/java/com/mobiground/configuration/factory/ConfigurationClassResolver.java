package com.mobiground.configuration.factory;

import java.util.Set;

import com.mobiground.ecap.configuration.Configuration;

/**
 * The Interface ConfigurationClassResolver.
 */
public interface ConfigurationClassResolver {

	/**
	 * Gets the default configuration classes, using "com.mobiground" as the root path for scanning classpath classes.
	 *
	 * @return the default configuration classes
	 */
	Set<Class<? extends Configuration>> getDefaultConfigurationClasses();

	/**
	 * Gets the configuration classes by path.
	 *
	 * @param rootSearchPath the root search path
	 * @return the configuration classes by path
	 */
	Set<Class<? extends Configuration>> getConfigurationClassesByPath(String rootSearchPath);
}
