package wcs.core;

import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.junit.Test;


public class AssemblerTest {

	@Test
	public void test() {
		URI uri = Assembler.buildUri("http://localhost:8080/cs/Satellite/kpn",
				"/something with spaces and ö/and another slash");
		assertEquals(
				uri.toASCIIString(),
				"http://localhost:8080/cs/Satellite/kpn/something%20with%20spaces%20and%20%C3%B6/and%20another%20slash");
	}
}
