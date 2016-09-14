package com.mobiground.configuration.factory;

import java.util.Properties;

import com.mobiground.ecap.configuration.Configuration;

/**
 * The Interface PropertySetter.
 */
public interface PropertySetter {

	/**
	 * Fill properties.
	 *
	 * @param <CONFIG_CLASS> the generic type
	 * @param properties the properties
	 * @param configurationInstance the configuration instance
	 * @return the config class
	 */
	<CONFIG_CLASS extends Configuration> CONFIG_CLASS fillProperties(Properties properties,
			Class<CONFIG_CLASS> configurationInstance);

}
