package wcs.java;

import static wcs.Api.*;
import wcs.api.Arg;
import wcs.api.Id;
import wcs.api.Log;
import wcs.core.tag.CommercecontextTag;
import java.util.ArrayList;
import java.util.List;
import COM.FutureTense.Interfaces.ICS;

/**
 * A decorator to add recommandation handling functions
 * 
 * @author msciab
 * 
 */
public class Recommendation {

	private static Log log = Log.getLog(Recommendation.class);

	Env e;
	ICS i;

	String name = null;
	Long id = null;

	Arg[] args;

	public Recommendation(Env env, String name, Arg[] args) {
		e = env;
		i = e.ics;
		this.name = name;
		this.args = args;
	}

	public Recommendation(Env env, Long id, Arg[] args) {
		e = env;
		i = e.ics;
		this.id = id;
		this.args = args;
	}

	/**
	 * Return recommendation childrens as list of ID
	 */
	public List<Id> getChildren() {

		CommercecontextTag.Getrecommendations rec = CommercecontextTag
				.getrecommendations();

		String output = tmp();
		rec.listvarname(output);

		if (id != null)
			rec.collectionid(id.toString());
		else
			rec.collection(name);

		rec.set(args).run(i);

		log.debug("found " + e.GetList(output));

		List<Id> result = new ArrayList<Id>();
		if (e.isList(output))
			for (int pos : e.getRange(output))
				result.add(new Id(e.getString(output, pos, "assettype"), e
						.getLong(output, pos, "assetid")));

		return result;
	}

}
