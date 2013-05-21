package eu.inmite.android.fw.validation.exception;

/**
 * @author Tomas Vondracek
 */
public class UiValidationException extends RuntimeException {

	public UiValidationException() {
	}

	public UiValidationException(String detailMessage) {
		super(detailMessage);
	}

	public UiValidationException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public UiValidationException(Throwable throwable) {
		super(throwable);
	}
}
