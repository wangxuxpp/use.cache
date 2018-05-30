package use.cache.com.except;

public class RedisWriteException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4162565336434333319L;

	public RedisWriteException() {
    	super();
    }
       
    public RedisWriteException(String message) {
    	super(message);
    }
    
    public RedisWriteException(String message, Throwable cause) {
        super(message, cause);
    }
        
    public RedisWriteException(Throwable cause) {
        super(cause);
    }

}

