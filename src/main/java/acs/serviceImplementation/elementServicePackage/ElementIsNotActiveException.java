package acs.serviceImplementation.elementServicePackage;

public class ElementIsNotActiveException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	
	public ElementIsNotActiveException() {
		super();
	}

	public ElementIsNotActiveException(String message, Throwable cause) {
		super(message, cause);
	}

	public ElementIsNotActiveException(String message) {
		super(message);
	}

	public ElementIsNotActiveException(Throwable cause) {
		super(cause);
	}
}
