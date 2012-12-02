package wcs.java;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public abstract class TestRunnerElement extends Element {

	@SuppressWarnings("rawtypes")
	abstract public Class[] tests();

	private String testform(String pagename) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tests().length; i++)
			sb.append("<option>").append(tests()[i].getCanonicalName())
					.append("</option>");
		return "<form method=\"get\">\n" + "Test to run:<br>"
				+ "<select name='test'>" + sb.toString() + "</select><br>"
				+ "<input name='pagename' type='hidden' value='" + pagename
				+ "'>" + "<input type='submit' value='Run Test'>\n"
				+ "</form>\n" //
				+ "</form>\n"//
		;
	}

	static class TestListener extends RunListener {
		StringBuffer sb = new StringBuffer();
		StringBuffer failures = new StringBuffer();
		StringBuffer header = new StringBuffer();
		int failureCount = 0;
		String lastFailure = null;

		@Override
		public void testAssumptionFailure(Failure failure) {
			sb.append("Assumption").append(failure.getMessage()).append("<br>");
			super.testAssumptionFailure(failure);
		}

		@Override
		public void testStarted(Description description) throws Exception {
			// sb.append("TestStarted").append(description.toString()).append("<br>");
			sb.append("<b>").append(description.getClassName()).append(".")
					.append(description.getMethodName()).append("</b>: ");
			lastFailure = null;
			super.testStarted(description);
		}

		@Override
		public void testFailure(Failure failure) throws Exception {
			// sb.append("failure detected");
			// sb.append("Failure").append(failure.toString()).append("<br>");
			lastFailure = failure.getMessage();
			failureCount++;
			failures.append("<h2>").append(failure.getTestHeader())
					.append("</h2>");
			failures.append("<pre>").append(failure.getTrace())
					.append("</pre>");
			super.testFailure(failure);
		}

		@Override
		public void testFinished(Description description) throws Exception {
			// sb.append("Finished").append(description.toString()).append("<br>");
			if (lastFailure != null) {
				sb.append("KO:").append(lastFailure);
			} else
				sb.append("OK");
			sb.append("<br>");
			super.testFinished(description);
		}

		@Override
		public void testIgnored(Description description) throws Exception {
			sb.append("Skip: ").append(description.toString()).append("<br>");
			super.testIgnored(description);
		}

		public String toString() {
			return header.toString() + sb.toString() + failures.toString();
		}

		@Override
		public void testRunFinished(Result result) throws Exception {
			// sb.append("RunFinished").append(result.toString()).append("<br>");
			if (failureCount > 0) {
				header.append("<h1 style=\"color: red\">");
			} else {
				header.append("<h1>");
			}
			header.append("Test Count: #").append(result.getRunCount());
			if (failureCount > 0) {
				header.append(" Failures: # ").append(failureCount);
			}
			header.append("</h1>");

			super.testRunFinished(result);
		}

		@Override
		public void testRunStarted(Description description) throws Exception {
			// sb.append("RunStarted").append(description.toString())
			// .append("<br>");
			super.testRunStarted(description);
		}

	}

	@Override
	public String apply(Env e) {

		String test = e.getVar("test");
		if (test == null)
			return testform(e.getVar("pagename"));

		JUnitCore core = new JUnitCore();
		RunListener listener = new TestListener();

		core.addListener(listener);

		@SuppressWarnings("rawtypes")
		Class clazz;
		try {
			clazz = Class.forName(e.getVar("test"));
			core.run(clazz);
			return listener.toString();
		} catch (ClassNotFoundException ex) {
			return "<h1>Exception</h1><p>" + ex + "</p>";
		}

	}

}
