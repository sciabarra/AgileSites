package wcs.core;

import static java.lang.System.out;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.fatwire.cs.core.uri.Definition;
import com.fatwire.cs.core.uri.Definition.ContainerType;

public class AssemblerTest {

	Assembler as = new Assembler();

	@Before
	public void setup() throws IOException {
		Properties prp = new Properties();
		prp.load(this.getClass().getResourceAsStream(
				"/ServletRequest.properties"));
		as.setProperties(prp);
	}

	// utils
	String prefix = "http://localhost:8080/cs/Satellite/demo";
	URI uri(String path) {
		return Assembler.buildUri(prefix, path);
	}

	@Test
	public void test() {
		String a = uri("/something with spaces and \u00F6/and another slash")
				.toASCIIString();
		String b = "http://localhost:8080/cs/Satellite/demo/something%20with%20spaces%20and%20%C3%B6/and%20another%20slash";
		// out.println(uri.toString());
		//out.println(a);
		//out.println(b);
		assertEquals(a, b);
	}

	@Test
	public void disassemble() throws URISyntaxException {
		//TODO: add actual assertions 
		
		Definition d = as.disassemble(uri(""), ContainerType.SERVLET);
		
		//assertThat(d.getParameterNames(), is(containsInAnyOrder(""));
		out.println(d.getParameterNames());
		out.println("site="+d.getParameter("site"));

		d = as.disassemble(uri("/About"), ContainerType.SERVLET);
		out.println(d.getParameterNames());

		d = as.disassemble(uri("/1234.gif"), ContainerType.SERVLET);
		out.println(d.getParameterNames());
		
		d = as.disassemble(uri("/test.js"), ContainerType.SERVLET);
		out.println(d.getParameterNames());

	}

}
