package wcs.java.util;
import static wcs.Api.*;
import wcs.api.Log;
import wcs.api.Env;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import java.io.ByteArrayOutputStream;
import java.util.StringTokenizer;

public abstract class TestRunnerElement extends wcs.java.Element {

	final static Log log = Log.getLog(TestRunnerElement.class);

	abstract public Class<?>[] tests();

	private String alltests() {
		StringBuilder sb = new StringBuilder();
		for (Class<?> clazz : tests()) {
			sb.append(clazz.getCanonicalName()).append(";");
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	private String testform(String pagename) {

		Class<?>[] tests = tests();
		StringBuffer opts = new StringBuffer();
		StringBuffer hids = new StringBuffer();
		for (int i = 0; i < tests().length; i++) {
			opts.append("<option>").append(tests[i].getCanonicalName())
					.append("</option>");
			hids.append("<input type='hidden' name='test' value='")
					.append(tests[i].getCanonicalName()).append("'>");
		}

		return "<h1>Test Runner</h1>"
				+ "<form method=\"get\">\n"
				+ "<input type='hidden' name='d'>\n"
				+ "<input type='submit' value='Run All Tests'>"
				+ "<input name='pagename' type='hidden' value='"
				+ pagename
				+ "'>"
				+ hids.toString()
				+ "</form>\n"
				+ "<form method=\"get\">\n"
				+ "Select tests to run:<br>"
				+ "<select name='test' multiple size='"
				+ tests.length
				+ "'>"
				+ opts.toString()
				+ "</select><br>"
				+ "<input name='pagename' type='hidden' value='"
				+ pagename
				+ "'>"
				+ "<input name='run_some' type='submit' value='Run Some Tests'>\n"
				+ "</form>\n"
				+ "<a href='/cs/ContentServer?xml=1&cs.contenttype=text/xml&pagename="
				+ pagename + "'>XML</a>";
	}

	StringBuilder header = new StringBuilder();

	static class TestListener extends RunListener {

		String lastFailure = null;
		StringBuilder sb = new StringBuilder();
		StringBuilder failures = new StringBuilder();
		int failureCount = 0;
		int runCount = 0;
		int skipCount = 0;

		/*
		 * public void testAssumptionFailure(Failure failure) { //
		 * sb.append("Assumption").append(failure.getMessage()).append("<br>");
		 * super.testAssumptionFailure(failure); }
		 */

		public TestListener append(String msg) {
			sb.append(msg);
			return this;
		}

		@Override
		public void testStarted(Description description) throws Exception {

			log.trace("testStarted");

			sb.append("<b>").append(description).append("</b>: ");
			lastFailure = null;

			super.testStarted(description);
		}

		@Override
		public void testFinished(Description description) throws Exception {
			log.trace("testFinished");

			// sb.append("Finished").append(description.toString()).append("<br>");
			if (lastFailure != null) {
				sb.append("KO:<pre>").append(lastFailure).append("</pre>");
			} else
				sb.append("OK");
			sb.append("<br>");
			super.testFinished(description);
		}

		@Override
		public void testFailure(Failure failure) throws Exception {

			log.trace("testFailure");

			lastFailure = failure.getMessage();
			if (lastFailure == null)
				lastFailure = "Null pointer";

			failureCount++;
			failures.append("<h2>").append(failure.getTestHeader())
					.append("</h2>");
			failures.append("<pre>").append(failure.getTrace())
					.append("</pre>");
			super.testFailure(failure);
		}

		@Override
		public void testIgnored(Description description) throws Exception {
			log.trace("testIgnored");
			sb.append("Skip: ").append(description.toString()).append("<br>");
			super.testIgnored(description);
		}

		public String toString() {
			String s = failures.length() > 0 ? "<hr><h1>Failures</h1>"
					+ failures.toString() : "";
			return sb.toString() + s;
		}

		@Override
		public void testRunFinished(Result result) throws Exception {
			log.trace("testRunFinished");
			runCount = result.getRunCount();
			skipCount = result.getIgnoreCount();
			super.testRunFinished(result);
		}

		@Override
		public void testRunStarted(Description description) throws Exception {
			log.trace("testRunStarted");
			super.testRunStarted(description);
		}

	}

	private static ThreadLocal<TestEnv> currTestEnv = new ThreadLocal<TestEnv>();

	public static TestEnv getTestEnv() {
		return currTestEnv.get();
	}


	@Override
	public String apply(Env e) {

		// create a new, modifiable env, then set it to a threadlocal
		// so the testelement can find and use it
		TestEnv te = new TestEnv(e.ics(), //
				arg("tid", e.getString("tid")), //
				arg("eid", e.getString("eid")));
		currTestEnv.set(te);

		// System.out.println(Thread.currentThread());
		JUnitCore core = new JUnitCore();
		String tests = e.getString("test");

		// XML Test?
		ByteArrayOutputStream xmlOutput = null;
		XmlTestReport report = null;
		if (e.getString("xml") != null) {
			xmlOutput = new ByteArrayOutputStream();
			report = new XmlTestReport();
			report.setOutput(xmlOutput);
			core.addListener(report);
			tests = alltests();
		}

		// do we have tests, or we need to ask for them?
		if (tests == null)
			return testform(e.getString("pagename"));

		TestListener listener = new TestListener();
		core.addListener(listener);

		// initialize the report
		int errorCount = 0;
		int skipCount = 0;
		int runCount = 0;
		int failureCount = 0;

		long timestamp = System.currentTimeMillis();

		if (report != null)
			report.startTestSuite(e.getString("site"));

		// run all the tests and collect results
		StringTokenizer st = new StringTokenizer(tests, ";");
		while (st.hasMoreTokens())
			try {
				String testName = st.nextToken();
				// System.out.println(testName);
				core.run(Class.forName(testName));

				skipCount += listener.skipCount;
				failureCount += listener.failureCount;
				runCount += listener.runCount;
			} catch (Throwable ex) {
				errorCount++;
				listener.append("<h1>Exception</h1><p>" + ex + "</p>");
			}

		// record the results
		if (report != null)
			report.endTestSuite(runCount, failureCount, errorCount, skipCount,
					System.currentTimeMillis() - timestamp);

		// display results
		if (failureCount > 0)
			header.append("<h1 style=\"color: red\">");
		else
			header.append("<h1>");
		header.append("Test Count: #").append(runCount);
		if (failureCount > 0)
			header.append(" Failures: # ").append(failureCount);
		header.append("</h1>");

		// removing the threadlocal in case of jar reloading
		currTestEnv.remove();

		// output
		if (xmlOutput == null)
			return header.toString() + listener.toString();
		else
			return new String(xmlOutput.toByteArray());
	}
}
