package wcs.java;

import java.util.List;
import wcs.java.util.Util;
import com.fatwire.assetapi.common.SiteAccessException;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.MutableAssetData;
import com.fatwire.assetapi.site.SiteInfo;
import com.fatwire.assetapi.site.SiteManager;

/**
 * Represent a site - currently only the name of the site is stored
 * 
 * It will contain much more in future - some private code is already available
 * 
 * @author msciab
 * 
 */
public class Site {
	
	//private static Log log = Log.getLog(Setup.class);

	public com.fatwire.assetapi.site.Site site;

	public String name;

	public Site(String name, final Setup setup) {

		this.name = name;

		site = new com.fatwire.assetapi.site.Site() {

			@Override
			public List<String> getAssetTypes() {
				return setup.getAssetTypes();
			}

			@Override
			public String getCSPrefix() {
				return null;
			}

			@Override
			public String getCSPreview() {
				return null;
			}

			@Override
			public AssetId getCSPreviewAsset() {
				return null;
			}

			@Override
			public String getDescription() {
				return setup.getDescription();
			}

			@Override
			public Long getId() {
				return setup.getId();
			}

			@Override
			public String getName() {
				return setup.getName();
			}

			@Override
			public String getPubroot() {
				return null;
			}

			@Override
			public List<String> getUserRoles(String arg0) {
				return setup.getUserRoles(arg0);
			}

			@Override
			public List<String> getUsers() {
				return setup.getUsers();
			}

			@Override
			public void setAssetTypes(List<String> arg0) {
				// do nothing
			}

			@Override
			public void setCSPrefix(String arg0) {
				// do nothing
			}

			@Override
			public void setCSPreview(String arg0) {
				// do nothing
			}

			@Override
			public void setCSPreviewAsset(AssetId arg0) {
				// do nothing
			}

			@Override
			public void setDescription(String arg0) {
				// do nothing
			}

			@Override
			public void setId(Long arg0) {
				// do nothing
			}

			@Override
			public void setName(String arg0) {
				// do nothing
			}

			@Override
			public void setPubroot(String arg0) {
				// do nothing
			}

			@Override
			public void setUserRoles(String arg0, List<String> arg1) {
				// do nothing
			}
		};
	}

	// update the site enabling asset types
	@SuppressWarnings("unchecked")
	String dropThencreateOrUpdate(boolean drop, SiteManager sim,
			StringBuilder msg) throws SiteAccessException {

		System.out.println("=======================");

		boolean found = false;
		for (SiteInfo inf : sim.list()) {
			if (inf.getName().equals(name)) {
				if (drop) {
					msg.append("\nDeleting site " + name);
					sim.delete(Util.listString(name));
				} else
					found = true;
			}
		}

		if (!found) {
			msg.append("\nCreating site " + name);
			sim.create(Util.list(site));
		} else {
			msg.append("\nUpdating site " + name);
			sim.update(Util.list(site));
		}
		return site.getName();
	}

	/**
	 * Set site data
	 * 
	 * @param data
	 */
	// @SuppressWarnings({ "rawtypes", "unchecked" })
	public void setData(MutableAssetData data) {
		AttributeData attrs = data.getAttributeData("Publist");
		if (attrs != null)
			attrs.setDataAsList(Util.listString(name));
	}

	public String toString() {
		return "Site(" + site.getName() + ":" + site.getId() + ")";
	}
}
