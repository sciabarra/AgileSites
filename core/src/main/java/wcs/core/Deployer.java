package wcs.core;

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
public class Deployer {

	String url;
	String username;
	String password;
	String cm;
	String cs;

	HttpClient client = new HttpClient();
	HttpState state = new HttpState();

	public Deployer() {
		this("http://localhost:8380/cs/", "fwadmin", "xceladmin");
	}

	/**
	 * Build the deployer
	 * 
	 * @param url
	 * @param username
	 * @param password
	 */
	public Deployer(String url, String username, String password) {
		this.username = username;
		this.password = password;
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
			// if (c != '\r')
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
	 * Create a sitecatalog entry
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public String createEntry(String name) throws IOException {
		String url = cm + //
				"?ftcmd=addrow" + //
				"&tablename=sitecatalog" + //
				"&pagename=" + name + //
				"&rootelement=" + name + // ,
				"&pageletonly=F" + //
				"&csstatus=live" + //
				"&cscacheinfo=false" + //
				"&sscacheinfo=false";
		return get(url);
	}

	/**
	 * Invoke an element by name passing username and password
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public String invoke(String name)
			throws IOException {
		String url = cs + //
				"?pagename=" + name + //
				"&username=" + username + //
				"&password=" + password;
		return get(url);
	}

	public String delete(String table, String key, String value)
			throws IOException {
		String url = cm + //
				"?ftcmd=deleterow" + //
				"&Delete+uploaded+file(s)=yes" + //
				"&tablekey=" + key + // ,
				"&tablekeyvalue=" + value;
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
				new StringPart("tablename", "ElementCatalog"),
				new StringPart("ftcmd", "addrow"),
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
	 * Perform all the deploy: createa a new element with a random name, then run the created element 
	 * that is supposed
	 * 
	 * @return
	 * @throws Exception
	 */
	public String deploy() throws Exception {
		StringBuilder sb = new StringBuilder();

		String name = "AAA-ScalaWCS-" + System.currentTimeMillis() + "-"
				+ Math.floor(Math.random() * 1000000);

		sb.append("<h1>Login</h1>\n");
		sb.append(login());

		sb.append("<h1>Create Element</h1>\n");
		sb.append(createElement(name, "Hello from deployer.\n3+3=<%= 3+3 %>.\n"));

		sb.append("<h1>Create Entry</h1>\n");
		sb.append(createEntry(name));

		sb.append("<h1>Deploy</h1>\n");
		sb.append(invoke(name));

		sb.append("<b1>Remove Element and Entry</h1>\n");
		sb.append(delete("elementcatalog", "elementname", name));
		sb.append(delete("sitecatalog", "pagename", name));

		sb.append("<h1>Logout</h1>\n");
		sb.append(logout());

		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		System.out.println(new Deployer().deploy());
	}
}
