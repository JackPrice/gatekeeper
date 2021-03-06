package io.gatekeeper.api;

/**
 * An arbitrary exception that encapsulates an HTTP response code and message.
 */
public class HttpResponseException extends Exception {

    /**
     * The HTTP status code this message represents;
     */
    protected Integer code;

    /**
     * A description of this status.
     */
    protected String message;

    /**
     * Extra detail in this message.
     */
    protected String detail;

    /**
     * Default constructor.
     *
     * @param code    The HTTP status code of this exception
     * @param message A description of this status
     */
    public HttpResponseException(Integer code, String message) {
        assert null != code;
        assert null != message;

        this.code = code;
        this.message = message;
    }

    /**
     * Constructor with extra information
     *
     * @param code    The HTTP status code of this exception
     * @param message A description of this status
     * @param detail  Additional information about this exception
     */
    public HttpResponseException(Integer code, String message, String detail) {
        assert null != code;
        assert null != message;
        assert null != detail;

        this.code = code;
        this.message = message;
        this.detail = detail;
    }

    /**
     * @return The HTTP status code this message represents
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @return A description of this status
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * @return Any extra information for this message
     */
    public String getDetail() {
        return detail;
    }
}
