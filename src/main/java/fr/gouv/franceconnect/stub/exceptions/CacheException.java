package fr.gouv.franceconnect.stub.exceptions;

/**
 * Thrown if there is a problem at storing / retrieveing element in cache.
 */
public class CacheException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message exception message.
     */
    public CacheException(String message) {
        super(message);
    }


    /**
     * Constructor.
     *
     * @param message   exception message.
     * @param exception parent exception.
     */
    public CacheException(String message, Exception exception) {
        super(message, exception);
    }
}
