package eu.inmite.android.lib.validations.form.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validate input string with specified regular expression.
 * @author Tomas Vondracek
 */
@Target(value= ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface RegExp {

	// for patters se android.Util.Patterns


	String EMAIL =
			"[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
			"\\@" +
			"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
			"(" +
			"\\." +
			"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
			")+";

	/**
	 * regular expression
	 */
	String value();
	int messageId() default 0;
	int order() default 1000;
}



