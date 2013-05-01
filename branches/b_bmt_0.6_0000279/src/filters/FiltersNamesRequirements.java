package filters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class FiltersNamesRequirements implements Serializable {
	public static class FilterRequirement implements Serializable {
		/**
		 * 
		 */
		private static final long	serialVersionUID	= -1799742399913458193L;
		private String				ID;
		private String				name;
		private FilterTrigger		trigger;

		public FilterRequirement(final String name, final String iD,
				final FilterTrigger trigger) {
			super();
			this.name = name;
			ID = iD;
			this.trigger = trigger;
		}

		public String getID() {
			return ID;
		}

		public String getName() {
			return name;
		}

		public FilterTrigger getTrigger() {
			return trigger;
		}

		public void setID(final String iD) {
			ID = iD;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public void setTrigger(final FilterTrigger trigger) {
			this.trigger = trigger;
		}
		
		@Override
		public Object clone() throws CloneNotSupportedException {
			// we must use string instances in all arguments, to assure
			// the new object is fully independent on this instance.
			return new FilterRequirement(name, ID, FilterTrigger.valueOf(trigger.toString()));
		}
	}

	public static enum FilterTrigger {
		MANUAL, PROCESSING, STREAMING;
	}

	/**
	 * 
	 */
	private static final long					serialVersionUID	= 5586878050722171261L;

	private final ArrayList<FilterRequirement>	filtersRequirements;

	public FiltersNamesRequirements() {
		filtersRequirements = new ArrayList<FilterRequirement>();
	}

	public void addFilter(final String name, final String ID,
			final FilterTrigger trigger) {
		for(FilterRequirement filterRequirement:filtersRequirements)
			if(filterRequirement.getName().equals(name))
				throw new RuntimeException("Filter: "+ name +" already exists");
		filtersRequirements.add(new FilterRequirement(name, ID, trigger));
	}

	public void clearFilterNames() {
		filtersRequirements.clear();
	}

	public Iterator<FilterRequirement> getFilters() {
		return filtersRequirements.iterator();
	}

}
