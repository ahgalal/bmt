package filters;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class FiltersNamesRequirements {
	private HashMap<String, String> filtersRequirements;
	
	public FiltersNamesRequirements() {
		filtersRequirements=new HashMap<String, String>();
	}
	
	public void addFilter(String name,String ID){
		// TODO: check for previous existence
		filtersRequirements.put(name, ID);
	}
	
	public Iterator<Entry<String, String>> getFilters(){
		return filtersRequirements.entrySet().iterator();
	}
}
