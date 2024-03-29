package org.niolikon.alexandria.purchasing.system.exceptions;

public class InternalException extends RuntimeException {
	
    /** */
	private static final long serialVersionUID = 1L;

	public InternalException() {
        super();
    }

    public InternalException(String message) {
        super(message);
    }

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }
}