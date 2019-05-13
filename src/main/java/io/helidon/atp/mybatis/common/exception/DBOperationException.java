package io.helidon.atp.mybatis.common.exception;

public class DBOperationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4368574573550583865L;

	public DBOperationException() {
		super();
	}

	public DBOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DBOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public DBOperationException(String message) {
		super(message);
	}

	public DBOperationException(Throwable cause) {
		super(cause);
	}

}
