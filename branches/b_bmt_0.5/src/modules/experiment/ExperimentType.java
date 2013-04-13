package modules.experiment;

public enum ExperimentType {
	FORCED_SWIMMING("Forced Swimming"), OPEN_FIELD("Open Field");
	public static ExperimentType enumOf(final String str) {
		if (str.equals("Open Field"))
			return OPEN_FIELD;
		else if (str.equals("Forced Swimming"))
			return FORCED_SWIMMING;
		else
			return null;
	}

	private String	name;

	private ExperimentType(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}