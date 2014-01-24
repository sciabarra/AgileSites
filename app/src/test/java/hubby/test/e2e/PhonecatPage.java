package hubby.test.e2e;

import static org.fest.assertions.Assertions.assertThat;

import org.fluentlenium.core.FluentPage;

public class PhonecatPage extends FluentPage {

	@Override
	public String getUrl() {
		return "http://localhost:8181/hubby/index.html";
	}

	public void isAt() {
		assertThat(title()).isEqualTo("Google Phone Gallery");
	}

	public void search(String value) {
		fill("#search").with(value);
	}
}
