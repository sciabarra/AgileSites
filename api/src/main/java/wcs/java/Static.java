package wcs.java;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import wcs.java.util.Util;

import com.fatwire.assetapi.data.BlobObject;
import com.fatwire.assetapi.data.BlobObjectImpl;
import com.fatwire.assetapi.data.MutableAssetData;

/**
 * Static definition class
 * 
 * @author msciab
 * 
 */
public class Static extends AssetSetup {

	private File file;
	int prefixLen;

	/**
	 * Constructor from a file, with the length of the prefix to be stripped
	 * from the path name
	 */
	public Static(File file, int prefixLen) {
		super("Static", "", file.getAbsolutePath()
				.replace(File.separatorChar, '/').substring(1 + prefixLen));

		this.prefixLen = prefixLen;
		this.file = file;
	}

	/**
	 * Create a Static with a chained asset setup
	 * 
	 */
	public Static(File file, int prefixLen, AssetSetup nextSetup) {
		this(file, prefixLen);
		setNextSetup(nextSetup);
	}


	public List<String> getAttributes() {
		return Util.listString("name", "description", "url");
	}

	void setData(MutableAssetData data) {

		// blob
		byte[] bytes = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			bytes = new byte[(int) file.length()];
			fis.read(bytes);
			fis.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		String dir = file.getParent().substring(prefixLen);

		BlobObject blob = new BlobObjectImpl(file.getName(), dir, bytes);

		data.getAttributeData("url").setData(blob);

		// data.getAttributeData("Mapping").setData(new ArrayList());
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

	public String toString() {
		return "Static(" + getName()+ ")";
	}
}
