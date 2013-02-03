package wcs.core;

/**
 * Simple Unique id (c & cid) holder class
 * 
 * @author msciab
 * 
 */
public class Uid {
	public Uid(String c, Long cid) {
		this.c = c;
		this.cid = cid;
	}

	public String c;
	public Long cid;
}
