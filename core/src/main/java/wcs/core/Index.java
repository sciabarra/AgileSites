package wcs.core;

public interface Index {
	public String[] getTemplates();
	
	public String[] getCSElements();
	
	public String invoke(String name);

}

