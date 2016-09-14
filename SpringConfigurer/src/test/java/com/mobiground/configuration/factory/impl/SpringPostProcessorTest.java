package com.mobiground.configuration.factory.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import com.mobiground.configuration.factory.Orchestrator;
import com.mobiground.configuration.factory.bean.MobiBeanDefinition;
import com.mobiground.configuration.factory.configuration.ConfTwo;
import com.mobiground.configuration.factory.configuration.ObTwoATest;
import com.mobiground.ecap.configuration.Configuration;

@RunWith(MockitoJUnitRunner.class)
public class SpringPostProcessorTest {

	@InjectMocks
	private SpringPostProcessor postProcessor;

	@Mock
	private ApplicationContext context;

	@Mock
	private Orchestrator orchestrator;

	private Method method = ConfTwo.class.getDeclaredMethods()[0];
	private Configuration configurationInstance;

	@Before
	public void setup() {
		initMocks(this);
		configurationInstance = new ConfTwo();

	}

	@Test
	public void postProcessorBeforeInizialitingBeanTest() throws IllegalAccessException, IllegalArgumentException,
														  InvocationTargetException {
		ObTwoATest bean = new ObTwoATest();
		MobiBeanDefinition beanDefinition = createBeanDefinitio();
		List<MobiBeanDefinition> beanDefinitions = new ArrayList<MobiBeanDefinition>();
		beanDefinitions.add(beanDefinition);
		when(orchestrator.getMobiBeanDefinition()).thenReturn(beanDefinitions);
		List<String> beanNames = new ArrayList<String>();
		beanNames.add(ObTwoATest.class.getSimpleName());
		when(orchestrator.getBeanNames()).thenReturn(beanNames);
		when(orchestrator.addDependencies(beanDefinition, context)).thenReturn(beanDefinition);

		Object result = postProcessor.postProcessBeforeInitialization(bean, ObTwoATest.class.getSimpleName());
		assertEquals(bean.getClass(), result.getClass());
	}

	private MobiBeanDefinition createBeanDefinitio() {
		MobiBeanDefinition beanDefinition = new MobiBeanDefinition(ObTwoATest.class, configurationInstance);
		beanDefinition.setBeanName(ObTwoATest.class.getSimpleName());
		beanDefinition.setFactoryMethod(method);
		return beanDefinition;
	}
}
