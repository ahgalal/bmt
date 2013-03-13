package filters;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @author Creative
 */
public class FiltersSetup {
	private FiltersCollection filters;
	private final ArrayList<Link> links;
	private FiltersNamesRequirements filtersRequirements;
	private FiltersConnectionRequirements connectionRequirements;

	public FiltersConnectionRequirements getConnectionRequirements() {
		return connectionRequirements;
	}

	public FiltersSetup(FiltersNamesRequirements filtersRequirements,FiltersConnectionRequirements connectionRequirements) {
		this.filtersRequirements=filtersRequirements;
		this.connectionRequirements=connectionRequirements;
		links = new ArrayList<Link>();
	}
	
	public FiltersNamesRequirements getFiltersNamesRequirements(){
		return filtersRequirements;
	}
	
	public void setFiltersCollection(final FiltersCollection filters){
		for(Iterator<Entry<String , String>> it=filtersRequirements.getFilters();it.hasNext();){
			Entry<String , String> entry = it.next();
			
			// search in the input filtersCollection
			boolean found=false;
			for(Iterator<VideoFilter<?, ?>> it2=filters.getIterator();it2.hasNext();){
				VideoFilter<?, ?> videoFilter = it2.next();
				
				if(videoFilter.getName().equals(entry.getKey()) &&
						videoFilter.getID().equals(entry.getValue())){
					found=true;
					break;
				}
			}
			
			if(found==false){
				throw new RuntimeException("Input Filters does not match that required by the Experiment");
			}
		}
		this.filters = filters;
	}

	private void connectFilters(final String filterSrcName,
			final String filterDstName) {
		
		final VideoFilter<?, ?> srcFilter = filters
				.getFilterByName(filterSrcName);
		final VideoFilter<?, ?> dstFilter = filters
				.getFilterByName(filterDstName);

		Link lnk;
		if (srcFilter.getLinkOut() != null)
			lnk = srcFilter.getLinkOut();
		else {
			// TODO: make dims configurable instead of 640x480
			lnk = new Link(new Point(640, 480));
			srcFilter.setLinkOut(lnk);
		}

		dstFilter.setLinkIn(lnk);

		links.add(lnk);
		System.out.println("link added, from: " + filterSrcName + " to: " + dstFilter.getName());
	}
	
	/**
	 * Connects filters in FilterCollection instance according to specifications found in ConnectionRequirements instance.
	 */
	public void connectFilters(){
		links.clear();
		for(String[] connection:connectionRequirements.getConnections()){
			String srcFilterName = connection[0];
			String dstFilterName = connection[1];
			
			connectFilters(srcFilterName, dstFilterName);
		}
	}

	public void removeFilter(final String filterName) {
		final VideoFilter<?, ?> filter = filters.getFilterByName(filterName);
		
		// remove linkOut of the removed filter
		Link linkOut=filter.getLinkOut();
		links.remove(linkOut);
		
		// remove linkOut from all dependent filters (i.e. linkOut is their linkIn)
		for(VideoFilter<?, ?> depFilter:getFiltersByLinkIn(linkOut)){
			depFilter.setLinkIn(null);
		}
		
	}
	
	public ArrayList<VideoFilter<?, ?>> getFiltersByLinkIn(Link linkIn){
		ArrayList<VideoFilter<?, ?>> ret=new ArrayList<VideoFilter<?,?>>();
		for(Iterator<VideoFilter<?, ?>> it=filters.getIterator();it.hasNext(); ){
			VideoFilter<?, ?> filter = it.next();
			if(filter.getLinkIn()==linkIn)
				ret.add(filter);
		}
		return ret;
	}

	/**
	 * Validates all connections, assures that all filters are connected and no
	 * broken links.
	 * 
	 * @return
	 */
	public boolean validateConnections() {
		// TODO
		return false;
	}
}
