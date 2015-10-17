package constants;

public class Configuration {

	public static String getConfig(String s) {
		if (s.startsWith("\\${")) { // not replaced by maven.
			return s;
		} else {
			String var = s.replace("${", "").replace("}", "");
			return System.getProperty(var);
		}
	}
	
}
