package wcs.core;

/**
 * Simple Unique id (c & cid) holder class
 * 
 * @author msciab
 * 
 */
public class Id {
	public Id(String c, Long cid) {
		this.c = c;
		this.cid = cid;
	}

	public String c;
	public Long cid;
}
