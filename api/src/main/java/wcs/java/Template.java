package wcs.java;

public class Template {
	
	public Template(String name, String element, String cscache, String sscache) {
		super();
		this.name = name;
		this.element = element;
		this.cscache = cscache;
		this.sscache = sscache;
	}

	private String name;
	
	private String element;

	private String cscache;
	
	private String sscache;

	public String getName() {
		return name;
	}

	public String getElement() {
		return element;
	}

	public String getCscache() {
		return cscache;
	}

	public String getSscache() {
		return sscache;
	}
	
	
}
