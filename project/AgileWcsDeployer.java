import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

/**
 * This class is a deployer: it creates 2 temporary entries to invoke the
 * installer, run it then delete them.
 * 
 * @author msciab
 * 
 */
public class AgileWcsDeployer {

	String site;
	String url;
	String username;
	String password;
	String cs;

	String status = "OK";

	HttpClient client = new HttpClient();
	HttpState state = new HttpState();

	public AgileWcsDeployer() {
		// simple constructor for test
		this("http://localhost:8080/cs/", "AdminSite", "fwadmin", "xceladmin");
	}

	/**
	 * Build the deployer
	 * 
	 * @param url
	 * @param username
	 * @param password
	 */
	public AgileWcsDeployer(String url, String site, String username,
			String password) {
		this.username = username;
		this.password = password;
		this.site = site;
		this.url = url;
		if (!url.endsWith("/"))
			url = url + "/";
		cs = url + "ContentServer";
		client.setState(state);
	}

	private String slurp(InputStream in) throws IOException {
		StringBuffer sb = new StringBuffer();
		int c = in.read();
		while (c != -1) {
			// System.out.print((char)c);
			if (c == '\r')
				c = '\n';
			sb.append((char) c);
			c = in.read();
		}
		int res = sb.lastIndexOf("<!--FTCS");
		// System.out.println("=== " + res);
		if (res == -1) {
			// System.out.println("<<< !!! " + sb);
		} else {
			// System.out.println("<<< " + sb.substring(res));
			sb.setLength(res - 1);
		}
		return sb.toString();
	}

	private String get(String url) throws HttpException, IOException {
		GetMethod get = new GetMethod(url);
		//System.out.println("\n>>> GET " + url);
		client.executeMethod(get);
		// dumpCookie();
		return slurp(get.getResponseBodyAsStream());
	}

	/**
	 * Invoke an element by name passing username and password
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public String invoke(String name) throws IOException {
		String url = cs + //
				"?pagename=" + name + //
				"&site=" + site + //
				"&username=" + username + //
				"&password=" + password;

		// StringTokenizer st = new StringTokenizer(get(url), "\r\n");
		// StringBuilder sb = new StringBuilder();
		// while()

		return get(url).replace('\r', '\n');
	}

	/**
	 * Return the status
	 * 
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Perform all the deployment: createa a new element with a random name,
	 * then run the created element that is supposed
	 * 
	 * @return
	 * @throws Exception
	 */
	public String deploy() {
		StringBuilder sb = new StringBuilder();
		try {

			String name = "AAAgileWCSInstaller";

			sb.append("*** Deploy\n");
			sb.append(invoke(name));

		} catch (Exception ex) {
			status = "EXCEPTION: " + ex.getMessage();
			ex.printStackTrace();
			sb.append(status);
		}
		return sb.toString();
	}

	/**
	 * Testing main
	 * 
	 * @param args
	 * @throws Exception
	 */

	public static void main(String[] args) throws Exception {
		System.out.println(new AgileWcsDeployer().deploy());
	}
}
