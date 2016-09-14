package com.mobiground.configuration.factory.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import com.mobiground.configuration.factory.bean.Dependency;
import com.mobiground.configuration.factory.bean.MobiBeanDefinition;
import com.mobiground.configuration.factory.configuration.ObOneATest;
import com.mobiground.configuration.factory.configuration.ObOneBTest;
import com.mobiground.configuration.factory.configuration.confOne;
import com.mobiground.ecap.configuration.Configuration;
import com.mobiground.ecap.util.string.UString;

@RunWith(MockitoJUnitRunner.class)
public class SpringBeanInjectorTest {

	@InjectMocks
	private SpringBeanInjector beanInjector;
	@Mock
	private ApplicationContext context;
	private ApplicationContext genericContext = new GenericApplicationContext();
	private Map<String, Object> contextBeanMap;

	@Before
	public void setUp() {
		initMocks(this);
		contextBeanMap = new HashMap<String, Object>();
		contextBeanMap.put(UString.uncapitalize(ObOneATest.class.getSimpleName()), new ObOneATest());
	}

	@Test
	public void shouldGetBeanDefinitonTest() {

		Configuration configurationInstance = new confOne();
		List<MobiBeanDefinition> beans = beanInjector.getBeanDefinition(configurationInstance);

		assertEquals(2, beans.size());

		assertTrue(UString.uncapitalize(ObOneATest.class.getSimpleName()).equals(beans.get(1).getBeanName())
				|| UString.uncapitalize(ObOneBTest.class.getSimpleName()).equals(beans.get(1).getBeanName()));
	}

	@Test
	public void shouldAddDependencyTest() {
		Configuration configurationInstance = new confOne();
		MobiBeanDefinition beanDefinition = createMobiBeanDefinition(configurationInstance, creteDependency());
		when(context.getBeansOfType(beanDefinition.getDependencies().get(0).getClazz())).thenReturn(contextBeanMap);
		MobiBeanDefinition bean = beanInjector.addDependencies(beanDefinition, context);

		assertEquals(beanDefinition.getDependencies().get(0).getClazz(), bean.getCdiBeans().get(0).getClass());
	}

	@Test
	public void shouldAddDependencyWithoutDependencyTest() {
		Configuration configurationInstance = new confOne();
		MobiBeanDefinition beanDefinition = createMobiBeanDefinition(configurationInstance);
		MobiBeanDefinition bean = beanInjector.addDependencies(beanDefinition, context);

		assertTrue(beanDefinition.getDependencies().size() == 0);
	}

	@Test
	public void shouldAddDependencyByNameTest() {
		Configuration configurationInstance = new confOne();
		MobiBeanDefinition beanDefinition =
				createMobiBeanDefinition(configurationInstance, new Dependency("context", genericContext.getClass()));
		when(context.containsBean(anyString())).thenReturn(Boolean.TRUE);
		when(context.getBeansOfType(beanDefinition.getDependencies().get(0).getClazz())).thenReturn(contextBeanMap);
		MobiBeanDefinition bean = beanInjector.addDependencies(beanDefinition, context);

		assertEquals(context.getClass(), bean.getCdiBeans().get(0).getClass());
	}

	@Test
	public void shouldAddDependenciesTest() {
		Configuration configurationInstance = new confOne();
		MobiBeanDefinition beanDefinition =
				createMobiBeanDefinition(configurationInstance, creteDependency(), creteDependency());
		when(context.getBeansOfType(beanDefinition.getDependencies().get(0).getClazz())).thenReturn(contextBeanMap);
		contextBeanMap.put(UString.uncapitalize(UString.join("test", ObOneATest.class.getSimpleName())),
			new ObOneATest());
		MobiBeanDefinition bean = beanInjector.addDependencies(beanDefinition, context);

		assertEquals(beanDefinition.getDependencies().get(0).getClazz(), bean.getCdiBeans().get(0).getClass());
		assertEquals(beanDefinition.getDependencies().get(1).getClazz(), bean.getCdiBeans().get(1).getClass());
	}

	private MobiBeanDefinition createMobiBeanDefinition(Configuration configurationInstance, Dependency... dependency) {
		MobiBeanDefinition beanDefinition = new MobiBeanDefinition(ObOneBTest.class, configurationInstance);
		for (int i = 0; i < dependency.length; i++) {
			beanDefinition.addDependency(dependency[i]);
		}
		return beanDefinition;
	}

	private Dependency creteDependency() {
		Dependency dependency = new Dependency();
		dependency.setBeanName(ObOneATest.class.getSimpleName());
		dependency.setClazz(ObOneATest.class);
		return dependency;
	}

}
