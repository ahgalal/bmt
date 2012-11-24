package utils;

import java.io.File;
import java.util.ArrayList;

import sys.utils.EnvVar;

import modules.experiment.Experiment;

public class Utils {
	public static boolean equalsOneOf(Object obj,Object[] arr){
		for(Object o:arr)
			if(o.equals(obj))
				return true;
		return false;
	}
	public static boolean equalsOneOf(Object obj,ArrayList<Object> arr){
		for(Object o:arr)
			if(o.equals(obj))
				return true;
		return false;
	}
	
	public static boolean compareExperiments(Experiment exp1,Experiment exp2){
		if(exp1.type!=exp2.type)
			return false;
		if(exp1.getDate().equals(exp2.getDate())==false)
			return false;
		if(exp1.getExpParametersList().length!=exp2.getExpParametersList().length)
			return false;
		for(String str1:exp1.getExpParametersList())
			if(equalsOneOf(str1, exp2.getExpParametersList())==false)
				return false;
		
		// TODO: check actual group data, not just number of groups
		if(exp1.getGroups().size()!=exp2.getGroups().size())
			return false;
		if(exp1.getName().equals(exp2.getName())==false)
			return false;
		if(exp1.getNotes().equals(exp2.getNotes())==false)
			return false;
		if(exp1.getUser().equals(exp2.getUser())==false)
			return false;
		
		return true;
	}
	
	public static String getResourcesDirPath(){
		return EnvVar.getEnvVariableValue("BMT_WS")+"/BMT/ants/test/resources/".replace("/", File.separator);
	}
	
	public static void sleep(int ms){
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
