package com.sciabarra.fatwire.serializer;

import com.fatwire.assetapi.data.AssetId;
import com.openmarket.xcelerate.asset.AssetIdImpl;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class AssetIdConverter implements Converter {

	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) {
		return clazz.equals(AssetIdImpl.class);
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter out,
			MarshallingContext ctx) {

		// output <asset>
		AssetId data = (AssetId) value;
		out.addAttribute("c", data.getType());
		out.addAttribute("cid", "" + data.getId());
		// out.setValue(data.getType()+":"+data.getId());
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		return new AssetIdImpl(reader.getAttribute("c"), Long.parseLong(reader
				.getAttribute("cid")));

	}
}
