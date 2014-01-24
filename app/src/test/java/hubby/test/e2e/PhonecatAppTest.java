package hubby.test.e2e;

import static org.fest.assertions.Assertions.*;
import org.fluentlenium.core.annotation.Page;
import org.junit.Test;

import wcs.hubby.HubbyTest;

public class PhonecatAppTest extends HubbyTest {

	@Page
	public PhonecatPage page;

	@Test
	public void test() {

		 page.go(); 
		 page.search("");
		 assertThat(find("li").size()).isEqualTo(3); page.search("Nex");
		 assertThat(find("li").size()).isEqualTo(2); page.search("One");
		 assertThat(find("li").size()).isEqualTo(1); page.search("None");
		 assertThat(find("li").size()).isEqualTo(0);
		 
	}


	
	/*
	@Override
	public WebDriver getDefaultDriver() {
		return TestUtils.remoteChrome();
	}*/
}
