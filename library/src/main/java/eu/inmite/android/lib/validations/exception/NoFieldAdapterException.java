package eu.inmite.android.lib.validations.exception;

import android.view.View;

import java.lang.annotation.Annotation;

/**
 * @author Tomas Vondracek
 */
public class NoFieldAdapterException extends FormsValidationException {

	public NoFieldAdapterException(final View view, final Annotation annotation) {
		super("no adapter was found for view " + view + " with annotation " + annotation);
	}
}
