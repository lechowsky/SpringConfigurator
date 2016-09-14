package com.mobiground.configuration.factory;

import java.util.Map;
import java.util.Properties;

import com.mobiground.ecap.configuration.Configuration;

/**
 * The Interface PropertyResolver.
 */
public interface PropertyResolver {

	/**
	 * Gets the config properties, based on a set of conventions: <br>
	 * -The properties file name has to finish in <strong>".configuration.properties"</strong> <br>
	 * -The properties file has to be placed in classpath of the application
	 * 
	 * By convention, the name of property file has to begin by the first piece of the name of configuration class.
	 * Example:
	 * 
	 * Given this class name: <br>
	 * ExampleConfiguration.java <br>
	 * The configuration properties file will be:<br>
	 * Example.configuration.properties
	 *
	 * @return the config properties
	 */
	Map<String, Properties> getConfigProperties();

	/**
	 * Gets the config properties for the given configuration class following the pattern of
	 * {@link PropertyResolver#getConfigProperties()} method.
	 *
	 * @param <CONFIG_CLASS> the generic type
	 * @param configurationClass the configuration class
	 * @return the config properties
	 */
	<CONFIG_CLASS extends Configuration> Map<String, Properties> getConfigProperties(
			Class<CONFIG_CLASS> configurationClass);
}
