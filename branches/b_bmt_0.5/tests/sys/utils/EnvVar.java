package sys.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnvVar {
	/**
	 * @param str
	 * @return
	 */
	public static String replaceEnvVarInStringByValue(String str) {
		String envVarRegEx="\\{[^\\}]*\\}";//nice regEx ;)
		Pattern pattern = Pattern.compile(envVarRegEx);
		Matcher matcher = pattern.matcher(str);
		
		if(matcher.find()){
			String envVarName = matcher.group();
			envVarName=envVarName.substring(1,envVarName.length()-1);
			
			String envVarValue = getEnvVariableValue(envVarName);
			if(envVarValue!=null){
			envVarValue = envVarValue .replace("\\", "\\\\");
			str =str.replaceFirst(envVarRegEx, envVarValue);
			}else{
				System.err.println("Undefined Env. Variable: "+ envVarName);
				return null;
			}
		}
		return str;
	}
	/**
	 * @param name 
	 * @return
	 */
	public static String getEnvVariableValue(String name) {
		return System.getenv().get(name);
	}
}
