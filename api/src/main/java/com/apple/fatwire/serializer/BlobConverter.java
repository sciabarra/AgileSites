package com.apple.fatwire.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.assetapi.data.BlobObjectImpl;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class BlobConverter implements Converter {
	private static final Log log = LogFactory.getLog(BlobConverter.class);

	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) {
		return clazz.equals(BlobObjectImpl.class);
	}

	@SuppressWarnings("unused")
	private String readBlob(Reader in) {
		StringBuffer sb = new StringBuffer();
		int c;
		try {
			c = in.read();
			while (c != -1) {
				sb.append((char) c);
				c = in.read();
			}
		} catch (IOException e) {
			log.warn(e);
		}
		return sb.toString();
	}

	private byte[] readBytes(InputStream in) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			int c = in.read();
			while (c != -1) {
				baos.write(c);
				c = in.read();
			}
			return baos.toByteArray();
		} catch (IOException e) {
			log.warn(e);
		}
		return null;
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter out,
			MarshallingContext ctx) {

		BlobObjectImpl blob = (BlobObjectImpl) obj;
		if (blob.getFilename() != null)
			out.addAttribute("filename", blob.getFilename());
		if (blob.getFoldername() != null)
			out.addAttribute("folder", blob.getFoldername());

		// read body
		InputStream in = blob.getBinaryStream();

		if (blob.getBlobAddress() != null) {
			if (blob.getBlobAddress().getTableName() != null)
				out.addAttribute("table", blob.getBlobAddress().getTableName());
			if (blob.getBlobAddress().getColumnName() != null)
				out.addAttribute("column", blob.getBlobAddress()
						.getColumnName());
			if (blob.getBlobAddress().getIdentifier() != null)
				out.addAttribute("blobid", blob.getBlobAddress()
						.getIdentifier().toString());
			if (blob.getBlobAddress().getIdentifier() != null)
				out.addAttribute("colid", blob.getBlobAddress()
						.getIdentifierColumnName().toString());
		}

		if (in != null) {
			byte[] data = readBytes(blob.getBinaryStream());
			ctx.convertAnother(data);
			// out.setValue(data);
		}

		// out.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader hsw,
			UnmarshallingContext ctx) {
		String filename = hsw.getAttribute("filename");
		String folder = hsw.getAttribute("folder");
		byte[] bytes = null;
		if (filename != null)
			bytes = (byte[]) ctx.convertAnother(ctx.currentObject(),
					byte[].class);
		return new BlobObjectImpl(filename, folder, bytes);
	}
}
