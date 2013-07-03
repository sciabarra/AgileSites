package com.sciabarra.fatwire.serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.AttributeDataImpl;
import com.fatwire.assetapi.data.BlobObject;
import com.fatwire.assetapi.def.AttributeTypeEnum;
import com.openmarket.xcelerate.asset.AttributeDefImpl;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class AttributeDataConverter implements Converter {

	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) {
		return AttributeData.class.isAssignableFrom(clazz);
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter out,
			MarshallingContext ctx) {

		// output <asset>
		AttributeData data = (AttributeData) value;

		// out.startNode("attrdata");
		String name = data.getAttributeName();
		String type = data.getType().toString();
		out.addAttribute("type", type);
		out.addAttribute("name", name);

		// special case, remove the url from the map for a template
		if (ctx.get("currentType").equals("Template") && type.equals("struct")
				&& name.equals("Structure Element")) {
			try {
				@SuppressWarnings("rawtypes")
				Map map = (Map) data.getData();
				AttributeData adata = (AttributeData) map.get("url");
				BlobObject blob = (BlobObject) adata.getData();
				ctx.put("filename", blob.getFilename());
				// map.remove("url");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		// System.out.println("attrdata type=" + type + " name=" + name);
		/*
		 * if (data.getData() != null) out.addAttribute("classOfSon",
		 * data.getData().getClass().toString()); else
		 * out.addAttribute("classOfSon", "**NULL**");
		 * 
		 * if (data.getDataAsList() != null) {
		 * out.addAttribute("classOfSonAsList", data.getDataAsList()
		 * .getClass().toString()); if (data.getDataAsList().size() > 0)
		 * out.addAttribute("classOfFirstSon", data.getDataAsList().get(0)
		 * .getClass().toString()); } else out.addAttribute("classOfFirstSon",
		 * "**NULL**");
		 */

		// continue
		ctx.convertAnother(data.getDataAsList());
		// out.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		// AttributeData data = new AttributeDataImpl();

		String name = reader.getAttribute("name");
		String stype = reader.getAttribute("type").toUpperCase();

		AttributeTypeEnum type = AttributeTypeEnum.valueOf(stype);

		AttributeDefImpl def = new AttributeDefImpl(name, type);

		@SuppressWarnings("rawtypes")
		List list = (List) ctx.convertAnother(ctx.currentObject(),
				ArrayList.class);

		if (type.equals(AttributeTypeEnum.ARRAY))
			return new AttributeDataImpl(def, name, type, (Object) list);

		if (list.size() == 1)
			return new AttributeDataImpl(def, name, type, list.get(0));

		// should not happen
		return new AttributeDataImpl(def, name, type, "ERROR");

	}
}
