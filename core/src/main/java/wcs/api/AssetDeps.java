package wcs.api;

public enum AssetDeps {
	NONE("none"), EXACT("exact"), EXISTS("exists");

	private String value;

	private AssetDeps(String value) {
		this.value = value;
	}

	public String toString() {
		return value;
	}

	
	public static void main(String[] args) {
		System.out.println(NONE.toString());	
		System.out.println(EXISTS.toString());
		System.out.println(EXACT.toString());
	}
	
}
