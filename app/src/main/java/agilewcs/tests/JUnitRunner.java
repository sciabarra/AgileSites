package agilewcs.tests;

import wcs.java.util.TestRunnerElement;

public class JUnitRunner extends TestRunnerElement {

	public boolean isProduction() {
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] tests() {
		return new Class[] { AllTests.class, ElementTest.class };
	}

}
