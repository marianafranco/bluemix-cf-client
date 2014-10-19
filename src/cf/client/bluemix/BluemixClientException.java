package cf.client.bluemix;

/**
 * 
 * @author marianafranco
 *
 */
public class BluemixClientException extends Exception {

	private static final long serialVersionUID = 6736265085629548976L;
	
	public BluemixClientException(String message) {
        super(message);
    }

    public BluemixClientException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
