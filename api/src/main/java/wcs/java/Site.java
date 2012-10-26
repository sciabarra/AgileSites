package wcs.java;

import java.util.List;

import com.fatwire.assetapi.common.SiteAccessException;
import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.site.SiteInfo;
import com.fatwire.assetapi.site.SiteManager;

public class Site {

	private static Log log = new Log(Setup.class);

	private com.fatwire.assetapi.site.Site site;

	private String name;

	public Site(final Long id, final String name, final String description,
			final String[] types, final String[] users, final String[] roles) {
		super();

		this.name = name;

		site = new com.fatwire.assetapi.site.Site() {

			@Override
			public List<String> getAssetTypes() {
				return Util.array2list(types);
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
				return description;
			}

			@Override
			public Long getId() {
				return id;
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public String getPubroot() {
				return null;
			}

			@Override
			public List<String> getUserRoles(String arg0) {
				return Util.array2list(roles);
			}

			@Override
			public List<String> getUsers() {
				return Util.array2list(users);

			}

			@Override
			public void setAssetTypes(List<String> arg0) {

			}

			@Override
			public void setCSPrefix(String arg0) {

			}

			@Override
			public void setCSPreview(String arg0) {

			}

			@Override
			public void setCSPreviewAsset(AssetId arg0) {

			}

			@Override
			public void setDescription(String arg0) {

			}

			@Override
			public void setId(Long arg0) {

			}

			@Override
			public void setName(String arg0) {

			}

			@Override
			public void setPubroot(String arg0) {

			}

			@Override
			public void setUserRoles(String arg0, List<String> arg1) {

			}
		};
	}

	// update the site enabling asset types
	@SuppressWarnings("unchecked")
	String createOrUpdate(SiteManager sim) throws SiteAccessException {
		// sim.update(arg0
		boolean found = false;
		for (SiteInfo inf : sim.list()) {
			// {dbg(inf.getName());
			if (inf.getName().equals(site.getName()))
				found = true;
		}
		if (!found) {
			log.info("Creating  " + site);
			sim.create(Util.list(site));
		} else {
			log.info("Updating site " + site);
			sim.update(Util.list(site));
		}
		return site.getName();
	}

	/**
	 * Set site data
	 * 
	 * @param data
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setData(AssetData data) {

		AttributeData attrs = data.getAttributeData("Publist");
		List list = attrs.getDataAsList();
		for (Object obj : list)
			if (obj.toString().equals(name))
				return;
		list.add(name);
		attrs.setDataAsList(list);
	}

	public String toString() {
		return "Site(" + site.getName() + ":" + site.getId() + ")";
	}
}
