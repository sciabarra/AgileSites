package wcs.java;

import java.io.File;
import java.util.List;

import wcs.java.util.Util;

import com.fatwire.assetapi.data.BlobObject;
import com.fatwire.assetapi.data.BlobObjectImpl;
import com.fatwire.assetapi.data.MutableAssetData;

/**
 * CSElement definition class
 * 
 * @author msciab
 * 
 */
public class CSElementJar extends AssetSetup {

	
	private File jarFile;

	/**
	 * Create a CSElement with attached a jar
	 * 
	 * @param name
	 * @param elementClass
	 */
	public CSElementJar(String name, File jarFile) {
		super("CSElement", "", name);
		this.jarFile = jarFile;
	}

	
	public List<String> getAttributes() {
		return Util.listString("name", "description", "elementname",
				"rootelement", "url", "resdetails1", "resdetails2");
	}

	
	void setData(MutableAssetData data) {
		String elementName = getSite() + "/" + getName();
		
		// root element
		data.getAttributeData("rootelement").setData(elementName);
		// addAttribute(data, "rootelement", element);

		// element name
		data.getAttributeData("elementname").setData(elementName);
		// addAttribute(data, "elementname", element);

		data.getAttributeData("resdetails1").setData(
				"eid=" + data.getAssetId().getId());
		data.getAttributeData("resdetails2").setData(
				"timestamp=" + System.currentTimeMillis());

		// blob
		byte[] bytes = Util.readFile(jarFile);
		BlobObject blob = new BlobObjectImpl(elementName+".jar", "AgileSites", bytes);
		data.getAttributeData("url").setData(blob);
		
		// data.getAttributeData("createdby").setData("agilesites");
		// data.getAttributeData("createddate").setData(new Date());
		// data.getAttributeData("Mapping").setData(new ArrayList());
		// data.getAttributeData("Mapping").setData(new AttributeMan HashMap());
	}

	/**
	 * Fluent description setter
	 * 
	 * @param description
	 * @return
	 */
	public AssetSetup description(String description) {
		setDescription(description);
		return this;
	}
}
