package com.mobiground.configuration.factory;

import org.springframework.context.ConfigurableApplicationContext;

import com.mobiground.ecap.configuration.Configuration;

@Deprecated
public interface BeanAutowirer {

	<CONFIG_CLASS extends Configuration> CONFIG_CLASS injectDependencies(ConfigurableApplicationContext context,
			CONFIG_CLASS configurationClass);

}
