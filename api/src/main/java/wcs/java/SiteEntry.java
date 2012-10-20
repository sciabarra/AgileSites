package wcs.java;

public class SiteEntry {

	public SiteEntry(String name, String element, boolean wrapper) {
		super();
		this.name = name;
		this.element = element;
		this.wrapper = wrapper;
	}

	private String name;
	private String element;
	private boolean wrapper;

	public String getName() {
		return name;
	}

	public String getElement() {
		return element;
	}

	public boolean isWrapper() {
		return wrapper;
	}

}
