package com.mobiground.configuration.factory.impl;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.mobiground.ecap.configuration.Configuration;

@RunWith(MockitoJUnitRunner.class)
public class ReflectionsConfigurationClassResolverTest {

	private ReflectionsConfigurationClassResolver configurationClassResolver;

	@Before
	public void initTestBean() {
		configurationClassResolver = new ReflectionsConfigurationClassResolver();
	}

	@Test
	public void shouldFindClassesTest() {
		Set<Class<? extends Configuration>> classes = configurationClassResolver
			.getConfigurationClassesByPath("com.mobiground.configuration.factory.configuration");

		assertEquals(2, classes.size());

	}
}
