package filters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class FiltersNamesRequirements implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5586878050722171261L;
	private ArrayList<FilterRequirement> filtersRequirements;
	
	public FiltersNamesRequirements() {
		filtersRequirements=new ArrayList<FilterRequirement>();
	}
	
	public void addFilter(String name,String ID, FilterTrigger trigger){
		// TODO: check for previous existence
		filtersRequirements.add(new FilterRequirement(name, ID, trigger));
	}
	
	public Iterator<FilterRequirement> getFilters(){
		return filtersRequirements.iterator();
	}

	public void clearFilterNames() {
		filtersRequirements.clear();		
	}
	
	public static class FilterRequirement implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1799742399913458193L;
		private String name;
		private String ID;
		private FilterTrigger trigger;
		public void setName(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setID(String iD) {
			ID = iD;
		}
		public String getID() {
			return ID;
		}
		public void setTrigger(FilterTrigger trigger) {
			this.trigger = trigger;
		}
		public FilterTrigger getTrigger() {
			return trigger;
		}
		public FilterRequirement(String name, String iD, FilterTrigger trigger) {
			super();
			this.name = name;
			ID = iD;
			this.trigger = trigger;
		}
	}
	
	public static enum FilterTrigger{
		STREAMING,PROCESSING,MANUAL;
	}
}
