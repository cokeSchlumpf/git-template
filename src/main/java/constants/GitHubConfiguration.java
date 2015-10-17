package constants;

public class GitHubConfiguration {

	private GitHubConfiguration() {
		
	}
	
	public static final String USERNAME = Configuration.getConfig("${github.username}");
	
	public static final String PASSWORD = Configuration.getConfig("${github.password}");
	
}
