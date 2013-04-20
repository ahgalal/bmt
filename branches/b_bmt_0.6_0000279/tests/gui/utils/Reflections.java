package gui.utils;

import modules.ExperimentManager;
import modules.experiment.Experiment;
import utils.ReflectUtils;

public class Reflections{
	public static Experiment getLoadedExperiment(){
		Experiment exp=null;
		exp=(Experiment) ReflectUtils.getField(ExperimentManager.getDefault(), "exp");
		return exp;
	}
}