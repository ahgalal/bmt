package filters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class FiltersConnectionRequirements implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2201049999499751252L;
	private HashMap<String, String> filtersConnections;
	
	public FiltersConnectionRequirements() {
		filtersConnections = new HashMap<String, String>();
	}
	
	public void connectFilters(String srcFilterName,String dstFilterName){
		
		// TODO: check if dstFilter is already connected, print a warning msg
		filtersConnections.put(dstFilterName, srcFilterName);
	}
	
	/**
	 * Gets available connections as filter pairs: Src,Dst.
	 * @return
	 */
	public ArrayList<String[]> getConnections(){
		ArrayList<String[]> ret = new ArrayList<String[]>();
		
		for(String dstFilterName:filtersConnections.keySet()){
			String srcFilterName = filtersConnections.get(dstFilterName);
			
			ret.add(new String[]{srcFilterName,dstFilterName});
		}
		
		return ret;
	}
}
