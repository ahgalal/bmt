package modules;

import java.util.ArrayList;

import filters.Data;

public abstract class ModuleData extends Data {

	private ArrayList<String> parameters;
	
	public void addParameter(String param){
		parameters.add(param);
	}
	
	public void removeParameter(String param){
		parameters.remove(param);
	}
	
	public ArrayList<String> getParameters(){
		return parameters;
	}
	
	public ModuleData() {
		parameters=new ArrayList<String>();
	}
}
