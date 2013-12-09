package wcs.java.util;

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

import static wcs.Api.*;
import wcs.api.Log;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;


/**
 * Prints XML output of the test to a specified Writer.
 * 
 * @see FormatterElement
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class XmlTestReport extends RunListener {

	final static Log log = Log.getLog(XmlTestReport.class);

	static class JUnitVersionHelper {

		private static Method testCaseName = null;

		/**
		 * Name of the JUnit4 class we look for. * {@value}
		 * 
		 * @since Ant 1.7.1
		 */
		public static final String JUNIT_FRAMEWORK_JUNIT4_TEST_CASE_FACADE = "junit.framework.JUnit4TestCaseFacade";
		private static final String UNKNOWN_TEST_CASE_NAME = "unknown";

		static {
			try {
				testCaseName = TestCase.class
						.getMethod("getName", new Class[0]);
			} catch (NoSuchMethodException e) {
				// pre JUnit 3.7
				try {
					testCaseName = TestCase.class.getMethod("name",
							new Class[0]);
				} catch (NoSuchMethodException ignored) {
					// ignore
				}
			}
		}

		/**
		 * JUnit 3.7 introduces TestCase.getName() and subsequent versions of
		 * JUnit remove the old name() method. This method provides access to
		 * the name of a TestCase via reflection that is supposed to work with
		 * version before and after JUnit 3.7.
		 * 
		 * <p>
		 * since Ant 1.5.1 this method will invoke &quot;<code>public
		 * String getName()</code>&quot; on any implementation of Test if it
		 * exists.
		 * </p>
		 * 
		 * <p>
		 * Since Ant 1.7 also checks for JUnit4TestCaseFacade explicitly. This
		 * is used by junit.framework.JUnit4TestAdapter.
		 * </p>
		 * 
		 * @param t
		 *            the test.
		 * @return the name of the test.
		 */
		public static String getTestCaseName(Test t) {
			if (t == null) {
				return UNKNOWN_TEST_CASE_NAME;
			}
			if (t.getClass().getName()
					.equals(JUNIT_FRAMEWORK_JUNIT4_TEST_CASE_FACADE)) {
				// Self-describing as of JUnit 4 (#38811). But trim
				// "(ClassName)".
				String name = t.toString();
				if (name.endsWith(")")) {
					int paren = name.lastIndexOf('(');
					return name.substring(0, paren);
				} else {
					return name;
				}
			}
			if (t instanceof TestCase && testCaseName != null) {
				try {
					return (String) testCaseName.invoke(t, new Object[0]);
				} catch (Throwable ignored) {
					// ignore
				}
			} else {
				try {
					Method getNameMethod = null;
					try {
						getNameMethod = t.getClass().getMethod("getName",
								new Class[0]);
					} catch (NoSuchMethodException e) {
						getNameMethod = t.getClass().getMethod("name",
								new Class[0]);
					}
					if (getNameMethod != null
							&& getNameMethod.getReturnType() == String.class) {
						return (String) getNameMethod.invoke(t, new Object[0]);
					}
				} catch (Throwable ignored) {
					// ignore
				}
			}
			return UNKNOWN_TEST_CASE_NAME;
		}

		/**
		 * Tries to find the name of the class which a test represents across
		 * JUnit 3 and 4. For Junit4 it parses the toString() value of the test,
		 * and extracts it from there.
		 * 
		 * @since Ant 1.7.1 (it was private until then)
		 * @param test
		 *            test case to look at
		 * @return the extracted class name.
		 */
		public static String getTestCaseClassName(Test test) {
			String className = test.getClass().getName();
			/*
			 * if (test instanceof JUnitTaskMirrorImpl.VmExitErrorTest) {
			 * className = ((JUnitTaskMirrorImpl.VmExitErrorTest) test)
			 * .getClassName(); } else
			 */if (className.equals(JUNIT_FRAMEWORK_JUNIT4_TEST_CASE_FACADE)) {
				// JUnit 4 wraps solo tests this way. We can extract
				// the original test name with a little hack.
				String name = test.toString();
				int paren = name.lastIndexOf('(');
				if (paren != -1 && name.endsWith(")")) {
					className = name.substring(paren + 1, name.length() - 1);
				}
			}
			return className;
		}

	}

	/** the testsuites element for the aggregate document */
	String TESTSUITES = "testsuites";

	/** the testsuite element */
	String TESTSUITE = "testsuite";

	/** the testcase element */
	String TESTCASE = "testcase";

	/** the error element */
	String ERROR = "error";

	/** the failure element */
	String FAILURE = "failure";

	/** the system-err element */
	String SYSTEM_ERR = "system-err";

	/** the system-out element */
	String SYSTEM_OUT = "system-out";

	/** package attribute for the aggregate document */
	String ATTR_PACKAGE = "package";

	/** name attribute for property, testcase and testsuite elements */
	String ATTR_NAME = "name";

	/** time attribute for testcase and testsuite elements */
	String ATTR_TIME = "time";

	/** errors attribute for testsuite elements */
	String ATTR_ERRORS = "errors";

	/** failures attribute for testsuite elements */
	String ATTR_FAILURES = "failures";

	/** tests attribute for testsuite elements */
	String ATTR_TESTS = "tests";

	String ATTR_SKIPPED = "skipped";

	/** type attribute for failure and error elements */
	String ATTR_TYPE = "type";

	/** message attribute for failure elements */
	String ATTR_MESSAGE = "message";

	/** the properties element */
	String PROPERTIES = "properties";

	/** the property element */
	String PROPERTY = "property";

	/** value attribute for property elements */
	String ATTR_VALUE = "value";

	/** classname attribute for testcase elements */
	String ATTR_CLASSNAME = "classname";

	/** id attribute */
	String ATTR_ID = "id";

	/**
	 * timestamp of test cases
	 */
	String TIMESTAMP = "timestamp";

	/**
	 * name of host running the tests
	 */
	String HOSTNAME = "hostname";

	private static final double ONE_SECOND = 1000.0;

	/** constant for unnnamed testsuites/cases */
	private static final String UNKNOWN = "unknown";

	private static DocumentBuilder getDocumentBuilder() {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (Exception exc) {
			throw new ExceptionInInitializerError(exc);
		}
	}

	/**
	 * The XML document.
	 */
	private Document doc;
	/**
	 * The wrapper for the whole testsuite.
	 */
	private Element rootElement;
	/**
	 * Element for the current test.
	 * 
	 * The keying of this map is a bit of a hack: tests are keyed by
	 * caseName(className) since the Test we get for Test-start isn't the same
	 * as the Test we get during test-assumption-fail, so we can't easily match
	 * Test objects without manually iterating over all keys and checking
	 * individual fields.
	 */
	private Hashtable<String, Element> testElements = new Hashtable<String, Element>();
	/**
	 * tests that failed.
	 */
	private Hashtable failedTests = new Hashtable();
	/**
	 * Tests that were skipped.
	 */
	private Hashtable<String, Description> skippedTests = new Hashtable<String, Description>();
	/**
	 * Tests that were ignored. See the note above about the key being a bit of
	 * a hack.
	 */
	private Hashtable<String, Description> ignoredTests = new Hashtable<String, Description>();
	/**
	 * Timing helper.
	 */
	private Hashtable<String, Long> testStarts = new Hashtable<String, Long>();
	/**
	 * Where to write the log to.
	 */
	private OutputStream out;

	/** No arg constructor. */
	public XmlTestReport() {
	}

	/** {@inheritDoc}. */
	public void setOutput(OutputStream out) {
		this.out = out;
	}

	/** {@inheritDoc}. */
	public void setSystemOutput(String out) {
		formatOutput(SYSTEM_OUT, out);
	}

	/** {@inheritDoc}. */
	public void setSystemError(String out) {
		formatOutput(SYSTEM_ERR, out);
	}

	/**
	 * The whole testsuite started.
	 * 
	 * @param suite
	 *            the testsuite.
	 */
	public void startTestSuite(String name/* JunitTest suite */) {
		doc = getDocumentBuilder().newDocument();
		rootElement = doc.createElement(TESTSUITE);
		String n = name /* suite.getName() */;
		rootElement.setAttribute(ATTR_NAME, n == null ? UNKNOWN : n);

		// add the timestamp
		final String timestamp = DateUtils.format(new Date(),
				DateUtils.ISO8601_DATETIME_PATTERN);
		rootElement.setAttribute(TIMESTAMP, timestamp);
		// and the hostname.
		rootElement.setAttribute(HOSTNAME, getHostname());

		// Output properties
		Element propsElement = doc.createElement(PROPERTIES);
		rootElement.appendChild(propsElement);
		/*
		 * Properties props = suite.getProperties(); if (props != null) {
		 * Enumeration e = props.propertyNames(); while (e.hasMoreElements()) {
		 * String name = (String) e.nextElement(); Element propElement =
		 * doc.createElement(PROPERTY); propElement.setAttribute(ATTR_NAME,
		 * name); propElement.setAttribute(ATTR_VALUE, props.getProperty(name));
		 * propsElement.appendChild(propElement); } }
		 */
	}

	/**
	 * get the local hostname
	 * 
	 * @return the name of the local host, or "localhost" if we cannot work it
	 *         out
	 */
	private String getHostname() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "localhost";
		}
	}

	/**
	 * The whole testsuite ended.
	 * 
	 * @param suite
	 *            the testsuite.
	 * @throws BuildException
	 *             on error.
	 */
	public void endTestSuite(int runCount, int failureCount, int errorCount,
			int skipCount, long runTime /* XmlTestReport suite/*JUnitTest suite */) {
		rootElement
				.setAttribute(ATTR_TESTS, "" + runCount /* suite.runCount() */);
		rootElement.setAttribute(ATTR_FAILURES, "" + failureCount/*
																 * suite.
																 * rootElement
																 * .setAttribute
																 * (ATTR_ERRORS,
																 * "" +
																 * errorCount/*
																 * suite
																 * .errorCount()
																 */);
		rootElement
				.setAttribute(ATTR_SKIPPED, "" + skipCount /* suite.skipCount() */);
		rootElement.setAttribute(ATTR_TIME, ""
				+ (runTime /* suite.getRunTime() *// ONE_SECOND));
		if (out != null) {
			Writer wri = null;
			try {
				wri = new BufferedWriter(new OutputStreamWriter(out, "UTF8"));
				wri.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
				(new DOMElementWriter()).write(rootElement, wri, 0, "  ");
			} catch (IOException exc) {
				log.error(exc, "Unable to write log file", exc);
			} finally {
				if (wri != null) {
					try {
						wri.flush();
						if (out != System.out && out != System.err)
							wri.close();
					} catch (IOException ex) {
						// ignore
					}
				}
			}
		}
	}

	/**
	 * Interface TestListener.
	 * 
	 * <p>
	 * A new Test is started.
	 * 
	 * @param t
	 *            the test.
	 */
	public void startTest(Description t) {
		testStarts.put(createDescription(t), System.currentTimeMillis());
	}

	private static String createDescription(Description test) /* throws Exception */{
		return test.getDisplayName() + "(" + test.getClass().getName() + ")";
	}

	/**
	 * Interface TestListener.
	 * 
	 * <p>
	 * A Test is finished.
	 * 
	 * @param test
	 *            the test.
	 */
	public void endTest(Description test) {
		String testDescription = createDescription(test);

		// Fix for bug #5637 - if a junit.extensions.TestSetup is
		// used and throws an exception during setUp then startTest
		// would never have been called
		if (!testStarts.containsKey(testDescription)) {
			startTest(test);
		}
		Element currentTest;
		if (!failedTests.containsKey(test)
				&& !skippedTests.containsKey(testDescription)
				&& !ignoredTests.containsKey(testDescription)) {
			currentTest = doc.createElement(TESTCASE);
			String n = test.getDisplayName();
			currentTest.setAttribute(ATTR_NAME, n == null ? UNKNOWN : n);
			// a TestSuite can contain Tests from multiple classes,
			// even tests with the same name - disambiguate them.
			currentTest.setAttribute(ATTR_CLASSNAME, test.getClass().getName());
			rootElement.appendChild(currentTest);
			testElements.put(createDescription(test), currentTest);
		} else {
			currentTest = testElements.get(testDescription);
		}

		Long l = testStarts.get(createDescription(test));
		currentTest.setAttribute(ATTR_TIME, ""
				+ ((System.currentTimeMillis() - l) / ONE_SECOND));
	}

	/**
	 * Interface TestListener for JUnit &lt;= 3.4.
	 * 
	 * <p>
	 * A Test failed.
	 * 
	 * @param test
	 *            the test.
	 * @param t
	 *            the exception.
	 */
	public void addFailure(Description test, Throwable t) {
		formatError(FAILURE, test, t);
	}

	/**
	 * Interface TestListener for JUnit &gt; 3.4.
	 * 
	 * <p>
	 * A Test failed.
	 * 
	 * @param test
	 *            the test.
	 * @param t
	 *            the assertion.
	 */
	public void addFailure(Description test, AssertionFailedError t) {
		addFailure(test, (Throwable) t);
	}

	/**
	 * Interface TestListener.
	 * 
	 * <p>
	 * An error occurred while running the test.
	 * 
	 * @param test
	 *            the test.
	 * @param t
	 *            the error.
	 */
	public void addError(Description test, Throwable t) {
		formatError(ERROR, test, t);
	}

	private void formatError(String type, Description test, Throwable t) {
		if (test != null) {
			endTest(test);
			failedTests.put(test, test);
		}

		Element nested = doc.createElement(type);
		Element currentTest;
		if (test != null) {
			currentTest = testElements.get(createDescription(test));
		} else {
			currentTest = rootElement;
		}

		currentTest.appendChild(nested);

		String message = t.getMessage();
		if (message != null && message.length() > 0) {
			nested.setAttribute(ATTR_MESSAGE, t.getMessage());
		}
		nested.setAttribute(ATTR_TYPE, t.getClass().getName());

		// String strace = JUnitTestRunner.getFilteredTrace(t);
		String strace = ex2str(t);
		Text trace = doc.createTextNode(strace);
		nested.appendChild(trace);
	}

	private void formatOutput(String type, String output) {
		Element nested = doc.createElement(type);
		rootElement.appendChild(nested);
		nested.appendChild(doc.createCDATASection(output));
	}

	public void testIgnored(Description test) {
		formatSkip(test, "ignored" /* JUnitVersionHelper.getIgnoreMessage(test) */);
		if (test != null) {
			ignoredTests.put(createDescription(test), test);
		}
	}

	public void formatSkip(Description test, String message) {
		if (test != null) {
			endTest(test);
		}

		Element nested = doc.createElement("skipped");

		if (message != null) {
			nested.setAttribute("message", message);
		}

		Element currentTest;
		if (test != null) {
			currentTest = testElements.get(createDescription(test));
		} else {
			currentTest = rootElement;
		}

		currentTest.appendChild(nested);

	}

	public void testAssumptionFailure(Description test, Throwable failure) {
		formatSkip(test, failure.getMessage());
		skippedTests.put(createDescription(test), test);

	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		addFailure(failure.getDescription(), failure.getException());
		super.testFailure(failure);
	}

	@Override
	public void testFinished(Description description) throws Exception {
		startTest(description);
	}

	@Override
	public void testStarted(Description description) throws Exception {
		endTest(description);
	}

} // XMLJUnitResultFormatter