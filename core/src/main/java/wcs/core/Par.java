package wcs.core;

/**
 * Simple Arg holder class
 * 
 * @author msciab
 * 
 */
public class Par {

	public String name;
	public String value;
	public String[] values;
	public int count = 0;
	
	public static Par param(String name, String... values) {
		return new Par(name, values);
	}

	public Par(String name, String... values) {
		this.values = values;
		count = values.length;
		if (count > 0)
			this.value = values[0];
		else
			value = null;
	}


}
