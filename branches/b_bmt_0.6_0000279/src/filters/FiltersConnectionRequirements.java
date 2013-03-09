package filters;

import java.util.ArrayList;
import java.util.HashMap;

public class FiltersConnectionRequirements {
	private HashMap<String, String> filtersConnections;
	
	public FiltersConnectionRequirements() {
		filtersConnections = new HashMap<String, String>();
	}
	
	public void connectFilters(String srcFilterName,String dstFilterName){
		
		// TODO: check if dstFilter is already connected, print a warning msg
		filtersConnections.put(dstFilterName, srcFilterName);
	}
	
	public ArrayList<String[]> getConnections(){
		ArrayList<String[]> ret = new ArrayList<String[]>();
		
		for(String dstFilterName:filtersConnections.keySet()){
			String srcFilterName = filtersConnections.get(dstFilterName);
			
			ret.add(new String[]{srcFilterName,dstFilterName});
		}
		
		return ret;
	}
}
