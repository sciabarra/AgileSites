package agilesites.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ EnvTest.class, AssetTest.class, UrlTest.class })
public class AllTests {
}
