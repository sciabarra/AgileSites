package wcs.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.junit.Before;
import org.junit.Test;
import org.xeustechnologies.jcl.JarClassLoader;

/**
 * 
 * Part of this class must be run manually as a java application because the
 * class loader are not compatible with the test runner...
 * 
 * @author msciab
 * 
 */
public class LoaderTest {

	static {
		// org.apache.log4j.BasicConfigurator.configure();
	}

	public LoaderTest() {
		tempDir = new File(System.getProperty("java.io.tmpdir"), "testdir");
		tempDir.mkdirs();
		parent = LoaderTest.class.getClassLoader();
		loader = new Loader(tempDir, 100, parent);
	}

	public LoaderTest(LoaderTest loaderTest) {
		tempDir = loaderTest.tempDir;
		parent = loaderTest.parent;
		loader = loaderTest.loader;
	}

	Loader loader;
	ClassLoader parent;
	File tempDir;

	@Before
	public void setup() throws Exception {
		emptyDir();
		msg("[" + tempDir + "]");
	}

	// workaround for classloading test problems
	private void assertTr(boolean b) throws Exception {
		if (!b) {
			System.out.println("!!!!!!!!!!! Failed !!!!!!!!!!!!!");
			throw new Exception("test failed");
		}
	}

	private void msg(String s) {
		System.out.println("[" + s + "]");
	}

	private void updateJar(String name) {
		copyJar(name, name);
	}

	private void copyJar(String src, String dst) {
		Path ps = new File("src/test/resources/" + src + ".jar").toPath();
		Path pt = new File(tempDir, dst + ".jar").toPath();

		try {
			Files.copy(ps, pt, StandardCopyOption.REPLACE_EXISTING);
			pt.toFile().setLastModified(System.currentTimeMillis());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Ex!" + e.getMessage());
		}
	}

	private void emptyDir() throws Exception {

		if (loader != null)
			loader.close();

		for (File f : tempDir.listFiles()) {
			if (f.isDirectory())
				continue;
			if (!f.delete()) {
				System.out.println("cannot delete " + f);
				throw new Exception("cannot empty dir");
			}
		}
	}

	private int load(String name) throws InstantiationException,
			IllegalAccessException {
		Class<?> clazz = loader.loadClass("wcs.core.test." + name);
		if (clazz == null)
			return 0;
		Object object = clazz.newInstance();
		Readable r = (Readable) object;
		try {
			int res = r.read(null);
			return res;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Test
	public void testEmpty() throws Exception {

		msg("Empty");

		assertNull(loader.getJarsIfSomeIsModifiedAfterInterval());
		assertEquals(loader.getClassLoader(), loader.getParentClassLoader());

		// loader.getClassLoader()

		Thread.sleep(200);
		assertNull(loader.getJarsIfSomeIsModifiedAfterInterval());
		assertEquals(loader.getClassLoader(), loader.getParentClassLoader());
	}

	@Test
	public void testOne() throws Exception {

		updateJar("a");

		// find it first time

		msg("One modified");

		File[] jars = loader.getJarsIfSomeIsModifiedAfterInterval();
		assertNotNull(jars);
		assertEquals(jars.length, 1);

		// not yet reached the interval
		msg("Not yet time ");
		Thread.sleep(10);
		assertNull(loader.getJarsIfSomeIsModifiedAfterInterval());

		// reached the interval not changed
		msg("It is time no change");
		Thread.sleep(110);
		assertNull(loader.getJarsIfSomeIsModifiedAfterInterval());
		// waiting again
		msg("It is time again  no change");
		Thread.sleep(200);
		assertNull(loader.getJarsIfSomeIsModifiedAfterInterval());

		// changed
		updateJar("a");
		msg("Change");
		jars = loader.getJarsIfSomeIsModifiedAfterInterval();
		assertNotNull(jars);
		assertEquals(jars.length, 1);
		msg("Re check");
		assertNull(loader.getJarsIfSomeIsModifiedAfterInterval());

		// loader.close();
	}

	@Test
	public void testTwo() throws Exception {

		updateJar("a");
		updateJar("b");

		// find it first time

		msg("One modified");

		File[] jars = loader.getJarsIfSomeIsModifiedAfterInterval();
		assertNotNull(jars);
		assertEquals(jars.length, 2);

		// not yet reached the interval
		msg("Not yet time ");
		Thread.sleep(10);
		assertNull(loader.getJarsIfSomeIsModifiedAfterInterval());

		// reached the interval not changed
		msg("It is time no change");
		Thread.sleep(110);
		assertNull(loader.getJarsIfSomeIsModifiedAfterInterval());
		// waiting again
		msg("It is time again  no change");
		Thread.sleep(200);
		assertNull(loader.getJarsIfSomeIsModifiedAfterInterval());

		// changed
		updateJar("a");
		msg("Change a");
		jars = loader.getJarsIfSomeIsModifiedAfterInterval();
		assertNotNull(jars);
		assertEquals(jars.length, 2);
		msg("Re check");
		assertNull(loader.getJarsIfSomeIsModifiedAfterInterval());

		// changed
		updateJar("b");
		msg("Change b");
		jars = loader.getJarsIfSomeIsModifiedAfterInterval();
		assertNull(jars);
		Thread.sleep(110);
		jars = loader.getJarsIfSomeIsModifiedAfterInterval();
		assertNotNull(jars);
		assertEquals(jars.length, 2);
		msg("Re check");
		assertNull(loader.getJarsIfSomeIsModifiedAfterInterval());

		// loader.close();
	}

	// manual test - launch main
	public void testZeroJar() throws Exception {
		assertTr(loader.getClassLoader() == parent);
		// loader.close();
	}

	// manual test - launch main
	@Test
	public void testOneJar() throws Exception {
		updateJar("a");

		msg("change to urlcl");
		Thread.sleep(200);
		ClassLoader cur = loader.getClassLoader();
		assertTr(cur != parent);
		assertTr(cur instanceof JarClassLoader);

		// loader.close();
		msg("no change");
		Thread.sleep(200);
		assertTr(cur == loader.getClassLoader());

		msg("change to another urlcl");
		updateJar("a");
		Thread.sleep(200);
		assertTr(cur != loader.getClassLoader());
	}

	@Test
	public void testTwoJar() throws Exception {

		updateJar("a");
		updateJar("b");

		msg("change to urlcl");
		Thread.sleep(200);
		ClassLoader cur = loader.getClassLoader();
		assertTr(cur != parent);
		assertTr(cur instanceof JarClassLoader);

		// loader.close();
		msg("no change");
		Thread.sleep(200);
		assertTr(cur == loader.getClassLoader());

		msg("change to another urlcl");
		updateJar("a");
		Thread.sleep(200);
		assertTr(cur != loader.getClassLoader());

		msg("change to another urlcl");
		updateJar("b");
		Thread.sleep(200);
		assertTr(cur != loader.getClassLoader());
	}

	// manual test
	public void testThreeJar() throws Exception {

		updateJar("a");
		updateJar("b");
		updateJar("c");

		msg("change to urlcl");
		Thread.sleep(200);
		ClassLoader cur = loader.getClassLoader();
		assertTr(cur != parent);
		assertTr(cur instanceof JarClassLoader);

		// loader.close();
		msg("no change");
		Thread.sleep(200);
		assertTr(cur == loader.getClassLoader());

		msg("change to another urlcl");
		updateJar("c");
		Thread.sleep(200);
		assertTr(cur != loader.getClassLoader());

		msg("change to another urlcl");
		updateJar("b");
		Thread.sleep(200);
		assertTr(cur != loader.getClassLoader());
	}

	// manual test
	@Test
	public void testLoadClassFromOneJar() throws Exception {
		updateJar("a");
		msg("loading class");
		assertTr(load("a.A") == 'a');
	}

	@Test
	public void testLoadClassFromTwoJar() throws Exception {
		updateJar("a");
		updateJar("b");
		msg("loading class from b");
		assertTr(load("a.A") == 'a');
		assertTr(load("b.B") == 'b');
	}

	@Test
	public void testLoadClassFromThreeJar() throws Exception {
		updateJar("a");
		updateJar("b");
		updateJar("c");

		msg("loading class from b");
		assertTr(load("a.A") == 'a');
		assertTr(load("b.B") == 'b');
		assertTr(load("c.C") == 'c');

	}

	// @Test
	public void replaceJar() throws Exception {
		updateJar("a");
		updateJar("b");

		msg("loading class from b");
		assertTr(load("a.A") == 'a');
		assertTr(load("b.B") == 'b');

		copyJar("c", "a");
		Thread.sleep(1000);

		assertTr(load("c.C") == 'c');
		try {
			assertTr(load("a.A") == 'a');
			throw new Error("a.A should be gone in "
					+ loader.getCurrentSpoolDir());
		} catch (Exception e) {
			msg("ex is ok:" + e.getMessage());
		}

		copyJar("a", "b");
		Thread.sleep(200);

		assertTr(load("a.A") == 'a');
		try {
			assertTr(load("b.B") == 'b');
			throw new Exception("b.B should be gone in "
					+ loader.getCurrentSpoolDir());
		} catch (Exception e) {
			msg("ex is ok:" + e.getMessage());
		}
	}

}
