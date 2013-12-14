package wcs.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * Part of this class must be run manually as a java application because the
 * class loader are not compantible with the test runner...
 * 
 * @author msciab
 * 
 */
public class LoaderTest {

	Loader loader;
	ClassLoader parent;
	File tempDir;

	@Before
	public void setup() {
		tempDir = new File(System.getProperty("java.io.tmpdir"), "testdir");
		tempDir.mkdirs();
		parent = LoaderTest.class.getClassLoader();
		loader = new Loader(tempDir, 100, parent);
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

		for (File f : tempDir.listFiles()) {
			if (f.isDirectory())
				continue;
			if (!f.delete())
				throw new Exception("cannot empty dir");
		}

		for (File f : new File(tempDir, "spool").listFiles()) {
			if (!f.delete())
				throw new Exception("cannot empty dir");
		}

	}

	private wcs.api.Element load(String name) throws InstantiationException,
			IllegalAccessException {
		Class<?> clazz = loader.loadClass("wcs.core.test." + name);
		if (clazz == null)
			return null;
		Object object = clazz.newInstance();
		return (wcs.api.Element) object;
	}

	@Test
	public void testEmpty() throws Exception {

		msg("Empty");

		emptyDir();
		assertNull(loader.getJarsIfSomeIsModifiedAfterInterval());
		assertEquals(loader.getClassLoader(), loader.getParentClassLoader());

		// loader.getClassLoader()

		Thread.sleep(200);
		assertNull(loader.getJarsIfSomeIsModifiedAfterInterval());
		assertEquals(loader.getClassLoader(), loader.getParentClassLoader());
	}

	@Test
	public void testOne() throws Exception {

		emptyDir();
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

		emptyDir();
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
		emptyDir();
		assertTr(loader.getClassLoader() == parent);
		// loader.close();
	}

	// manual test - launch main
	public void testOneJar() throws Exception {
		emptyDir();

		updateJar("a");

		msg("change to urlcl");
		Thread.sleep(200);
		ClassLoader cur = loader.getClassLoader();
		assertTr(cur != parent);
		assertTr(cur instanceof URLClassLoader);

		// loader.close();
		msg("no change");
		Thread.sleep(200);
		assertTr(cur == loader.getClassLoader());

		msg("change to another urlcl");
		updateJar("a");
		Thread.sleep(200);
		assertTr(cur != loader.getClassLoader());
	}

	// manual test
	public void testTwoJar() throws Exception {

		updateJar("a");
		updateJar("b");

		msg("change to urlcl");
		Thread.sleep(200);
		ClassLoader cur = loader.getClassLoader();
		assertTr(cur != parent);
		assertTr(cur instanceof URLClassLoader);

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

		emptyDir();

		updateJar("a");
		updateJar("b");
		updateJar("c");

		msg("change to urlcl");
		Thread.sleep(200);
		ClassLoader cur = loader.getClassLoader();
		assertTr(cur != parent);
		assertTr(cur instanceof URLClassLoader);

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
	public void testLoadClassFromOneJar() throws Exception {
		emptyDir();
		updateJar("a");
		msg("loading class");
		assertTr(load("a.A").equals("a"));
	}

	/*
	 * 
	 * @Test public void testLoadOneJar() throws Exception { emptyDir();
	 * updateJar("a"); // assertFalse(cl == loader.getDefaultClassLoader());
	 * Element e = load("a.A"); assertEquals(e.exec(null), "a"); loader.close();
	 * }
	 * 
	 * public static void main(String[] args) throws Exception { LoaderTest lt =
	 * new LoaderTest(); lt.setup(); lt.emptyDir(); lt.updateJar("a");
	 * lt.updateJar("b"); System.out.println(lt.tempDir.getAbsolutePath()); }
	 * 
	 * public static void main(String[] args) throws Exception {
	 * 
	 * LoaderTest test = new LoaderTest(); test.setup(); //test.testZeroJar();
	 * //test.testOneJar(); //test.testTwoJar(); //test.testThreeJar();
	 * test.testLoadClassFromOneJar(); }
	 */
}
