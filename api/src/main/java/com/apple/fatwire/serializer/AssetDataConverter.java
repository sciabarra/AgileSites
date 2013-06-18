package com.apple.fatwire.serializer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AssetDataManager;
import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.AttributeDataImpl;
import com.fatwire.assetapi.data.BlobObject;
import com.fatwire.assetapi.data.MutableAssetData;
import com.fatwire.assetapi.def.AttributeTypeEnum;
import com.openmarket.xcelerate.asset.AssetIdImpl;
import com.openmarket.xcelerate.asset.AttributeDefImpl;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class AssetDataConverter implements Converter {

	private static final Log log = LogFactory.getLog(AssetDataConverter.class);

	AssetDataManager adm;

	AssetDataConverter(AssetDataManager adm) {
		this.adm = adm;
	}

	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) {
		return AssetData.class.isAssignableFrom(clazz);
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter out,
			MarshallingContext ctx) {

		// output <asset>
		AssetData data = (AssetData) value;
		String c = data.getAssetId().getType();

		out.addAttribute("c", c);
		ctx.put("currentType", c);

		/*
		 * if (c.equals("CSElement") || c.equals("Template")) {
		 * ctx.put("captureRootelement", c); ctx.put("captureUrl", c); }
		 */

		ctx.put("c", data.getAssetId().getType());

		String subtype = data.getAssetTypeDef().getSubtype();
		if (subtype == null)
			subtype = "";

		String assetName = data.getAttributeData("name").getData().toString();

		out.addAttribute("cid", "" + data.getAssetId().getId());
		out.addAttribute("subtype", subtype);
		out.addAttribute("name", assetName);

		for (AttributeData attr : data.getAttributeData()) {

			String name = attr.getAttributeName();
			String type = attr.getType().toString();

			// no id, name or subtype properties
			if (name.equals("id") || name.equals("name")
					|| name.equals("subtype"))
				continue;

			// XXX skip dimensions for now
			if (name.startsWith("Dimension"))
				continue;

			@SuppressWarnings("rawtypes")
			List list = attr.getDataAsList();

			// skip void data values
			if (list.size() == 0)
				continue;

			// capture the rootelement for template and cselement
			if (name.equals("rootelement")
					&& (ctx.get("currentType").equals("Template") //
					|| ctx.get("currentType").equals("CSElement"))) {
				// System.out.println("eccolo! " + name);
				ctx.put("rootelement", list.get(0).toString());
			}

			// capture filename for cselement
			if (name.equals("url")
					&& ctx.get("currentType").equals("CSElement")) {
				if (list.size() > 0 && list.get(0) instanceof BlobObject) {
					BlobObject blob = (BlobObject) list.get(0);
					ctx.put("filename", blob.getFilename());
				}
			}

			ctx.put("currentAttribute", name);
			out.startNode("attribute");
			out.addAttribute("name", name);
			out.addAttribute("type", type);

			//out.addAttribute("classOfSon", attr.getData().getClass().toString());

			// out.addAttribute("count", list.size() + "");
			// out.addAttribute("classSon",list.getClass().toString());
			ctx.convertAnother(list);
			out.endNode();
		}

		// add filename and rootelement if captured
		if (ctx.get("rootelement") != null) {
			out.startNode("rootelement");
			out.setValue(ctx.get("rootelement").toString());
			out.endNode();
		}
		if (ctx.get("filename") != null) {
			out.startNode("filename");
			out.setValue(ctx.get("filename").toString());
			out.endNode();
		}
	}

	private AttributeData attrData(String name, String stype, Object value) {
		AttributeTypeEnum type = AttributeTypeEnum.valueOf(stype.toUpperCase());
		AttributeDefImpl def = new AttributeDefImpl(name, type);
		return new AttributeDataImpl(def, name, type, value);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		try {

			String c = reader.getAttribute("c");
			long cid = Long.parseLong(reader.getAttribute("cid"));
			String subtype = reader.getAttribute("subtype");
			String assetName = reader.getAttribute("name");

			MutableAssetData data = adm.newAssetData(reader.getAttribute("c"),
					reader.getAttribute("subtype"));
			data.setAssetId(new AssetIdImpl(c, cid));
			data.addAttributeData(attrData("name", "string", assetName));
			data.addAttributeData(attrData("subtype", "string", subtype));
			data.addAttributeData(attrData("id", "long", cid));

			while (reader.hasMoreChildren()) {
				reader.moveDown();
				String node = reader.getNodeName();

				// ignore anything that is not an attribute
				if (node.equals("attribute")) {

					String name = reader.getAttribute("name");
					// String type = reader.getAttribute("type");
					ArrayList list = (ArrayList) ctx.convertAnother(
							ctx.currentObject(), ArrayList.class);
					data.getAttributeData(name).setDataAsList(list);
				} else {
					log.trace("ignored " + node);
				}
				reader.moveUp();
			}
			return data;
		} catch (AssetAccessException e) {
			log.warn(e);
		}
		return null;
	}
}
