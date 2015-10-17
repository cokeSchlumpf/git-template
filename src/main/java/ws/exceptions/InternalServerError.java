package ws.exceptions;

import javax.ws.rs.core.Response.Status;

public class InternalServerError extends WebApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4990436862790588134L;

	public InternalServerError(Throwable cause) {
		super(cause, 500l, "Internal Server Error", new String[] { }, Status.EXPECTATION_FAILED);
	}
	
}
