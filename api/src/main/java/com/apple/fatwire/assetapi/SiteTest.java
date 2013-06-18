package com.apple.fatwire.assetapi;

import java.util.Arrays;
import java.util.List;

import com.fatwire.assetapi.common.SiteAccessException;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.site.Site;

public class SiteTest extends Test {

	Site testSite = new Site() {

		@Override
		public List<String> getAssetTypes() {

			return list("Page", "Template", "CSElement", "AttrTypes",
					"APL_Filter", "APL_Attribute", "APL_ContentItem_PD",
					"APL_ContentItem_CD", "APL_ContentItem_P",
					"APL_ContentItem_C");
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
			return "Test Site";
		}

		@Override
		public Long getId() {
			return 1000000000000l;
		}

		@Override
		public String getName() {
			return "TestSite";
		}

		@Override
		public String getPubroot() {
			return null;
		}

		@Override
		public List<String> getUserRoles(String arg0) {
			return list("AdvancedUser", "GeneralAdmin", "SiteAdmin");
		}

		@Override
		public List<String> getUsers() {
			return list("fwadmin");
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

	@Override
	public String doIt() {

		try {

			// dump(sim.read(list("AdminSite")));

			sim.create(Arrays.asList(testSite));
			return sim.list().toString();
		} catch (SiteAccessException e) {
			return e.getMessage();
		}

	}

	public String deleteAll() {
		try {
			sim.delete(list("TestSite"));
			return sim.list().toString();
		} catch (SiteAccessException e) {
			return e.getMessage();
		}
	}

}
