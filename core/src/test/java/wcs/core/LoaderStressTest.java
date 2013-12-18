package wcs.core;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Random;

public class LoaderStressTest {
	static {
		org.apache.log4j.BasicConfigurator.configure();
	}

	static File tempDir = new File(System.getProperty("java.io.tmpdir"),
			"testdir");

	private static void copyJar(String src, String dst) {
		Path ps = new File("src/test/resources/" + src + ".jar").toPath();
		Path pt = new File(tempDir, dst + ".jar").toPath();

		try {
			Files.copy(ps, pt, StandardCopyOption.REPLACE_EXISTING);
			pt.toFile().setLastModified(System.currentTimeMillis());
			System.out.println("************* copied " + dst);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Ex!" + e.getMessage());
		}
	}

	private static int load(Loader loader, String name)
			throws InstantiationException, IllegalAccessException {
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

	private static void assertTr(boolean b) throws Exception {
		if (!b) {
			System.out.println("!!!!!!!!!!! Failed !!!!!!!!!!!!!");
			// throw new Exception("test failed");
		}
	}

	public static void main(String[] args) throws Exception {

		tempDir.mkdirs();
		copyJar("a", "a");
		copyJar("b", "b");
		copyJar("c", "c");

		ClassLoader parent = Thread.currentThread().getContextClassLoader(); //LoaderTest.class.getClassLoader();

		final Loader loader = new Loader(tempDir, 100, parent);

		assertTr(load(loader, "a.A") == 'a');
		assertTr(load(loader, "b.B") == 'b');
		assertTr(load(loader, "c.C") == 'c');

		for (int i = 1; i < 1000; i++) {
			final int j = i;
			Thread t = new Thread() {
				Random rnd = new Random();

				public void run() {
					try {
						for (int k = 1; k < 1000; k++) {

							switch (rnd.nextInt(100)) {
							case 10:
								copyJar("a", "a");
								break;

							case 20:
								copyJar("b", "b");
								break;

							case 30:
								copyJar("c", "c");
								break;
							}

							assertTr(load(loader, "a.A") == 'a');
							Thread.sleep(10);
							assertTr(load(loader, "b.B") == 'b');
							Thread.sleep(10);
							assertTr(load(loader, "c.C") == 'c');
						}
						System.out.println(j + " end");
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			Thread.sleep(3);
			t.start();
		}

	}
}
