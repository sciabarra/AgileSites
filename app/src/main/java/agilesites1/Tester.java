package agilesites1;

import agilesites1.tests.AllTests;
import agilesites1.tests.AssetTest;
import agilesites1.tests.EnvTest;
import agilesites1.tests.UrlTest;
import wcs.java.util.TestRunnerElement;

public class Tester extends TestRunnerElement {

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] tests() {
		return new Class[] { AllTests.class, EnvTest.class, AssetTest.class,
				UrlTest.class };
	}

}
