package tech.ldxy.json;

public class SameMethodNameException extends RuntimeException {
	
	private static final long serialVersionUID = -2188176922507443136L;

	SameMethodNameException(String message) {
        super(message);
    }

}
