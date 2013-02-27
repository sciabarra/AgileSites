package wcs.core;

import java.util.HashMap;
import java.util.Map;

public class Call {

	private Map<String, String> map = new HashMap<String, String>();
	private String target;
	private String[] args;

	public Call(String target, Arg... args) {
		this.target = target;
		this.args = new String[args.length];
		int n = 0;
		for (Arg arg : args) {
			map.put(arg.name, arg.value);
			this.args[n++] = arg.name;
		}
	}

	public String target() {
		return target;
	}

	public String get(String name) {
		return (String) map.get(name);
	}

	public String[] args() {
		return args;
	}

}
