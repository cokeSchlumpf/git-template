package ws.exceptions;

public class NoTemplateConfigurationException extends WebApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 346346167539223692L;

	public NoTemplateConfigurationException(String username, String repository) {
		super(102l, "The repository %s/%s does not contain a .template file.", username, repository);
	}
	
}
