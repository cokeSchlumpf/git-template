package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import com.cloudant.client.api.CloudantClient;

import constants.CloudantConfiguration;
import constants.GitHubConfiguration;

public class GitHubTest {

	@Test
	public void gh() throws IOException {
		System.out.println(GitHubConfiguration.USERNAME);
		System.out.println(GitHubConfiguration.PASSWORD);
		GHRepository rep = GitHub//
				.connectUsingPassword(GitHubConfiguration.USERNAME, GitHubConfiguration.PASSWORD) //
				.getUser("cokeSchlumpf") //
				.getRepository("react-flatui");
		
		Arrays.asList(rep.getRefs()).stream().forEach(ref -> System.out.println(ref.getRef()));
		
		listContent(rep, rep.getDirectoryContent("")).stream().forEach(s -> System.out.println(s));
	}

	private List<String> listContent(GHRepository repositoy, List<GHContent> content) throws IOException {
		return content.stream().map(file -> {
			List<String> list = new ArrayList<>();
			list.add(repositoy.getName() + "/" + file.getPath());
			if (file.isDirectory()) {
				try {
					list.addAll(listContent(repositoy, repositoy.getDirectoryContent(file.getPath())));
				} catch (Exception e) {

				}
			}
			return list;
		}).reduce(new ArrayList<String>(), (all, list) -> {
			all.addAll(list);
			return all;
		});
	}

	@Test
	public void cloudant() {
		CloudantClient client = new CloudantClient(CloudantConfiguration.USERNAME, CloudantConfiguration.USERNAME, CloudantConfiguration.PASSWORD);

		System.out.println("Connected to Cloudant");
		System.out.println("Server Version: " + client.serverVersion());

		List<String> databases = client.getAllDbs();
		System.out.println("All my databases : ");
		for (String db : databases) {
			System.out.println(db);
		}
	}
}
