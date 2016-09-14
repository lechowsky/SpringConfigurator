package com.mobiground.configuration.factory.configuration;

import com.mobiground.ecap.configuration.Configuration;

public class ConfTwo extends Configuration {

	private String myProperty;

	public ObTwoATest getObTwoATest() {
		return new ObTwoATest(myProperty);
	}
}
