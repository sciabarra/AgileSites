package test.template;

import test.tests.AllTests;
import test.tests.ElementTest;
import test.tests.RunTagTest;
import wcs.java.TestRunnerElement;

public class Runner extends TestRunnerElement {

	String boh="";
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class[] tests() {
		return new Class[] { AllTests.class, ElementTest.class,
				RunTagTest.class };
	}

	
}
