package modules;

import java.util.ArrayList;

import filters.Data;

public class ModuleData extends Data {

	//public ExperimentType[] expType;
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
	
	public ModuleData(String name) {
		super(name);
		parameters=new ArrayList<String>();
	}

}
