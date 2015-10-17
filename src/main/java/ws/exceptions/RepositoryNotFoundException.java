package ws.exceptions;

public class RepositoryNotFoundException extends WebApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RepositoryNotFoundException(String username, String repository) {
		super(101l, "Repository not found: %s/%s", username, repository);
	}

}
