package com.mobiground.configuration.factory.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiground.configuration.factory.PropertySetter;
import com.mobiground.ecap.configuration.Configuration;

public class JacksonPorpertySetter implements PropertySetter {

	@Override
	public <CONFIG_CLASS extends Configuration> CONFIG_CLASS fillProperties(Properties properties,
			Class<CONFIG_CLASS> configurationInstance) {
		Map<String, String> mapProperties = PtopertiesToMap(properties);
		ObjectMapper mapper = createObjectMapper();

		return mapper.convertValue(mapProperties, configurationInstance);
	}

	private Map<String, String> PtopertiesToMap(Properties properties) {
		Map<String, String> propertiesMap = new HashMap<String, String>();
		for (String name : properties.stringPropertyNames()) {
			propertiesMap.put(name, properties.getProperty(name));
		}
		return propertiesMap;
	}

	private ObjectMapper createObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);
		return mapper;
	}

}
