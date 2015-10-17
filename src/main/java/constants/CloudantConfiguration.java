package constants;

public class CloudantConfiguration {

	private CloudantConfiguration() {

	}

	public static final String USERNAME = Configuration.getConfig("${cloudant.username}");

	public static final String PASSWORD = Configuration.getConfig("${cloudant.password}");

	public static final String DATABASE = Configuration.getConfig("${cloudant.database}");

	public static String getConfiguration() {
		return CloudantConfiguration.class.getName() + "[ USERNAME: " + CloudantConfiguration.USERNAME + ", DATABASE: "
				+ CloudantConfiguration.DATABASE + " ]";
	}

}
