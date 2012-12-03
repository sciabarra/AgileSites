package test.tests;

import static org.junit.Assert.*;
import static test.template.JUnitRunner.getEnv;

import org.junit.Before;
import org.junit.Test;

import wcs.java.Env;

public class ElementTest {

	Env env;

	@Before
	public void setUp() {
		env = getEnv();
	}

	@Test
	public void testOk() {
		System.out.println(Thread.currentThread());
		try {
			assertTrue(env.getVar("p").equals("1"));
		} catch (Exception e) {
			fail("no p");
		}
	}

	@Test
	public void testOk2() {
		// fail("ko!");
		assertTrue(true);
	}

}
