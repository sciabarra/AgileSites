package wcs.hubby;

import java.net.URL;

import org.fluentlenium.adapter.FluentTest;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Base class for Hubby Tests
 * 
 * @author msciab
 * 
 */
public class BrowserTest extends FluentTest {
	
	/**
	 * Returns an url or null (avoid the exception with new URL)
	 * 
	 * @param url
	 * @return
	 */
	public static URL url(String url) {
		try {
			return new URL(url);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Return a local url, so you just need to write locurl(":4545/web/hub") for
	 * a local url in port 4545
	 * 
	 * @param url
	 * @return
	 */
	public static URL locurl(String url) {
		return url("http://localhost" + url);
	}

	/**
	 * Return a remote webdriver with Firefox capabilities for quick setup of a
	 * webdriver for tests.
	 * 
	 * @return
	 */
	public static WebDriver remoteFirefox() {
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		return new RemoteWebDriver(locurl(":8182/wd/hub"), capabilities);
	}

	/**
	 * Return a remote webdriver with Firefox capabilities for quick setup of a
	 * webdriver for tests.
	 * 
	 * @return
	 */
	public static WebDriver remoteChrome() {
		return new RemoteWebDriver(locurl(":8182/wd/hub"),
				DesiredCapabilities.chrome());
	}


	// TODO change with a system property
	WebDriver webDriver = BrowserTest.remoteChrome();

	@Before
	public void intilializeWebDriver() {
		initFluent(webDriver);
		initTest();
	}
	
	@After
	public void shutdownWebDriver() {
		webDriver.quit();
	}


}
