package com.mobiground.configuration.factory.configuration;

import com.mobiground.ecap.configuration.Configuration;

public class confOne extends Configuration {

	public ObOneATest getObjectOne() {
		return new ObOneATest();
	}

	public ObOneBTest getObOneB(ObOneATest oneATest) {
		return new ObOneBTest(oneATest);
	}
}
