package com.mobiground.configuration.factory.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import com.mobiground.configuration.factory.PropertyResolver;
import com.mobiground.ecap.configuration.Configuration;
import com.mobiground.ecap.util.file.UFile;
import com.mobiground.ecap.util.string.UString;

/**
 * The Class SpringPropertyResolver.
 */
public class SpringPropertyResolver implements PropertyResolver {

	/** The Constant CONFIGURATION_PROPERTIES_SUFFIX. */
	protected static final String CONFIGURATION_PROPERTIES_SUFFIX = ".configuration.properties";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiground.configuration.factory.PropertyResolver#getConfigProperties()
	 */
	@Override
	public Map<String, Properties> getConfigProperties() {

		Reflections reflections = new Reflections("", new ResourcesScanner());
		Set<String> propertiesPaths =
				reflections.getResources(Pattern.compile(UString.join(".*", CONFIGURATION_PROPERTIES_SUFFIX)));
		return getPropertiesMap(propertiesPaths);
	}

	/**
	 * Gets the properties map.
	 *
	 * @param paths the paths
	 * @return the properties map
	 */
	private Map<String, Properties> getPropertiesMap(final Set<String> paths) {
		Map<String, Properties> propertiesMap = new HashMap<>();
		Iterator<String> pathsIterator = paths.iterator();
		while (pathsIterator.hasNext()) {
			String path = (String) pathsIterator.next();
			propertiesMap
				.put(UString.rigthFromLastIndexOf(UString.replace(path, CONFIGURATION_PROPERTIES_SUFFIX, UString.EMPTY),
					UFile.SYSTEM_SEPARATOR), getProperty(path));
		}

		return propertiesMap;
	}

	/**
	 * Gets the property.
	 *
	 * @param propertyPath the property path
	 * @return the property
	 */
	private Properties getProperty(final String propertyPath) {

		Properties properties = new Properties();
		InputStream stream = ClassLoader.getSystemResourceAsStream(propertyPath);
		try {
			properties.load(stream);
		} catch (IOException e) {
			// TODO generar exception
			e.printStackTrace();
		}
		return properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiground.configuration.factory.PropertyResolver#getConfigProperties(java.lang.Class)
	 */
	@Override
	public <CONFIG_CLASS extends Configuration> Map<String, Properties> getConfigProperties(
			final Class<CONFIG_CLASS> configurationClass) {
		Map<String, Properties> propertiesMap = getConfigProperties();
		Map<String, Properties> propertiesMapClean = new HashMap<String, Properties>();
		for (String key : propertiesMap.keySet()) {
			if (UString.containsIgnoreCase(configurationClass.getSimpleName(), UString.leftFrom(key, UString.DOT))) {
				propertiesMapClean.put(key, propertiesMap.get(key));
			}
		}
		return propertiesMapClean;
	}

}
