package tech.ldxy.json;

public class IncludeAndExcludeConflictException extends RuntimeException{

	private static final long serialVersionUID = 7267894929027420617L;

	IncludeAndExcludeConflictException(String message) {
        super(message);
    }
}
