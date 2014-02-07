package wcs.java;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ MapIListTest.class, ModelTest.class, PickerTest.class })
public class AllTests {
	static {
		org.apache.log4j.BasicConfigurator.configure();
	}
}
