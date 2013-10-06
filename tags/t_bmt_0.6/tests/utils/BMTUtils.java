package utils;

import sys.utils.EnvVar;
import sys.utils.Files;


public class BMTUtils {
	public static String getResourcesDirPath(){
		return Files.convertPathToPlatformPath(EnvVar.getEnvVariableValue("BMT_WS")+"/BMT/ants/test/resources/");
	}
}
