package com.mobiground.configuration.factory.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.reflections.Reflections;

import com.mobiground.ecap.configuration.Configuration;
import com.mobiground.ecap.util.string.UString;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Reflections.class, SpringPropertyResolver.class, Properties.class, ClassLoader.class })
public class SpringPropertyResolverTest {

	@InjectMocks
	private SpringPropertyResolver propertyResolver;
	@Mock
	private Reflections reflectionMock;
	@Mock
	private Properties properties;

	@Before
	public void setUp() {
		initMocks(this);

	}

	@Test
	public void shouldReturnPropertiesTest() throws Exception {
		commonMocks();

		Map<String, Properties> result = propertyResolver.getConfigProperties();

		assertEquals(2, result.keySet().size());

	}

	@Test
	public void shouldReturnPropertiesPTest() throws Exception {
		commonMocks();

		Map<String, Properties> result = propertyResolver.getConfigProperties(p.class);

		assertEquals(1, result.keySet().size());

	}

	private void commonMocks() throws Exception {
		PowerMockito.whenNew(Reflections.class).withAnyArguments().thenReturn(reflectionMock);
		Set<String> propertySet = new HashSet<String>();
		propertySet.add(UString.join("p", SpringPropertyResolver.CONFIGURATION_PROPERTIES_SUFFIX));
		propertySet.add(UString.join("b", SpringPropertyResolver.CONFIGURATION_PROPERTIES_SUFFIX));
		PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(properties);
		when(reflectionMock.getResources(any(Pattern.class))).thenReturn(propertySet);
		PowerMockito.mockStatic(ClassLoader.class);
	}

	class StreamTest extends InputStream {

		@Override
		public int read() throws IOException {
			return 0;
		}

	}

	class p extends Configuration {

	}
}
