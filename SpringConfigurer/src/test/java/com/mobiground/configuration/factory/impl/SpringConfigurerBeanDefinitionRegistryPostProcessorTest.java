package com.mobiground.configuration.factory.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.mobiground.configuration.factory.bean.MobiBeanDefinition;
import com.mobiground.ecap.configuration.Configuration;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SpringConfigurerBeanDefinitionRegistryPostProcessor.class, GenericOrchestrator.class })
public class SpringConfigurerBeanDefinitionRegistryPostProcessorTest {

	@InjectMocks
	private SpringConfigurerBeanDefinitionRegistryPostProcessor postProcessor;

	@Mock
	private GenericOrchestrator orchestrator;
	@Mock
	private ConfigurableListableBeanFactory beanFactory;
	@Mock
	private BeanDefinitionRegistry registry;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void shouldPostProcessBeanFactoryTest() {
		postProcessor.postProcessBeanFactory(beanFactory);
	}

	@Test
	public void shouldPostProcessBeanDefinitionRegistryTest() throws Exception {
		List<MobiBeanDefinition> beans = new ArrayList<MobiBeanDefinition>();
		MobiBeanDefinition bean = new MobiBeanDefinition(Bean.class, new Conf());
		beans.add(bean);
		PowerMockito.whenNew(GenericOrchestrator.class).withNoArguments().thenReturn(orchestrator);
		when(orchestrator.orchestrate()).thenReturn(beans);
		doNothing().when(registry).registerBeanDefinition(bean.getBeanName(), bean);

		postProcessor.postProcessBeanDefinitionRegistry(registry);
	}

	class Bean {

	}

	class Conf extends Configuration {

	}
}
