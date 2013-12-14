package wcs.api;


public interface SitePlan {

	/**
	 * Return id of the current page
	 * 
	 * @return
	 */
	public abstract Id current();

	/**
	 * Move to the page identified by the id
	 * 
	 * @param id
	 * @return
	 */
	public abstract SitePlan goTo(Id id);

	/**
	 * 
	 * Get the children of a page
	 * 
	 * @return
	 */
	public abstract Id[] children();

	/**
	 * 
	 * Get the path of a page up to the root
	 * 
	 * @return
	 */
	public abstract Id[] path();

}