package use.cache.com.except;

public class RedisReadException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3613626012554205334L;
	public RedisReadException() {
    	super();
    }
       
    public RedisReadException(String message) {
    	super(message);
    }
    
    public RedisReadException(String message, Throwable cause) {
        super(message, cause);
    }
        
    public RedisReadException(Throwable cause) {
        super(cause);
    }

}
