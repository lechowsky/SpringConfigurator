package com.mobiground.configuration.factory.impl;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.mobiground.ecap.configuration.Configuration;

@RunWith(MockitoJUnitRunner.class)
public class GenericOrchestratorTest {

	@InjectMocks
	private GenericOrchestrator<Configuration> genericOrchestrator = new GenericOrchestrator<Configuration>();

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void test() {
		genericOrchestrator.orchestrate();
	}
}
