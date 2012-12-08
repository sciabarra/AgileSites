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

	String invoker = "<%@ taglib prefix=\"cs\" uri=\"futuretense_cs/ftcs1_0.tld\"\n"
			+ "%><cs:ftcs><% String result =\"\"; try { result = wcs.core.WCS.deploy(ics, "
			+ "ics.GetVar(\"site\"), "
			+ "ics.GetVar(\"username\"),"
			+ "ics.GetVar(\"password\")); } catch(Exception ex) { ex.printStackTrace(); }"
			+ " %><%= result %></cs:ftcs>";

	String site;
	String url;
	String username;
	String password;
	String cm;
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
		cm = url + "CatalogManager";
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
		// System.out.println("\n>>> GET " + url);
		client.executeMethod(get);
		// dumpCookie();
		return slurp(get.getResponseBodyAsStream());
	}

	/**
	 * Login to cs
	 * 
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public String login() throws HttpException, IOException {
		String url = cm + //
				"?ftcmd=login" + //
				"&password=" + password + //
				"&username=" + username;
		return get(url);
	}

	/**
	 * Logout from cs
	 * 
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public String logout() throws HttpException, IOException {
		String url = cm + //
				"?ftcmd=logout" + //
				"&killsession=true" + //
				"&username=" + username;
		return get(url);
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
	 * Delete an entry from a table
	 * 
	 * @param table
	 * @param key
	 * @param value
	 * @return
	 * @throws IOException
	 */
	public String delete(String table, String key, String value)
			throws IOException {
		String url = cm + //
				"?ftcmd=deleterow" + //
				"&tablename=" + table + //
				"&tablekey=" + key + //
				"&tablekeyvalue=" + value + //
				"&Delete+uploaded+file(s)=yes"; //
		return get(url);
	}

	/**
	 * Create an element in the element catalog
	 * 
	 * @param name
	 * @param body
	 * @return
	 * @throws IOException
	 */
	public String createElement(String name, String body) throws IOException {

		PostMethod post = new PostMethod(cm);
		Part[] parts = { //
				new StringPart("ftcmd", "addrow"),
				new StringPart("tablename", "ElementCatalog"),
				new StringPart("elementname", name),
				new StringPart("url_folder", ""),
				new FilePart("url", new ByteArrayPartSource(name + ".jsp",
						body.getBytes("UTF-8")), "application/octet-stream",
						"UTF-8") };

		post.setRequestEntity(new MultipartRequestEntity(parts, post
				.getParams()));

		int result = client.executeMethod(post);
		if (result == 200)
			return slurp(post.getResponseBodyAsStream());
		else
			return "ERROR";
	}

	/**
	 * Create an element in the element catalog
	 * 
	 * @param name
	 * @param body
	 * @return
	 * @throws IOException
	 */
	public String createEntry(String name) throws IOException {

		PostMethod post = new PostMethod(cm);
		Part[] parts = { //
		new StringPart("ftcmd", "addrow"),
				new StringPart("tablename", "SiteCatalog"),
				new StringPart("pagename", name),
				new StringPart("rootelement", name),
				new StringPart("pageletonly", "F"),
				new StringPart("csstatus", "live"),
				new StringPart("cscacheinfo", "false"),
				new StringPart("sscacheinfo", "false") };

		post.setRequestEntity(new MultipartRequestEntity(parts, post
				.getParams()));

		int result = client.executeMethod(post);
		if (result == 200)
			return slurp(post.getResponseBodyAsStream());
		else
			return "ERROR";
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

			String name = "AAAgileWcsDeployer";

			sb.append("*** Login\n");
			sb.append(login());

			sb.append("*** Create Element\n");
			sb.append(createElement(name, invoker));

			sb.append("*** Create Entry\n");
			sb.append(createEntry(name));

			sb.append("*** Deploy\n");
			sb.append(invoke(name));

			sb.append("*** Remove Element and Entry\n");
			sb.append(delete("elementcatalog", "elementname", name));
			sb.append(delete("sitecatalog", "pagename", name));

			sb.append("*** Logout\n");
			sb.append(logout());
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
