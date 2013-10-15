package modules.experiment;

public enum ExperimentType {
	FORCED_SWIMMING("Forced Swimming"),
	OPEN_FIELD("Open Field"),
	PARKINSON("Parkinson");
	public static ExperimentType enumOf(final String str) {
		if (str.equals(OPEN_FIELD.toString()))
			return OPEN_FIELD;
		else if (str.equals(FORCED_SWIMMING.toString()))
			return FORCED_SWIMMING;
		else if (str.equals(PARKINSON.toString()))
			return PARKINSON;
		else
			return null;
	}
	
	private String	name;

	ExperimentType(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}