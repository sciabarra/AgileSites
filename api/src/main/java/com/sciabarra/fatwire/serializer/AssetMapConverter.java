package com.sciabarra.fatwire.serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.AttributeDataImpl;
import com.fatwire.assetapi.def.AttributeTypeEnum;
import com.openmarket.xcelerate.asset.AttributeDefImpl;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class AssetMapConverter implements Converter {

	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) {
		return clazz.equals(java.util.HashMap.class);
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter out,
			MarshallingContext ctx) {

		@SuppressWarnings("unchecked")
		Map<String, AttributeData> map = (Map<String, AttributeData>) value;
		for (Map.Entry<String, AttributeData> entry : map.entrySet()) {
			out.startNode("item");
			out.addAttribute("name", entry.getKey());
			out.addAttribute("type", entry.getValue().getType().toString());

			// out.addAttribute("debug",
			// entry.getValue().getClass().toString());

			AttributeData data = entry.getValue();

			/*
			 * if (data.getData() != null) out.addAttribute("classOfSon",
			 * data.getData().getClass() .toString()); else
			 * out.addAttribute("classOfSon", "**NULL**"); if (data.getData() !=
			 * null) out.addAttribute("classOfSonAsList", data.getDataAsList()
			 * .getClass().toString()); else
			 * out.addAttribute("classOfSonAsList", "**NULL**");
			 */

			ctx.convertAnother(data.getDataAsList());
			out.endNode();
		}
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		HashMap<String, AttributeData> map = new HashMap<String, AttributeData>();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			String stype = reader.getAttribute("type").toUpperCase();
			String name = reader.getAttribute("name");
			AttributeTypeEnum type = AttributeTypeEnum.valueOf(stype);
			AttributeDefImpl def = new AttributeDefImpl(name, type);

			@SuppressWarnings("rawtypes")
			ArrayList list = (ArrayList) ctx.convertAnother(
					ctx.currentObject(), ArrayList.class);

			AttributeData data;
			if (type.equals(AttributeTypeEnum.ARRAY)) {
				data = new AttributeDataImpl(def, name, type, (Object) list);
			} else if (list.size() == 1)
				data = new AttributeDataImpl(def, name, type, list.get(0));
			else
				data = new AttributeDataImpl(def, name, type, "ERROR");

			map.put(name, data);
			reader.moveUp();
		}
		return map;
	}
}
