package gui.utils;

import utils.ReflectUtils;
import modules.ExperimentManager;
import modules.experiment.Experiment;

public class Reflections{
	public static Experiment getLoadedExperiment(){
		Experiment exp=null;
		exp=(Experiment) ReflectUtils.getField(ExperimentManager.getDefault(), "exp");
		return exp;
	}
}