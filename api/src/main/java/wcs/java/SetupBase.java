package wcs.java;

abstract public class SetupBase {

	/**
	 * Execute the backup creating the assets using the Asset API
	 * 
	 * @param username
	 * @param password
	 */
	public String exec(String username, String password) {
		
		StringBuilder sb = new StringBuilder();

		sb.append("Installing: username="+username+ "password="+password);

		for (CSElement cselement : getCSElements()) {
			sb.append("CSElement: " + cselement.getName());
		}
		for (SiteEntry siteentry : getSiteEntries()) {
			sb.append("SiteEntry: " + siteentry.getName());
		}
		for (Template template : getTemplates()) {
			sb.append("Template: " + template.getName());
		}
		return sb.toString();
	}

	/**
	 * Return templates to create
	 * 
	 * @return
	 */
	public abstract Template[] getTemplates();

	/**
	 * Return cselements to create
	 * 
	 * @return
	 */
	public abstract CSElement[] getCSElements();

	/**
	 * Return cselements to create
	 * 
	 * @return
	 */
	public abstract SiteEntry[] getSiteEntries();
	
}
