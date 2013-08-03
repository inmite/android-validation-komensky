package eu.inmite.android.fw.validation.exception;

import java.util.List;
import java.util.Map;

/**
 * @author Tomáš Kypta
 * @since 17/04/2013
 */
public class PojoValidationException extends Exception {

	private final Class<?> objectClass;
	private final Map<String, List<String>> invalidFields;

	public PojoValidationException(Class<?> objectClass, Map<String, List<String>> invalidFields) {
		this.objectClass = objectClass;
		this.invalidFields = invalidFields;
	}

	public PojoValidationException(String detailMessage, Class<?> objectClass, Map<String, List<String>> invalidFields) {
		super(detailMessage);
		this.objectClass = objectClass;
		this.invalidFields = invalidFields;
	}

	public PojoValidationException(String detailMessage, Throwable throwable, Class<?> objectClass, Map<String, List<String>> invalidFields) {
		super(detailMessage, throwable);
		this.objectClass = objectClass;
		this.invalidFields = invalidFields;
	}

	public PojoValidationException(Throwable throwable, Class<?> objectClass, Map<String, List<String>> invalidFields) {
		super(throwable);
		this.objectClass = objectClass;
		this.invalidFields = invalidFields;
	}

	public Class<?> getObjectClass() {
		return objectClass;
	}

	public Map<String, List<String>> getInvalidFields() {
		return invalidFields;
	}
}
