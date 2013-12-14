package wcs.api;

/**
 * Simple Arg holder class
 * 
 * @author msciab
 * 
 */
public class Arg {

	public String name;
	public String value;

	public Arg(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String toString() {
		return "(" + name + " -> " + value + ")";
	}
}
