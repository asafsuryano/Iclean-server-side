package acs.serviceImplementation.userServicePackage;

public class UserAllReadyExsistExeption extends RuntimeException {
	private static final long serialVersionUID = 1L; 
	
	public UserAllReadyExsistExeption(){
		super();
	}
	public UserAllReadyExsistExeption(String message) {
		super(message);
	}

}
