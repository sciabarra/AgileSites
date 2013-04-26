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

	public String toString() {
		return c + ":" + cid.toString();
	}

	/**
	 * Check if 2 id refers to the same asset
	 * 
	 */
	public boolean equals(Object o) {
		if (o instanceof Id) {
			Id id = (Id) o;
			return id.c != null && id.cid != null && c != null && cid != null
					&& id.c.equals(c) && id.cid.equals(cid);
		}
		return false;
	}
}
